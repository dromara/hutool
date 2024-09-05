package cn.hutool.core.convert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConvertToBooleanTest {

	@Test
	public void intToBooleanTest(){
		int a = 100;
		final Boolean aBoolean = Convert.toBool(a);
		assertTrue(aBoolean);

		int b = 0;
		final Boolean bBoolean = Convert.toBool(b);
		assertFalse(bBoolean);
	}

	@Test
	void toBooleanWithDefaultTest() {
		Assertions.assertFalse(Convert.toBool("ddddd", false));
	}
}
