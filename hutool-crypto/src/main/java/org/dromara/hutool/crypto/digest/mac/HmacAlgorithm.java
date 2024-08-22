/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.crypto.digest.mac;

/**
 * HMAC算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Mac
 *
 * @author Looly
 */
public enum HmacAlgorithm {
	/**
	 * HmacMD5
	 */
	HmacMD5("HmacMD5"),
	/**
	 * HmacSHA1
	 */
	HmacSHA1("HmacSHA1"),
	/**
	 * HmacSHA256
	 */
	HmacSHA256("HmacSHA256"),
	/**
	 * HmacSHA384
	 */
	HmacSHA384("HmacSHA384"),
	/**
	 * HmacSHA512
	 */
	HmacSHA512("HmacSHA512"),
	/**
	 * HmacSM3算法实现，需要BouncyCastle库支持
	 */
	HmacSM3("HmacSM3"),
	/**
	 * SM4 CMAC模式实现，需要BouncyCastle库支持
	 */
	SM4CMAC("SM4CMAC");

	private final String value;

	HmacAlgorithm(final String value) {
		this.value = value;
	}

	/**
	 * 获取算法名称值
	 *
	 * @return 算法名称值
	 */
	public String getValue() {
		return this.value;
	}
}
