package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.text.StrFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrFormatterTest {

	@Test
	public void formatTest() {
		//通常使用
		final String result1 = StrFormatter.format("this is {} for {}", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义{}
		final String result2 = StrFormatter.format("this is \\{} for {}", "a", "b");
		Assertions.assertEquals("this is {} for a", result2);

		//转义\
		final String result3 = StrFormatter.format("this is \\\\{} for {}", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}

	@Test
	public void formatWithTest() {
		//通常使用
		final String result1 = StrFormatter.formatWith("this is ? for ?", "?", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义?
		final String result2 = StrFormatter.formatWith("this is \\? for ?", "?", "a", "b");
		Assertions.assertEquals("this is ? for a", result2);

		//转义\
		final String result3 = StrFormatter.formatWith("this is \\\\? for ?", "?", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}

	@Test
	public void formatWithTest2() {
		//通常使用
		final String result1 = StrFormatter.formatWith("this is $$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义?
		final String result2 = StrFormatter.formatWith("this is \\$$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is $$$ for a", result2);

		//转义\
		final String result3 = StrFormatter.formatWith("this is \\\\$$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}
}
