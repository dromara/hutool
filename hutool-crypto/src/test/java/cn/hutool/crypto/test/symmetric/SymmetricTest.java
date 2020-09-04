package cn.hutool.crypto.test.symmetric;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * 对称加密算法单元测试
 * 
 * @author Looly
 *
 */
public class SymmetricTest {

	@Test
	public void aesTest() {
		String content = "test中文";

		// 随机生成密钥
		byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
		
		// 构建
		SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

		// 加密
		byte[] encrypt = aes.encrypt(content);
		// 解密
		byte[] decrypt = aes.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

		// 加密为16进制表示
		String encryptHex = aes.encryptHex(content);
		// 解密为字符串
		String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void aesTest2() {
		String content = "test中文";

		// 随机生成密钥
		byte[] key = KeyUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

		// 构建
		AES aes = SecureUtil.aes(key);

		// 加密
		byte[] encrypt = aes.encrypt(content);
		// 解密
		byte[] decrypt = aes.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		// 加密为16进制表示
		String encryptHex = aes.encryptHex(content);
		// 解密为字符串
		String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void aesTest3() {
		String content = "test中文aaaaaaaaaaaaaaaaaaaaa";

		AES aes = new AES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "0102030405060708".getBytes());
		
		// 加密
		byte[] encrypt = aes.encrypt(content);
		// 解密
		byte[] decrypt = aes.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		// 加密为16进制表示
		String encryptHex = aes.encryptHex(content);
		// 解密为字符串
		String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

		Assert.assertEquals(content, decryptStr);
	}
	
	@Test
	public void aesTest4() {
		String content = "4321c9a2db2e6b08987c3b903d8d11ff";
		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, "0123456789ABHAEQ".getBytes(), "DYgjCEIMVrj2W9xN".getBytes());
		
		// 加密为16进制表示
		String encryptHex = aes.encryptHex(content);

		Assert.assertEquals("cd0e3a249eaf0ed80c330338508898c4bddcfd665a1b414622164a273ca5daf7b4ebd2c00aaa66b84dd0a237708dac8e", encryptHex);
	}

	@Test
	public void aesZeroPaddingTest() {
		String content = RandomUtil.randomString(RandomUtil.randomInt(200));
		AES aes = new AES(Mode.CBC, Padding.ZeroPadding, "0123456789ABHAEQ".getBytes(), "DYgjCEIMVrj2W9xN".getBytes());

		// 加密为16进制表示
		String encryptHex = aes.encryptHex(content);
		// 解密
		String decryptStr = aes.decryptStr(encryptHex);
		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void desTest() {
		String content = "test中文";

		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

		SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DES, key);
		byte[] encrypt = des.encrypt(content);
		byte[] decrypt = des.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		String encryptHex = des.encryptHex(content);
		String decryptStr = des.decryptStr(encryptHex);

		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void desTest2() {
		String content = "test中文";

		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();

		DES des = SecureUtil.des(key);
		byte[] encrypt = des.encrypt(content);
		byte[] decrypt = des.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		String encryptHex = des.encryptHex(content);
		String decryptStr = des.decryptStr(encryptHex);

		Assert.assertEquals(content, decryptStr);
	}
	
	@Test
	public void desTest3() {
		String content = "test中文";
		
		DES des = new DES(Mode.CTS, Padding.PKCS5Padding, "0CoJUm6Qyw8W8jud".getBytes(), "01020304".getBytes());
		
		byte[] encrypt = des.encrypt(content);
		byte[] decrypt = des.decrypt(encrypt);
		
		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));
		
		String encryptHex = des.encryptHex(content);
		String decryptStr = des.decryptStr(encryptHex);
		
		Assert.assertEquals(content, decryptStr);
	}

	@Test
	public void desdeTest() {
		String content = "test中文";

		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();

		DESede des = SecureUtil.desede(key);
		
		byte[] encrypt = des.encrypt(content);
		byte[] decrypt = des.decrypt(encrypt);

		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));

		String encryptHex = des.encryptHex(content);
		String decryptStr = des.decryptStr(encryptHex);

		Assert.assertEquals(content, decryptStr);
	}
	
	@Test
	public void desdeTest2() {
		String content = "test中文";
		
		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();
		
		DESede des = new DESede(Mode.CBC, Padding.PKCS5Padding, key, "12345678".getBytes());
		
		byte[] encrypt = des.encrypt(content);
		byte[] decrypt = des.decrypt(encrypt);
		
		Assert.assertEquals(content, StrUtil.utf8Str(decrypt));
		
		String encryptHex = des.encryptHex(content);
		String decryptStr = des.decryptStr(encryptHex);
		
		Assert.assertEquals(content, decryptStr);
	}
	
	@Test
	public void vigenereTest() {
		String content = "Wherethereisawillthereisaway";
		String key = "CompleteVictory";
		
		String encrypt = Vigenere.encrypt(content, key);
		Assert.assertEquals("zXScRZ]KIOMhQjc0\\bYRXZOJK[Vi", encrypt);
		String decrypt = Vigenere.decrypt(encrypt, key);
		Assert.assertEquals(content, decrypt);
	}
}
