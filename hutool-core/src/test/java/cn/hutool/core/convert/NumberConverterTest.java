package cn.hutool.core.convert;

import cn.hutool.core.convert.impl.NumberConverter;
import org.junit.Assert;
import org.junit.Test;

public class NumberConverterTest {

	@Test
	public void toDoubleTest(){
		final NumberConverter numberConverter = new NumberConverter();
		final Number convert = numberConverter.convert(Double.class, "1,234.55", null);
		Assert.assertEquals(1234.55D, convert);
	}

	@Test
	public void toIntegerTest(){
		final NumberConverter numberConverter = new NumberConverter();
		final Number convert = numberConverter.convert(Integer.class, "1,234.55", null);
		Assert.assertEquals(1234, convert);
	}
}
