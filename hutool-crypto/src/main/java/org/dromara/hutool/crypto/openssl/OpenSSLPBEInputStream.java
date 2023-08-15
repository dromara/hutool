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
