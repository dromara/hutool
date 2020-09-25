package cn.hutool.crypto.test.symmetric;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.junit.Assert;
import org.junit.Test;

public class AESTest {

	@Test
	public void encryptTest() {
		// 构建
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,
				"1234567890123456".getBytes(), "1234567890123456".getBytes());
		String encryptHex = aes.encryptHex("123456");
		Assert.assertEquals("d637735ae9e21ba50cb686b74fab8d2c", encryptHex);
	}

	@Test
	public void encryptTest2() {
		String content = "test中文";
		AES aes = new AES(Mode.CTS, Padding.PKCS5Padding,
				"0CoJUm6Qyw8W8jue".getBytes(), "0102030405060708".getBytes());
		final String encryptHex = aes.encryptHex(content);
		Assert.assertEquals("8dc9de7f050e86ca2c8261dde56dfec9", encryptHex);
	}

	@Test
	public void encryptPKCS7Test() {
		// 构建
		AES aes = new AES(Mode.CBC.name(), "pkcs7padding",
				"1234567890123456".getBytes(), "1234567890123456".getBytes());
		String encryptHex = aes.encryptHex("123456");
		Assert.assertEquals("d637735ae9e21ba50cb686b74fab8d2c", encryptHex);
	}

}
