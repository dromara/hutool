/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.openssl;

import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;

public class OpenSSLPBECommon {
	protected static final int SALT_SIZE_BYTES = 8;
	/**
	 * OpenSSL's magic initial bytes.
	 */
	protected static final byte[] SALTED_MAGIC = "Salted__".getBytes(StandardCharsets.US_ASCII);
	protected static final String OPENSSL_HEADER_ENCODE = "ASCII";

	protected static Cipher initializeCipher(char[] password, byte[] salt, int cipherMode,
											 final String algorithm, int iterationCount)
		throws Exception {

		final SecretKey key = KeyUtil.generateKey(algorithm, new PBEKeySpec(password));

		final Cipher cipher = SecureUtil.createCipher(algorithm);
		cipher.init(cipherMode, key, new PBEParameterSpec(salt, iterationCount));

		return cipher;
	}
}
