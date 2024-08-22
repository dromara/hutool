/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
