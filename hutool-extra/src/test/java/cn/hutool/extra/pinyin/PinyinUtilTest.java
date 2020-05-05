package cn.hutool.extra.pinyin;

import org.junit.Assert;
import org.junit.Test;

public class PinyinUtilTest {

	@Test
	public void getPinyinTest(){
		final String pinyin = PinyinUtil.getPinyin("你好", " ");
		Assert.assertEquals("ni hao", pinyin);
	}

	@Test
	public void getPinyinUpperCaseTest(){
		final String pinyin = PinyinUtil.getPinyin("你好怡", " ");
		Assert.assertEquals("ni hao yi", pinyin);
	}

	@Test
	public void getFirstLetterTest(){
		final String result = PinyinUtil.getFirstLetter("H是第一个", ", ");
		Assert.assertEquals("h, s, d, y, g", result);
	}
}
