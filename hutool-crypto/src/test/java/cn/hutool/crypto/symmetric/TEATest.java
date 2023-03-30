package cn.hutool.crypto.symmetric;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * TEA（Tiny Encryption Algorithm）和 XTEA算法单元测试
 */
public class TEATest {

	@Test
	public void teaTest() {
		final String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final SymmetricCrypto tea = new SymmetricCrypto("TEA", "MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assertions.assertEquals(data, decryptStr);
	}

	@Test
	public void xteaTest() {
		final String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final SymmetricCrypto tea = new SymmetricCrypto("XTEA", "MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assertions.assertEquals(data, decryptStr);
	}

	@Test
	public void xxteaTest() {
		final String data = "测试的加密数据 by Hutool";

		// 密钥必须为128bit
		final XXTEA tea = new XXTEA("MyPassword123456".getBytes());
		final byte[] encrypt = tea.encrypt(data);

		// 解密
		final String decryptStr = tea.decryptStr(encrypt);

		Assertions.assertEquals(data, decryptStr);
	}
}
