package cn.hutool.crypto.test.symmetric;

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
}
