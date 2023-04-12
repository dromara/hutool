/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.symmetric;

/**
 * 对称算法类型<br>
 * see: https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyGenerator
 *
 * @author Looly
 */
public enum SymmetricAlgorithm {
	/**
	 * 默认的AES加密方式：AES/ECB/PKCS5Padding
	 */
	AES("AES"),
	/**
	 * ARCFOUR
	 */
	ARCFOUR("ARCFOUR"),
	/**
	 * Blowfish
	 */
	Blowfish("Blowfish"),
	/**
	 * 默认的DES加密方式：DES/ECB/PKCS5Padding
	 */
	DES("DES"),
	/**
	 * 3DES算法，默认实现为：DESede/ECB/PKCS5Padding
	 */
	DESede("DESede"),
	/**
	 * RC2
	 */
	RC2("RC2"),
	/**
	 *PBEWithMD5AndDES
	 */
	PBEWithMD5AndDES("PBEWithMD5AndDES"),
	/**
	 * PBEWithSHA1AndDESede
	 */
	PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"),
	/**
	 * PBEWithSHA1AndRC2_40
	 */
	PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40");

	private final String value;

	/**
	 * 构造
	 *
	 * @param value 算法的字符串表示，区分大小写
	 */
	SymmetricAlgorithm(final String value) {
		this.value = value;
	}

	/**
	 * 获得算法的字符串表示形式
	 *
	 * @return 算法字符串
	 */
	public String getValue() {
		return this.value;
	}
}
