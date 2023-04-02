package org.dromara.hutool.symmetric;

import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RC4Test {

	@Test
	public void testCryptMessage() {
		final String key = "This is pretty long key";
		final RC4 rc4 = new RC4(key);
		final String message = "Hello, World!";
		final byte[] crypt = rc4.encrypt(message);
		final String msg = rc4.decrypt(crypt);
		Assertions.assertEquals(message, msg);

		final String message2 = "Hello, World， this is megssage 2";
		final byte[] crypt2 = rc4.encrypt(message2);
		final String msg2 = rc4.decrypt(crypt2);
		Assertions.assertEquals(message2, msg2);
	}

	@Test
	public void testCryptWithChineseCharacters() {
		final String message = "这是一个中文消息！";
		final String key = "我是一个文件密钥";
		final RC4 rc4 = new RC4(key);
		final byte[] crypt = rc4.encrypt(message);
		final String msg = rc4.decrypt(crypt);
		Assertions.assertEquals(message, msg);

		final String message2 = "这是第二个中文消息！";
		final byte[] crypt2 = rc4.encrypt(message2);
		final String msg2 = rc4.decrypt(crypt2);
		Assertions.assertEquals(message2, msg2);
	}

	@Test
	public void testDecryptWithHexMessage() {
		final String message = "这是第一个用来测试密文为十六进制字符串的消息！";
		final String key = "生成一个密钥";
		final RC4 rc4 = new RC4(key);
		final String encryptHex = rc4.encryptHex(message, CharsetUtil.UTF_8);
		final String msg = rc4.decrypt(encryptHex);
		Assertions.assertEquals(message, msg);

		final String message2 = "这是第二个用来测试密文为十六进制字符串的消息！";
		final String encryptHex2 = rc4.encryptHex(message2);
		final String msg2 = rc4.decrypt(encryptHex2);
		Assertions.assertEquals(message2, msg2);
	}


	@Test
	public void testDecryptWithBase64Message() {
		final String message = "这是第一个用来测试密文为Base64编码的消息！";
		final String key = "生成一个密钥";
		final RC4 rc4 = new RC4(key);
		final String encryptHex = rc4.encryptBase64(message, CharsetUtil.UTF_8);
		final String msg = rc4.decrypt(encryptHex);
		Assertions.assertEquals(message, msg);

		final String message2 = "这是第一个用来测试密文为Base64编码的消息！";
		final String encryptHex2 = rc4.encryptBase64(message2);
		final String msg2 = rc4.decrypt(encryptHex2);
		Assertions.assertEquals(message2, msg2);
	}
}
