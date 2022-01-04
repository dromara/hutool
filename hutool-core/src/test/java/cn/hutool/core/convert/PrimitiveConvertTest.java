package cn.hutool.core.convert;

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
		Assertions.assertThrows(NumberFormatException.class, () -> {
			Convert.convert(int.class, "aaaa");
		});
	}
}
