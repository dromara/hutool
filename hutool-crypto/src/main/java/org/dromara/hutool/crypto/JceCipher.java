/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 提供{@link javax.crypto.Cipher}的方法包装
 *
 * @author Looly
 */
public class JceCipher implements Cipher, Wrapper<javax.crypto.Cipher> {

	private final javax.crypto.Cipher cipher;

	/**
	 * 构造
	 *
	 * @param algorithm 算法名称
	 */
	public JceCipher(final String algorithm) {
		this(SecureUtil.createCipher(algorithm));
	}

	/**
	 * 构造
	 *
	 * @param cipher {@link javax.crypto.Cipher}
	 */
	public JceCipher(final javax.crypto.Cipher cipher) {
		this.cipher = cipher;
	}

	@Override
	public javax.crypto.Cipher getRaw() {
		return this.cipher;
	}

	@Override
	public String getAlgorithmName() {
		return this.cipher.getAlgorithm();
	}

	@Override
	public int getBlockSize() {
		return this.cipher.getBlockSize();
	}

	@Override
	public void init(final CipherMode mode, final Parameters parameters) {
		Assert.isInstanceOf(JceParameters.class, parameters, "Only support JceParameters!");

		try {
			init(mode.getValue(), (JceParameters) parameters);
		} catch (final InvalidAlgorithmParameterException | InvalidKeyException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 执行初始化参数操作
	 *
	 * @param mode          模式
	 * @param jceParameters {@link JceParameters}
	 * @throws InvalidAlgorithmParameterException 无效算法参数
	 * @throws InvalidKeyException                无效key
	 */
	public void init(final int mode, final JceParameters jceParameters) throws InvalidAlgorithmParameterException, InvalidKeyException {
		final javax.crypto.Cipher cipher = this.cipher;
		if (null != jceParameters.parameterSpec) {
			if (null != jceParameters.random) {
				cipher.init(mode, jceParameters.key, jceParameters.parameterSpec, jceParameters.random);
			} else {
				cipher.init(mode, jceParameters.key, jceParameters.parameterSpec);
			}
		} else {
			if (null != jceParameters.random) {
				cipher.init(mode, jceParameters.key, jceParameters.random);
			} else {
				cipher.init(mode, jceParameters.key);
			}
		}
	}

	@Override
	public byte[] process(final byte[] data) {
		return new byte[0];
	}

	/**
	 * JCE的{@link AlgorithmParameterSpec} 参数包装
	 */
	public static class JceParameters implements Parameters {
		private final Key key;
		/**
		 * 算法参数
		 */
		private final AlgorithmParameterSpec parameterSpec;
		/**
		 * 随机数生成器，可自定义随机数种子
		 */
		private final SecureRandom random;

		/**
		 * 构造
		 *
		 * @param key           密钥
		 * @param parameterSpec {@link AlgorithmParameterSpec}
		 * @param random        自定义随机数生成器
		 */
		public JceParameters(final Key key, final AlgorithmParameterSpec parameterSpec, final SecureRandom random) {
			this.key = key;
			this.parameterSpec = parameterSpec;
			this.random = random;
		}
	}
}
