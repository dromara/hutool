package cn.hutool.crypto.test;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.junit.Assert;
import org.junit.Test;

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
}
