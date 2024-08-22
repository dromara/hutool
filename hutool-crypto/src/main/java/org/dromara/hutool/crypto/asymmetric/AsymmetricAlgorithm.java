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

package org.dromara.hutool.crypto.asymmetric;

/**
 * 非对称算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
 *
 * @author Looly
 *
 */
public enum AsymmetricAlgorithm {
	/** RSA算法 */
	RSA("RSA"),
	/** RSA算法，此算法用了默认补位方式为RSA/ECB/PKCS1Padding */
	RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding"),
	/** RSA算法，此算法用了默认补位方式为RSA/ECB/NoPadding */
	RSA_ECB("RSA/ECB/NoPadding"),
	/** RSA算法，此算法用了RSA/None/NoPadding */
	RSA_None("RSA/None/NoPadding");

	private final String value;

	/**
	 * 构造
	 * @param value 算法字符表示，区分大小写
	 */
	AsymmetricAlgorithm(final String value) {
		this.value = value;
	}

	/**
	 * 获取算法字符串表示，区分大小写
	 * @return 算法字符串表示
	 */
	public String getValue() {
		return this.value;
	}
}
