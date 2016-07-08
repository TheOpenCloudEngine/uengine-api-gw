package org.opencloudengine.garuda.gateway;

/**
 * Created by uengine on 2016. 6. 12..
 */

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.opencloudengine.garuda.util.ApplicationContextRegistry;
import org.springframework.context.ApplicationContext;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * An HTTP reverse proxy/gateway servlet. It is designed to be extended for customization
 * if desired. Most of the work is handled by
 * <a href="http://hc.apache.org/httpcomponents-client-ga/">Apache HttpClient</a>.
 * <p>
 * There are alternatives to a servlet based proxy such as Apache mod_proxy if that is available to you. However
 * this servlet is easily customizable by Java, secure-able by your web application's authentication (e.g. spring-authentication),
 * portable across servlet engines, and is embeddable into another web application.
 * </p>
 * <p>
 * Inspiration: http://httpd.apache.org/docs/2.0/mod/mod_proxy.html
 * </p>
 *
 * @author David Smiley dsmiley@mitre.org
 */
public class GatewayServlet extends HttpServlet {

    private HttpClient proxyClient;

    private HttpClient proxyHttpsClient;

    @Override
    public String getServletInfo() {
        return "A proxy servlet by David Smiley, dsmiley@apache.org";
    }

    /**
     * Reads a configuration parameter. By default it reads servlet init parameters but
     * it can be overridden.
     */
    public String getConfigParam(String key) {
        return getServletConfig().getInitParameter(key);
    }

    @Override
    public void init() throws ServletException {
        HttpParams hcParams = new BasicHttpParams();
        hcParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
        hcParams.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false); // See #70
        readConfigParam(hcParams, ClientPNames.HANDLE_REDIRECTS, Boolean.class);
        proxyClient = createHttpClient(hcParams);
        proxyHttpsClient = createHttpsClient(hcParams);
    }

    /**
     * Called from {@link #init(javax.servlet.ServletConfig)}. HttpClient offers many opportunities
     * for customization. By default,
     * <a href="http://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/impl/client/SystemDefaultHttpClient.html">
     * SystemDefaultHttpClient</a> is used if available, otherwise it falls
     * back to:
     * <pre>new DefaultHttpClient(new ThreadSafeClientConnManager(),hcParams)</pre>
     * SystemDefaultHttpClient uses PoolingClientConnectionManager. In any case, it should be thread-safe.
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    protected HttpClient createHttpClient(HttpParams hcParams) {
        try {
            //as of HttpComponents v4.2, this class is better since it uses System
            // Properties:
            Class clientClazz = Class.forName("org.apache.http.impl.client.SystemDefaultHttpClient");
            Constructor constructor = clientClazz.getConstructor(HttpParams.class);
            return (HttpClient) constructor.newInstance(hcParams);
        } catch (ClassNotFoundException e) {
            //no problem; use v4.1 below
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Fallback on using older client:
        return new DefaultHttpClient(new ThreadSafeClientConnManager(), hcParams);
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    protected HttpClient createHttpsClient(HttpParams hcParams) {
        try {
            TrustManager easyTrustManager = new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    // no-op
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }
            };
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{easyTrustManager}, null);

            SSLSocketFactory socketFactory = new SSLSocketFactory(sslcontext,
                    SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

            Scheme sch = new Scheme("https", 443, socketFactory);

            try {
                //as of HttpComponents v4.2, this class is better since it uses System
                // Properties:
                Class clientClazz = Class.forName("org.apache.http.impl.client.SystemDefaultHttpClient");
                Constructor constructor = clientClazz.getConstructor(HttpParams.class);
                HttpClient httpClient = (HttpClient) constructor.newInstance(hcParams);
                httpClient.getConnectionManager().getSchemeRegistry().register(sch);
                return httpClient;
            } catch (ClassNotFoundException e) {
                //no problem; use v4.1 below
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //Fallback on using older client:
            DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(), hcParams);
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);
            return httpClient;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * The http client used.
     *
     * @see #createHttpClient(HttpParams)
     */
    public HttpClient getProxyClient() {
        return proxyClient;
    }

    public HttpClient getProxyHttpsClient() {
        return proxyHttpsClient;
    }

    /**
     * Reads a servlet config parameter by the name {@code hcParamName} of type {@code type}, and
     * set it in {@code hcParams}.
     */
    protected void readConfigParam(HttpParams hcParams, String hcParamName, Class type) {
        String val_str = getConfigParam(hcParamName);
        if (val_str == null)
            return;
        Object val_obj;
        if (type == String.class) {
            val_obj = val_str;
        } else {
            try {
                //noinspection unchecked
                val_obj = type.getMethod("valueOf", String.class).invoke(type, val_str);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        hcParams.setParameter(hcParamName, val_obj);
    }

    @Override
    public void destroy() {
        //As of HttpComponents v4.3, clients implement closeable
        if (proxyClient instanceof Closeable) {//TODO AutoCloseable in Java 1.6
            try {
                ((Closeable) proxyClient).close();
            } catch (IOException e) {
                log("While destroying servlet, shutting down HttpClient: " + e, e);
            }
        } else {
            //Older releases require we do this:
            if (proxyClient != null)
                proxyClient.getConnectionManager().shutdown();
        }
        super.destroy();
    }

    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse)
            throws ServletException, IOException {

        ApplicationContext context = ApplicationContextRegistry.getApplicationContext();
        GatewayService gatewayService = context.getBean(GatewayService.class);
        gatewayService.start(this, servletRequest, servletResponse);
    }
}
