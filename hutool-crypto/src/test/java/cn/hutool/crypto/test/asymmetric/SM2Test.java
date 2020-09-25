package cn.hutool.crypto.test.asymmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * SM2算法单元测试
 *
 * @author Looly, Gsealy
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
		sm2.setMode(SM2Engine.Mode.C1C2C3);

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
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			text.append(textBase);
		}

		SM2 sm2 = new SM2();

		// 公钥加密，私钥解密
		String encryptStr = sm2.encryptBase64(text.toString(), KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(sm2.decrypt(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text.toString(), decryptStr);

		// 测试自定义密钥后是否生效
		PrivateKey privateKey = sm2.getPrivateKey();
		PublicKey publicKey = sm2.getPublicKey();

		sm2 = SmUtil.sm2();
		sm2.setPrivateKey(privateKey);
		sm2.setPublicKey(publicKey);
		String decryptStr2 = StrUtil.utf8Str(sm2.decrypt(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text.toString(), decryptStr2);
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
	public void sm2SignAndVerifyHexTest() {
		String content = "我是Hanley.";

		final SM2 sm2 = SmUtil.sm2();

		String sign = sm2.signHex(HexUtil.encodeHexStr(content));
		boolean verify = sm2.verifyHex(HexUtil.encodeHexStr(content), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2SignAndVerifyUseKeyTest() {
		String content = "我是Hanley.";

		KeyPair pair = SecureUtil.generateKeyPair("SM2");

		final SM2 sm2 = new SM2(pair.getPrivate(), pair.getPublic());

		byte[] sign = sm2.sign(content.getBytes());
		boolean verify = sm2.verify(content.getBytes(), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2SignAndVerifyUseKeyTest2() {
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

	@Test
	public void sm2WithPointTest() {
		String d = "FAB8BBE670FAE338C9E9382B9FB6485225C11A3ECB84C938F10F20A93B6215F0";
		String x = "9EF573019D9A03B16B0BE44FC8A5B4E8E098F56034C97B312282DD0B4810AFC3";
		String y = "CC759673ED0FC9B9DC7E6FA38F0E2B121E02654BF37EA6B63FAF2A0D6013EADF";

		String data = "434477813974bf58f94bcf760833c2b40f77a5fc360485b0b9ed1bd9682edb45";
		String id = "31323334353637383132333435363738";

		final SM2 sm2 = new SM2(d, x, y);
		final String sign = sm2.signHex(data, id);
		Assert.assertTrue(sm2.verifyHex(data, sign));
	}

	@Test
	public void sm2PlainWithPointTest() {
		// 测试地址：https://i.goto327.top/CryptTools/SM2.aspx?tdsourcetag=s_pctim_aiomsg

		String d = "FAB8BBE670FAE338C9E9382B9FB6485225C11A3ECB84C938F10F20A93B6215F0";
		String x = "9EF573019D9A03B16B0BE44FC8A5B4E8E098F56034C97B312282DD0B4810AFC3";
		String y = "CC759673ED0FC9B9DC7E6FA38F0E2B121E02654BF37EA6B63FAF2A0D6013EADF";

		String data = "434477813974bf58f94bcf760833c2b40f77a5fc360485b0b9ed1bd9682edb45";
		String id = "31323334353637383132333435363738";

		final SM2 sm2 = new SM2(d, x, y);
		// 生成的签名是64位
		sm2.usePlainEncoding();

		String sign = "DCA0E80A7F46C93714B51C3EFC55A922BCEF7ECF0FE9E62B53BA6A7438B543A76C145A452CA9036F3CB70D7E6C67D4D9D7FE114E5367A2F6F5A4D39F2B10F3D6";
		Assert.assertTrue(sm2.verifyHex(data, sign));
	}

	@Test
	public void sm2PlainWithPointTest2() {
		String d = "4BD9A450D7E68A5D7E08EB7A0BFA468FD3EB32B71126246E66249A73A9E4D44A";
		String q = "04970AB36C3B870FBC04041087DB1BC36FB4C6E125B5EA406DB0EC3E2F80F0A55D8AFF28357A0BB215ADC2928BE76F1AFF869BF4C0A3852A78F3B827812C650AD3";

		String data = "123456";

		final ECPublicKeyParameters ecPublicKeyParameters = ECKeyUtil.toSm2PublicParams(q);
		final ECPrivateKeyParameters ecPrivateKeyParameters = ECKeyUtil.toSm2PrivateParams(d);

		final SM2 sm2 = new SM2(ecPrivateKeyParameters, ecPublicKeyParameters);
		sm2.setMode(SM2Engine.Mode.C1C2C3);
		final String encryptHex = sm2.encryptHex(data, KeyType.PublicKey);
		Console.log(encryptHex);
		final String decryptStr = sm2.decryptStr(encryptHex, KeyType.PrivateKey);

		Assert.assertEquals(data, decryptStr);
	}

	@Test
	public void encryptAndSignTest(){
		SM2 sm2 = SmUtil.sm2();

		String src = "Sm2Test";
		byte[] data = sm2.encrypt(src, KeyType.PublicKey);
		byte[] sign =  sm2.sign(src.getBytes());

		Assert.assertTrue(sm2.verify( src.getBytes(), sign));

		byte[] dec =  sm2.decrypt(data, KeyType.PrivateKey);
		Assert.assertArrayEquals(dec, src.getBytes());
	}

}
