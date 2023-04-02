package org.dromara.hutool.pinyin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PinyinUtilTest {

	@Test
	public void getPinyinTest(){
		final String pinyin = PinyinUtil.getPinyin("你好怡", " ");
		Assertions.assertEquals("ni hao yi", pinyin);
	}

	@Test
	public void getFirstLetterTest(){
		final String result = PinyinUtil.getFirstLetter("H是第一个", ", ");
		Assertions.assertEquals("h, s, d, y, g", result);
	}

	@Test
	public void getFirstLetterTest2(){
		final String result = PinyinUtil.getFirstLetter("崞阳", ", ");
		Assertions.assertEquals("g, y", result);
	}
}
