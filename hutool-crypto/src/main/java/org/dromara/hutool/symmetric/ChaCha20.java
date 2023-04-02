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

package org.dromara.hutool.symmetric;

import org.dromara.hutool.KeyUtil;
import org.dromara.hutool.util.RandomUtil;

import javax.crypto.spec.IvParameterSpec;

/**
 * ChaCha20算法实现<br>
 * ChaCha系列流密码，作为salsa密码的改良版，具有更强的抵抗密码分析攻击的特性，“20”表示该算法有20轮的加密计算。
 *
 * @author looly
 * @since 5.7.12
 */
public class ChaCha20 extends SymmetricCrypto {
	private static final long serialVersionUID = 1L;

	public static final String ALGORITHM_NAME = "ChaCha20";

	/**
	 * 构造
	 *
	 * @param key 密钥
	 * @param iv  加盐，12bytes（64bit）
	 */
	public ChaCha20(final byte[] key, final byte[] iv) {
		super(ALGORITHM_NAME,
				KeyUtil.generateKey(ALGORITHM_NAME, key),
				generateIvParam(iv));
	}

	/**
	 * 生成加盐参数
	 *
	 * @param iv 加盐
	 * @return {@link IvParameterSpec}
	 */
	private static IvParameterSpec generateIvParam(byte[] iv) {
		if (null == iv) {
			iv = RandomUtil.randomBytes(12);
		}
		return new IvParameterSpec(iv);
	}
}
