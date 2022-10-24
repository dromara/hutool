package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class BCDTest {

	@Test
	public void bcdTest(){
		final String strForTest = "123456ABCDEF";

		//转BCD
		final byte[] bcd = BCD.strToBcd(strForTest);
		final String str = BCD.bcdToStr(bcd);
		//解码BCD
		Assert.assertEquals(strForTest, str);
	}
}
