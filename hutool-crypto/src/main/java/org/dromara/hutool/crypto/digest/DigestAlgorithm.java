/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
