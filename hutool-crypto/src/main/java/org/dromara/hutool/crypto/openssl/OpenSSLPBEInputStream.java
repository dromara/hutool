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

import org.dromara.hutool.crypto.CryptoException;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.dromara.hutool.crypto.SpecUtil;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.InputStream;

/**
 * OpenSSL风格的PBE输入流，用于密文解密
 * 来自：http://stackoverflow.com/questions/11783062/how-to-decrypt-an-encrypted-file-in-java-with-openssl-with-aes
 *
 * @author looly
 * @since 6.0.0
 */
public class OpenSSLPBEInputStream extends CipherInputStream {

	/**
	 * 构造
	 *
	 * @param in             流
	 * @param algorithm      算法
	 * @param iterationCount 摘要次数
	 * @param password       口令
	 */
	public OpenSSLPBEInputStream(final InputStream in,
								 final String algorithm,
								 final int iterationCount,
								 final char[] password) {

		super(in, createDecryptCipher(algorithm,
			SaltMagic.getSalt(in), iterationCount, password));
	}

	private static Cipher createDecryptCipher(final String algorithm, final byte[] salt,
											  final int iterationCount,
											  final char[] password) {
		final Cipher cipher = SecureUtil.createCipher(algorithm);
		try {
			cipher.init(Cipher.DECRYPT_MODE,
				KeyUtil.generatePBEKey(algorithm, password),
				SpecUtil.createPBEParameterSpec(salt, iterationCount));
		} catch (final Exception e) {
			throw new CryptoException(e);
		}
		return cipher;
	}
}
