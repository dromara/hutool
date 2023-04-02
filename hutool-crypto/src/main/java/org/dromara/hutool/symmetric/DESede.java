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

import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.KeyUtil;
import org.dromara.hutool.Mode;
import org.dromara.hutool.Padding;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * DESede是由DES对称加密算法改进后的一种对称加密算法，又名3DES、TripleDES。<br>
 * 使用 168 位的密钥对资料进行三次加密的一种机制；它通常（但非始终）提供极其强大的安全性。<br>
 * 如果三个 56 位的子元素都相同，则三重 DES 向后兼容 DES。<br>
 * Java中默认实现为：DESede/ECB/PKCS5Padding
 *
 * @author Looly
 * @since 3.3.0
 */
public class DESede extends SymmetricCrypto {
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，默认DESede/ECB/PKCS5Padding，使用随机密钥
	 */
	public DESede() {
		super(SymmetricAlgorithm.DESede);
	}

	/**
	 * 构造，使用默认的DESede/ECB/PKCS5Padding
	 *
	 * @param key 密钥
	 */
	public DESede(final byte[] key) {
		super(SymmetricAlgorithm.DESede, key);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public DESede(final Mode mode, final Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 */
	public DESede(final Mode mode, final Padding padding, final byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DESede(final Mode mode, final Padding padding, final byte[] key, final byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @since 3.3.0
	 */
	public DESede(final Mode mode, final Padding padding, final SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度24位
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DESede(final Mode mode, final Padding padding, final SecretKey key, final IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 */
	public DESede(final String mode, final String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度24位
	 */
	public DESede(final String mode, final String padding, final byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度24位
	 * @param iv 加盐
	 */
	public DESede(final String mode, final String padding, final byte[] key, final byte[] iv) {
		this(mode, padding, KeyUtil.generateKey(SymmetricAlgorithm.DESede.getValue(), key), null == iv ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥
	 */
	public DESede(final String mode, final String padding, final SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥
	 * @param iv 加盐
	 */
	public DESede(final String mode, final String padding, final SecretKey key, final IvParameterSpec iv) {
		super(StrUtil.format("{}/{}/{}", SymmetricAlgorithm.DESede.getValue(), mode, padding), key, iv);
	}
	// ------------------------------------------------------------------------- Constructor end
}
