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
