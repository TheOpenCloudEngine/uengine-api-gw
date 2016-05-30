package org.opencloudengine.garuda.wrapper.message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.opencloudengine.garuda.wrapper.Constants;
import org.opencloudengine.garuda.wrapper.exception.APIServiceException;
import org.opencloudengine.garuda.wrapper.message.APIServiceStatus;

import java.util.List;
import java.util.Map;

import static org.opencloudengine.garuda.util.StringUtils.isEmpty;

/**
 * External Application에서 요청한 HTTP Request의 Wrapper.
 *
 * @author Edward KIM
 * @version 0.1
 */
public class APIServiceRequest {

	/**
	 * Netty의 Channel Handler Context.
	 */
	private ChannelHandlerContext ctx;

	/**
	 * Netty HTTP Request Wrapper.
	 */
	private HttpRequest httpRequest;

	/**
	 * Netty HTTP Content (Body) Wrapper.
	 */
	private HttpContent content;

	/**
	 * HTTP Request의 Query String 디코더.
	 */
	private QueryStringDecoder decoder;

	/**
	 * 기본 생성자.
	 *
	 * @param ctx         Netty의 Channel Handler Context
	 * @param httpRequest HTTP Request
	 * @param content     {@link io.netty.handler.codec.http.HttpContent}
	 * @param decoder     Query String Decoder
	 */
	public APIServiceRequest(ChannelHandlerContext ctx, HttpRequest httpRequest, HttpContent content, QueryStringDecoder decoder) {
		this.ctx = ctx;
		this.httpRequest = httpRequest;
		this.content = content;
		this.decoder = decoder;
	}

	/**
	 * API Service Request를 생성한다. External Application에서 송신한 메시지가 HTTP Request가 아닌 경우
	 * 예외를 발생시킨다.
	 *
	 * @param ctx     {@link io.netty.channel.ChannelHandlerContext}
	 * @param msg     External Application에서 송신한 Request Message
	 * @param content {@link io.netty.handler.codec.http.HttpContent}
	 * @return API Service Request
	 * @throws APIServiceException HTTP Request가 아닌 경우
	 */
	public static APIServiceRequest parse(final ChannelHandlerContext ctx, Object msg, HttpContent content) {
		if (msg instanceof HttpRequest) {
			DefaultHttpRequest req = (DefaultHttpRequest) msg;
			QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
			return new APIServiceRequest(ctx, req, content, decoder);
		}
		throw new APIServiceException(APIServiceStatus.NO_HTTP_MESSAGE);
	}

	public String path() {
		return decoder.path();
	}

	public Map<String, List<String>> parameters() {
		return decoder.parameters();
	}

	public HttpMethod getMethod() {
		return httpRequest.getMethod();
	}

	public String getUri() {
		return httpRequest.getUri();
	}

	public HttpHeaders headers() {
		return httpRequest.headers();
	}

	public HttpVersion getProtocolVersion() {
		return httpRequest.getProtocolVersion();
	}

	public String getServiceId() {
		return this.httpRequest.headers().get(Constants.HTTP_HEADER_SERVICE_ID);
	}

	public String getAPIId() {
		return this.httpRequest.headers().get(Constants.HTTP_HEADER_API_ID);
	}

	public String getUserAccessKey() {
		return this.httpRequest.headers().get(Constants.HTTP_HEADER_USER_ACCESS_KEY);
	}

	public String getAppAccessKey() {
		return this.httpRequest.headers().get(Constants.HTTP_HEADER_SERVICE_ACCESS_KEY);
	}

	public String getAdmin() {
		return this.httpRequest.headers().get(Constants.HTTP_HEADER_ADMIN);
		}
	public String getOwner() {
		return this.httpRequest.headers().get(Constants.HTTP_HEADER_OWNER);
	}
	public String getUser() {
		return this.httpRequest.headers().get(Constants.HTTP_HEADER_USER);
	}


	public HttpRequest getHttpRequest() {
		return httpRequest;
	}

	public HttpContent getContent() {
		return content;
	}

	public boolean isValid() {
		return !isEmpty(getUserAccessKey()) || !isEmpty(getAppAccessKey()) || !isEmpty(getServiceId()) || !isEmpty(getAPIId());
	}
}
