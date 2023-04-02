package org.dromara.hutool.extra.pinyin;

import org.dromara.hutool.extra.pinyin.engine.houbbpinyin.HoubbPinyinEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HoubbPinyinTest {

	final HoubbPinyinEngine engine = new HoubbPinyinEngine();

	@Test
	public void getFirstLetterTest(){
		final String result = engine.getFirstLetter("林海", "");
		Assertions.assertEquals("lh", result);
	}

	@Test
	public void getPinyinTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		Assertions.assertEquals("ni hao h", pinyin);
	}
}
