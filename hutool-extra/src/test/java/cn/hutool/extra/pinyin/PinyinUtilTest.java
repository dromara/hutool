package cn.hutool.extra.pinyin;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PinyinUtilTest {

	@Test
	public void getPinyinTest(){
		final String pinyin = PinyinUtil.getPinyin("你好怡", " ");
		assertEquals("ni hao yi", pinyin);
	}

	@Test
	public void getFirstLetterTest(){
		final String result = PinyinUtil.getFirstLetter("H是第一个", ", ");
		assertEquals("h, s, d, y, g", result);
	}

	@Test
	public void getFirstLetterTest2(){
		final String result = PinyinUtil.getFirstLetter("崞阳", ", ");
		assertEquals("g, y", result);
	}
}
