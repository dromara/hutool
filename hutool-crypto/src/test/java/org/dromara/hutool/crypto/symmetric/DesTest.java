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

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.Mode;
import org.dromara.hutool.crypto.Padding;
import org.dromara.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * DES加密解密单元测试
 */
public class DesTest {

	@Test
	public void desTest() {
		final String content = "test中文";

		final byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

		final SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
		final byte[] encrypt = des.encrypt(content);
		final byte[] decrypt = des.decrypt(encrypt);

		Assertions.assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		Assertions.assertEquals(content, decryptStr);
	}

	@Test
	public void desTest2() {
		final String content = "test中文";

		final byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

		final DES des = SecureUtil.des(key);
		final byte[] encrypt = des.encrypt(content);
		final byte[] decrypt = des.decrypt(encrypt);

		Assertions.assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		Assertions.assertEquals(content, decryptStr);
	}

	@Test
	public void desTest3() {
		final String content = "test中文";

		final DES des = new DES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "01020304".getBytes());

		final byte[] encrypt = des.encrypt(content);
		final byte[] decrypt = des.decrypt(encrypt);

		Assertions.assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		Assertions.assertEquals(content, decryptStr);
	}

	@Test
	public void encryptDecryptTest(){
		final String content = "我是一个测试的test字符串123";
		final DES des = SecureUtil.des();

		final String encryptHex = des.encryptHex(content);
		final String result = des.decryptStr(encryptHex);

		Assertions.assertEquals(content, result);
	}

	@Test
	public void encryptDecryptWithCustomTest(){
		final String content = "我是一个测试的test字符串123";
		final DES des = new DES(
				Mode.CTS,
				Padding.PKCS5Padding,
				ByteUtil.toUtf8Bytes("12345678"),
				ByteUtil.toUtf8Bytes("11223344")
		);

		final String encryptHex = des.encryptHex(content);
		final String result = des.decryptStr(encryptHex);

		Assertions.assertEquals(content, result);
	}

	@Test
	public void issueI5I5B3Test(){
		final DES des = new DES(Mode.CTS, Padding.PKCS5Padding, "1234567890".getBytes(), "12345678".getBytes());
		des.encryptHex("root");
	}
}
