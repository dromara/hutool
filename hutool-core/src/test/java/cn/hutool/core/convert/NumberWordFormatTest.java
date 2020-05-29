package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

public class NumberWordFormatTest {
	
	@Test
	public void formatTest() {
		String format = NumberWordFormatter.format(100.23);
		Assert.assertEquals("ONE HUNDRED AND CENTS TWENTY THREE ONLY", format);
		
		String format2 = NumberWordFormatter.format("2100.00");
		Assert.assertEquals("TWO THOUSAND ONE HUNDRED AND CENTS  ONLY", format2);
	}
}
