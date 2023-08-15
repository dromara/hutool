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

package org.dromara.hutool.crypto.digest.mac;

import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 默认的HMAC算法实现引擎，使用{@link Mac} 实现摘要<br>
 * 当引入BouncyCastle库时自动使用其作为Provider
 *
 * @author Looly
 * @since 4.5.13
 */
public class DefaultHMacEngine implements MacEngine {

	private Mac mac;

	// region ----- Constructor

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public DefaultHMacEngine(final String algorithm, final byte[] key) {
		this(algorithm, (null == key) ? null : new SecretKeySpec(key, algorithm));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public DefaultHMacEngine(final String algorithm, final Key key) {
		this(algorithm, key, null);
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @param spec      {@link AlgorithmParameterSpec}
	 * @since 5.7.12
	 */
	public DefaultHMacEngine(final String algorithm, final Key key, final AlgorithmParameterSpec spec) {
		init(algorithm, key, spec);
	}
	// endregion

	// region ----- init

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @return this
	 */
	public DefaultHMacEngine init(final String algorithm, final byte[] key) {
		return init(algorithm, (null == key) ? null : new SecretKeySpec(key, algorithm));
	}

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥 {@link SecretKey}
	 * @return this
	 * @throws CryptoException Cause by IOException
	 */
	public DefaultHMacEngine init(final String algorithm, final Key key) {
		return init(algorithm, key, null);
	}

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥 {@link SecretKey}
	 * @param spec      {@link AlgorithmParameterSpec}
	 * @return this
	 * @throws CryptoException Cause by IOException
	 * @since 5.7.12
	 */
	public DefaultHMacEngine init(final String algorithm, Key key, final AlgorithmParameterSpec spec) {
		try {
			mac = SecureUtil.createMac(algorithm);
			if (null == key) {
				key = KeyUtil.generateKey(algorithm);
			}
			if (null != spec) {
				mac.init(key, spec);
			} else {
				mac.init(key);
			}
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
		return this;
	}
	// endregion

	/**
	 * 获得 {@link Mac}
	 *
	 * @return {@link Mac}
	 */
	public Mac getMac() {
		return mac;
	}

	@Override
	public void update(final byte[] in) {
		this.mac.update(in);
	}

	@Override
	public void update(final byte[] in, final int inOff, final int len) {
		this.mac.update(in, inOff, len);
	}

	@Override
	public byte[] doFinal() {
		return this.mac.doFinal();
	}

	@Override
	public void reset() {
		this.mac.reset();
	}

	@Override
	public int getMacLength() {
		return mac.getMacLength();
	}

	@Override
	public String getAlgorithm() {
		return this.mac.getAlgorithm();
	}
}
