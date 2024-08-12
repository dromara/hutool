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

package org.dromara.hutool.crypto.openssl;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.dromara.hutool.crypto.SpecUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * OpenSSL风格的PBE输出流，用于生成密文
 * 来自：http://stackoverflow.com/questions/11783062/how-to-decrypt-an-encrypted-file-in-java-with-openssl-with-aes
 *
 * @author looly
 * @since 6.0.0
 */
public class OpenSSLPBEOutputStream extends CipherOutputStream {
	protected static final int WRITE_BLOCK_SIZE = 64 * 1024;

	/**
	 * 构造
	 *
	 * @param out            流
	 * @param algorithm      算法
	 * @param iterationCount 摘要次数
	 * @param password       口令
	 */
	public OpenSSLPBEOutputStream(final OutputStream out,
								  final String algorithm,
								  final int iterationCount,
								  final char[] password) {
		super(out, createEncryptCipher(algorithm, writeRandomHeader(out),
			iterationCount, password));
	}

	private static byte[] writeRandomHeader(final OutputStream out) throws IORuntimeException {
		final byte[] salt = RandomUtil.randomBytes(SaltMagic.SALT_LEN);
		try {
			out.write(SaltMagic.SALTED_MAGIC);
			out.write(salt);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
		return salt;
	}

	private static Cipher createEncryptCipher(final String algorithm, final byte[] salt,
											  final int iterationCount,
											  final char[] password) {
		final Cipher cipher = SecureUtil.createCipher(algorithm);
		try {
			cipher.init(Cipher.ENCRYPT_MODE,
				KeyUtil.generatePBEKey(algorithm, password),
				SpecUtil.createPBEParameterSpec(salt, iterationCount));
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
		return cipher;
	}
}
