package cn.hutool.crypto.test.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
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

	/**
	 * AES加密/解密
	 * 加解密模式:ECB模式 数据填充模式:PKCS7
	 * <p>
	 * 数据：16c5
	 * 密钥: 0102030405060708090a0b0c0d0e0f10
	 * 数据格式:hex格式 加解密模式:ECB模式 数据填充模式:PKCS7
	 * 结果: 25869eb3ff227d9e34b3512d3c3c92ed 【加密后的Hex】
	 * 结果: JYaes/8ifZ40s1EtPDyS7Q== 【加密后的Base64】
	 * <p>
	 * 数据：16c5
	 * 密钥: 0102030405060708090a0b0c0d0e0f10
	 * 数据格式:UTF-8格式 加解密模式:ECB模式 数据填充模式:PKCS7
	 * 结果: 79c210d3e304932cf9ea6a9c887c6d7c 【加密后的Hex】
	 * 结果: ecIQ0+MEkyz56mqciHxtfA== 【加密后的Base64】
	 * <p>
	 * AES在线解密 AES在线加密 Aes online hex 十六进制密钥 - The X 在线工具
	 * https://the-x.cn/cryptography/Aes.aspx
	 * <p>
	 */
	@Test
	public void encryptPKCS7Test2() {
		// 构建
		AES aes = new AES(Mode.ECB.name(), "pkcs7padding",
				HexUtil.decodeHex("0102030405060708090a0b0c0d0e0f10"));

		// ------------------------------------------------------------------------
		// 加密数据为16进制字符串
		String encryptHex = aes.encryptHex(HexUtil.decodeHex("16c5"));
		// 加密后的Hex
		Assert.assertEquals("25869eb3ff227d9e34b3512d3c3c92ed", encryptHex);

		// 加密数据为16进制字符串
		String encryptHex2 = aes.encryptBase64(HexUtil.decodeHex("16c5"));
		// 加密后的Base64
		Assert.assertEquals("JYaes/8ifZ40s1EtPDyS7Q==", encryptHex2);

		// 解密
		Assert.assertEquals("16c5", HexUtil.encodeHexStr(aes.decrypt("25869eb3ff227d9e34b3512d3c3c92ed")));
		Assert.assertEquals("16c5", HexUtil.encodeHexStr(aes.decrypt(HexUtil.encodeHexStr(Base64.decode("JYaes/8ifZ40s1EtPDyS7Q==")))));
		// ------------------------------------------------------------------------

		// ------------------------------------------------------------------------
		// 加密数据为字符串(UTF-8)
		String encryptStr = aes.encryptHex("16c5");
		// 加密后的Hex
		Assert.assertEquals("79c210d3e304932cf9ea6a9c887c6d7c", encryptStr);

		// 加密数据为字符串(UTF-8)
		String encryptStr2 = aes.encryptBase64("16c5");
		// 加密后的Base64
		Assert.assertEquals("ecIQ0+MEkyz56mqciHxtfA==", encryptStr2);

		// 解密
		Assert.assertEquals("16c5", aes.decryptStr("79c210d3e304932cf9ea6a9c887c6d7c"));
		Assert.assertEquals("16c5", aes.decryptStr(Base64.decode("ecIQ0+MEkyz56mqciHxtfA==")));
		// ------------------------------------------------------------------------
	}
}
