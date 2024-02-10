/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.crypto.asymmetric;

import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.dromara.hutool.crypto.KeyUtil;
import org.dromara.hutool.crypto.SecureUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		final KeyPair pair = KeyUtil.generateKeyPair("RSA");
		Assertions.assertNotNull(pair.getPrivate());
		Assertions.assertNotNull(pair.getPublic());
	}

	@Test
	public void rsaCustomKeyTest() {
		final KeyPair pair = KeyUtil.generateKeyPair("RSA");
		final byte[] privateKey = pair.getPrivate().getEncoded();
		final byte[] publicKey = pair.getPublic().getEncoded();

		final RSA rsa = SecureUtil.rsa(privateKey, publicKey);

		// 公钥加密，私钥解密
		final byte[] encrypt = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);
		final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 私钥加密，公钥解密
		final byte[] encrypt2 = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PrivateKey);
		final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.UTF_8));
	}

	@Test
	public void rsaTest() {
		final RSA rsa = new RSA();

		// 获取私钥和公钥
		Assertions.assertNotNull(rsa.getPrivateKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());
		Assertions.assertNotNull(rsa.getPublicKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		final byte[] encrypt = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);

		final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 私钥加密，公钥解密
		final byte[] encrypt2 = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PrivateKey);
		final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.UTF_8));
	}

	@Test
	public void rsaECBTest() {
		final RSA rsa = new RSA(AsymmetricAlgorithm.RSA_ECB.getValue());

		// 获取私钥和公钥
		Assertions.assertNotNull(rsa.getPrivateKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());
		Assertions.assertNotNull(rsa.getPublicKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		final byte[] encrypt = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);

		final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 私钥加密，公钥解密
		final byte[] encrypt2 = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PrivateKey);
		final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.UTF_8));
	}

	@Test
	public void rsaNoneTest() {
		final RSA rsa = new RSA(AsymmetricAlgorithm.RSA_None.getValue());

		// 获取私钥和公钥
		Assertions.assertNotNull(rsa.getPrivateKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());
		Assertions.assertNotNull(rsa.getPublicKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		final byte[] encrypt = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);

		final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 私钥加密，公钥解密
		final byte[] encrypt2 = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PrivateKey);
		final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.UTF_8));
	}

	@Test
	public void rsaWithBlockTest2() {
		final RSA rsa = new RSA();
		rsa.setEncryptBlockSize(3);

		// 获取私钥和公钥
		Assertions.assertNotNull(rsa.getPrivateKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());
		Assertions.assertNotNull(rsa.getPublicKey());
		Assertions.assertNotNull(rsa.getPrivateKeyBase64());

		// 公钥加密，私钥解密
		final byte[] encrypt = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PublicKey);
		final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt, CharsetUtil.UTF_8));

		// 私钥加密，公钥解密
		final byte[] encrypt2 = rsa.encrypt(ByteUtil.toBytes("我是一段测试aaaa", CharsetUtil.UTF_8), KeyType.PrivateKey);
		final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
		Assertions.assertEquals("我是一段测试aaaa", StrUtil.str(decrypt2, CharsetUtil.UTF_8));
	}

	@Test
	public void rsaBase64Test() {
		final String textBase = "我是一段特别长的测试";
		final StringBuilder text = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			text.append(textBase);
		}

		final RSA rsa = new RSA();

		// 公钥加密，私钥解密
		final String encryptStr = rsa.encryptBase64(text.toString(), KeyType.PublicKey);
		final String decryptStr = StrUtil.utf8Str(rsa.decrypt(encryptStr, KeyType.PrivateKey));
		Assertions.assertEquals(text.toString(), decryptStr);

		// 私钥加密，公钥解密
		final String encrypt2 = rsa.encryptBase64(text.toString(), KeyType.PrivateKey);
		final String decrypt2 = StrUtil.utf8Str(rsa.decrypt(encrypt2, KeyType.PublicKey));
		Assertions.assertEquals(text.toString(), decrypt2);
	}

	@Test
	public void rsaDecodeTest() {
		final String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIL7pbQ+5KKGYRhw7jE31hmA" //
				+ "f8Q60ybd+xZuRmuO5kOFBRqXGxKTQ9TfQI+aMW+0lw/kibKzaD/EKV91107xE384qOy6IcuBfaR5lv39OcoqNZ"//
				+ "5l+Dah5ABGnVkBP9fKOFhPgghBknTRo0/rZFGI6Q1UHXb+4atP++LNFlDymJcPAgMBAAECgYBammGb1alndta" //
				+ "xBmTtLLdveoBmp14p04D8mhkiC33iFKBcLUvvxGg2Vpuc+cbagyu/NZG+R/WDrlgEDUp6861M5BeFN0L9O4hz"//
				+ "GAEn8xyTE96f8sh4VlRmBOvVdwZqRO+ilkOM96+KL88A9RKdp8V2tna7TM6oI3LHDyf/JBoXaQJBAMcVN7fKlYP" //
				+ "Skzfh/yZzW2fmC0ZNg/qaW8Oa/wfDxlWjgnS0p/EKWZ8BxjR/d199L3i/KMaGdfpaWbYZLvYENqUCQQCobjsuCW"//
				+ "nlZhcWajjzpsSuy8/bICVEpUax1fUZ58Mq69CQXfaZemD9Ar4omzuEAAs2/uee3kt3AvCBaeq05NyjAkBme8SwB0iK"//
				+ "kLcaeGuJlq7CQIkjSrobIqUEf+CzVZPe+AorG+isS+Cw2w/2bHu+G0p5xSYvdH59P0+ZT0N+f9LFAkA6v3Ae56OrI"//
				+ "wfMhrJksfeKbIaMjNLS9b8JynIaXg9iCiyOHmgkMl5gAbPoH/ULXqSKwzBw5mJ2GW1gBlyaSfV3AkA/RJC+adIjsRGg"//
				+ "JOkiRjSmPpGv3FOhl9fsBPjupZBEIuoMWOC8GXK/73DHxwmfNmN7C9+sIi4RBcjEeQ5F5FHZ";

		final RSA rsa = new RSA(PRIVATE_KEY, null);

		final String a = "2707F9FD4288CEF302C972058712F24A5F3EC62C5A14AD2FC59DAB93503AA0FA17113A020EE4EA35EB53F" //
				+ "75F36564BA1DABAA20F3B90FD39315C30E68FE8A1803B36C29029B23EB612C06ACF3A34BE815074F5EB5AA3A"//
				+ "C0C8832EC42DA725B4E1C38EF4EA1B85904F8B10B2D62EA782B813229F9090E6F7394E42E6F44494BB8";

		final byte[] aByte = HexUtil.decode(a);
		final byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);

		Assertions.assertEquals("虎头闯杭州,多抬头看天,切勿只管种地", StrUtil.str(decrypt, CharsetUtil.UTF_8));
	}

	@Test
	public void rsaTest2() throws Exception {
		final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgtQn2JZ34ZC28NWYpAUd98iZ37BUrX/aKzmFbt7clFSs6s" +
				"XqHauqKWqdtLkF2KexO40H1YTX8z2lSgBBOAxLsvaklV8k4cBFK9snQXE9/DDaFt6Rr7iVZMldczhC0JNgTz+SHXT6CBHuX3e9S" +
				"dB1Ua44oncaTWz7OBGLbCiK45wIDAQAB";

		final byte[] keyBytes = Base64.decode(publicKeyStr);
		final PublicKey publicKey = KeyUtil.generateRSAPublicKey(keyBytes);

		final byte[] data = RandomUtil.randomStringLower("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 16).getBytes();
		//长度不满足128补0
		final byte[] finalData = ArrayUtil.resize(data, 128);

		//jdk原生加密
		final Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		final String result1 = HexUtil.encodeStr(cipher.doFinal(finalData));

		//hutool加密
		final RSA rsa = new RSA("RSA/ECB/NoPadding", null, publicKeyStr);
		rsa.setEncryptBlockSize(128);
		final String result2 = rsa.encryptHex(finalData, KeyType.PublicKey);

		Assertions.assertEquals(result1, result2);
	}

	@Test
	public void exponentTest(){
		final String modulus = "BD99BAAB9E56B7FD85FB8BCF53CAD2913C1ACEF9063E7C913CD6FC4FEE040DA44D8" +
				"ADAA35A9DCABD6E936C402D47278049638407135BAB22BB091396CB6873195C8AC8B0B7AB123" +
				"C3BF7A6341A4419BDBC0EFB85DBCD9A3AD12C99E2265BDCC1197913749E2AFA568EB7623DA3A" +
				"361335AA1F9FFA6E1801DDC8228AA86306B87";
		final String publicExponent = "65537";
		final RSA rsa = new RSA(new BigInteger(modulus, 16), null, new BigInteger(publicExponent));

		final String encryptBase64 = rsa.encryptBase64("测试内容", KeyType.PublicKey);
		Assertions.assertNotNull(encryptBase64);
	}
}
