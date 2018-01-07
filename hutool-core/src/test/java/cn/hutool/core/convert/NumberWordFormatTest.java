package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.convert.NumberWordFormater;

public class NumberWordFormatTest {
	
	@Test
	public void formatTest() {
		String format = NumberWordFormater.format(100.23);
		Assert.assertEquals("ONE HUNDRED AND CENTS TWENTY THREE ONLY", format);
		
		String format2 = NumberWordFormater.format("2100.00");
		Assert.assertEquals("TWO THOUSAND ONE HUNDRED AND CENTS  ONLY", format2);
	}
}
