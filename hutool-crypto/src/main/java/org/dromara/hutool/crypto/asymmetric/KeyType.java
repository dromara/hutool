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

import javax.crypto.Cipher;

/**
 * 密钥类型
 *
 * @author Looly
 *
 */
public enum KeyType {
	/**
	 * 公钥
	 */
	PublicKey(Cipher.PUBLIC_KEY),
	/**
	 * 私钥
	 */
	PrivateKey(Cipher.PRIVATE_KEY),
	/**
	 * 密钥
	 */
	SecretKey(Cipher.SECRET_KEY);


	/**
	 * 构造
	 *
	 * @param value 见{@link Cipher}
	 */
	KeyType(final int value) {
		this.value = value;
	}

	private final int value;

	/**
	 * 获取枚举值对应的int表示
	 *
	 * @return 枚举值对应的int表示
	 */
	public int getValue() {
		return this.value;
	}
}
