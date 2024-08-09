package cn.hutool.core.codec;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BCDTest {

	@Test
	public void bcdTest(){
		final String strForTest = "123456ABCDEF";

		//转BCD
		final byte[] bcd = BCD.strToBcd(strForTest);
		final String str = BCD.bcdToStr(bcd);
		//解码BCD
		assertEquals(strForTest, str);
	}
}
