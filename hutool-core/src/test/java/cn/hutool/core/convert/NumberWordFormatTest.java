package cn.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberWordFormatTest {

	@Test
	public void formatTest() {
		String format = NumberWordFormatter.format(100.23);
		Assertions.assertEquals("ONE HUNDRED AND CENTS TWENTY THREE ONLY", format);

		String format2 = NumberWordFormatter.format("2100.00");
		Assertions.assertEquals("TWO THOUSAND ONE HUNDRED AND CENTS  ONLY", format2);
	}

	@Test
	public void formatSimpleTest() {
		String format1 = NumberWordFormatter.formatSimple(1200, false);
		Assertions.assertEquals("1.2k", format1);

		String format2 = NumberWordFormatter.formatSimple(4384324, false);
		Assertions.assertEquals("4.38m", format2);

		String format3 = NumberWordFormatter.formatSimple(4384324, true);
		Assertions.assertEquals("438.43w", format3);

		String format4 = NumberWordFormatter.formatSimple(4384324);
		Assertions.assertEquals("438.43w", format4);

		String format5 = NumberWordFormatter.formatSimple(438);
		Assertions.assertEquals("438", format5);
	}
}
