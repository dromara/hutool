package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.convert.impl.PrimitiveConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimitiveConvertTest {

	@Test
	public void toIntTest(){
		final int convert = Convert.convert(int.class, "123");
		Assertions.assertEquals(123, convert);
	}

	@Test
	public void toIntErrorTest(){
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			Convert.convert(int.class, "aaaa");
		});
	}

	@Test
	public void toIntValueTest() {
		final Object a = PrimitiveConverter.INSTANCE.convert(int.class, null);
		Assertions.assertNull(a);
	}
}
