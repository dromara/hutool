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
import org.dromara.hutool.crypto.digest.DigestUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * OpenSSL风格前缀和加盐相关工具类<br>
 * 参考：https://stackoverflow.com/questions/32508961/java-equivalent-of-an-openssl-aes-cbc-encryption
 *
 * @author looly
 * @since 6.0.0
 */
public class SaltUtil {

	private final static byte SALT_LEN = 8;
	private final static byte IV_LEN = 16;
	private final static byte KEY_LEN = 32;

	/**
	 * OpenSSL's magic initial bytes.
	 */
	private static final byte[] SALTED_MAGIC = "Salted__".getBytes(StandardCharsets.US_ASCII);

	/**
	 * 获取8位salt随机数<br>
	 *
	 * @param encryptedData 密文
	 * @return salt随机数
	 */
	public static byte[] getSalt(final byte[] encryptedData) {
		if (ArrayUtil.startWith(encryptedData, SALTED_MAGIC)) {
			return Arrays.copyOfRange(encryptedData, SALTED_MAGIC.length, SALTED_MAGIC.length + SALT_LEN);
		}
		return null;
	}

	/**
	 * 通过密钥和salt值，获取实际的密钥
	 *
	 * @param pass 密钥
	 * @param salt 加盐值
	 * @return 实际密钥
	 */
	public static byte[] getKey(final byte[] pass, final byte[] salt) {
		if (null == salt) {
			return pass;
		}
		final byte[] passAndSalt = arrayConcat(pass, salt);

		byte[] hash = new byte[0];
		byte[] keyAndIv = new byte[0];
		for (int i = 0; i < 3 && keyAndIv.length < 48; i++) {
			final byte[] hashData = arrayConcat(hash, passAndSalt);
			hash = DigestUtil.md5(hashData);
			keyAndIv = arrayConcat(keyAndIv, hash);
		}

		return Arrays.copyOfRange(keyAndIv, 0, KEY_LEN);
	}

	private static byte[] arrayConcat(final byte[] a, final byte[] b) {
		if (ArrayUtil.isEmpty(a)) {
			return b;
		}
		final byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}
