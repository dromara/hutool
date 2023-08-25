package cn.hutool.crypto.asymmetric;

import cn.hutool.core.util.StrUtil;
import org.bouncycastle.jce.spec.IESParameterSpec;
import org.junit.Assert;
import org.junit.Test;

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
	private void doTest(AsymmetricCrypto cryptoForEncrypt, AsymmetricCrypto cryptoForDecrypt){
		String textBase = "我是一段特别长的测试";
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			text.append(textBase);
		}

		// 公钥加密，私钥解密
		String encryptStr = cryptoForEncrypt.encryptBase64(text.toString(), KeyType.PublicKey);

		String decryptStr = StrUtil.utf8Str(cryptoForDecrypt.decrypt(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text.toString(), decryptStr);
	}
}
