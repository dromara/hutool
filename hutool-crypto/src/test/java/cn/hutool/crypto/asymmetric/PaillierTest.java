package cn.hutool.crypto.asymmetric;

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
		PaillierKeyPair paillierKeyPair = Paillier.generateKey();
		PaillierpublicKey publicKey = paillierKeyPair.getPublicKey();
		PaillierPrivateKey privateKey = paillierKeyPair.getPrivateKey();

		//创建两个大整数m1,m2:
		BigInteger m1 = new BigInteger("10");
		BigInteger m2 = new BigInteger("40");

		//公钥加密私钥解
		byte[] em1 = Paillier.encrypt(m1, publicKey);
		System.out.println("m1解密结果" + Paillier.decrypt(em1, privateKey));

		//同态特性
		byte[] em2 = Paillier.encrypt(m2, publicKey);
		System.out.println("m1加密em1 :"+ new BigInteger(em1).toString(16));
		System.out.println("m2加密em2 :"+ new BigInteger(em2).toString(16));
		byte[] add = Paillier.add(em1, em2, publicKey);
		System.out.println("m1+m2 密文的和：" + new BigInteger(add).toString(16));
		System.out.println("m1+m2 密文的和的解：" + Paillier.decrypt(add,privateKey));

		byte[] mul = Paillier.multiply(em1, m2, publicKey);
		System.out.println("m1*m2 密文的积：" + new BigInteger(mul).toString(16));
		System.out.println("m1*m2 密文的积的解：" + Paillier.decrypt(mul,privateKey));

		//加解密字符串
		String s = "123456dfsgsdg!@#%!@@#$!#%是豆腐干山豆根v啊微软";
		System.out.println("字符串明文： "+s);

		byte[] encrypt = Paillier.encryptString(s, publicKey);
		System.out.println("字符串密文： "+ new BigInteger(encrypt).toString(16) );

		String decrypt = Paillier.decryptString(encrypt, privateKey);
		System.out.println("字符串密文解密： "+ decrypt);
	}
}
