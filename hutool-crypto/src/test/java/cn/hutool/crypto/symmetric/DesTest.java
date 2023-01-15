package cn.hutool.crypto.symmetric;

import cn.hutool.core.text.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import org.junit.Assert;
import org.junit.Test;

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

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void desTest2() {
		final String content = "test中文";

		final byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

		final DES des = SecureUtil.des(key);
		final byte[] encrypt = des.encrypt(content);
		final byte[] decrypt = des.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void desTest3() {
		final String content = "test中文";

		final DES des = new DES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "01020304".getBytes());

		final byte[] encrypt = des.encrypt(content);
		final byte[] decrypt = des.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		final String encryptHex = des.encryptHex(content);
		final String decryptStr = des.decryptStr(encryptHex);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void encryptDecryptTest(){
		final String content = "我是一个测试的test字符串123";
		final DES des = SecureUtil.des();

		final String encryptHex = des.encryptHex(content);
		final String result = des.decryptStr(encryptHex);

		Assert.assertEquals(content, result);
	}

	@Test
	public void encryptDecryptWithCustomTest(){
		final String content = "我是一个测试的test字符串123";
		final DES des = new DES(
				Mode.CTS,
				Padding.PKCS5Padding,
				StrUtil.bytes("12345678"),
				StrUtil.bytes("11223344")
		);

		final String encryptHex = des.encryptHex(content);
		final String result = des.decryptStr(encryptHex);

		Assert.assertEquals(content, result);
	}

	@Test
	public void issueI5I5B3Test(){
		final DES des = new DES(Mode.CTS, Padding.PKCS5Padding, "1234567890".getBytes(), "12345678".getBytes());
		des.encryptHex("root");
	}
}
