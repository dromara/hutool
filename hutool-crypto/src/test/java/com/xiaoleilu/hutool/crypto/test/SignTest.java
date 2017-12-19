package com.xiaoleilu.hutool.crypto.test;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.crypto.asymmetric.Sign;
import com.xiaoleilu.hutool.crypto.asymmetric.SignAlgorithm;

/**
 * 签名单元测试
 * 
 * @author looly
 *
 */
public class SignTest {

	@Test
	public void signAndVerifyTest() {
		byte[] data = "我是一段测试字符串".getBytes();
		
		Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA);
		
		//签名
		byte[] signed = sign.sign(data);
		
		//验证签名
		boolean verify = sign.verify(data, signed);
		Assert.assertTrue(verify);
	}
}
