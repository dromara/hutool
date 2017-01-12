package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import com.xiaoleilu.hutool.crypto.asymmetric.AsymmetricCriptor;
import com.xiaoleilu.hutool.crypto.asymmetric.KeyType;
import com.xiaoleilu.hutool.crypto.asymmetric.RSA;
import com.xiaoleilu.hutool.crypto.digest.Digester;
import com.xiaoleilu.hutool.crypto.digest.HMac;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.xiaoleilu.hutool.crypto.symmetric.SymmetricCriptor;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 加密解密示例
 * 
 * @author Looly
 *
 */
public class CryptoDemo {
	public static void main(String[] args) {
		String testStr = "我是测试内容";
		byte[] result;
		String resultStr;

		/////////////////////////////////////// 摘要算法////////////////////////////////////////////////
		Console.log("------------------------------MD5------------------------------------");
		Digester digester = SecureUtil.md5();
		result = digester.digest(testStr);
		Console.log(result);
		// 16进制输出
		resultStr = digester.digestHex(testStr);
		Console.log(resultStr);
		Console.log("-----------------------------------------------------------------------");

		Console.log("------------------------------SHA1------------------------------------");
		result = SecureUtil.sha1().digest(testStr);
		Console.log(result);
		String result2Str = SecureUtil.sha1().digestHex(testStr);
		Console.log(result2Str);
		Console.log("-----------------------------------------------------------------------");

		Console.log("------------------------------HmacMD5------------------------------------");
		HMac hmac = SecureUtil.hmacMd5();
		result = hmac.digest(testStr.getBytes());// 加密
		Console.log(result);
		Console.log("-----------------------------------------------------------------------------");

		/////////////////////////////////////// 对称加密算法////////////////////////////////////////////////
		Console.log("------------------------------AES------------------------------------");
		SymmetricCriptor aes = SecureUtil.aes();
		result = aes.encrypt(testStr.getBytes());// 加密
		Console.log(result);
		String decryptStr = StrUtil.utf8Str(aes.decrypt(result));// 解密，必须使用相同key
		Console.log(decryptStr);
		Console.log("----------------------------------------------------------------------");

		Console.log("------------------------------DES------------------------------------");
		SymmetricCriptor des = SecureUtil.des();
		result = des.encrypt(testStr.getBytes());// 加密
		Console.log(result);
		decryptStr = StrUtil.utf8Str(des.decrypt(result));// 解密，必须使用相同key
		Console.log(decryptStr);
		Console.log("----------------------------------------------------------------------");

		Console.log("------------------------------PBE------------------------------------");
		SymmetricCriptor peb = new SymmetricCriptor(SymmetricAlgorithm.PBEWithMD5AndDES);
		result = peb.encrypt(testStr.getBytes());// 加密
		Console.log(result);
		decryptStr = new String(peb.decrypt(result));// 解密，必须使用相同key
		Console.log(decryptStr);
		Console.log("----------------------------------------------------------------------");

		/////////////////////////////////////// 非对称加密算法////////////////////////////////////////////////
		Console.log("------------------------------RSA------------------------------------");
		AsymmetricCriptor rsa = new AsymmetricCriptor(AsymmetricAlgorithm.RSA);//实例化方法1
		rsa = new RSA();//实例化方法2
		Console.log("Public key: {}", rsa.getPublicKeyBase64());
		Console.log("Private key: {}", rsa.getPrivateKeyBase64());
		
		//公钥加密私钥解密
		byte[] encrypted = rsa.encrypt(testStr, KeyType.PublicKey);
		result = rsa.decrypt(encrypted, KeyType.PrivateKey);
		Console.log("Encrypt by public key and decrypt by private key result: {}", result);
		
		//私钥加密公钥解密
		encrypted = rsa.encrypt(testStr, KeyType.PrivateKey);
		result = rsa.decrypt(encrypted, KeyType.PublicKey);
		Console.log("Encrypt by private key and decrypt by public key result: {}", result);
		
		//私钥签名，公钥验证
		byte[] sign = rsa.sign(testStr.getBytes());
		boolean isPass = rsa.verify(testStr.getBytes(), sign);
		Console.log("Is verify pass: {}", isPass);
		
		Console.log("----------------------------------------------------------------------");
	}
}
