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
import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;

import javax.crypto.ShortBufferException;
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
public class JceCipher extends SimpleWrapper<javax.crypto.Cipher> implements Cipher {

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
	 * @param cipher {@link javax.crypto.Cipher}，可以通过{@link javax.crypto.Cipher#getInstance(String)}创建
	 */
	public JceCipher(final javax.crypto.Cipher cipher) {
		super(Assert.notNull(cipher));
	}

	@Override
	public String getAlgorithmName() {
		return this.raw.getAlgorithm();
	}

	@Override
	public int getBlockSize() {
		return this.raw.getBlockSize();
	}

	@Override
	public int getOutputSize(final int len) {
		return this.raw.getOutputSize(len);
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
		final javax.crypto.Cipher cipher = this.raw;
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
	public int process(final byte[] in, final int inOff, final int len, final byte[] out, final int outOff) {
		try {
			return this.raw.update(in, inOff, len, out, outOff);
		} catch (final ShortBufferException e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public int doFinal(final byte[] out, final int outOff) {
		try {
			return this.raw.doFinal(out, outOff);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public byte[] processFinal(final byte[] data) {
		try {
			return this.raw.doFinal(data);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
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
