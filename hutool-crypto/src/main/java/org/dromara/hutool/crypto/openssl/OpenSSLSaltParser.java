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

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.crypto.digest.MD5;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * OpenSSL中加盐解析器<br>
 * 参考：
 * <pre>
 *     https://stackoverflow.com/questions/11783062/how-to-decrypt-file-in-java-encrypted-with-openssl-command-using-aes
 *     https://stackoverflow.com/questions/32508961/java-equivalent-of-an-openssl-aes-cbc-encryption
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class OpenSSLSaltParser {

	/**
	 * 创建MD5 OpenSSLSaltParser
	 *
	 * @param keyLength 密钥长度
	 * @param algorithm 算法
	 * @return OpenSSLSaltParser
	 */
	public static OpenSSLSaltParser ofMd5(final int keyLength, final String algorithm) {
		return of(MD5.of().getRaw(), keyLength, algorithm);
	}

	/**
	 * 创建OpenSSLSaltParser
	 *
	 * @param digest    {@link MessageDigest}
	 * @param keyLength 密钥长度
	 * @param algorithm 算法
	 * @return OpenSSLSaltParser
	 */
	public static OpenSSLSaltParser of(final MessageDigest digest, final int keyLength, final String algorithm) {
		return new OpenSSLSaltParser(digest, keyLength, algorithm);
	}

	private final MessageDigest digest;
	private final int keyLength;
	private final int ivLength;
	private String algorithm;

	/**
	 * 构造
	 *
	 * @param digest    {@link MessageDigest}
	 * @param keyLength 密钥长度
	 * @param algorithm 算法
	 */
	public OpenSSLSaltParser(final MessageDigest digest, final int keyLength, final String algorithm) {
		int ivLength = 16;
		if (StrUtil.containsIgnoreCase(algorithm, "des")) {
			ivLength = 8;
		}
		this.digest = digest;
		this.keyLength = keyLength;
		this.ivLength = ivLength;
		this.algorithm = algorithm;
	}

	/**
	 * 构造
	 *
	 * @param digest    {@link MessageDigest}
	 * @param keyLength 密钥长度
	 * @param ivLength  IV长度
	 */
	public OpenSSLSaltParser(final MessageDigest digest, final int keyLength, final int ivLength) {
		this.digest = digest;
		this.keyLength = keyLength;
		this.ivLength = ivLength;
	}

	/**
	 * 通过密钥和salt值，获取实际的密钥
	 *
	 * @param pass 密钥
	 * @param salt 加盐值
	 * @return 实际密钥
	 */
	public byte[][] getKeyAndIV(final byte[] pass, final byte[] salt) {
		final byte[][] keyAndIvResult = new byte[2][];
		if (null == salt) {
			keyAndIvResult[0] = pass;
			return keyAndIvResult;
		}
		Assert.isTrue(SaltMagic.SALT_LEN == salt.length);

		final byte[] passAndSalt = ByteUtil.concat(pass, salt);

		byte[] hash = new byte[0];
		byte[] keyAndIv = new byte[0];
		for (int i = 0; i < 3 && keyAndIv.length < keyLength + ivLength; i++) {
			final byte[] hashData = ByteUtil.concat(hash, passAndSalt);
			hash = digest.digest(hashData);
			keyAndIv = ByteUtil.concat(keyAndIv, hash);
		}

		keyAndIvResult[0] = Arrays.copyOfRange(keyAndIv, 0, keyLength);
		if (!StrUtil.containsAnyIgnoreCase(algorithm, "RC", "DES")) {
			keyAndIvResult[1] = Arrays.copyOfRange(keyAndIv, keyLength, keyLength + ivLength);
		}
		return keyAndIvResult;
	}
}
