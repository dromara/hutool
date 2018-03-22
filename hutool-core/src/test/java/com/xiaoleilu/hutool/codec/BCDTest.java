package com.xiaoleilu.hutool.codec;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.codec.BCD;

public class BCDTest {
	
	@Test
	public void bcdTest(){
		String strForTest = "123456ABCDEF";
		
		//转BCD
		byte[] bcd = BCD.strToBcd(strForTest);
		String str = BCD.bcdToStr(bcd);
		//解码BCD
		Assert.assertEquals(strForTest, str);
	}
}
