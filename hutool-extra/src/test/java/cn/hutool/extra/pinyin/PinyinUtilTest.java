package cn.hutool.extra.pinyin;

import cn.hutool.core.util.ArrayUtil;
import org.junit.Assert;
import org.junit.Test;

public class PinyinUtilTest {

	@Test
	public void getPinyinTest(){
		final String pinyin = PinyinUtil.getPinyin("你好", false);
		Assert.assertEquals("ni hao", pinyin);
	}

	@Test
	public void getPinyinUpperCaseTest(){
		final String pinyin = PinyinUtil.getPinyin("你好怡", true);
		Assert.assertEquals("NI HAO YI", pinyin);
	}

	@Test
	public void getFirstLetterTest(){
		final char[] result = PinyinUtil.getFirstLetter("H是第一个", false);
		Assert.assertEquals("h, s, d, y, g", ArrayUtil.join(result, ", "));
	}
}
