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

package org.dromara.hutool.crypto.asymmetric;

/**
 * 签名算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#Signature
 *
 * @author Looly
 */
public enum SignAlgorithm {
	// The RSA signature algorithm
	NONEwithRSA("NONEwithRSA"),

	// The MD2/MD5 with RSA Encryption signature algorithm
	MD2withRSA("MD2withRSA"),
	MD5withRSA("MD5withRSA"),

	// The signature algorithm with SHA-* and the RSA
	SHA1withRSA("SHA1withRSA"),
	SHA256withRSA("SHA256withRSA"),
	SHA384withRSA("SHA384withRSA"),
	SHA512withRSA("SHA512withRSA"),

	// The Digital Signature Algorithm
	NONEwithDSA("NONEwithDSA"),
	// The DSA with SHA-1 signature algorithm
	SHA1withDSA("SHA1withDSA"),

	// The ECDSA signature algorithms
	NONEwithECDSA("NONEwithECDSA"),
	SHA1withECDSA("SHA1withECDSA"),
	SHA256withECDSA("SHA256withECDSA"),
	SHA384withECDSA("SHA384withECDSA"),
	SHA512withECDSA("SHA512withECDSA"),

	// 需要BC库加入支持
	SHA256withRSA_PSS("SHA256WithRSA/PSS"),
	SHA384withRSA_PSS("SHA384WithRSA/PSS"),
	SHA512withRSA_PSS("SHA512WithRSA/PSS");

	private final String value;

	/**
	 * 构造
	 *
	 * @param value 算法字符表示，区分大小写
	 */
	SignAlgorithm(final String value) {
		this.value = value;
	}

	/**
	 * 获取算法字符串表示，区分大小写
	 *
	 * @return 算法字符串表示
	 */
	public String getValue() {
		return this.value;
	}
}
