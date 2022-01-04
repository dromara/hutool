package cn.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BCDTest {

	@Test
	public void bcdTest(){
		String strForTest = "123456ABCDEF";

		//转BCD
		byte[] bcd = BCD.strToBcd(strForTest);
		String str = BCD.bcdToStr(bcd);
		//解码BCD
		Assertions.assertEquals(strForTest, str);
	}
}
