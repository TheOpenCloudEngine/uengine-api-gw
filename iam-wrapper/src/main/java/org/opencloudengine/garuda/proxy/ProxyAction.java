package org.opencloudengine.garuda.proxy;

import com.cloudant.http.Http;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;
import org.opencloudengine.garuda.common.exception.ServiceException;
import org.opencloudengine.garuda.gateway.GatewayServlet;
import org.opencloudengine.garuda.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;

/**
 * Created by uengine on 2016. 6. 15..
 */
public class ProxyAction {

    /**
     * SLF4J Logging
     */
    private Logger logger = LoggerFactory.getLogger(ProxyAction.class);

    protected boolean doLog = false;
    protected boolean doForwardIP = true;
    /**
     * User agents shouldn't send the url fragment but what if it does?
     */
    protected boolean doSendUrlFragment = true;

    //These next 3 are cached here, and should only be referred to in initialization logic. See the
    // ATTR_* parameters.

    private GatewayServlet proxyServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String host;
    private String path;
    private HttpClient proxyClient;
    private HttpClient proxyHttpsClient;
    private ServletConfig servletConfig;

    /**
     * From the configured parameter "host".
     */
    protected URI targetUriObj;//new URI(targetUri)
    protected HttpHost targetHost;//URIUtils.extractHost(targetUriObj);

    public ProxyAction(ProxyRequest proxyRequest) {
        this.proxyServlet = proxyRequest.getProxyServlet();
        this.request = proxyRequest.getRequest();
        this.response = proxyRequest.getResponse();
        this.host = proxyRequest.getHost();
        this.path = proxyRequest.getPath();
        this.proxyClient = proxyRequest.getProxyServlet().getProxyClient();
        this.proxyHttpsClient = proxyRequest.getProxyServlet().getProxyHttpsClient();
        this.servletConfig = this.proxyServlet.getServletConfig();

        try {
            targetUriObj = new URI(host);
        } catch (Exception e) {
            throw new ServiceException("Trying to process targetUri init parameter: " + e, e);
        }
        targetHost = URIUtils.extractHost(targetUriObj);
    }

    protected void service() throws ServletException, IOException {

        // Make the Request
        //note: we won't transfer the protocol version because I'm not sure it would truly be compatible
        String method = request.getMethod();
        String proxyRequestUri = rewriteUrlFromRequest(request);
        HttpRequest proxyRequest;
        //spec: RFC 2616, sec 4.3: either of these two headers signal that there is a message body.
        if (request.getHeader(HttpHeaders.CONTENT_LENGTH) != null ||
                request.getHeader(HttpHeaders.TRANSFER_ENCODING) != null) {
            proxyRequest = newProxyRequestWithEntity(method, proxyRequestUri, request);
        } else {
            proxyRequest = new BasicHttpRequest(method, proxyRequestUri);
        }

        copyRequestHeaders(request, proxyRequest);

        setXForwardedForHeader(request, proxyRequest);

        HttpResponse proxyResponse = null;
        try {
            // Execute the request
            if (doLog) {
                logger.info("proxy " + method + " uri: " + request.getRequestURI() + " -- " + proxyRequest.getRequestLine().getUri());
            }

            if (proxyRequestUri.startsWith("https")) {
                proxyResponse = proxyHttpsClient.execute(targetHost, proxyRequest);
            } else {
                proxyResponse = proxyClient.execute(targetHost, proxyRequest);
            }

            // Process the response:

            // Pass the response code. This method with the "reason phrase" is deprecated but it's the
            //   only way to pass the reason along too.
            int statusCode = proxyResponse.getStatusLine().getStatusCode();
            //noinspection deprecation
            response.setStatus(statusCode, proxyResponse.getStatusLine().getReasonPhrase());

            // Copying response headers to make sure SESSIONID or other Cookie which comes from the remote
            // server will be saved in client when the proxied url was redirected to another one.
            // See issue [#51](https://github.com/mitre/HTTP-Proxy-Servlet/issues/51)
            copyResponseHeaders(proxyResponse, request, response);

            if (statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
                // 304 needs special handling.  See:
                // http://www.ics.uci.edu/pub/ietf/http/rfc1945.html#Code304
                // Don't send body entity/content!
                response.setIntHeader(HttpHeaders.CONTENT_LENGTH, 0);
            } else {
                // Send the content to the client
                copyResponseEntity(proxyResponse, response, proxyRequest, request);
            }

        } catch (Exception e) {
            //abort request, according to best practice with HttpClient
            if (proxyRequest instanceof AbortableHttpRequest) {
                AbortableHttpRequest abortableHttpRequest = (AbortableHttpRequest) proxyRequest;
                abortableHttpRequest.abort();
            }
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            if (e instanceof ServletException)
                throw (ServletException) e;
            //noinspection ConstantConditions
            if (e instanceof IOException)
                throw (IOException) e;
            throw new RuntimeException(e);

        } finally {
            // make sure the entire entity was consumed, so the connection is released
            if (proxyResponse != null)
                consumeQuietly(proxyResponse.getEntity());
            //Note: Don't need to close servlet outputStream:
            // http://stackoverflow.com/questions/1159168/should-one-call-close-on-httpservletresponse-getoutputstream-getwriter
        }
    }

    private HttpRequest newProxyRequestWithEntity(String method, String proxyRequestUri,
                                                  HttpServletRequest servletRequest)
            throws IOException {
        HttpEntityEnclosingRequest eProxyRequest =
                new BasicHttpEntityEnclosingRequest(method, proxyRequestUri);
        // Add the input entity (streamed)
        //  note: we don't bother ensuring we close the servletInputStream since the container handles it
        eProxyRequest.setEntity(
                new InputStreamEntity(servletRequest.getInputStream(), servletRequest.getContentLength()));
        return eProxyRequest;
    }

    /**
     * HttpClient v4.1 doesn't have the
     * {@link org.apache.http.util.EntityUtils#consumeQuietly(org.apache.http.HttpEntity)} method.
     */
    protected void consumeQuietly(HttpEntity entity) {
        try {
            EntityUtils.consume(entity);
        } catch (IOException e) {//ignore
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * These are the "hop-by-hop" headers that should not be copied.
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html
     * I use an HttpClient HeaderGroup class instead of Set<String> because this
     * approach does case insensitive lookup faster.
     */
    protected static final HeaderGroup hopByHopHeaders;

    static {
        hopByHopHeaders = new HeaderGroup();
        String[] headers = new String[]{
                "Connection", "Keep-Alive", "Proxy-Authenticate", "Proxy-Authorization",
                "TE", "Trailers", "Transfer-Encoding", "Upgrade"};
        for (String header : headers) {
            hopByHopHeaders.addHeader(new BasicHeader(header, null));
        }
    }

    /**
     * Copy request headers from the servlet client to the proxy request.
     */
    protected void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        // Get an Enumeration of all of the header names sent by the client
        Enumeration enumerationOfHeaderNames = servletRequest.getHeaderNames();
        while (enumerationOfHeaderNames.hasMoreElements()) {
            String headerName = (String) enumerationOfHeaderNames.nextElement();
            copyRequestHeader(servletRequest, proxyRequest, headerName);
        }
    }

    /**
     * Copy a request header from the servlet client to the proxy request.
     * This is easily overwritten to filter out certain headers if desired.
     */
    protected void copyRequestHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest,
                                     String headerName) {
        //Instead the content-length is effectively set via InputStreamEntity
        if (headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH))
            return;
        if (hopByHopHeaders.containsHeader(headerName))
            return;

        Enumeration headers = servletRequest.getHeaders(headerName);
        while (headers.hasMoreElements()) {//sometimes more than one value
            String headerValue = (String) headers.nextElement();
            // In case the proxy host is running multiple virtual servers,
            // rewrite the Host header to ensure that we get content from
            // the correct virtual server
            if (headerName.equalsIgnoreCase(HttpHeaders.HOST)) {
                HttpHost host = targetHost;
                headerValue = host.getHostName();
                if (host.getPort() != -1)
                    headerValue += ":" + host.getPort();
            } else if (headerName.equalsIgnoreCase(org.apache.http.cookie.SM.COOKIE)) {
                headerValue = getRealCookie(headerValue);
            }
            proxyRequest.addHeader(headerName, headerValue);
        }
    }

    private void setXForwardedForHeader(HttpServletRequest servletRequest,
                                        HttpRequest proxyRequest) {
        if (doForwardIP) {
            String headerName = "X-Forwarded-For";
            String newHeader = servletRequest.getRemoteAddr();
            String existingHeader = servletRequest.getHeader(headerName);
            if (existingHeader != null) {
                newHeader = existingHeader + ", " + newHeader;
            }
            proxyRequest.setHeader(headerName, newHeader);
        }
    }

    /**
     * Copy proxied response headers back to the servlet client.
     */
    protected void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
                                       HttpServletResponse servletResponse) {
        for (Header header : proxyResponse.getAllHeaders()) {
            copyResponseHeader(servletRequest, servletResponse, header);
        }
    }

    /**
     * Copy a proxied response header back to the servlet client.
     * This is easily overwritten to filter out certain headers if desired.
     */
    protected void copyResponseHeader(HttpServletRequest servletRequest,
                                      HttpServletResponse servletResponse, Header header) {
        String headerName = header.getName();
        if (hopByHopHeaders.containsHeader(headerName))
            return;
        String headerValue = header.getValue();
        if (headerName.equalsIgnoreCase(org.apache.http.cookie.SM.SET_COOKIE) ||
                headerName.equalsIgnoreCase(org.apache.http.cookie.SM.SET_COOKIE2)) {
            copyProxyCookie(servletRequest, servletResponse, headerValue);
        } else if (headerName.equalsIgnoreCase(HttpHeaders.LOCATION)) {
            // LOCATION Header may have to be rewritten.
            servletResponse.addHeader(headerName, rewriteUrlFromResponse(servletRequest, headerValue));
        } else {
            servletResponse.addHeader(headerName, headerValue);
        }
    }

    /**
     * Copy cookie from the proxy to the servlet client.
     * Replaces cookie path to local path and renames cookie to avoid collisions.
     */
    protected void copyProxyCookie(HttpServletRequest servletRequest,
                                   HttpServletResponse servletResponse, String headerValue) {
        List<HttpCookie> cookies = HttpCookie.parse(headerValue);
        String path = servletRequest.getContextPath(); // path starts with / or is empty string
        path += servletRequest.getServletPath(); // servlet path starts with / or is empty string

        for (HttpCookie cookie : cookies) {
            //set cookie name prefixed w/ a proxy value so it won't collide w/ other cookies
            String proxyCookieName = getCookieNamePrefix() + cookie.getName();
            Cookie servletCookie = new Cookie(proxyCookieName, cookie.getValue());
            servletCookie.setComment(cookie.getComment());
            servletCookie.setMaxAge((int) cookie.getMaxAge());
            servletCookie.setPath(path); //set to the path of the proxy servlet
            // don't set cookie domain
            servletCookie.setSecure(cookie.getSecure());
            servletCookie.setVersion(cookie.getVersion());
            servletResponse.addCookie(servletCookie);
        }
    }

    /**
     * Take any client cookies that were originally from the proxy and prepare them to send to the
     * proxy.  This relies on cookie headers being set correctly according to RFC 6265 Sec 5.4.
     * This also blocks any local cookies from being sent to the proxy.
     */
    protected String getRealCookie(String cookieValue) {
        StringBuilder escapedCookie = new StringBuilder();
        String cookies[] = cookieValue.split("; ");
        for (String cookie : cookies) {
            String cookieSplit[] = cookie.split("=");
            if (cookieSplit.length == 2) {
                String cookieName = cookieSplit[0];
                if (cookieName.startsWith(getCookieNamePrefix())) {
                    cookieName = cookieName.substring(getCookieNamePrefix().length());
                    if (escapedCookie.length() > 0) {
                        escapedCookie.append("; ");
                    }
                    escapedCookie.append(cookieName).append("=").append(cookieSplit[1]);
                }
            }

            cookieValue = escapedCookie.toString();
        }
        return cookieValue;
    }

    /**
     * The string prefixing rewritten cookies.
     */
    protected String getCookieNamePrefix() {
        return "!Proxy!" + servletConfig.getServletName();
    }

    /**
     * Copy response body data (the entity) from the proxy to the servlet client.
     */
    protected void copyResponseEntity(HttpResponse proxyResponse, HttpServletResponse servletResponse,
                                      HttpRequest proxyRequest, HttpServletRequest servletRequest)
            throws IOException {
        HttpEntity entity = proxyResponse.getEntity();
        if (entity != null) {
            OutputStream servletOutputStream = servletResponse.getOutputStream();
            try {
                entity.writeTo(servletOutputStream);
            }
            //broken pipe 에러는 프록시측의 소켓이 먼저 종료되었을경우로 주로 404 에러상황에서 자주 발생한다.
            //특별한 실패 처리는 하지 않도록 한다.
            catch (IOException ex) {
                logger.warn("Pipe closed while copyResponseEntity", ex);
            }
        }
    }

    /**
     * Reads the request URI from {@code servletRequest} and rewrites it, considering targetUri.
     * It's used to make the new request.
     */
    protected String rewriteUrlFromRequest(HttpServletRequest servletRequest) {
        StringBuilder uri = new StringBuilder(500);
        uri.append(host);

        if (!StringUtils.isEmpty(path)) {
            uri.append(encodeUriQuery(path));
        }

        // Handle the query string & fragment
        String queryString = servletRequest.getQueryString();//ex:(following '?'): name=value&foo=bar#fragment
        String fragment = null;
        //split off fragment from queryString, updating queryString if found
        if (queryString != null) {
            int fragIdx = queryString.indexOf('#');
            if (fragIdx >= 0) {
                fragment = queryString.substring(fragIdx + 1);
                queryString = queryString.substring(0, fragIdx);
            }
        }

        queryString = rewriteQueryStringFromRequest(servletRequest, queryString);
        if (queryString != null && queryString.length() > 0) {
            uri.append('?');
            uri.append(encodeUriQuery(queryString));
        }

        if (doSendUrlFragment && fragment != null) {
            uri.append('#');
            uri.append(encodeUriQuery(fragment));
        }
        return uri.toString();
    }

    protected String rewriteQueryStringFromRequest(HttpServletRequest servletRequest, String queryString) {
        return queryString;
    }

    /**
     * For a redirect response from the target server, this translates {@code theUrl} to redirect to
     * and translates it to one the original client can use.
     */
    protected String rewriteUrlFromResponse(HttpServletRequest servletRequest, String theUrl) {
        //TODO document example paths
        final String targetUri = host;
        if (theUrl.startsWith(targetUri)) {
      /*-
       * The URL points back to the back-end server.
       * Instead of returning it verbatim we replace the target path with our
       * source path in a way that should instruct the original client to
       * request the URL pointed through this Proxy.
       * We do this by taking the current request and rewriting the path part
       * using this servlet's absolute path and the path from the returned URL
       * after the base target URL.
       */
            StringBuffer curUrl = servletRequest.getRequestURL();//no query
            int pos;
            // Skip the protocol part
            if ((pos = curUrl.indexOf("://")) >= 0) {
                // Skip the authority part
                // + 3 to skip the separator between protocol and authority
                if ((pos = curUrl.indexOf("/", pos + 3)) >= 0) {
                    // Trim everything after the authority part.
                    curUrl.setLength(pos);
                }
            }
            // Context path starts with a / if it is not blank
            curUrl.append(servletRequest.getContextPath());
            // Servlet path starts with a / if it is not blank
            curUrl.append(servletRequest.getServletPath());
            curUrl.append(theUrl, targetUri.length(), theUrl.length());
            theUrl = curUrl.toString();
        }
        return theUrl;
    }


    /**
     * Encodes characters in the query or fragment part of the URI.
     * <p/>
     * <p>Unfortunately, an incoming URI sometimes has characters disallowed by the spec.  HttpClient
     * insists that the outgoing proxied request has a valid URI because it uses Java's {@link URI}.
     * To be more forgiving, we must escape the problematic characters.  See the URI class for the
     * spec.
     *
     * @param in example: name=value&foo=bar#fragment
     */
    protected static CharSequence encodeUriQuery(CharSequence in) {
        //Note that I can't simply use URI.java to encode because it will escape pre-existing escaped things.
        StringBuilder outBuf = null;
        Formatter formatter = null;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            boolean escape = true;
            if (c < 128) {
                if (asciiQueryChars.get((int) c)) {
                    escape = false;
                }
            } else if (!Character.isISOControl(c) && !Character.isSpaceChar(c)) {//not-ascii
                escape = false;
            }
            if (!escape) {
                if (outBuf != null)
                    outBuf.append(c);
            } else {
                //escape
                if (outBuf == null) {
                    outBuf = new StringBuilder(in.length() + 5 * 3);
                    outBuf.append(in, 0, i);
                    formatter = new Formatter(outBuf);
                }
                //leading %, 0 padded, width 2, capital hex
                formatter.format("%%%02X", (int) c);//TODO
            }
        }
        return outBuf != null ? outBuf : in;
    }

    protected static final BitSet asciiQueryChars;

    static {
        char[] c_unreserved = "_-!.~'()*".toCharArray();//plus alphanum
        char[] c_punct = ",;:$&+=".toCharArray();
        char[] c_reserved = "?/[]@".toCharArray();//plus punct

        asciiQueryChars = new BitSet(128);
        for (char c = 'a'; c <= 'z'; c++) asciiQueryChars.set((int) c);
        for (char c = 'A'; c <= 'Z'; c++) asciiQueryChars.set((int) c);
        for (char c = '0'; c <= '9'; c++) asciiQueryChars.set((int) c);
        for (char c : c_unreserved) asciiQueryChars.set((int) c);
        for (char c : c_punct) asciiQueryChars.set((int) c);
        for (char c : c_reserved) asciiQueryChars.set((int) c);

        asciiQueryChars.set((int) '%');//leave existing percent escapes in place
    }
}
