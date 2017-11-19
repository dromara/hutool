package com.xiaoleilu.hutool.util;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Dict;
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
		Assert.assertArrayEquals(new String[] {"aaab", "bbcc", "cddd", "aadf", "dfsd", "fsdf", "0"}, cut);
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
	
	@Test
	public void formatTest() {
		String template = "你好，我是{name}，我的电话是：{phone}";
		String result = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", "13888881111"));
		Assert.assertEquals("你好，我是张三，我的电话是：13888881111", result);
	}
	
	@Test
	public void stripTest() {
		String str = "abcd123";
		String strip = StrUtil.strip(str, "ab", "23");
		Assert.assertEquals("cd1", strip);
		
		str = "abcd123";
		strip = StrUtil.strip(str, "ab", "");
		Assert.assertEquals("cd123", strip);
		
		str = "abcd123";
		strip = StrUtil.strip(str, null, "");
		Assert.assertEquals("abcd123", strip);
		
		str = "abcd123";
		strip = StrUtil.strip(str, null, "567");
		Assert.assertEquals("abcd123", strip);
	}
	
	@Test
	public void stripIgnoreCaseTest() {
		String str = "abcd123";
		String strip = StrUtil.stripIgnoreCase(str, "Ab", "23");
		Assert.assertEquals("cd1", strip);
		
		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, "AB", "");
		Assert.assertEquals("cd123", strip);
		
		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, "ab", "");
		Assert.assertEquals("cd123", strip);
		
		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, null, "");
		Assert.assertEquals("abcd123", strip);
		
		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, null, "567");
		Assert.assertEquals("abcd123", strip);
	}
	
	@Test
	public void indexOfIgnoreCaseTest() {
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase(null, "balabala", 0));
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("balabala" , null, 0));
		Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("", "", 0));
		Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0));
		Assert.assertEquals(2 , StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0));
		Assert.assertEquals(1 , StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0));
		Assert.assertEquals(5 , StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3));
		Assert.assertEquals(-1 , StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9));
		Assert.assertEquals(2 , StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1));
		Assert.assertEquals(2 , StrUtil.indexOfIgnoreCase("aabaabaa", "", 2));
		Assert.assertEquals(-1 , StrUtil.indexOfIgnoreCase("abc", "", 9));
	}
	
	@Test
	public void lastIndexOfIgnoreCaseTest() {
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase(null, "balabala", 0));
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("balabala" , null));
		Assert.assertEquals(0, StrUtil.lastIndexOfIgnoreCase("", ""));
		Assert.assertEquals(7, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "A"));
		Assert.assertEquals(5 , StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B"));
		Assert.assertEquals(4 , StrUtil.lastIndexOfIgnoreCase("aabaabaa", "AB"));
		Assert.assertEquals(2 , StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 3));
		Assert.assertEquals(5 , StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 9));
		Assert.assertEquals(-1 , StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", -1));
		Assert.assertEquals(2 , StrUtil.lastIndexOfIgnoreCase("aabaabaa", "", 2));
		Assert.assertEquals(3 , StrUtil.lastIndexOfIgnoreCase("abc", "", 9));
	}
}
