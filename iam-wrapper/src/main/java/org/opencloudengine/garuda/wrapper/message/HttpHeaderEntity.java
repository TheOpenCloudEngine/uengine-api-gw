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

import io.netty.util.CharsetUtil;

/**
 * HTTP Header의 name 또는 value에 들어갈 값을 재사용하기 위한 HTTP Header Entity.
 * 이 HTTP Header Entity는 재사용을 위해서 사용하며 헤더를 구성하는 name 또는 value와
 * name 또는 value의 해쉬 코드를 유지한다.
 *
 * @author Edward KIM
 * @version 0.1
 */
public class HttpHeaderEntity implements CharSequence {

	/**
	 * HTTP Header의 name 또는 value의 이름을 표현하는 필드.
	 */
	private final String name;

	/**
	 * {@link #name}의 해쉬코드.
	 */
	private final int hash;

	/**
	 * {@link #name}의 {@link io.netty.util.CharsetUtil#US_ASCII}으로 인코딩한 바이트 배열.
	 */
	private final byte[] bytes;

	public HttpHeaderEntity(String name) {
		this.name = name;
		this.hash = hash(name);
		this.bytes = name.getBytes(CharsetUtil.US_ASCII);
	}

	int hash() {
		return hash;
	}

	@Override
	public int length() {
		return bytes.length;
	}

	@Override
	public char charAt(int index) {
		return (char) bytes[index];
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new HttpHeaderEntity(name.substring(start, end));
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * 지정한 문자열의 해쉬 코드를 생성한다.
	 *
	 * @param name 해쉬 코드를 생성할 문자열
	 * @return 해쉬 코드
	 */
	static int hash(CharSequence name) {
		if (name instanceof HttpHeaderEntity) {
			return ((HttpHeaderEntity) name).hash();
		}
		int h = 0;
		for (int i = name.length() - 1; i >= 0; i--) {
			char c = name.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				c += 32;
			}
			h = 31 * h + c;
		}

		if (h > 0) {
			return h;
		} else if (h == Integer.MIN_VALUE) {
			return Integer.MAX_VALUE;
		} else {
			return -h;
		}
	}
}