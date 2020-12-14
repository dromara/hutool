package cn.hutool.crypto.test;

import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.crypto.KeyUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyUtilTest {

	/**
	 * 测试关闭BouncyCastle支持时是否会正常抛出异常，即关闭是否有效
	 */
	@Test(expected = CryptoException.class)
	@Ignore
	public void generateKeyPairTest() {
		GlobalBouncyCastleProvider.setUseBouncyCastle(false);
		KeyPair pair = KeyUtil.generateKeyPair("SM2");
		Assert.assertNotNull(pair);
	}

	@Test
	public void getRSAPublicKeyTest(){
		final KeyPair keyPair = KeyUtil.generateKeyPair("RSA");
		final PrivateKey aPrivate = keyPair.getPrivate();
		final PublicKey rsaPublicKey = KeyUtil.getRSAPublicKey(aPrivate);
		Assert.assertEquals(rsaPublicKey, keyPair.getPublic());
	}

	/**
	 * 测试EC和ECIES算法生成的KEY是一致的
	 */
	@Test
	public void generateECIESKeyTest(){
		final KeyPair ecies = KeyUtil.generateKeyPair("ECIES");
		Assert.assertNotNull(ecies.getPrivate());
		Assert.assertNotNull(ecies.getPublic());

		byte[] privateKeyBytes = ecies.getPrivate().getEncoded();

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("EC", privateKeyBytes);
		Assert.assertEquals(ecies.getPrivate(), privateKey);
	}
}
