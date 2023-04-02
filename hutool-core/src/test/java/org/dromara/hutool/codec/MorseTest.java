package org.dromara.hutool.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MorseTest {

	private final Morse morseCoder = new Morse();

	@Test
	public void test0() {
		final String text = "Hello World!";
		final String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
		Assertions.assertEquals(morse, morseCoder.encode(text));
		Assertions.assertEquals(morseCoder.decode(morse), text.toUpperCase());
	}

	@Test
	public void test1() {
		final String text = "你好，世界！";
		final String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
		Assertions.assertEquals(morseCoder.encode(text), morse);
		Assertions.assertEquals(morseCoder.decode(morse), text);
	}

	@Test
	public void test2() {
		final String text = "こんにちは";
		final String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
		Assertions.assertEquals(morseCoder.encode(text), morse);
		Assertions.assertEquals(morseCoder.decode(morse), text);
	}
}
