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
package org.opencloudengine.garuda.wrapper.exception;

import org.opencloudengine.garuda.wrapper.message.APIServiceStatus;

/**
 * API Service 호출시 발생하는 모든 예외를 추상화하는 서비스 예외.
 * 이 API Service 예외는 예외처리를 간편하게 하기 위해서 {@link RuntimeException}을 상속하며
 * 예외 발생시 {@link org.opencloudengine.garuda.wrapper.message.APIServiceStatus} 및 원인이 되는
 * {@link Throwable}을 유지한다.
 *
 * @author Edward KIM
 * @version 0.1
 */
public class APIServiceException extends RuntimeException {

	/**
	 * 상태 코드 및 상태 코드 메시지
	 */
	private APIServiceStatus status;

	/**
	 * 기본 생성자.
	 */
	public APIServiceException() {
		super();
	}

	/**
	 * 기본 생성자.
	 *
	 * @param cause 예외 발생의 원인이 되는 예외
	 */
	public APIServiceException(Throwable cause) {
		super(cause);
	}

	/**
	 * 기본 생성자.
	 *
	 * @param status 상태 코드 및 상태 코드 메시지
	 */
	public APIServiceException(APIServiceStatus status) {
		this.status = status;
	}

	/**
	 * 기본 생성자.
	 *
	 * @param cause  예외 발생의 원인이 되는 예외
	 * @param status 상태 코드 및 상태 코드 메시지
	 */
	public APIServiceException(Throwable cause, APIServiceStatus status) {
		super(cause);
		this.status = status;
	}

	/**
	 * 상태 코드 및 상태 코드 메시지를 반환한다.
	 *
	 * @return 상태 코드 및 상태 코드 메시지
	 */
	public APIServiceStatus getStatus() {
		return status;
	}

	/**
	 * HTTP 상태 코드를 반환한다.
	 *
	 * @return HTTP 상태 코드
	 */
	public int getCode() {
		return status.getCode();
	}

	/**
	 * 상태 코드 메시지를 반환한다.
	 *
	 * @return 상태 코드 메시지
	 */
	public String getMessage() {
		return status.getMessage();
	}

	/**
	 * 예외 발생시 예외 메시지를 반환한다.
	 *
	 * @return 예외 메시지
	 */
	public String getExceptionMessage() {
		return super.getMessage();
	}
}
