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

	@Test
	public void formatSimpleTest() {
		String format1 = NumberWordFormatter.formatSimple(1200, false);
		Assert.assertEquals("1.2k", format1);

		String format2 = NumberWordFormatter.formatSimple(4384324, false);
		Assert.assertEquals("4.38m", format2);

		String format3 = NumberWordFormatter.formatSimple(4384324, true);
		Assert.assertEquals("438.43w", format3);

		String format4 = NumberWordFormatter.formatSimple(4384324);
		Assert.assertEquals("438.43w", format4);

		String format5 = NumberWordFormatter.formatSimple(438);
		Assert.assertEquals("438", format5);
	}
}
