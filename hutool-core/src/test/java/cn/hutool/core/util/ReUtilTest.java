package cn.hutool.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.PatternPool;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ReUtilTest {
	final String content = "ZZZaaabbbccc中文1234";

	@Test
	public void getTest() {
		final String resultGet = ReUtil.get("\\w{2}", content, 0);
		Assert.assertEquals("ZZ", resultGet);
	}

	@Test
	public void extractMultiTest() {
		// 抽取多个分组然后把它们拼接起来
		final String resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
		Assert.assertEquals("Z-a", resultExtractMulti);
	}

	@Test
	public void extractMultiTest2() {
		// 抽取多个分组然后把它们拼接起来
		final String resultExtractMulti = ReUtil.extractMulti("(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)", content, "$1-$2-$3-$4-$5-$6-$7-$8-$9-$10");
		Assert.assertEquals("Z-Z-Z-a-a-a-b-b-b-c", resultExtractMulti);
	}

	@Test
	public void delFirstTest() {
		// 删除第一个匹配到的内容
		final String resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
		Assert.assertEquals("ZZbbbccc中文1234", resultDelFirst);
	}

	@Test
	public void delLastTest(){
		final String blank = "";
		final String word = "180公斤";
		final String sentence = "10.商品KLS100021型号xxl适合身高180体重130斤的用户";
		//空字符串兼容
		Assert.assertEquals(blank,ReUtil.delLast("\\d+", blank));
		Assert.assertEquals(blank,ReUtil.delLast(PatternPool.NUMBERS, blank));

		//去除数字
		Assert.assertEquals("公斤",ReUtil.delLast("\\d+", word));
		Assert.assertEquals("公斤",ReUtil.delLast(PatternPool.NUMBERS, word));
		//去除汉字
		Assert.assertEquals("180",ReUtil.delLast("[\u4E00-\u9FFF]+", word));
		Assert.assertEquals("180",ReUtil.delLast(PatternPool.CHINESES, word));

		//多个匹配删除最后一个 判断是否不在包含最后的数字
		String s = ReUtil.delLast("\\d+", sentence);
		Assert.assertEquals("10.商品KLS100021型号xxl适合身高180体重斤的用户", s);
		s = ReUtil.delLast(PatternPool.NUMBERS, sentence);
		Assert.assertEquals("10.商品KLS100021型号xxl适合身高180体重斤的用户", s);

		//多个匹配删除最后一个 判断是否不在包含最后的数字
		Assert.assertFalse(ReUtil.delLast("[\u4E00-\u9FFF]+", sentence).contains("斤的用户"));
		Assert.assertFalse(ReUtil.delLast(PatternPool.CHINESES, sentence).contains("斤的用户"));
	}

	@Test
	public void delAllTest() {
		// 删除所有匹配到的内容
		final String content = "发东方大厦eee![images]http://abc.com/2.gpg]好机会eee![images]http://abc.com/2.gpg]好机会";
		final String resultDelAll = ReUtil.delAll("!\\[images\\][^\\u4e00-\\u9fa5\\\\s]*", content);
		Assert.assertEquals("发东方大厦eee好机会eee好机会", resultDelAll);
	}

	@Test
	public void findAllTest() {
		// 查找所有匹配文本
		final List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<>());
		final ArrayList<String> expected = CollectionUtil.newArrayList("ZZ", "Za", "aa", "bb", "bc", "cc", "12", "34");
		Assert.assertEquals(expected, resultFindAll);
	}

	@Test
	public void getFirstNumberTest() {
		// 找到匹配的第一个数字
		final Integer resultGetFirstNumber = ReUtil.getFirstNumber(content);
		Assert.assertEquals(Integer.valueOf(1234), resultGetFirstNumber);
	}

	@Test
	public void isMatchTest() {
		// 给定字符串是否匹配给定正则
		final boolean isMatch = ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content);
		Assert.assertTrue(isMatch);
	}

	@Test
	public void replaceAllTest() {
		//通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
		//此处把1234替换为 ->1234<-
		final String replaceAll = ReUtil.replaceAll(content, "(\\d+)", "->$1<-");
		Assert.assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
	}

	@Test
	public void replaceAllTest2() {
		//此处把1234替换为 ->1234<-
		final String replaceAll = ReUtil.replaceAll(this.content, "(\\d+)", parameters -> "->" + parameters.group(1) + "<-");
		Assert.assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
	}

	@Test
	public void replaceTest() {
		final String str = "AAABBCCCBBDDDBB";
		String replace = StrUtil.replace(str, 0, "BB", "22", false);
		Assert.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 3, "BB", "22", false);
		Assert.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "BB", "22", false);
		Assert.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "22", true);
		Assert.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "", true);
		Assert.assertEquals("AAABBCCCDDD", replace);

		replace = StrUtil.replace(str, 4, "bb", null, true);
		Assert.assertEquals("AAABBCCCDDD", replace);
	}

	@Test
	public void escapeTest() {
		//转义给定字符串，为正则相关的特殊符号转义
		final String escape = ReUtil.escape("我有个$符号{}");
		Assert.assertEquals("我有个\\$符号\\{\\}", escape);
	}

	@Test
	public void escapeTest2(){
		final String str = "a[bbbc";
		final String re = "[";
		final String s = ReUtil.get(ReUtil.escape(re), str, 0);
		Assert.assertEquals("[", s);
	}

	@Test
	public void escapeTest3(){
		final String context = "{prefix}_";
		final String regex = "{prefix}_";
		final boolean b = ReUtil.isMatch(ReUtil.escape(regex), context);
		Assert.assertTrue(b);
	}

	@Test
	public void getAllGroupsTest() {
		//转义给定字符串，为正则相关的特殊符号转义
		final Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
		List<String> allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1");
		Assert.assertEquals("192-168-1", allGroups.get(0));
		Assert.assertEquals("192", allGroups.get(1));
		Assert.assertEquals("168", allGroups.get(2));
		Assert.assertEquals("1", allGroups.get(3));

		allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1", false);
		Assert.assertEquals("192", allGroups.get(0));
		Assert.assertEquals("168", allGroups.get(1));
		Assert.assertEquals("1", allGroups.get(2));
	}

	@Test
	public void matchTest(){
		final boolean match = ReUtil.isMatch(
				"(.+?)省(.+?)市(.+?)区", "广东省深圳市南山区");
		Console.log(match);
	}

	@Test
	public void getByGroupNameTest() {
		final String content = "2021-10-11";
		final String regex = "(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)";
		final String year = ReUtil.get(regex, content, "year");
		Assert.assertEquals("2021", year);
		final String month = ReUtil.get(regex, content, "month");
		Assert.assertEquals("10", month);
		final String day = ReUtil.get(regex, content, "day");
		Assert.assertEquals("11", day);
	}

	@Test
	public void getAllGroupNamesTest() {
		final String content = "2021-10-11";
		final String regex = "(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)";
		final Map<String, String> map = ReUtil.getAllGroupNames(PatternPool.get(regex, Pattern.DOTALL), content);
		Assert.assertEquals(map.get("year"), "2021");
		Assert.assertEquals(map.get("month"), "10");
		Assert.assertEquals(map.get("day"), "11");
	}

	@Test
	public void issuesI5TQDRTest(){
		final Pattern patternIp = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))"
				+ "|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})");
		final String s = ReUtil.replaceAll("1.2.3.4", patternIp, "$1.**.**.$10");
		Assert.assertEquals("1.**.**.4", s);
	}
}
