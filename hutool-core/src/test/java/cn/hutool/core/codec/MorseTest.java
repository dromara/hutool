package cn.hutool.core.codec;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MorseTest {

	private final Morse morseCoder = new Morse();

	@Test
	public void test0() {
		String text = "Hello World!";
		String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
		Assertions.assertEquals(morse, morseCoder.encode(text));
		Assertions.assertEquals(morseCoder.decode(morse), text.toUpperCase());
	}

	@Test
	public void test1() {
		String text = "你好，世界！";
		String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
		Assertions.assertEquals(morseCoder.encode(text), morse);
		Assertions.assertEquals(morseCoder.decode(morse), text);
	}

	@Test
	public void test2() {
		String text = "こんにちは";
		String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
		Assertions.assertEquals(morseCoder.encode(text), morse);
		Assertions.assertEquals(morseCoder.decode(morse), text);
	}
}
