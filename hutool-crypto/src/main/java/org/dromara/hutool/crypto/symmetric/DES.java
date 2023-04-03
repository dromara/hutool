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

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.Mode;
import org.dromara.hutool.crypto.Padding;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加密算法实现<br>
 * DES全称为Data Encryption Standard，即数据加密标准，是一种使用密钥加密的块算法<br>
 * Java中默认实现为：DES/ECB/PKCS5Padding
 *
 * @author Looly
 * @since 3.0.8
 */
public class DES extends SymmetricCrypto {
	private static final long serialVersionUID = 1L;

	// ------------------------------------------------------------------------- Constrctor start
	/**
	 * 构造，默认DES/ECB/PKCS5Padding，使用随机密钥
	 */
	public DES() {
		super(SymmetricAlgorithm.DES);
	}

	/**
	 * 构造，使用默认的DES/ECB/PKCS5Padding
	 *
	 * @param key 密钥
	 */
	public DES(final byte[] key) {
		super(SymmetricAlgorithm.DES, key);
	}

	/**
	 * 构造，使用随机密钥
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 */
	public DES(final Mode mode, final Padding padding) {
		this(mode.name(), padding.name());
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 */
	public DES(final Mode mode, final Padding padding, final byte[] key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DES(final Mode mode, final Padding padding, final byte[] key, final byte[] iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 * @since 3.3.0
	 */
	public DES(final Mode mode, final Padding padding, final SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式{@link Mode}
	 * @param padding {@link Padding}补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 偏移向量，加盐
	 * @since 3.3.0
	 */
	public DES(final Mode mode, final Padding padding, final SecretKey key, final IvParameterSpec iv) {
		this(mode.name(), padding.name(), key, iv);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 */
	public DES(final String mode, final String padding) {
		this(mode, padding, (byte[]) null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 */
	public DES(final String mode, final String padding, final byte[] key) {
		this(mode, padding, KeyUtil.generateKey("DES", key), null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 加盐
	 */
	public DES(final String mode, final String padding, final byte[] key, final byte[] iv) {
		this(mode, padding, KeyUtil.generateKey("DES", key), null == iv ? null : new IvParameterSpec(iv));
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 */
	public DES(final String mode, final String padding, final SecretKey key) {
		this(mode, padding, key, null);
	}

	/**
	 * 构造
	 *
	 * @param mode 模式
	 * @param padding 补码方式
	 * @param key 密钥，长度：8的倍数
	 * @param iv 加盐
	 */
	public DES(final String mode, final String padding, final SecretKey key, final IvParameterSpec iv) {
		super(StrUtil.format("DES/{}/{}", mode, padding), key, iv);
	}
	// ------------------------------------------------------------------------- Constrctor end
}
