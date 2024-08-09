package cn.hutool.core.io.unit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DataSizeUtilTest {

	@Test
	public void parseTest(){
		long parse = DataSizeUtil.parse("3M");
		assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3m");
		assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3MB");
		assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3mb");
		assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3.1M");
		assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1m");
		assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1MB");
		assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("-3.1MB");
		assertEquals(-3250585, parse);

		parse = DataSizeUtil.parse("+3.1MB");
		assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1mb");
		assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1");
		assertEquals(3, parse);

		try {
			DataSizeUtil.parse("3.1.3");
		} catch (IllegalArgumentException ie) {
			assertEquals("'3.1.3' is not a valid data size", ie.getMessage());
		}


	}

	@Test
	public void formatTest(){
		String format = DataSizeUtil.format(Long.MAX_VALUE);
		assertEquals("8 EB", format);

		format = DataSizeUtil.format(1024L * 1024 * 1024 * 1024 * 1024);
		assertEquals("1 PB", format);

		format = DataSizeUtil.format(1024L * 1024 * 1024 * 1024);
		assertEquals("1 TB", format);
	}

	@Test
	public void issueI88Z4ZTest() {
		final String size = DataSizeUtil.format(10240000);
		final long bytes = DataSize.parse(size).toBytes();
		assertEquals(10244587, bytes);
	}
}
