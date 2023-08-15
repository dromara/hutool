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
