package cn.hutool.core.convert;

import cn.hutool.core.convert.impl.PrimitiveConverter;
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

	@Test
	public void toIntValueTest() {
		final Object a = PrimitiveConverter.INSTANCE.convert(int.class, null);
		Assert.assertNull(a);
	}
}
