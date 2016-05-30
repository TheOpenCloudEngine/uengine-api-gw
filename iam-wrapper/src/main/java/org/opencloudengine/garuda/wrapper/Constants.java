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
package org.opencloudengine.garuda.wrapper;

/**
 * 서비스 및 HTTP Header 등의 상수 코드를 유지하는 상수 클래스.
 *
 * @author Edward KIM
 * @version 0.1
 */
public class Constants {

	/**
	 * 클라이언트 요청시 HTTP Header에 포함되어야 할 User Access Key Header Name.
	 * 이 헤더는 클라이언트 요청시 반드시 포함되어야 하며 포함되지 않는 경우 에러가 발생한다.
	 */
	public static String HTTP_HEADER_USER_ACCESS_KEY = "Garuda-User-AccessKey";

	/**
	 * 클라이언트 요청시 HTTP Header에 포함되어야 할 Service Access Key Header Name.
	 * 이 헤더는 클라이언트 요청시 반드시 포함되어야 하며 포함되지 않는 경우 에러가 발생한다.
	 */
	public static String HTTP_HEADER_SERVICE_ACCESS_KEY = "Garuda-Service-AccessKey";

	/**
	 * 클라이언트 요청시 HTTP Header에 포함되어야 할 Service ID Header Name.
	 * 이 헤더는 클라이언트 요청시 반드시 포함되어야 하며 포함되지 않는 경우 에러가 발생한다.
	 */
	public static String HTTP_HEADER_SERVICE_ID = "Garuda-Service-Id";

	/**
	 * 클라이언트 요청시 HTTP Header에 포함되어야 할 API ID Header Name.
	 * 이 헤더는 클라이언트 요청시 반드시 포함되어야 하며 포함되지 않는 경우 에러가 발생한다.
	 */
	public static String HTTP_HEADER_API_ID = "Garuda-API-Id";

	/**
	 * 클라이언트 요청시 HTTP Header에 포함되어야 할 ADMIN Header Name.
	 * 이 헤더는 클라이언트 요청시 포함할 경우 VM 연동을하고 포함되지 않는 경우 VM 연동을 하지 않는다.
	 */
	public static String HTTP_HEADER_ADMIN = "admin";

	/**
	 * 클라이언트 요청시 HTTP Header에 포함되어야 할 owner Header Name.
	 * 이 헤더는 클라이언트 요청시 포함할 경우 VM 연동을하고 포함되지 않는 경우 VM 연동을 하지 않는다.
	 */
	public static String HTTP_HEADER_OWNER = "owner";

	/**
	 * 클라이언트 요청시 HTTP Header에 포함되어야 할 user Header Name.
	 * 이 헤더는 클라이언트 요청시 반드시 포함되어야 하며 포함하지 않은 경우 anoymous 로 입력된다.
	 */
	public static String HTTP_HEADER_USER = "user";
}
