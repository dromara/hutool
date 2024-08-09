package cn.hutool.crypto;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class KeyUtilTest {

	/**
	 * 测试关闭BouncyCastle支持时是否会正常抛出异常，即关闭是否有效
	 */
	@Test
	@Disabled
	public void generateKeyPairTest() {
		assertThrows(CryptoException.class, () -> {
			GlobalBouncyCastleProvider.setUseBouncyCastle(false);
			KeyPair pair = KeyUtil.generateKeyPair("SM2");
			assertNotNull(pair);
		});
	}

	@Test
	public void getRSAPublicKeyTest(){
		final KeyPair keyPair = KeyUtil.generateKeyPair("RSA");
		final PrivateKey aPrivate = keyPair.getPrivate();
		final PublicKey rsaPublicKey = KeyUtil.getRSAPublicKey(aPrivate);
		assertEquals(rsaPublicKey, keyPair.getPublic());
	}

	/**
	 * 测试EC和ECIES算法生成的KEY是一致的
	 */
	@Test
	public void generateECIESKeyTest(){
		final KeyPair ecies = KeyUtil.generateKeyPair("ECIES");
		assertNotNull(ecies.getPrivate());
		assertNotNull(ecies.getPublic());

		byte[] privateKeyBytes = ecies.getPrivate().getEncoded();

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("EC", privateKeyBytes);
		assertEquals(ecies.getPrivate(), privateKey);
	}

	@Test
	public void generateDHTest(){
		final KeyPair dh = KeyUtil.generateKeyPair("DH");
		assertNotNull(dh.getPrivate());
		assertNotNull(dh.getPublic());

		byte[] privateKeyBytes = dh.getPrivate().getEncoded();

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("DH", privateKeyBytes);
		assertEquals(dh.getPrivate(), privateKey);
	}

	@Test
	public void generateSm4KeyTest(){
		// https://github.com/dromara/hutool/issues/2150
		assertEquals(16, KeyUtil.generateKey("sm4").getEncoded().length);
		assertEquals(32, KeyUtil.generateKey("sm4", 256).getEncoded().length);
	}
}
