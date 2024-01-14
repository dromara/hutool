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

import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;
import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * JDK提供的的MAC算法实现引擎，使用{@link Mac} 实现摘要<br>
 * 当引入BouncyCastle库时自动使用其作为Provider
 *
 * @author Looly
 * @since 4.5.13
 */
public class JCEMacEngine extends SimpleWrapper<Mac> implements MacEngine {

	// region ----- Constructor
	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public JCEMacEngine(final String algorithm, final byte[] key) {
		this(algorithm, (null == key) ? null : new SecretKeySpec(key, algorithm));
	}

	/**
	 * 构造
	 *
	 * @param algorithm 算法
	 * @param key       密钥
	 * @since 4.5.13
	 */
	public JCEMacEngine(final String algorithm, final Key key) {
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
	public JCEMacEngine(final String algorithm, final Key key, final AlgorithmParameterSpec spec) {
		super(initMac(algorithm, key, spec));
	}
	// endregion

	@Override
	public void update(final byte[] in) {
		this.raw.update(in);
	}

	@Override
	public void update(final byte[] in, final int inOff, final int len) {
		this.raw.update(in, inOff, len);
	}

	@Override
	public byte[] doFinal() {
		return this.raw.doFinal();
	}

	@Override
	public void reset() {
		this.raw.reset();
	}

	@Override
	public int getMacLength() {
		return this.raw.getMacLength();
	}

	@Override
	public String getAlgorithm() {
		return this.raw.getAlgorithm();
	}

	/**
	 * 初始化
	 *
	 * @param algorithm 算法
	 * @param key       密钥 {@link SecretKey}
	 * @param spec      {@link AlgorithmParameterSpec}
	 * @return this
	 * @throws CryptoException Cause by IOException
	 */
	private static Mac initMac(final String algorithm, Key key, final AlgorithmParameterSpec spec) {
		final Mac mac;
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
		return mac;
	}
}
