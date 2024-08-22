/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.crypto.asymmetric.paillier;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.crypto.Cipher;
import org.dromara.hutool.crypto.CipherMode;
import org.dromara.hutool.crypto.CryptoException;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;

/**
 * Paillier加密算法实现
 *
 * @author Looly
 * @since 6.0.0
 */
public class PaillierCipher implements Cipher {

	private final PaillierCipherSpiImpl impl;

	/**
	 * 构造
	 */
	public PaillierCipher() {
		this.impl = new PaillierCipherSpiImpl();
	}

	@Override
	public String getAlgorithmName() {
		return PaillierKey.ALGORITHM_NAME;
	}

	@Override
	public int getBlockSize() {
		return impl.engineGetBlockSize();
	}

	@Override
	public void init(final CipherMode mode, final Parameters parameters) {
		Assert.isInstanceOf(PaillierParameters.class, parameters, "Only support JceParameters!");
		final PaillierParameters paillierParameters = (PaillierParameters) parameters;
		try {
			impl.engineInit(mode.getValue(), paillierParameters.key, paillierParameters.random);
		} catch (final InvalidKeyException e) {
			throw new CryptoException(e);
		}
	}

	@Override
	public int getOutputSize(final int len) {
		return impl.engineGetOutputSize(len);
	}

	@Override
	public int process(final byte[] in, final int inOff, final int len, final byte[] out, final int outOff) {
		return impl.engineUpdate(in, inOff, len, out, outOff);
	}

	@Override
	public int doFinal(final byte[] out, final int outOff) {
		return impl.engineDoFinal(null, 0, 0, out, outOff);
	}

	@Override
	public byte[] processFinal(final byte[] in, final int inOffset, final int inputLen) {
		return impl.engineDoFinal(in, inOffset, inputLen);
	}

	/**
	 * Paillier算法的key和random封装
	 */
	public static class PaillierParameters implements Parameters {
		private final Key key;
		/**
		 * 随机数生成器，可自定义随机数种子
		 */
		private final SecureRandom random;

		/**
		 * 构造
		 *
		 * @param key    密钥
		 * @param random 自定义随机数生成器
		 */
		public PaillierParameters(final Key key, final SecureRandom random) {
			this.key = key;
			this.random = random;
		}
	}
}
