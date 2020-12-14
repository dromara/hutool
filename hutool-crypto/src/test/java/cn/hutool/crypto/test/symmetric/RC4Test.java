package cn.hutool.crypto.test.symmetric;

import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import cn.hutool.crypto.symmetric.RC4;

public class RC4Test {
	
	@Test
	public void testCryptMessage() {
		String key = "This is pretty long key";
		RC4 rc4 = new RC4(key);
		String message = "Hello, World!";
		byte[] crypt = rc4.encrypt(message);
		String msg = rc4.decrypt(crypt);
		Assert.assertEquals(message, msg);
		
		String message2 = "Hello, World， this is megssage 2";
		byte[] crypt2 = rc4.encrypt(message2);
		String msg2 = rc4.decrypt(crypt2);
		Assert.assertEquals(message2, msg2);
	}

	@Test
	public void testCryptWithChineseCharacters() {
		String message = "这是一个中文消息！";
		String key = "我是一个文件密钥";
		RC4 rc4 = new RC4(key);
		byte[] crypt = rc4.encrypt(message);
		String msg = rc4.decrypt(crypt);
		Assert.assertEquals(message, msg);
		
		String message2 = "这是第二个中文消息！";
		byte[] crypt2 = rc4.encrypt(message2);
		String msg2 = rc4.decrypt(crypt2);
		Assert.assertEquals(message2, msg2);
	}

	@Test
	public void testDecryptWithHexMessage() {
		String message = "这是第一个用来测试密文为十六进制字符串的消息！";
		String key = "生成一个密钥";
		RC4 rc4 = new RC4(key);
		String encryptHex = rc4.encryptHex(message, CharsetUtil.CHARSET_UTF_8);
		String msg = rc4.decrypt(encryptHex);
		Assert.assertEquals(message, msg);

		String message2 = "这是第二个用来测试密文为十六进制字符串的消息！";
		String encryptHex2 = rc4.encryptHex(message2);
		String msg2 = rc4.decrypt(encryptHex2);
		Assert.assertEquals(message2, msg2);
	}


	@Test
	public void testDecryptWithBase64Message() {
		String message = "这是第一个用来测试密文为Base64编码的消息！";
		String key = "生成一个密钥";
		RC4 rc4 = new RC4(key);
		String encryptHex = rc4.encryptBase64(message, CharsetUtil.CHARSET_UTF_8);
		String msg = rc4.decrypt(encryptHex);
		Assert.assertEquals(message, msg);

		String message2 = "这是第一个用来测试密文为Base64编码的消息！";
		String encryptHex2 = rc4.encryptBase64(message2);
		String msg2 = rc4.decrypt(encryptHex2);
		Assert.assertEquals(message2, msg2);
	}
}
