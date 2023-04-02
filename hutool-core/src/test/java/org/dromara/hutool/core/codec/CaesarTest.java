package org.dromara.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CaesarTest {

	@Test
	public void caesarTest() {
		final String str = "1f2e9df6131b480b9fdddc633cf24996";

		final String encode = Caesar.encode(str, 3);
		Assertions.assertEquals("1H2G9FH6131D480D9HFFFE633EH24996", encode);

		final String decode = Caesar.decode(encode, 3);
		Assertions.assertEquals(str, decode);
	}
}
