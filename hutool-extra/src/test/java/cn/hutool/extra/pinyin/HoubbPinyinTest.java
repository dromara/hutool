package cn.hutool.extra.pinyin;

import cn.hutool.extra.pinyin.engine.houbbpinyin.HoubbPinyinEngine;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class HoubbPinyinTest {

	final HoubbPinyinEngine engine = new HoubbPinyinEngine();

	@Test
	public void getFirstLetterTest(){
		final String result = engine.getFirstLetter("林海", "");
		assertEquals("lh", result);
	}

	@Test
	public void getPinyinTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		assertEquals("ni hao h", pinyin);
	}
}
