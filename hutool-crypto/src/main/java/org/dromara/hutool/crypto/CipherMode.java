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

package org.dromara.hutool.crypto;

import javax.crypto.Cipher;

/**
 * Cipher模式的枚举封装
 *
 * @author looly
 * @since 5.4.3
 */
public enum CipherMode {
	/**
	 * 加密模式
	 */
	ENCRYPT(Cipher.ENCRYPT_MODE),
	/**
	 * 解密模式
	 */
	DECRYPT(Cipher.DECRYPT_MODE),
	/**
	 * 包装模式
	 */
	WRAP(Cipher.WRAP_MODE),
	/**
	 * 拆包模式
	 */
	UNWRAP(Cipher.UNWRAP_MODE);


	/**
	 * 构造
	 *
	 * @param value 见{@link Cipher}
	 */
	CipherMode(final int value) {
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
