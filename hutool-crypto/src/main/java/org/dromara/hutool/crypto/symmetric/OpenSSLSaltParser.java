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

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.crypto.digest.MD5;

import java.nio.charset.StandardCharsets;
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
	private final static byte SALT_LEN = 8;

	/**
	 * OpenSSL's magic initial bytes.
	 */
	private static final byte[] SALTED_MAGIC = "Salted__".getBytes(StandardCharsets.US_ASCII);

	/**
	 * 获取魔术值和随机盐的长度：16（128位）
	 */
	public static final int MAGIC_SALT_LENGTH = SALTED_MAGIC.length + SALT_LEN;

	/**
	 * 获取去除头部盐的加密数据<br>
	 *
	 * @param encryptedData 密文
	 * @return 实际密文
	 */
	public static byte[] getData(final byte[] encryptedData) {
		if (ArrayUtil.startWith(encryptedData, SALTED_MAGIC)) {
			return Arrays.copyOfRange(encryptedData, SALTED_MAGIC.length + SALT_LEN, encryptedData.length);
		}
		return encryptedData;
	}

	/**
	 * 获取8位salt随机数<br>
	 *
	 * @param encryptedData 密文
	 * @return salt随机数
	 */
	public static byte[] getSalt(final byte[] encryptedData) {
		if (ArrayUtil.startWith(encryptedData, SALTED_MAGIC)) {
			return Arrays.copyOfRange(encryptedData, SALTED_MAGIC.length, MAGIC_SALT_LENGTH);
		}
		return null;
	}

	/**
	 * 为加密后的数据添加Magic头，生成的密文格式为：
	 * <pre>
	 *     Salted__[salt][data]
	 * </pre>
	 *
	 * @param data 数据
	 * @param salt 加盐值，必须8位，{@code null}表示返回原文
	 * @return 密文
	 */
	public static byte[] addMagic(final byte[] data, final byte[] salt) {
		if (null == salt) {
			return data;
		}
		Assert.isTrue(SALT_LEN == salt.length);
		return ByteUtil.concat(SALTED_MAGIC, salt, data);
	}

	/**
	 * 获取Magic头，生成的密文格式为：
	 * <pre>
	 *     Salted__[salt]
	 * </pre>
	 *
	 * @param salt 加盐值，必须8位，不能为{@code null}
	 * @return Magic头
	 */
	public static byte[] getSaltedMagic(final byte[] salt) {
		return ByteUtil.concat(SALTED_MAGIC, salt);
	}

	/**
	 * 创建MD5 OpenSSLSaltParser
	 *
	 * @param keyLength 密钥长度
	 * @param algorithm 算法
	 * @return OpenSSLSaltParser
	 */
	public static OpenSSLSaltParser ofMd5(final int keyLength, final String algorithm) {
		return of(new MD5().getDigest(), keyLength, algorithm);
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
		Assert.isTrue(SALT_LEN == salt.length);

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
