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

package org.dromara.hutool.crypto.bc;

import org.bouncycastle.crypto.*;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;
import org.dromara.hutool.crypto.Cipher;
import org.dromara.hutool.crypto.CipherMode;
import org.dromara.hutool.crypto.CryptoException;

import java.util.Arrays;

/**
 * 基于BouncyCastle库封装的加密解密实现，包装包括：
 * <ul>
 *     <li>{@link BufferedBlockCipher}</li>
 *     <li>{@link BlockCipher}</li>
 *     <li>{@link StreamCipher}</li>
 *     <li>{@link AEADBlockCipher}</li>
 * </ul>
 *
 * @author Looly, changhr2013
 */
public class BCCipher implements Cipher, Wrapper<Object> {

	/**
	 * {@link BufferedBlockCipher}，块加密，包含engine、mode、padding
	 */
	private BufferedBlockCipher bufferedBlockCipher;
	/**
	 * {@link BlockCipher} 块加密，一般用于AES等对称加密
	 */
	private BlockCipher blockCipher;
	/**
	 * {@link AEADBlockCipher}, 关联数据的认证加密(Authenticated Encryption with Associated Data)
	 */
	private AEADBlockCipher aeadBlockCipher;
	/**
	 * {@link StreamCipher}
	 */
	private StreamCipher streamCipher;

	// region ----- 构造

	/**
	 * 构造
	 *
	 * @param bufferedBlockCipher {@link BufferedBlockCipher}
	 */
	public BCCipher(final BufferedBlockCipher bufferedBlockCipher) {
		this.bufferedBlockCipher = Assert.notNull(bufferedBlockCipher);
	}

	/**
	 * 构造
	 *
	 * @param blockCipher {@link BlockCipher}
	 */
	public BCCipher(final BlockCipher blockCipher) {
		this.blockCipher = Assert.notNull(blockCipher);
	}

	/**
	 * 构造
	 *
	 * @param aeadBlockCipher {@link AEADBlockCipher}
	 */
	public BCCipher(final AEADBlockCipher aeadBlockCipher) {
		this.aeadBlockCipher = Assert.notNull(aeadBlockCipher);
	}

	/**
	 * 构造
	 *
	 * @param streamCipher {@link StreamCipher}
	 */
	public BCCipher(final StreamCipher streamCipher) {
		this.streamCipher = Assert.notNull(streamCipher);
	}
	// endregion

	@Override
	public Object getRaw() {
		if (null != this.bufferedBlockCipher) {
			return this.bufferedBlockCipher;
		}
		if (null != this.blockCipher) {
			return this.blockCipher;
		}
		if (null != this.aeadBlockCipher) {
			return this.aeadBlockCipher;
		}
		return this.streamCipher;
	}

	@Override
	public String getAlgorithmName() {
		if (null != this.bufferedBlockCipher) {
			return this.bufferedBlockCipher.getUnderlyingCipher().getAlgorithmName();
		}
		if (null != this.blockCipher) {
			return this.blockCipher.getAlgorithmName();
		}
		if (null != this.aeadBlockCipher) {
			return this.aeadBlockCipher.getUnderlyingCipher().getAlgorithmName();
		}
		return this.streamCipher.getAlgorithmName();
	}

	@Override
	public int getBlockSize() {
		if (null != this.bufferedBlockCipher) {
			return this.bufferedBlockCipher.getBlockSize();
		}
		if (null != this.blockCipher) {
			return this.blockCipher.getBlockSize();
		}
		if (null != this.aeadBlockCipher) {
			return this.aeadBlockCipher.getUnderlyingCipher().getBlockSize();
		}
		return -1;
	}

	@Override
	public void init(final CipherMode mode, final Parameters parameters) {
		Assert.isInstanceOf(BCParameters.class, parameters, "Only support BCParameters!");

		final boolean forEncryption;
		if (mode == CipherMode.ENCRYPT) {
			forEncryption = true;
		} else if (mode == CipherMode.DECRYPT) {
			forEncryption = false;
		} else {
			throw new IllegalArgumentException("Invalid mode: " + mode.name());
		}
		final CipherParameters cipherParameters = ((BCParameters) parameters).parameters;

		if (null != this.bufferedBlockCipher) {
			this.bufferedBlockCipher.init(forEncryption, cipherParameters);
			return;
		}
		if (null != this.blockCipher) {
			this.blockCipher.init(forEncryption, cipherParameters);
		}
		if (null != this.aeadBlockCipher) {
			this.aeadBlockCipher.init(forEncryption, cipherParameters);
			return;
		}
		this.streamCipher.init(forEncryption, cipherParameters);
	}

	@Override
	public int getOutputSize(final int len) {
		if (null != this.bufferedBlockCipher) {
			return this.bufferedBlockCipher.getOutputSize(len);
		}
		if (null != this.aeadBlockCipher) {
			return this.aeadBlockCipher.getOutputSize(len);
		}
		return -1;
	}

	@Override
	public int process(final byte[] in, final int inOff, final int len, final byte[] out, final int outOff) {
		if (null != this.bufferedBlockCipher) {
			return this.bufferedBlockCipher.processBytes(in, inOff, len, out, outOff);
		}
		if (null != this.blockCipher) {
			final byte[] subBytes;
			if (inOff + len < in.length) {
				subBytes = Arrays.copyOf(in, inOff + len);
			} else {
				subBytes = in;
			}
			return this.blockCipher.processBlock(subBytes, inOff, out, outOff);
		}
		if (null != this.aeadBlockCipher) {
			return this.aeadBlockCipher.processBytes(in, inOff, len, out, outOff);
		}
		return this.streamCipher.processBytes(in, inOff, len, out, outOff);
	}

	@Override
	public int doFinal(final byte[] out, final int outOff) {
		if (null != this.bufferedBlockCipher) {
			try {
				return this.bufferedBlockCipher.doFinal(out, outOff);
			} catch (final InvalidCipherTextException e) {
				throw new CryptoException(e);
			}
		}
		if (null != this.aeadBlockCipher) {
			try {
				return this.aeadBlockCipher.doFinal(out, outOff);
			} catch (final InvalidCipherTextException e) {
				throw new CryptoException(e);
			}
		}
		return 0;
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
		protected final CipherParameters parameters;

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
