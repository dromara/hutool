package cn.hutool.crypto.asymmetric;

import cn.hutool.core.codec.BaseN.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.codec.HexUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.crypto.ECKeyUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.spec.OpenSSHPrivateKeySpec;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
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
		final KeyPair pair = KeyUtil.generateKeyPair("SM2");
		Assert.assertNotNull(pair.getPrivate());
		Assert.assertNotNull(pair.getPublic());

		new SM2(pair.getPrivate(), pair.getPublic());
	}

	@Test
	public void KeyPairOIDTest() {
		// OBJECT IDENTIFIER 1.2.156.10197.1.301
		final String OID = "06082A811CCF5501822D";
		final KeyPair pair = KeyUtil.generateKeyPair("SM2");
		Assert.assertTrue(HexUtil.encodeHexStr(pair.getPrivate().getEncoded()).toUpperCase().contains(OID));
		Assert.assertTrue(HexUtil.encodeHexStr(pair.getPublic().getEncoded()).toUpperCase().contains(OID));
	}

	@Test
	public void sm2CustomKeyTest() {
		final KeyPair pair = KeyUtil.generateKeyPair("SM2");
		final byte[] privateKey = pair.getPrivate().getEncoded();
		final byte[] publicKey = pair.getPublic().getEncoded();

		final SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
		sm2.setMode(SM2Engine.Mode.C1C2C3);

		// 公钥加密，私钥解密
		final byte[] encrypt = sm2.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);
		final byte[] decrypt = sm2.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));
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
		final byte[] encrypt = sm2.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);
		final byte[] decrypt = sm2.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));
	}

	@Test
	public void sm2BcdTest() {
		final String text = "我是一段测试aaaa";

		final SM2 sm2 = SmUtil.sm2();

		// 公钥加密，私钥解密
		final String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey);
		final String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text, decryptStr);
	}

	@Test
	public void sm2Base64Test() {
		final String textBase = "我是一段特别长的测试";
		final StringBuilder text = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			text.append(textBase);
		}

		SM2 sm2 = new SM2();

		// 公钥加密，私钥解密
		final String encryptStr = sm2.encryptBase64(text.toString(), KeyType.PublicKey);
		final String decryptStr = StrUtil.utf8Str(sm2.decrypt(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text.toString(), decryptStr);

		// 测试自定义密钥后是否生效
		final PrivateKey privateKey = sm2.getPrivateKey();
		final PublicKey publicKey = sm2.getPublicKey();

		sm2 = SmUtil.sm2();
		sm2.setPrivateKey(privateKey);
		sm2.setPublicKey(publicKey);
		final String decryptStr2 = StrUtil.utf8Str(sm2.decrypt(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text.toString(), decryptStr2);
	}

	@Test
	public void sm2SignTest(){
		//需要签名的明文,得到明文对应的字节数组
		final byte[] dataBytes = "我是一段测试aaaa".getBytes(StandardCharsets.UTF_8);

		//指定的私钥
		final String privateKeyHex = "1ebf8b341c695ee456fd1a41b82645724bc25d79935437d30e7e4b0a554baa5e";
		final SM2 sm2 = new SM2(privateKeyHex, null, null);
		sm2.usePlainEncoding();
		final byte[] sign = sm2.sign(dataBytes, null);
		// 64位签名
		Assert.assertEquals(64, sign.length);
	}

	@Test
	public void sm2VerifyTest(){
		//指定的公钥
		final String publicKeyHex = "04db9629dd33ba568e9507add5df6587a0998361a03d3321948b448c653c2c1b7056434884ab6f3d1c529501f166a336e86f045cea10dffe58aa82ea13d7253763";
		//需要加密的明文,得到明文对应的字节数组
		final byte[] dataBytes = "我是一段测试aaaa".getBytes(StandardCharsets.UTF_8);
		//签名值
		final String signHex = "2881346e038d2ed706ccdd025f2b1dafa7377d5cf090134b98756fafe084dddbcdba0ab00b5348ed48025195af3f1dda29e819bb66aa9d4d088050ff148482a1";

		final SM2 sm2 = new SM2(null, publicKeyHex);
		sm2.usePlainEncoding();

		final boolean verify = sm2.verify(dataBytes, HexUtil.decodeHex(signHex));
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2SignAndVerifyTest() {
		final String content = "我是Hanley.";

		final SM2 sm2 = SmUtil.sm2();

		final byte[] sign = sm2.sign(StrUtil.utf8Bytes(content));
		final boolean verify = sm2.verify(StrUtil.utf8Bytes(content), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2SignAndVerifyHexTest() {
		final String content = "我是Hanley.";

		final SM2 sm2 = SmUtil.sm2();

		final String sign = sm2.signHex(HexUtil.encodeHexStr(content));
		final boolean verify = sm2.verifyHex(HexUtil.encodeHexStr(content), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2SignAndVerifyUseKeyTest() {
		final String content = "我是Hanley.";

		final KeyPair pair = KeyUtil.generateKeyPair("SM2");

		final SM2 sm2 = new SM2(pair.getPrivate(), pair.getPublic());

		final byte[] sign = sm2.sign(content.getBytes(StandardCharsets.UTF_8));
		final boolean verify = sm2.verify(content.getBytes(StandardCharsets.UTF_8), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2SignAndVerifyUseKeyTest2() {
		final String content = "我是Hanley.";

		final KeyPair pair = KeyUtil.generateKeyPair("SM2");

		final SM2 sm2 = new SM2(//
				HexUtil.encodeHexStr(pair.getPrivate().getEncoded()), //
				HexUtil.encodeHexStr(pair.getPublic().getEncoded())//
		);

		final byte[] sign = sm2.sign(content.getBytes(StandardCharsets.UTF_8));
		final boolean verify = sm2.verify(content.getBytes(StandardCharsets.UTF_8), sign);
		Assert.assertTrue(verify);
	}

	@Test
	public void sm2PublicKeyEncodeDecodeTest() {
		final KeyPair pair = KeyUtil.generateKeyPair("SM2");
		final PublicKey publicKey = pair.getPublic();
		final byte[] data = KeyUtil.encodeECPublicKey(publicKey);
		final String encodeHex = HexUtil.encodeHexStr(data);
		final String encodeB64 = Base64.encode(data);
		final PublicKey Hexdecode = KeyUtil.decodeECPoint(encodeHex, KeyUtil.SM2_DEFAULT_CURVE);
		final PublicKey B64decode = KeyUtil.decodeECPoint(encodeB64, KeyUtil.SM2_DEFAULT_CURVE);
		Assert.assertEquals(HexUtil.encodeHexStr(publicKey.getEncoded()), HexUtil.encodeHexStr(Hexdecode.getEncoded()));
		Assert.assertEquals(HexUtil.encodeHexStr(publicKey.getEncoded()), HexUtil.encodeHexStr(B64decode.getEncoded()));
	}

	@Test
	public void sm2WithPointTest() {
		final String d = "FAB8BBE670FAE338C9E9382B9FB6485225C11A3ECB84C938F10F20A93B6215F0";
		final String x = "9EF573019D9A03B16B0BE44FC8A5B4E8E098F56034C97B312282DD0B4810AFC3";
		final String y = "CC759673ED0FC9B9DC7E6FA38F0E2B121E02654BF37EA6B63FAF2A0D6013EADF";

		final String data = "434477813974bf58f94bcf760833c2b40f77a5fc360485b0b9ed1bd9682edb45";
		final String id = "31323334353637383132333435363738";

		final SM2 sm2 = new SM2(d, x, y);
		final String sign = sm2.signHex(data, id);
		Assert.assertTrue(sm2.verifyHex(data, sign));
	}

	@Test
	public void sm2PlainWithPointTest() {
		// 测试地址：https://i.goto327.top/CryptTools/SM2.aspx?tdsourcetag=s_pctim_aiomsg

		final String d = "FAB8BBE670FAE338C9E9382B9FB6485225C11A3ECB84C938F10F20A93B6215F0";
		final String x = "9EF573019D9A03B16B0BE44FC8A5B4E8E098F56034C97B312282DD0B4810AFC3";
		final String y = "CC759673ED0FC9B9DC7E6FA38F0E2B121E02654BF37EA6B63FAF2A0D6013EADF";

		final String data = "434477813974bf58f94bcf760833c2b40f77a5fc360485b0b9ed1bd9682edb45";
		final String id = "31323334353637383132333435363738";

		final SM2 sm2 = new SM2(d, x, y);
		// 生成的签名是64位
		sm2.usePlainEncoding();


		final String sign = "DCA0E80A7F46C93714B51C3EFC55A922BCEF7ECF0FE9E62B53BA6A7438B543A76C145A452CA9036F3CB70D7E6C67D4D9D7FE114E5367A2F6F5A4D39F2B10F3D6";
		Assert.assertTrue(sm2.verifyHex(data, sign));

		final String sign2 = sm2.signHex(data, id);
		Assert.assertTrue(sm2.verifyHex(data, sign2));
	}

	@Test
	public void sm2PlainWithPointTest2() {
		final String d = "4BD9A450D7E68A5D7E08EB7A0BFA468FD3EB32B71126246E66249A73A9E4D44A";
		final String q = "04970AB36C3B870FBC04041087DB1BC36FB4C6E125B5EA406DB0EC3E2F80F0A55D8AFF28357A0BB215ADC2928BE76F1AFF869BF4C0A3852A78F3B827812C650AD3";

		final String data = "123456";

		final SM2 sm2 = new SM2(d, q);
		sm2.setMode(SM2Engine.Mode.C1C2C3);
		final String encryptHex = sm2.encryptHex(data, KeyType.PublicKey);
		final String decryptStr = sm2.decryptStr(encryptHex, KeyType.PrivateKey);

		Assert.assertEquals(data, decryptStr);
	}

	@Test
	public void encryptAndSignTest(){
		final SM2 sm2 = SmUtil.sm2();

		final String src = "Sm2Test";
		final byte[] data = sm2.encrypt(src, KeyType.PublicKey);
		final byte[] sign =  sm2.sign(src.getBytes(StandardCharsets.UTF_8));

		Assert.assertTrue(sm2.verify( src.getBytes(StandardCharsets.UTF_8), sign));

		final byte[] dec =  sm2.decrypt(data, KeyType.PrivateKey);
		Assert.assertArrayEquals(dec, src.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void getPublicKeyByPrivateKeyTest(){
		// issue#I38SDP，openSSL生成的PKCS#1格式私钥
		final String priKey = "MHcCAQEEIE29XqAFV/rkJbnJzCoQRJLTeAHG2TR0h9ZCWag0+ZMEoAoGCCqBHM9VAYItoUQDQgAESkOzNigIsH5ehFvr9y" +
				"QNQ66genyOrm+Q4umCA4aWXPeRzmcTAWSlTineiReTFN2lqor2xaulT8u3a4w3AM/F6A==";

		final PrivateKey privateKey = KeyUtil.generatePrivateKey("sm2", new OpenSSHPrivateKeySpec(SecureUtil.decode(priKey)));
		final ECPrivateKeyParameters privateKeyParameters = ECKeyUtil.toPrivateParams(privateKey);

		final SM2 sm2 = new SM2(privateKeyParameters, ECKeyUtil.getPublicParams(privateKeyParameters));

		final String src = "Sm2Test";
		final byte[] data = sm2.encrypt(src, KeyType.PublicKey);
		final byte[] sign =  sm2.sign(src.getBytes(StandardCharsets.UTF_8));

		Assert.assertTrue(sm2.verify( src.getBytes(StandardCharsets.UTF_8), sign));

		final byte[] dec =  sm2.decrypt(data, KeyType.PrivateKey);
		Assert.assertArrayEquals(dec, src.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void readPublicKeyTest(){
		final String priKey = "MHcCAQEEIE29XqAFV/rkJbnJzCoQRJLTeAHG2TR0h9ZCWag0+ZMEoAoGCCqBHM9VAYItoUQDQgAESkOzNigIsH5ehFvr9y" +
				"QNQ66genyOrm+Q4umCA4aWXPeRzmcTAWSlTineiReTFN2lqor2xaulT8u3a4w3AM/F6A==";
		final String pubKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAESkOzNigIsH5ehFvr9yQNQ66genyOrm+Q4umCA4aWXPeRzmcTAWSlTineiReTFN2lqor2xaulT8u3a4w3AM/F6A==";

		final SM2 sm2 = SmUtil.sm2(priKey, pubKey);

		final String src = "Sm2Test中文";
		final byte[] data = sm2.encrypt(src, KeyType.PublicKey);
		final byte[] sign =  sm2.sign(src.getBytes(StandardCharsets.UTF_8));

		Assert.assertTrue(sm2.verify( src.getBytes(StandardCharsets.UTF_8), sign));

		final byte[] dec =  sm2.decrypt(data, KeyType.PrivateKey);
		Assert.assertArrayEquals(dec, src.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	public void dLengthTest(){
		final SM2 sm2 = SmUtil.sm2();
		Assert.assertEquals(64, sm2.getDHex().length());
		Assert.assertEquals(32, sm2.getD().length);

		// 04占位一个字节
		Assert.assertEquals(65, sm2.getQ(false).length);
	}

	@Test
	public void issuesI5PWQ4Test(){

	}
}
