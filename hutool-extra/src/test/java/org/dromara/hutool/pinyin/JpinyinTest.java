package org.dromara.hutool.pinyin;

import org.dromara.hutool.pinyin.engine.jpinyin.JPinyinEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JpinyinTest {

	final JPinyinEngine engine = new JPinyinEngine();

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
