package cn.hutool.core.io.unit;

import org.junit.Assert;
import org.junit.Test;

public class DataSizeUtilTest {

	@Test
	public void parseTest(){
		long parse = DataSizeUtil.parse("3M");
		Assert.assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3m");
		Assert.assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3MB");
		Assert.assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3mb");
		Assert.assertEquals(3145728, parse);

		parse = DataSizeUtil.parse("3.1M");
		Assert.assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1m");
		Assert.assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1MB");
		Assert.assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("-3.1MB");
		Assert.assertEquals(-3250585, parse);

		parse = DataSizeUtil.parse("+3.1MB");
		Assert.assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1mb");
		Assert.assertEquals(3250585, parse);

		parse = DataSizeUtil.parse("3.1");
		Assert.assertEquals(3, parse);

		try {
			DataSizeUtil.parse("3.1.3");
		} catch (IllegalArgumentException ie) {
			Assert.assertEquals("'3.1.3' is not a valid data size", ie.getMessage());
		}


	}

	@Test
	public void formatTest(){
		final String format = DataSizeUtil.format(Long.MAX_VALUE);
		Assert.assertEquals("8,192 EB", format);
	}
}
