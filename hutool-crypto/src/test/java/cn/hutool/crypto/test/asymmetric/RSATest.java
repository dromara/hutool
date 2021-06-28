package cn.hutool.crypto.test.asymmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;

/**
 * RSA算法单元测试
 *
 * @author Looly
 */
public class RSATest {

	@Test
	public void generateKeyPairTest() {
		KeyPair pair = KeyUtil.generateKeyPair("RSA");
		Assert.assertNotNull(pair.getPrivate());
		Assert.assertNotNull(pair.getPublic());
	}

	@Test
	public void rsaCustomKeyTest() {
		KeyPair pair = KeyUtil.generateKeyPair("RSA");
		byte[] privateKey = pair.getPrivate().getEncoded();
		byte[] publicKey = pair.getPublic().getEncoded();

		RSA rsa = SecureUtil.rsa(privateKey, publicKey);

		// 公钥加密，私钥解密
		byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
		byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

		// 私钥加密，公钥解密
		byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
		byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void rsaTest() {
		final RSA rsa = new RSA();

		// 获取私钥和公钥
		Assert.assertNotNull(rsa.getPrivateKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());
		Assert.assertNotNull(rsa.getPublicKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

		byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

		// 私钥加密，公钥解密
		byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
		byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void rsaECBTest() {
		final RSA rsa = new RSA(AsymmetricAlgorithm.RSA_ECB.getValue());

		// 获取私钥和公钥
		Assert.assertNotNull(rsa.getPrivateKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());
		Assert.assertNotNull(rsa.getPublicKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

		byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

		// 私钥加密，公钥解密
		byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
		byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void rsaNoneTest() {
		final RSA rsa = new RSA(AsymmetricAlgorithm.RSA_None.getValue());

		// 获取私钥和公钥
		Assert.assertNotNull(rsa.getPrivateKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());
		Assert.assertNotNull(rsa.getPublicKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

		byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

		// 私钥加密，公钥解密
		byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
		byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void rsaWithBlockTest2() {
		final RSA rsa = new RSA();
		rsa.setEncryptBlockSize(3);

		// 获取私钥和公钥
		Assert.assertNotNull(rsa.getPrivateKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());
		Assert.assertNotNull(rsa.getPublicKey());
		Assert.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		byte[] encrypt = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
		byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

		// 私钥加密，公钥解密
		byte[] encrypt2 = rsa.encrypt(StrUtil.bytes("我是一段测试aaaa", CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
		byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assert.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void rsaBcdTest() {
		String text = "我是一段测试aaaa";

		final RSA rsa = new RSA();

		// 公钥加密，私钥解密
		String encryptStr = rsa.encryptBcd(text, KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(rsa.decryptFromBcd(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text, decryptStr);

		// 私钥加密，公钥解密
		String encrypt2 = rsa.encryptBcd(text, KeyType.PrivateKey);
		String decrypt2 = StrUtil.utf8Str(rsa.decryptFromBcd(encrypt2, KeyType.PublicKey));
		Assert.assertEquals(text, decrypt2);
	}

	@Test
	public void rsaBase64Test() {
		String textBase = "我是一段特别长的测试";
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			text.append(textBase);
		}

		final RSA rsa = new RSA();

		// 公钥加密，私钥解密
		String encryptStr = rsa.encryptBase64(text.toString(), KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(rsa.decrypt(encryptStr, KeyType.PrivateKey));
		Assert.assertEquals(text.toString(), decryptStr);

		// 私钥加密，公钥解密
		String encrypt2 = rsa.encryptBase64(text.toString(), KeyType.PrivateKey);
		String decrypt2 = StrUtil.utf8Str(rsa.decrypt(encrypt2, KeyType.PublicKey));
		Assert.assertEquals(text.toString(), decrypt2);
	}

	@Test
	public void rsaDecodeTest() {
		String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIL7pbQ+5KKGYRhw7jE31hmA" //
				+ "f8Q60ybd+xZuRmuO5kOFBRqXGxKTQ9TfQI+aMW+0lw/kibKzaD/EKV91107xE384qOy6IcuBfaR5lv39OcoqNZ"//
				+ "5l+Dah5ABGnVkBP9fKOFhPgghBknTRo0/rZFGI6Q1UHXb+4atP++LNFlDymJcPAgMBAAECgYBammGb1alndta" //
				+ "xBmTtLLdveoBmp14p04D8mhkiC33iFKBcLUvvxGg2Vpuc+cbagyu/NZG+R/WDrlgEDUp6861M5BeFN0L9O4hz"//
				+ "GAEn8xyTE96f8sh4VlRmBOvVdwZqRO+ilkOM96+KL88A9RKdp8V2tna7TM6oI3LHDyf/JBoXaQJBAMcVN7fKlYP" //
				+ "Skzfh/yZzW2fmC0ZNg/qaW8Oa/wfDxlWjgnS0p/EKWZ8BxjR/d199L3i/KMaGdfpaWbYZLvYENqUCQQCobjsuCW"//
				+ "nlZhcWajjzpsSuy8/bICVEpUax1fUZ58Mq69CQXfaZemD9Ar4omzuEAAs2/uee3kt3AvCBaeq05NyjAkBme8SwB0iK"//
				+ "kLcaeGuJlq7CQIkjSrobIqUEf+CzVZPe+AorG+isS+Cw2w/2bHu+G0p5xSYvdH59P0+ZT0N+f9LFAkA6v3Ae56OrI"//
				+ "wfMhrJksfeKbIaMjNLS9b8JynIaXg9iCiyOHmgkMl5gAbPoH/ULXqSKwzBw5mJ2GW1gBlyaSfV3AkA/RJC+adIjsRGg"//
				+ "JOkiRjSmPpGv3FOhl9fsBPjupZBEIuoMWOC8GXK/73DHxwmfNmN7C9+sIi4RBcjEeQ5F5FHZ";

		RSA rsa = new RSA(PRIVATE_KEY, null);

		String a = "2707F9FD4288CEF302C972058712F24A5F3EC62C5A14AD2FC59DAB93503AA0FA17113A020EE4EA35EB53F" //
				+ "75F36564BA1DABAA20F3B90FD39315C30E68FE8A1803B36C29029B23EB612C06ACF3A34BE815074F5EB5AA3A"//
				+ "C0C8832EC42DA725B4E1C38EF4EA1B85904F8B10B2D62EA782B813229F9090E6F7394E42E6F44494BB8";

		byte[] aByte = HexUtil.decodeHex(a);
		byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);

		Assert.assertEquals("虎头闯杭州,多抬头看天,切勿只管种地", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void rsaTest2() throws Exception {
		String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6s" +
				"XqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9S" +
				"dB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";

		byte[] keyBytes = Base64.decode(publicKeyStr);
		PublicKey publicKey = KeyUtil.generateRSAPublicKey(keyBytes);

		byte[] data = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 16).getBytes();
		//长度不满足128补0
		byte[] finalData = ArrayUtil.resize(data, 128);

		//jdk原生加密
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		String result1 = HexUtil.encodeHexStr(cipher.doFinal(finalData));

		//hutool加密
		RSA rsa = new RSA("RSA/ECB/NoPadding", null, publicKeyStr);
		rsa.setEncryptBlockSize(128);
		String result2 = rsa.encryptHex(finalData, KeyType.PublicKey);

		Assert.assertEquals(result1, result2);
	}

	@Test
	public void exponentTest(){
		String modulus = "BD99BAAB9E56B7FD85FB8BCF53CAD2913C1ACEF9063E7C913CD6FC4FEE040DA44D8" +
				"ADAA35A9DCABD6E936C402D47278049638407135BAB22BB091396CB6873195C8AC8B0B7AB123" +
				"C3BF7A6341A4419BDBC0EFB85DBCD9A3AD12C99E2265BDCC1197913749E2AFA568EB7623DA3A" +
				"361335AA1F9FFA6E1801DDC8228AA86306B87";
		String publicExponent = "65537";
		RSA rsa = new RSA(new BigInteger(modulus, 16), null, new BigInteger(publicExponent));

		final String encryptBase64 = rsa.encryptBase64("测试内容", KeyType.PublicKey);
		Assert.assertNotNull(encryptBase64);
	}

	@Test
	public void decryptFromJsTest(){
		// https://oktools.net/rsa
		RSA rsa = new RSA("RSA/ECB/PKCS1Padding",
				"MIICXQIBAAKBgQDtYB02dwR2upvBiTw9tjpNiOsh3E1JnD9V7xJieJ+pG1jPgUF5\n" +
						"XDT2OvsZq18d0Wp3O2byiNLbl3n53nWboPwx36aqE3zQRVn9lqyIFE30qsc2ojFz\n" +
						"XUsI1JbtM+uy8Z4cceH9YcogHbLO8JXuOmxGBvCQcQmUB8V+ZaId1TArwQIDAQAB\n" +
						"AoGAdh0XLBy4qeNc0UZaJVLhW+c/KJAYHQKUOUxGV50xxNGItWfZjmulJsheXX16\n" +
						"TLoBQeba6N/QG9gZp41b89583dbUOzF7ZFmJJq5kEgAhAxZp7c6swnH1kRbC3hVn\n" +
						"Lb4CeVgF9Qpjrtz3YYOnAeqQqUOdvH6G8eKgMPMGvtobxgECQQD6E3Bzlrb3lOVY\n" +
						"6wvzOBhAZ8gIOOoXp+LfydV265UlpTB0MvEGVJ9iOfNCKMbqpRcJBOlhjv+TwXuO\n" +
						"n1VGYMfRAkEA8v+nbADer47MyYiFB6x3427VK17hXO/ilPrml8uqw5ooTyT/00v6\n" +
						"+RBlxtI47mHOMeHKqueOJRZubWEBGucQ8QJBAKsXvan3hXriDf/F4EkpKb52wq6N\n" +
						"cet5W4lMp5VTHJnC6OG3MJvqLZmgmEO9bkp/ZMEw8RNkKIvIpmYvsMIT2OECQQC/\n" +
						"U53uAu1TCzug/rXHqfsnfq+nG2iUNWiQ5tBC+qB0rF9KmZY4Nx3flxKbrhAXWbjx\n" +
						"O5fIYtNfzj6aLoOhPAfxAkA2l87oSAYfH0ye2GYj1gyJRJBXk6NDigN5cKhfls+A\n" +
						"Z4TmIew5JbI0iQiDvL4XavF3lNFwQAMA5vdN1orZWGGk",null);

		Assert.assertEquals("123", rsa.decryptStr("z4mJQnkh1gjPp9f4t2ufsM0dFW2d98l" +
						"+mpCsZQYYhe5d/E6IrKuOe0KOiz8vbfJi9rqnBZhH+I7DEfSrm0nqGA+jtRCkA6wJHeAnI" +
						"p9PMoDBsTY2EZr4NXBgfsbcms+vtTQiPMlay/TmGJ1ZTQFH2FNUl2ACOhchhqheh6l/Tuw=",
				KeyType.PrivateKey));
	}
}
