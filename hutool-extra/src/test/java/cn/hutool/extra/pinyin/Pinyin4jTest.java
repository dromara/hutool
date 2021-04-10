package cn.hutool.extra.pinyin;

import cn.hutool.extra.pinyin.engine.pinyin4j.Pinyin4jEngine;
import org.junit.Assert;
import org.junit.Test;

public class Pinyin4jTest {

	final Pinyin4jEngine engine = new Pinyin4jEngine();

	@Test
	public void getFirstLetterByPinyin4jTest(){
		final String result = engine.getFirstLetter("林海", "");
		Assert.assertEquals("lh", result);
	}

	@Test
	public void getPinyinByPinyin4jTest() {
		final String pinyin = engine.getPinyin("你好h", " ");
		Assert.assertEquals("ni hao h", pinyin);
	}
}
