/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencloudengine.garuda.wrapper.message;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * HTTP Message를 처리하는 다양한 유틸리티를 제공하는 Helper.
 *
 * @author Edward KIM
 * @version 0.1
 */
public class HttpMessageHelper {

	/**
	 * HTTP Header : {@code "Garuda-Message"}
	 */
	public static final CharSequence MESSAGE = newEntity("Garuda-Messge");

	/**
	 * HTTP Header : {@code "Garuda-Code"}
	 */
	public static final CharSequence CODE = newEntity("Garuda-Code");

	/**
	 * HTTP Header : {@code "Garuda-Cause"}
	 */
	public static final CharSequence CAUSE = newEntity("Garuda-Cause");

	/**
	 * HTTP Response Code : 600 Invalid Request Message (Not HTTP)
	 */
	public static final HttpResponseStatus NO_HTTP_MESSAGE = new HttpResponseStatus(APIServiceStatus.NO_HTTP_MESSAGE.getCode(), APIServiceStatus.NO_HTTP_MESSAGE.getMessage());

	/**
	 * HTTP Response Code : 601 No Required Header (Access, Secret Key)
	 */
	public static final HttpResponseStatus NO_REQUIRED_HEADER = new HttpResponseStatus(APIServiceStatus.NO_REQUIRED_HEADER.getCode(), APIServiceStatus.NO_REQUIRED_HEADER.getMessage());

	/**
	 * HTTP Response Code : 605 No Required Header_VM (Access, Secret Key)
	 */
	public static final HttpResponseStatus NO_REQUIRED_HEADER_VM = new HttpResponseStatus(APIServiceStatus.NO_REQUIRED_HEADER_VM.getCode(), APIServiceStatus.NO_REQUIRED_HEADER_VM.getMessage());

	/**
	 * HTTP Response Code : 602 Incorrect Secret Key
	 */
	public static final HttpResponseStatus INCORRECT_SECRET_KEY = new HttpResponseStatus(APIServiceStatus.INCORRECT_SECRET_KEY.getCode(), APIServiceStatus.INCORRECT_SECRET_KEY.getMessage());

	/**
	 * HTTP Response Code : 603 No Application
	 */
	public static final HttpResponseStatus NO_APPLICATION = new HttpResponseStatus(APIServiceStatus.NO_APPLICATION.getCode(), APIServiceStatus.NO_APPLICATION.getMessage());

	/**
	 * HTTP Response Code : 603 Not Connect API Server
	 */
	public static final HttpResponseStatus NOT_CONNECT_SERVICE = new HttpResponseStatus(APIServiceStatus.NOT_CONNECT_SERVICE.getCode(), APIServiceStatus.NOT_CONNECT_SERVICE.getMessage());

	/**
	 * HTTP Response Code : 800 Unknown Server Error
	 */
	public static final HttpResponseStatus UNKNOWN_SERVER_ERROR = new HttpResponseStatus(APIServiceStatus.UNKNOWN_SERVER_ERROR.getCode(), APIServiceStatus.UNKNOWN_SERVER_ERROR.getMessage());

	/**
	 * HTTP Response Code : 605 Not Allowed Method Error
	 */
	public static final HttpResponseStatus NOT_ALLOW_METHOD = new HttpResponseStatus(APIServiceStatus.NOT_ALLOW_METHOD.getCode(), APIServiceStatus.NOT_ALLOW_METHOD.getMessage());

	/**
	 * HTTP Header의 name, value를 재사용하기 위한 {@link CharSequence}를 생성한다.
	 *
	 * @return HTTP Header의 name 또는 value를 구성하는 문자열
	 */
	public static CharSequence newEntity(String name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		return new HttpHeaderEntity(name);
	}

	/**
	 * API Service를 처리할 수 없는 에러가 발생한 경우 Unknown Server Error를 생성한다.
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage unknownServerError(Throwable cause) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, UNKNOWN_SERVER_ERROR.reasonPhrase());
		response.headers().set(CODE, UNKNOWN_SERVER_ERROR.code());
		response.headers().set(CAUSE, cause.getMessage());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 * API Service를 요청한 요청자의 메시지가 HTTP 메시지가 아닌 경우 요청자에게 전달하기 위한 Invalid Message HTTP Response를 생성한다.
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage noHttpRequest() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, NO_HTTP_MESSAGE.reasonPhrase());
		response.headers().set(CODE, NO_HTTP_MESSAGE.code());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 * API Service를 요청한 요청자의 메시지가 Access & Secret Key가 아닌 경우 요청자에게 전달하기 위한 No Required Message HTTP Response 를 생성한다.
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage noRequiredHttpHeader() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, NO_REQUIRED_HEADER.reasonPhrase());
		response.headers().set(CODE, NO_REQUIRED_HEADER.code());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 *
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage noRequiredHttpHeaderVm() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, NO_REQUIRED_HEADER_VM.reasonPhrase());
		response.headers().set(CODE, NO_REQUIRED_HEADER_VM.code());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 * API Service를 요청한 요청자의 메시지의 Secret Key가 틀린 경우 요청자에게 전달하기 위한 Incorrect Secret Key HTTP Response 를 생성한다.
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage incorrectSecretKey() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, INCORRECT_SECRET_KEY.reasonPhrase());
		response.headers().set(CODE, INCORRECT_SECRET_KEY.code());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 * 존재하지 않는 API Service를 요청한 경우 No Application HTTP Response를 생성한다.
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage noApplication() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, NO_APPLICATION.reasonPhrase());
		response.headers().set(CODE, NO_APPLICATION.code());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 * 개발자가 배포한 API Service에 접근할 수 없는 경우 Not Connect API Server HTTP Response를 생성한다.
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage notConnectService() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, NOT_CONNECT_SERVICE.reasonPhrase());
		response.headers().set(CODE, NOT_CONNECT_SERVICE.code());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}

	/**
	 * API Service 중에 PUT 메서드를 호출 한 경우 NOT Allowed Method API Server HTTP Response를 생성한다.
	 *
	 * @return HTTP Response Message
	 */
	public static FullHttpMessage notAllowMethod() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
		response.headers().set(CONTENT_TYPE, "application/json");
		response.headers().set(MESSAGE, NOT_ALLOW_METHOD.reasonPhrase());
		response.headers().set(CODE, NOT_ALLOW_METHOD.code());
		response.headers().set(CONTENT_LENGTH, 0);
		return response;
	}
}
