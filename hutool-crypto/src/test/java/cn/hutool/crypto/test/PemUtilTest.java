package cn.hutool.crypto.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SM2;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

public class PemUtilTest {

	@Test
	public void readPrivateKeyTest() {
		PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_private_key.pem"));
		Assert.assertNotNull(privateKey);
	}

	@Test
	public void readPublicKeyTest() {
		PublicKey publicKey = PemUtil.readPemPublicKey(ResourceUtil.getStream("test_public_key.csr"));
		Assert.assertNotNull(publicKey);
	}

	@Test
	public void readPemKeyTest() {
		PublicKey publicKey = (PublicKey) PemUtil.readPemKey(ResourceUtil.getStream("test_public_key.csr"));
		Assert.assertNotNull(publicKey);
	}

	@Test
	public void validateKey() {
		PrivateKey privateKey = PemUtil.readPemPrivateKey(ResourceUtil.getStream("test_private_key.pem"));
		PublicKey publicKey = PemUtil.readPemPublicKey(ResourceUtil.getStream("test_public_key.csr"));

		RSA rsa = new RSA(privateKey, publicKey);
		String str = "你好，Hutool";//测试字符串

		String encryptStr = rsa.encryptBase64(str, KeyType.PublicKey);
		String decryptStr = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
		Assert.assertEquals(str, decryptStr);
	}

	@Test
	public void readECPrivateKeyTest() {
		PrivateKey privateKey = PemUtil.readSm2PemPrivateKey(ResourceUtil.getStream("test_ec_private_key.pem"));
		SM2 sm2 = new SM2(privateKey, null);
		sm2.usePlainEncoding();

		//需要签名的明文,得到明文对应的字节数组
		byte[] dataBytes = "我是一段测试aaaa".getBytes(StandardCharsets.UTF_8);

		byte[] sign = sm2.sign(dataBytes, null);
		// 64位签名
		Assert.assertEquals(64, sign.length);
	}

	@Test
	@Ignore
	public void readECPrivateKeyTest2() {
		// https://gitee.com/loolly/hutool/issues/I37Z75
		byte[] d = PemUtil.readPem(FileUtil.getInputStream("d:/test/keys/priv.key"));
		byte[] publicKey = PemUtil.readPem(FileUtil.getInputStream("d:/test/keys/pub.key"));

		SM2 sm2 = new SM2(d, publicKey);
		sm2.usePlainEncoding();

		String content = "我是Hanley.";
		byte[] sign = sm2.sign(StrUtil.utf8Bytes(content));
		boolean verify = sm2.verify(StrUtil.utf8Bytes(content), sign);
		Assert.assertTrue(verify);
	}
}
