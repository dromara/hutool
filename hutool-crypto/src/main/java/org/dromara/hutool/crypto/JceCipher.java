/*
 * Copyright (c) 2013-2024 Hutool Team.
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

	/**
	 * 返回新缓冲区中的初始化向量（IV）<br>
	 * 这在创建随机IV的情况下，或在基于密码的加密或解密的上下文中是有用的，其中IV是从用户提供的密码导出的。
	 *
	 * @return 新缓冲区中的初始化向量，如果基础算法不使用IV，或者尚未设置IV，则为null。
	 */
	public byte[] getIV() {
		return this.raw.getIV();
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

	// region ----- process
	/**
	 * 继续多部分加密或解密操作（取决于此密码的初始化方式），处理另一个数据部分。
	 * 第一inputLen字节在input缓冲区中，从inputOffset以下，被处理，并且结果被存储在output缓冲器。
	 *
	 * @param in 输入缓冲区
	 * @param inOff 输入开始的 input中的偏移量
	 * @param len 输入长度
	 * @return 带有结果的新缓冲区，如果底层密码是块密码且输入数据太短而不能产生新块，则返回null。
	 */
	public byte[] process(final byte[] in, final int inOff, final int len) {
		return this.raw.update(in, inOff, len);
	}

	/**
	 * 继续多部分加密或解密操作（取决于此密码的初始化方式），处理另一个数据部分。
	 * 第一inputLen字节在input缓冲区中，从inputOffset以下，被处理，并且结果被存储在output缓冲器。
	 *
	 * @param in 输入缓冲区
	 * @param inOff 输入开始的 input中的偏移量
	 * @param len 输入长度
	 * @param out 结果的缓冲区
	 * @return 存储在 output的字节数
	 */
	public int process(final byte[] in, final int inOff, final int len, final byte[] out) {
		try {
			return this.raw.update(in, inOff, len, out);
		} catch (final ShortBufferException e) {
			throw new CryptoException(e);
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
	// endregion

	@Override
	public int doFinal(final byte[] out, final int outOff) {
		try {
			return this.raw.doFinal(out, outOff);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public byte[] processFinal(final byte[] in) {
		try {
			return this.raw.doFinal(in);
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public byte[] processFinal(final byte[] data, final int inOffset, final int inputLen) {
		try {
			return this.raw.doFinal(data, inOffset, inputLen);
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
