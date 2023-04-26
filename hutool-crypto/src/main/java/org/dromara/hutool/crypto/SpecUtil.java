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

package org.dromara.hutool.crypto;

import org.dromara.hutool.core.util.RandomUtil;

import javax.crypto.spec.*;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

/**
 * 规范相关工具类，用于生成密钥规范、参数规范等快捷方法。
 * <ul>
 *     <li>{@link KeySpec}: 密钥规范</li>
 *     <li>{@link java.security.spec.AlgorithmParameterSpec}: 参数规范</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class SpecUtil {

	/**
	 * 根据算法创建{@link KeySpec}
	 * <ul>
	 *     <li>DESede: {@link DESedeKeySpec}</li>
	 *     <li>DES   : {@link DESedeKeySpec}</li>
	 *     <li>其它  : {@link SecretKeySpec}</li>
	 * </ul>
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @return {@link KeySpec}
	 */
	public static KeySpec createKeySpec(final String algorithm, byte[] key) {
		try {
			if (algorithm.startsWith("DESede")) {
				if (null == key) {
					key = RandomUtil.randomBytes(24);
				}
				// DESede兼容
				return new DESedeKeySpec(key);
			} else if (algorithm.startsWith("DES")) {
				if (null == key) {
					key = RandomUtil.randomBytes(8);
				}
				return new DESKeySpec(key);
			}
		} catch (final InvalidKeyException e) {
			throw new CryptoException(e);
		}

		return new SecretKeySpec(key, algorithm);
	}

	/**
	 * 创建{@link PBEKeySpec}<br>
	 * PBE算法没有密钥的概念，密钥在其它对称加密算法中是经过算法计算得出来的，PBE算法则是使用口令替代了密钥。
	 *
	 * @param password 口令
	 * @return {@link PBEKeySpec}
	 */
	public static PBEKeySpec createPBEKeySpec(char[] password) {
		if (null == password) {
			password = RandomUtil.randomString(32).toCharArray();
		}
		return new PBEKeySpec(password);
	}

	/**
	 * 创建{@link PBEParameterSpec}
	 *
	 * @param salt           加盐值
	 * @param iterationCount 摘要次数
	 * @return {@link PBEParameterSpec}
	 */
	public static PBEParameterSpec createPBEParameterSpec(final byte[] salt, final int iterationCount) {
		return new PBEParameterSpec(salt, iterationCount);
	}
}
