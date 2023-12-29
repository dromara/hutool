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

package org.dromara.hutool.crypto.symmetric;

import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class RC4Test {

	@Test
	public void testCryptMessage() {
		final byte[] key = "This is pretty long key".getBytes();
		final SymmetricCrypto SymmetricCrypto = new SymmetricCrypto("RC4", key);
		final String message = "Hello, World!";
		final byte[] crypt = SymmetricCrypto.encrypt(message);
		final String msg = SymmetricCrypto.decryptStr(crypt);
		Assertions.assertEquals(message, msg);

		final String message2 = "Hello, World， this is megssage 2";
		final byte[] crypt2 = SymmetricCrypto.encrypt(message2);
		final String msg2 = SymmetricCrypto.decryptStr(crypt2);
		Assertions.assertEquals(message2, msg2);
	}

	@Test
	public void testCryptWithChineseCharacters() {
		final String message = "这是一个中文消息！";
		final byte[] key = "我是一个文件密钥".getBytes(StandardCharsets.UTF_8);
		final SymmetricCrypto SymmetricCrypto = new SymmetricCrypto("RC4", key);
		final byte[] crypt = SymmetricCrypto.encrypt(message);
		final String msg = SymmetricCrypto.decryptStr(crypt);
		Assertions.assertEquals(message, msg);

		final String message2 = "这是第二个中文消息！";
		final byte[] crypt2 = SymmetricCrypto.encrypt(message2);
		final String msg2 = SymmetricCrypto.decryptStr(crypt2);
		Assertions.assertEquals(message2, msg2);
	}

	@Test
	public void testDecryptWithHexMessage() {
		final String message = "这是第一个用来测试密文为十六进制字符串的消息！";
		final String key = "生成一个密钥";
		final SymmetricCrypto SymmetricCrypto = new SymmetricCrypto("RC4", key.getBytes(StandardCharsets.UTF_8));
		final String encryptHex = SymmetricCrypto.encryptHex(message, CharsetUtil.UTF_8);
		final String msg = SymmetricCrypto.decryptStr(encryptHex);
		Assertions.assertEquals(message, msg);

		final String message2 = "这是第二个用来测试密文为十六进制字符串的消息！";
		final String encryptHex2 = SymmetricCrypto.encryptHex(message2);
		final String msg2 = SymmetricCrypto.decryptStr(encryptHex2);
		Assertions.assertEquals(message2, msg2);
	}


	@Test
	public void testDecryptWithBase64Message() {
		final String message = "这是第一个用来测试密文为Base64编码的消息！";
		final String key = "生成一个密钥";
		final SymmetricCrypto SymmetricCrypto = new SymmetricCrypto("RC4", key.getBytes(StandardCharsets.UTF_8));
		final String encryptHex = SymmetricCrypto.encryptBase64(message, CharsetUtil.UTF_8);
		final String msg = SymmetricCrypto.decryptStr(encryptHex);
		Assertions.assertEquals(message, msg);

		final String message2 = "这是第一个用来测试密文为Base64编码的消息！";
		final String encryptHex2 = SymmetricCrypto.encryptBase64(message2);
		final String msg2 = SymmetricCrypto.decryptStr(encryptHex2);
		Assertions.assertEquals(message2, msg2);
	}
}
