package org.dromara.hutool.crypto;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.crypto.asymmetric.KeyType;
import org.dromara.hutool.crypto.asymmetric.RSA;
import org.dromara.hutool.crypto.asymmetric.SM2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PemUtilTest {

	@Test
	public void readPrivateKeyTest() {
		final PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_private_key.pem"));
		Assertions.assertNotNull(privateKey);
	}

	@Test
	public void readPublicKeyTest() {
		final PublicKey publicKey = PemUtil.readPemPublicKey(ResourceUtil.getStream("test_public_key.csr"));
		Assertions.assertNotNull(publicKey);
	}

	@Test
	public void readPemKeyTest() {
		final PublicKey publicKey = (PublicKey) PemUtil.readPemKey(ResourceUtil.getStream("test_public_key.csr"));
		Assertions.assertNotNull(publicKey);
	}

	@Test
	public void validateKey() {
		final PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_private_key.pem"));
		final PublicKey publicKey = PemUtil.readPemPublicKey(ResourceUtil.getStream("test_public_key.csr"));

		final RSA rsa = new RSA(privateKey, publicKey);
		final String str = "你好，Hutool";//测试字符串

		final String encryptStr = rsa.encryptBase64(str, KeyType.PublicKey);
		final String decryptStr = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
		Assertions.assertEquals(str, decryptStr);
	}

	@Test
	public void readECPrivateKeyTest() {
		final PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_ec_private_key.pem"));
		final SM2 sm2 = new SM2(privateKey, null);
		sm2.usePlainEncoding();

		//需要签名的明文,得到明文对应的字节数组
		final byte[] dataBytes = "我是一段测试aaaa".getBytes(StandardCharsets.UTF_8);

		final byte[] sign = sm2.sign(dataBytes, null);
		// 64位签名
		Assertions.assertEquals(64, sign.length);
	}

	@Test
	public void readECSec1PrivateKeyTest() {
		final PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_ec_sec1_private_key.pem"));
		final SM2 sm2 = new SM2(privateKey, null);
		sm2.usePlainEncoding();

		//需要签名的明文,得到明文对应的字节数组
		final byte[] dataBytes = "我是一段测试aaaa".getBytes(StandardCharsets.UTF_8);

		final byte[] sign = sm2.sign(dataBytes, null);
		// 64位签名
		Assertions.assertEquals(64, sign.length);
	}

	@Test
	@Disabled
	public void readECPrivateKeyTest2() {
		// https://gitee.com/dromara/hutool/issues/I37Z75
		final byte[] d = PemUtil.readPem(FileUtil.getInputStream("d:/test/keys/priv.key"));
		final byte[] publicKey = PemUtil.readPem(FileUtil.getInputStream("d:/test/keys/pub.key"));

		final SM2 sm2 = new SM2(d, publicKey);
		sm2.usePlainEncoding();

		final String content = "我是Hanley.";
		final byte[] sign = sm2.sign(ByteUtil.toUtf8Bytes(content));
		final boolean verify = sm2.verify(ByteUtil.toUtf8Bytes(content), sign);
		Assertions.assertTrue(verify);
	}
}
