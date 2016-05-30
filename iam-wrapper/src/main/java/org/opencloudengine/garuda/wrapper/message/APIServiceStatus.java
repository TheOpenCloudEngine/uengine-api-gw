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

/**
 * API 호출의 상태 코드 열거형 타입.
 *
 * @author Edward KIM
 * @version 0.1
 */
public enum APIServiceStatus {

	NOT_ALLOW_METHOD(606, "PUT is not allowed method"),
	NO_HTTP_MESSAGE(600, "No HTTP Request Message"),
	NO_REQUIRED_HEADER(601, "No Required Header (User Access, Application Access Key, Application Id)"),
	INCORRECT_SECRET_KEY(602, "Incorrect Secret Key"),
	NO_APPLICATION(603, "No Application"),
	NOT_CONNECT_SERVICE(604, "Not Connect API Server"),
	NO_REQUIRED_HEADER_VM(605, "No Required Header (vmId, ipAddress, port, userId)"),
	UNKNOWN_SERVER_ERROR(800, "Unknown Server Error"),
	OK(999, "OK");

	/**
	 * 에러 코드. 만약에 REST 호출 실패시 HTTP Response의 Header에 <pre>Garuda-Code</pre>으로 에러 코드가 설정된다.
	 */
	private int code;

	/**
	 * 에러 메시지
	 */
	private String message;

	/**
	 * 기본 생성자.
	 *
	 * @param code    에러 코드.
	 * @param message 에러 메시지.
	 */
	APIServiceStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
