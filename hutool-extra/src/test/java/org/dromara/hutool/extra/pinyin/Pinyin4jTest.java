package org.dromara.hutool.extra.pinyin;

import org.dromara.hutool.extra.pinyin.engine.pinyin4j.Pinyin4jEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Pinyin4jTest {

	final Pinyin4jEngine engine = new Pinyin4jEngine();

	@Test
	public void getFirstLetterByPinyin4jTest(){
		final String result = engine.getFirstLetter("林海", "");
		Assertions.assertEquals("lh", result);
	}

	@Test
	public void getPinyinByPinyin4jTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		Assertions.assertEquals("ni hao h", pinyin);
	}
}
