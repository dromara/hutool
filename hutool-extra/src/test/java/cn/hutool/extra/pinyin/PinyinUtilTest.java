package cn.hutool.extra.pinyin;

import org.junit.Assert;
import org.junit.Test;

public class PinyinUtilTest {

	@Test
	public void toPinyinTest(){
		final String pinyin = PinyinUtil.toPinyin("你好", false);
		Assert.assertEquals("ni hao", pinyin);
	}

	@Test
	public void toPinyinUpperCaseTest(){
		final String pinyin = PinyinUtil.toPinyin("你好怡", true);
		Assert.assertEquals("NI HAO YI", pinyin);
	}
}
