package cn.hutool.crypto.symmetric;

import cn.hutool.crypto.Padding;
import org.junit.Assert;
import org.junit.Test;

public class Issue2613Test {

	@Test
	public void aesGcmTest(){
		final AES aes = new AES("GCM", Padding.NoPadding.name(),
				"1234567890123456".getBytes(),
				"1234567890123456".getBytes());
		final String encryptHex = aes.encryptHex("123456");

		final String s = aes.decryptStr(encryptHex);
		Assert.assertEquals("123456", s);
	}
}
