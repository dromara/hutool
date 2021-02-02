package cn.hutool.crypto.test;

import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.PemUtil;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
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

	/**
	 * 测试EC PRIVATE KEY无法使用原来的@link cn.hutool.crypto.KeyUtil#generateRSAPrivateKey(byte[])读取
	 *
	 * @see cn.hutool.crypto.KeyUtil#generateECPrivateKey(byte[])
	 */
	@Test(expected = CryptoException.class)
	public void generateSM2PrivateKeyTest() {
		String pem = getECPrivateKeyPem("SM2");
		PemObject pemObject = PemUtil.readPemObject(new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8)));
		KeyUtil.generateRSAPrivateKey(pemObject.getContent());
	}

	/**
	 * @link cn.hutool.crypto.KeyUtil#generateECPrivateKey(byte[]) 支持读取SM2 PEM私钥
	 * @link cn.hutool.crypto.KeyUtil.ECDSAKeyPairParser#parse(byte[])
	 */
	@Test
	public void generateSM2PrivateKeyByParserTest() {
		String pem = getECPrivateKeyPem("SM2");
		PemObject pemObject = PemUtil.readPemObject(new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8)));
		PrivateKey privateKey = KeyUtil.generateECPrivateKey(pemObject.getContent());
		Assert.assertNotNull(privateKey);
	}

	/**
	 * @link cn.hutool.crypto.KeyUtil#generateECPrivateKey(byte[]) 同样支持读取ECIES PEM私钥
	 */
	@Test
	public void generateECIESPrivateKeyTest() {
		String pem = getECPrivateKeyPem("ECIES");
		PemObject pemObject = PemUtil.readPemObject(new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8)));
		PrivateKey privateKey = KeyUtil.generateECPrivateKey(pemObject.getContent());
		Assert.assertNotNull(privateKey);

	}

	private String getECPrivateKeyPem(String algorithm) {
		KeyPair keyPair = KeyUtil.generateKeyPair(algorithm);
		StringWriter stringWriter;
		try {
			stringWriter = new StringWriter();
			PemWriter w = new PemWriter(stringWriter);
			PrivateKeyInfo i = PrivateKeyInfo.getInstance(ASN1Sequence.getInstance(keyPair.getPrivate().getEncoded()));
			ASN1Object o = (ASN1Object) i.parsePrivateKey();
			w.writeObject(new PemObject("EC PRIVATE KEY", o.getEncoded("DER")));
			w.close();
			return stringWriter.toString();
		} catch (IOException e) {
			//ignore
		}
		return "";
	}
}
