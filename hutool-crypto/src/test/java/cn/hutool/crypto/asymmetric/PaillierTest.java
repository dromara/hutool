package cn.hutool.crypto.asymmetric;

import cn.hutool.core.lang.Console;
import cn.hutool.crypto.asymmetric.paillier.Paillier;
import cn.hutool.crypto.asymmetric.paillier.PaillierKeyPair;
import cn.hutool.crypto.asymmetric.paillier.PaillierPrivateKey;
import cn.hutool.crypto.asymmetric.paillier.PaillierpublicKey;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 * @author Revers.
 **/
public class PaillierTest {
	@Test
	public void test() throws NoSuchAlgorithmException {
		//生成 公钥和私钥
		final PaillierKeyPair paillierKeyPair = Paillier.generateKey();
		final PaillierpublicKey publicKey = paillierKeyPair.getPublicKey();
		final PaillierPrivateKey privateKey = paillierKeyPair.getPrivateKey();

		//创建两个大整数m1,m2:
		final BigInteger m1 = new BigInteger("10");
		final BigInteger m2 = new BigInteger("40");

		//公钥加密私钥解
		final byte[] em1 = Paillier.encrypt(m1, publicKey);
		Assert.assertEquals("10", Paillier.decrypt(em1, privateKey));

		//同态特性
		final byte[] em2 = Paillier.encrypt(m2, publicKey);
		Assert.assertNotNull(new BigInteger(em1).toString(16));
		Assert.assertNotNull(new BigInteger(em2).toString(16));
		//Console.log("m1加密em1 :"+ new BigInteger(em1).toString(16));
		//Console.log("m2加密em2 :"+ new BigInteger(em2).toString(16));
		final byte[] add = Paillier.add(em1, em2, publicKey);
		Assert.assertNotNull(new BigInteger(add).toString(16));
		Assert.assertNotNull(Paillier.decrypt(add,privateKey));
		//Console.log("m1+m2 密文的和：" + new BigInteger(add).toString(16));
		//Console.log("m1+m2 密文的和的解：" + Paillier.decrypt(add,privateKey));

		final byte[] mul = Paillier.multiply(em1, m2, publicKey);
		Assert.assertNotNull(new BigInteger(mul).toString(16));
		Assert.assertNotNull(Paillier.decrypt(mul,privateKey));
		//Console.log("m1*m2 密文的积：" + new BigInteger(mul).toString(16));
		//Console.log("m1*m2 密文的积的解：" + Paillier.decrypt(mul,privateKey));

		//加解密字符串
		final String s = "123456dfsgsdg!@#%!@@#$!#%是豆腐干山豆根v啊微软";

		final byte[] encrypt = Paillier.encryptString(s, publicKey);
		Assert.assertNotNull(new BigInteger(encrypt).toString(16));
		//Console.log("字符串密文： "+ new BigInteger(encrypt).toString(16) );

		final String decrypt = Paillier.decryptString(encrypt, privateKey);
		Assert.assertEquals(s, decrypt);
	}
}
