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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.crypto.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 对称加密算法单元测试
 *
 * @author Looly
 */
public class SymmetricTest {

	@Test
	public void aesTest() {
		final String content = "test中文";

		// 随机生成密钥
		final byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

		// 构建
		final SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

		// 加密
		final byte[] encrypt = aes.encrypt(content);
		// 解密
		final byte[] decrypt = aes.decrypt(encrypt);

		assertEquals(content, StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 加密为16进制表示
		final String encryptHex = aes.encryptHex(content);
		// 解密为字符串
		final String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.UTF_8);

		assertEquals(content, decryptStr);
	}

	@Test
	public void aesTest2() {
		final String content = "test中文";

		// 随机生成密钥
		final byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

		// 构建
		final AES aes = SecureUtil.aes(key);

		// 加密
		final byte[] encrypt = aes.encrypt(content);
		// 解密
		final byte[] decrypt = aes.decrypt(encrypt);

		assertEquals(content, StrUtil.utf8Str(decrypt));

		// 加密为16进制表示
		final String encryptHex = aes.encryptHex(content);
		// 解密为字符串
		final String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.UTF_8);

		assertEquals(content, decryptStr);
	}

	@Test
	public void aesTest3() {
		final String content = "test中文aaaaaaaaaaaaaaaaaaaaa";

		final AES aes = new AES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "0102030405060708".getBytes());

		// 加密
		final byte[] encrypt = aes.encrypt(content);
		// 解密
		final byte[] decrypt = aes.decrypt(encrypt);

		assertEquals(content, StrUtil.utf8Str(decrypt));

		// 加密为16进制表示
		final String encryptHex = aes.encryptHex(content);
		// 解密为字符串
		final String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.UTF_8);

		assertEquals(content, decryptStr);
	}

	@Test
	public void aesTest4() {
		final String content = "4321c9a2db2e6b08987c3b903d8d11ff";
		final AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, "0123456789ABHAEQ".getBytes(), "DYgjCEIMVrj2W9xN".getBytes());

		// 加密为16进制表示
		final String encryptHex = aes.encryptHex(content);

		assertEquals("cd0e3a249eaf0ed80c330338508898c4bddcfd665a1b414622164a273ca5daf7b4ebd2c00aaa66b84dd0a237708dac8e", encryptHex);
	}

	@Test
	public void pbeWithoutIvTest() {
		final String content = "4321c9a2db2e6b08987c3b903d8d11ff";
		final SymmetricCrypto crypto = new SymmetricCrypto(SymmetricAlgorithm.PBEWithMD5AndDES,
			"0123456789ABHAEQ".getBytes());

		// 加密为16进制表示
		final String encryptHex = crypto.encryptHex(content);
		final String data = crypto.decryptStr(encryptHex);

		assertEquals(content, data);
	}

	@Test
	public void aesUpdateTest() {
		final String content = "4321c9a2db2e6b08987c3b903d8d11ff";
		final AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, "0123456789ABHAEQ".getBytes(), "DYgjCEIMVrj2W9xN".getBytes());

		// 加密为16进制表示
		aes.setMode(CipherMode.ENCRYPT);
		final String randomData = aes.encryptHex(content.getBytes(StandardCharsets.UTF_8));
		assertEquals("cd0e3a249eaf0ed80c330338508898c4bddcfd665a1b414622164a273ca5daf7b4ebd2c00aaa66b84dd0a237708dac8e", randomData);

		/// 分段加密，update时只是生成部分密文
		String randomData2 = aes.updateHex(content.getBytes(StandardCharsets.UTF_8));
		assertEquals("cd0e3a249eaf0ed80c330338508898c4", randomData2);

		// doFinal后生成剩余内容
		randomData2 += aes.doFinalHex();
		assertEquals(randomData, randomData2);
	}


	@Test
	public void aesZeroPaddingTest() {
		final String content = RandomUtil.randomStringLower(RandomUtil.randomInt(200));
		final AES aes = new AES(Mode.CBC, Padding.ZeroPadding, "0123456789ABHAEQ".getBytes(), "DYgjCEIMVrj2W9xN".getBytes());

		// 加密为16进制表示
		final String encryptHex = aes.encryptHex(content);
		// 解密
		final String decryptStr = aes.decryptStr(encryptHex);
		assertEquals(content, decryptStr);
	}

	@Test
	public void aesZeroPaddingTest2() {
		final String content = "RandomUtil.randomString(RandomUtil.randomInt(2000))";
		final AES aes = new AES(Mode.CBC, Padding.ZeroPadding, "0123456789ABHAEQ".getBytes(), "DYgjCEIMVrj2W9xN".getBytes());

		final ByteArrayOutputStream encryptStream = new ByteArrayOutputStream();
		aes.encrypt(IoUtil.toUtf8Stream(content), encryptStream, true);

		final ByteArrayOutputStream contentStream = new ByteArrayOutputStream();
		aes.decrypt(IoUtil.toStream(encryptStream), contentStream, true);

		assertEquals(content, StrUtil.utf8Str(contentStream.toByteArray()));
	}

	@Test
	public void aesPkcs7PaddingTest() {
		final String content = RandomUtil.randomStringLower(RandomUtil.randomInt(200));
		final AES aes = new AES("CBC", "PKCS7Padding",
			RandomUtil.randomBytes(32),
			"DYgjCEIMVrj2W9xN".getBytes());

		// 加密为16进制表示
		final String encryptHex = aes.encryptHex(content);
		// 解密
		final String decryptStr = aes.decryptStr(encryptHex);
		assertEquals(content, decryptStr);
	}

	@Test
	public void desdeTest() {
		final String content = "test中文";

		final byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();

		final DESede des = SecureUtil.desede(key);

		final byte[] encrypt = des.encrypt(content);
		final byte[] decrypt = des.decrypt(encrypt);

		assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		assertEquals(content, decryptStr);
	}

	@Test
	public void desdeTest2() {
		final String content = "test中文";

		final byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();

		final DESede des = new DESede(Mode.CBC, Padding.PKCS5Padding, key, "12345678".getBytes());

		final byte[] encrypt = des.encrypt(content);
		final byte[] decrypt = des.decrypt(encrypt);

		assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		assertEquals(content, decryptStr);
	}

	@Test
	public void vigenereTest() {
		final String content = "Wherethereisawillthereisaway";
		final String key = "CompleteVictory";

		final String encrypt = Vigenere.encrypt(content, key);
		assertEquals("zXScRZ]KIOMhQjc0\\bYRXZOJK[Vi", encrypt);
		final String decrypt = Vigenere.decrypt(encrypt, key);
		assertEquals(content, decrypt);
	}
}
