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

import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class SaltUtilTest {

	/**
	 * 测试：
	 * https://www.bejson.com/enc/aesdes/
	 */
	@Test
	void rc4Test() {
		final String encrypted = "U2FsdGVkX19DSROPe0+Ejkw84osqWw==";

		final byte[] salt = OpenSSLSaltParser.getSalt(SecureUtil.decode(encrypted));
		Assertions.assertNotNull(salt);

		final byte[][] keyAndIV = OpenSSLSaltParser.ofMd5(32, "RC4")
			.getKeyAndIV("1234567890123456".getBytes(), salt);
		Assertions.assertNotNull(keyAndIV);
		Assertions.assertNotNull(keyAndIV[0]);

		final SecretKey rc4Key = KeyUtil.generateKey("RC4", keyAndIV[0]);
		Assertions.assertNotNull(rc4Key);

		final byte[] data = OpenSSLSaltParser.getData(SecureUtil.decode(encrypted));

		final SymmetricCrypto rc4 = new SymmetricCrypto("RC4", rc4Key);
		final String decrypt = rc4.decryptStr(data);
		Assertions.assertEquals("hutool", decrypt);
	}

	/**
	 * 测试：
	 * https://www.bejson.com/enc/aesdes/
	 */
	@Test
	void rc4Test2() {
		final String encrypted = "U2FsdGVkX19DSROPe0+Ejkw84osqWw==";
		final SymmetricCrypto rc4 = new SymmetricCrypto("RC4", "1234567890123456".getBytes());
		final String decrypt = rc4.decryptStr(encrypted);
		Assertions.assertEquals("hutool", decrypt);
	}

	/**
	 * 测试：
	 * https://www.bejson.com/enc/aesdes/
	 */
	@Test
	void aesTest() {
		final String encrypted = "U2FsdGVkX1+lqsuKAR+OdOeNduvx5wgXf6yEUdDIh3g=";
		final SymmetricCrypto des = new SymmetricCrypto("AES/CBC/PKCS5Padding", "1234567890123456".getBytes());
		final String decrypt = des.decryptStr(encrypted);
		Assertions.assertEquals("hutool", decrypt);
	}
}
