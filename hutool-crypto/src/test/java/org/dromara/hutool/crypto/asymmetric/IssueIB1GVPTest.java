package org.dromara.hutool.crypto.asymmetric;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIB1GVPTest {

	/**
	 * https://stackoverflow.com/questions/50298687/bouncy-castle-vs-java-default-rsa-with-oaep
	 */
	@Test
	void rsaOaepTest() {
		final RSA rsa = new RSA("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
		// 公钥加密，私钥解密
		final byte[] encrypt = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);
		final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 私钥加密，公钥解密
		final byte[] encrypt2 = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PrivateKey);
		final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.UTF_8));
	}
}
