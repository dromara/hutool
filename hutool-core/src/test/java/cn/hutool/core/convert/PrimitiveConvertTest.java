package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

public class PrimitiveConvertTest {

	@Test
	public void toIntTest(){
		final int convert = Convert.convert(int.class, "123");
		Assert.assertEquals(123, convert);
	}

	@Test(expected = NumberFormatException.class)
	public void toIntErrorTest(){
		final int convert = Convert.convert(int.class, "aaaa");
	}
}
