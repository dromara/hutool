package com.xiaoleilu.hutool.crypto.test;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.crypto.digest.HMac;
import com.xiaoleilu.hutool.crypto.digest.HmacAlgorithm;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * Hmac单元测试
 * @author Looly
 *
 */
public class HmacTest {
	@Test
	public void hmacTest(){
		String testStr = "test中文";
		
		byte[] key = "password".getBytes();
		HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);
		
		String macHex1 = mac.digestHex(testStr);
		Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex1);
		
		String macHex2 = mac.digestHex(IoUtil.toStream(testStr, CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("b977f4b13f93f549e06140971bded384", macHex2);
	}
}
