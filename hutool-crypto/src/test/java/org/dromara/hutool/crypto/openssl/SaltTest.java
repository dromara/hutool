/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.dromara.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;

public class SaltTest {

	/**
	 * 测试：
	 * https://www.bejson.com/enc/aesdes/
	 */
	@Test
	void rc4Test() {
		final String encrypted = "U2FsdGVkX19DSROPe0+Ejkw84osqWw==";

		final byte[] salt = SaltMagic.getSalt(SecureUtil.decode(encrypted));
		Assertions.assertNotNull(salt);

		final byte[][] keyAndIV = OpenSSLSaltParser.ofMd5(32, "RC4")
			.getKeyAndIV("1234567890123456".getBytes(), salt);
		Assertions.assertNotNull(keyAndIV);
		Assertions.assertNotNull(keyAndIV[0]);

		final SecretKey rc4Key = KeyUtil.generateKey("RC4", keyAndIV[0]);
		Assertions.assertNotNull(rc4Key);

		final byte[] data = SaltMagic.getData(SecureUtil.decode(encrypted));

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

	/**
	 * 测试：
	 * https://www.bejson.com/enc/aesdes/<br>
	 * https://stackoverflow.com/questions/11783062/how-to-decrypt-file-in-java-encrypted-with-openssl-command-using-aes
	 */
	@Test
	void aesUseStreamTest() {
		final String encrypted = "U2FsdGVkX1+lqsuKAR+OdOeNduvx5wgXf6yEUdDIh3g=";
		final String pass = "1234567890123456";
		final String algorithm = "PBEWITHMD5AND256BITAES-CBC-OPENSSL";
		final String result = "hutool";

		final OpenSSLPBEInputStream stream = new OpenSSLPBEInputStream(
			new ByteArrayInputStream(Base64.decode(encrypted)),
			algorithm,
			1,
			pass.toCharArray());

		final String s = IoUtil.readUtf8(stream);
		Assertions.assertEquals(result, s);
	}
}
