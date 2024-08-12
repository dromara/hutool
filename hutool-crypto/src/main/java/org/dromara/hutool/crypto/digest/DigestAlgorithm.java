/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.crypto.digest;

/**
 * 摘要算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest
 *
 * @author Looly
 */
public enum DigestAlgorithm {
	/**
	 * MD2
	 */
	MD2("MD2"),
	/**
	 * MD5
	 */
	MD5("MD5"),
	/**
	 * SHA-1
	 */
	SHA1("SHA-1"),
	/**
	 * SHA-256
	 */
	SHA256("SHA-256"),
	/**
	 * SHA-384
	 */
	SHA384("SHA-384"),
	/**
	 * SHA-512
	 */
	SHA512("SHA-512");

	private final String value;

	/**
	 * 构造
	 *
	 * @param value 算法字符串表示
	 */
	DigestAlgorithm(final String value) {
		this.value = value;
	}

	/**
	 * 获取算法字符串表示
	 * @return 算法字符串表示
	 */
	public String getValue() {
		return this.value;
	}
}
