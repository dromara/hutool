package cn.hutool.core.convert;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrimitiveConvertTest {

	@Test
	public void toIntTest() {
		final int convert = Convert.convert(int.class, "123");
		assertEquals(123, convert);
	}

	@Test
	public void toIntErrorTest() {
		assertThrows(NumberFormatException.class, () -> {
			Convert.convert(int.class, "aaaa");
		});
	}
}
