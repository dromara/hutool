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

package org.dromara.hutool.crypto.bc;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.crypto.Cipher;
import org.dromara.hutool.crypto.CipherMode;
import org.dromara.hutool.crypto.CryptoException;

/**
 * 基于BouncyCastle库的{@link BufferedBlockCipher}封装的加密解密实现
 *
 * @author Looly, changhr2013
 */
public class BCCipher implements Cipher, Wrapper<BufferedBlockCipher> {

	/**
	 * {@link BufferedBlockCipher}，包含engine、mode、padding
	 */
	private final BufferedBlockCipher blockCipher;

	/**
	 * 构造
	 *
	 * @param blockCipher {@link BufferedBlockCipher}
	 */
	public BCCipher(final BufferedBlockCipher blockCipher) {
		this.blockCipher = Assert.notNull(blockCipher);
	}

	@Override
	public BufferedBlockCipher getRaw() {
		return this.blockCipher;
	}

	@Override
	public String getAlgorithmName() {
		return this.blockCipher.getUnderlyingCipher().getAlgorithmName();
	}

	@Override
	public int getBlockSize() {
		return this.blockCipher.getBlockSize();
	}

	@Override
	public void init(final CipherMode mode, final Parameters parameters) {
		Assert.isInstanceOf(BCParameters.class, parameters, "Only support BCParameters!");
		this.blockCipher.init(mode == CipherMode.encrypt, ((BCParameters) parameters).parameters);
	}

	@Override
	public byte[] process(final byte[] data) {
		final byte[] out;
		try {
			final BufferedBlockCipher cipher = this.blockCipher;
			final int updateOutputSize = cipher.getOutputSize(data.length);
			final byte[] buf = new byte[updateOutputSize];
			int len = cipher.processBytes(data, 0, data.length, buf, 0);
			len += cipher.doFinal(buf, len);
			out = new byte[len];
			System.arraycopy(buf, 0, out, 0, len);
		} catch (final Exception e) {
			throw new CryptoException("encrypt/decrypt process exception.", e);
		}
		return out;
	}

	/**
	 * BouncyCastle库的{@link CipherParameters}封装
	 *
	 * @author Looly
	 */
	public static class BCParameters implements Parameters {
		/**
		 * 算法的参数
		 */
		private final CipherParameters parameters;

		/**
		 * 构造
		 *
		 * @param parameters {@link CipherParameters}
		 */
		public BCParameters(final CipherParameters parameters) {
			this.parameters = parameters;
		}
	}
}
