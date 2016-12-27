package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.SymmetricCriptor;
import com.xiaoleilu.hutool.lang.Console;

/**
 * 加密解密示例
 * @author Looly
 *
 */
public class CryptoDemo {
	public static void main(String[] args) {
		String testStr = "我是测试内容";
		byte[] result;
		String resultStr;
		
		///////////////////////////////////////摘要算法////////////////////////////////////////////////
		//-------------------------------------------------------------- MD5加密
		result = SecureUtil.md5().digest(testStr);
		Console.log(result);
		resultStr = SecureUtil.md5().digestHex(testStr);
		Console.log(resultStr);
		
		//-------------------------------------------------------------- SHA1加密
		result = SecureUtil.sha1().digest(testStr);
		Console.log(result);
		String result2Str = SecureUtil.sha1().digestHex(testStr);
		Console.log(result2Str);
		
		///////////////////////////////////////对称加密算法////////////////////////////////////////////////
		//-------------------------------------------------------------- AES加密
		SymmetricCriptor aes = SecureUtil.aes();
		aes = SecureUtil.aes("key".getBytes());	//自定义密钥
		result = aes.encrypt(testStr.getBytes());//加密
		Console.log(result);
		String decryptStr = new String(aes.decrypt(result));//解密，必须使用相同key
		Console.log(decryptStr);
	}
}
