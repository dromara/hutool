package com.xiaoleilu.hutool.core.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 字符串工具类单元测试
 * @author Looly
 *
 */
public class StrUtilTest {
	
	@Test
	public void isBlankTest(){
		String blank = "	  　";
		Assert.assertTrue(StrUtil.isBlank(blank));
	}
	
	@Test
	public void trimTest(){
		String blank = "	 哈哈 　";
		String trim = StrUtil.trim(blank);
		Assert.assertEquals("哈哈", trim);
	}
	
	@Test
	public void cleanBlankTest(){
		//包含：制表符、英文空格、不间断空白符、全角空格
		String str = "	 你 好　";
		String cleanBlank = StrUtil.cleanBlank(str);
		Assert.assertEquals("你好", cleanBlank);
	}
	
	@Test
	public void cutTest(){
		String str = "aaabbbcccdddaadfdfsdfsdf0";
		String[] cut = StrUtil.cut(str, 4);
		Console.log(cut);
	}
	
	@Test
	public void splitTest(){
		String str = "a,b ,c,d,,e";
		List<String> split = StrUtil.split(str, ',', -1, true, true);
		//测试空是否被去掉
		Assert.assertEquals(5, split.size());
		//测试去掉两边空白符是否生效
		Assert.assertEquals("b", split.get(1));
		
		
	}
}
