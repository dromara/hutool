package com.xiaoleilu.hutool.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.ReUtil;

public class ReUtilTest {
	final String content = "ZZZaaabbbccc中文1234";

	@Test
	public void getTest() {
		String resultGet = ReUtil.get("\\w{2}", content, 0);
		Assert.assertEquals("ZZ", resultGet);
	}

	@Test
	public void extractMultiTest() {
		// 抽取多个分组然后把它们拼接起来
		String resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
		Assert.assertEquals("Z-a", resultExtractMulti);
	}

	@Test
	public void delFirstTest() {
		// 删除第一个匹配到的内容
		String resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
		Assert.assertEquals("ZZbbbccc中文1234", resultDelFirst);
	}
	
	@Test
	public void delAllTest() {
		// 删除所有匹配到的内容
		String content = "发东方大厦eee![images]http://abc.com/2.gpg]好机会eee![images]http://abc.com/2.gpg]好机会";
		String resultDelAll = ReUtil.delAll("!\\[images\\][^\\u4e00-\\u9fa5\\\\s]*", content);
		Assert.assertEquals("发东方大厦eee好机会eee好机会", resultDelAll);
	}

	@Test
	public void findAllTest() {
		// 查找所有匹配文本
		List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<String>());
		ArrayList<String> expected = CollectionUtil.newArrayList("ZZ", "Za", "aa", "bb", "bc", "cc", "12", "34");
		Assert.assertEquals(expected, resultFindAll);
	}

	@Test
	public void getFirstNumberTest() {
		// 找到匹配的第一个数字
		Integer resultGetFirstNumber = ReUtil.getFirstNumber(content);
		Assert.assertEquals(Integer.valueOf(1234), resultGetFirstNumber);
	}
	
	@Test
	public void isMatchTest() {
		// 给定字符串是否匹配给定正则
		boolean isMatch = ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content);
		Assert.assertTrue(isMatch);
	}
	
	@Test
	public void replaceAllTest() {
		//通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
		//此处把1234替换为 ->1234<-
		String replaceAll = ReUtil.replaceAll(content, "(\\d+)", "->$1<-");
		Assert.assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
	}
	
	@Test
	public void escapeTest() {
		//转义给定字符串，为正则相关的特殊符号转义
		String escape = ReUtil.escape("我有个$符号{}");
		Assert.assertEquals("我有个\\$符号\\{\\}", escape);
	}
}
