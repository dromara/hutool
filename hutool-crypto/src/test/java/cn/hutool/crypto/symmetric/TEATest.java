package cn.hutool.crypto.symmetric;

import org.junit.Assert;
import org.junit.Test;

/**
 * TEA（Tiny Encryption Algorithm）和 XTEA算法单元测试
 */
public class TEATest {

	@Test
	public void teaTest() {
		String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final SymmetricCrypto tea = new SymmetricCrypto("TEA", "MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assert.assertEquals(data, decryptStr);
	}

	@Test
	public void xteaTest() {
		String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final SymmetricCrypto tea = new SymmetricCrypto("XTEA", "MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assert.assertEquals(data, decryptStr);
	}

	@Test
	public void xxteaTest() {
		String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final XXTEA tea = new XXTEA("MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assert.assertEquals(data, decryptStr);
	}
}
