package cn.hutool.core.codec;

import org.junit.Assert;
import org.junit.Test;

public class MorseTest {
	
	private final Morse morseCoder = new Morse();

	@Test
	public void test0() {
		String text = "Hello World!";
		String morse = "...././.-../.-../---/-...../.--/---/.-./.-../-../-.-.--/";
		Assert.assertEquals(morse, morseCoder.encode(text));
		Assert.assertEquals(morseCoder.decode(morse), text.toUpperCase());
	}

	@Test
	public void test1() {
		String text = "你好，世界！";
		String morse = "-..----.--...../-.--..-.-----.-/--------....--../-..---....-.--./---.-.-.-..--../--------.......-/";
		Assert.assertEquals(morseCoder.encode(text), morse);
		Assert.assertEquals(morseCoder.decode(morse), text);
	}

	@Test
	public void test2() {
		String text = "こんにちは";
		String morse = "--.....-.-..--/--....-..-..--/--.....--.-.--/--.....--....-/--.....--.----/";
		Assert.assertEquals(morseCoder.encode(text), morse);
		Assert.assertEquals(morseCoder.decode(morse), text);
	}
}
