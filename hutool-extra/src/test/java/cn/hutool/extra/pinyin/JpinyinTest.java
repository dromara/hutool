package cn.hutool.extra.pinyin;

import cn.hutool.extra.pinyin.engine.jpinyin.JPinyinEngine;
import org.junit.Assert;
import org.junit.Test;

public class JpinyinTest {

	final JPinyinEngine engine = new JPinyinEngine();

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
