package cn.hutool.crypto.test;

import java.security.KeyPair;
import java.security.PublicKey;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.asymmetric.SM2Engine.SM2Mode;

/**
 * SM2算法单元测试
 * 
 * @author Looly, Gsealy
 *
 */
public class SM2Test {

	@Test
	public void generateKeyPairTest() {
		KeyPair pair = SecureUtil.generateKeyPair("SM2");
		Assert.assertNotNull(pair.getPrivate());
		Assert.assertNotNull(pair.getPublic());
	}

	@Test
	public void KeyPairOIDTest() {
		// OBJECT IDENTIFIER 1.2.156.10197.1.301
		String OID = "06082A811CCF5501822D";
		KeyPair pair = SecureUtil.generateKeyPair("SM2");
		Assert.assertTrue(HexUtil.encodeHexStr(pair.getPrivate().getEncoded()).toUpperCase().contains(OID));
		Assert.assertTrue(HexUtil.encodeHexStr(pair.getPublic().getEncoded()).toUpperCase().contains(OID));
	}

	@Test
	public void sm2CustomKeyTest() {
		KeyPair pair = SecureUtil.generateKeyPair("SM2");
		byte[] privateKey = pair.getPrivate().getEncoded();
		byte[] publicKey = pair.getPublic().getEncoded();

		SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
		sm2.setMode(SM2Mode.C1C3C2);

		// 公钥加密，私钥解密
		byte[] encrypt = sm2.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
		byte[] decrypt = sm2.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void sm2Test() {
		final SM2 sm2 = SmUtil.sm2();

		// 获取私钥和公钥
		Assert.assertNotNull(sm2.getPrivateKey());
		Assert.assertNotNull(sm2.getPrivateKeyBase64());
		Assert.assertNotNull(sm2.getPublicKey());
		Assert.assertNotNull(sm2.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		byte[] encrypt = sm2.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
		byte[] decrypt = sm2.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void sm2BcdTest() {
		String text = "我是一段测试aaaa";

		final SM2 sm2 = SmUtil.sm2();

		// 公钥加密，私钥解密
		String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text, decryptStr);
	}

	@Test
	public void sm2Base64Test() {
		String textBase = "我是一段特别长的测试";
		String text = "";
		for (int i = 0; i < 100; i++) {
			text += textBase;
		}

		final SM2 sm2 = new SM2();

		// 公钥加密，私钥解密
		String encryptStr = sm2.encryptBase64(text, KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(sm2.decryptFromBase64(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text, decryptStr);
	}

	@Test
	public void sm2SignAndVerifyTest() {
		String content = "我是Hanley.";

		final SM2 sm2 = SmUtil.sm2();

		byte[] sign = sm2.sign(content.getBytes());
		boolean verify = sm2.verify(content.getBytes(), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2SignAndVerifyUseKeyTest() {
		String content = "我是Hanley.";

		KeyPair pair = SecureUtil.generateKeyPair("SM2");

		final SM2 sm2 = new SM2(//
				HexUtil.encodeHexStr(pair.getPrivate().getEncoded()), //
				HexUtil.encodeHexStr(pair.getPublic().getEncoded())//
		);

		byte[] sign = sm2.sign(content.getBytes());
		boolean verify = sm2.verify(content.getBytes(), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2PublicKeyEncodeDecodeTest() {
		KeyPair pair = SecureUtil.generateKeyPair("SM2");
		PublicKey publicKey = pair.getPublic();
		byte[] data = KeyUtil.encodeECPublicKey(publicKey);
		String encodeHex = HexUtil.encodeHexStr(data);
		String encodeB64 = Base64.encode(data);
		PublicKey Hexdecode = KeyUtil.decodeECPoint(encodeHex, KeyUtil.SM2_DEFAULT_CURVE);
		PublicKey B64decode = KeyUtil.decodeECPoint(encodeB64, KeyUtil.SM2_DEFAULT_CURVE);
		Assert.assertEquals(HexUtil.encodeHexStr(publicKey.getEncoded()), HexUtil.encodeHexStr(Hexdecode.getEncoded()));
		Assert.assertEquals(HexUtil.encodeHexStr(publicKey.getEncoded()), HexUtil.encodeHexStr(B64decode.getEncoded()));
	}
}
