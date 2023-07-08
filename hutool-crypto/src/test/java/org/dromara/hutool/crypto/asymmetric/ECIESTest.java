package org.dromara.hutool.crypto.asymmetric;

import org.bouncycastle.jce.spec.IESParameterSpec;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ECIESTest {

	@Test
	public void eciesTest(){
		final ECIES ecies = new ECIES();
		final IESParameterSpec iesParameterSpec = new IESParameterSpec(null, null, 128);
		ecies.setAlgorithmParameterSpec(iesParameterSpec);

		doTest(ecies, ecies);
	}

	@Test
	public void eciesTest2(){
		final IESParameterSpec iesParameterSpec = new IESParameterSpec(null, null, 128);

		final ECIES ecies = new ECIES();
		ecies.setAlgorithmParameterSpec(iesParameterSpec);

		final byte[] privateKeyBytes = ecies.getPrivateKey().getEncoded();
		final ECIES ecies2 = new ECIES(privateKeyBytes, null);
		ecies2.setAlgorithmParameterSpec(iesParameterSpec);


		doTest(ecies, ecies2);
	}

	/**
	 * 测试用例
	 *
	 * @param cryptoForEncrypt 加密的Crypto
	 * @param cryptoForDecrypt 解密的Crypto
	 */
	private void doTest(final AsymmetricCrypto cryptoForEncrypt, final AsymmetricCrypto cryptoForDecrypt){
		final String textBase = "我是一段特别长的测试";
		final StringBuilder text = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			text.append(textBase);
		}

		// 公钥加密，私钥解密
		final String encryptStr = cryptoForEncrypt.encryptBase64(text.toString(), KeyType.PublicKey);

		final String decryptStr = StrUtil.utf8Str(cryptoForDecrypt.decrypt(encryptStr, KeyType.PrivateKey));
		Assertions.assertEquals(text.toString(), decryptStr);
	}
}
