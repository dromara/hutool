package cn.hutool.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.PatternPool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ReUtilTest {
	final String content = "ZZZaaabbbccc中文1234";

	@Test
	public void getTest() {
		String resultGet = ReUtil.get("\\w{2}", content, 0);
		Assertions.assertEquals("ZZ", resultGet);
	}

	@Test
	public void extractMultiTest() {
		// 抽取多个分组然后把它们拼接起来
		String resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
		Assertions.assertEquals("Z-a", resultExtractMulti);
	}

	@Test
	public void extractMultiTest2() {
		// 抽取多个分组然后把它们拼接起来
		String resultExtractMulti = ReUtil.extractMulti("(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)", content, "$1-$2-$3-$4-$5-$6-$7-$8-$9-$10");
		Assertions.assertEquals("Z-Z-Z-a-a-a-b-b-b-c", resultExtractMulti);
	}

	@Test
	public void delFirstTest() {
		// 删除第一个匹配到的内容
		String resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
		Assertions.assertEquals("ZZbbbccc中文1234", resultDelFirst);
	}

	@Test
	public void delLastTest(){
		String blank = "";
		String word = "180公斤";
		String sentence = "10.商品KLS100021型号xxl适合身高180体重130斤的用户";
		//空字符串兼容
		Assertions.assertEquals(blank,ReUtil.delLast("\\d+", blank));
		Assertions.assertEquals(blank,ReUtil.delLast(PatternPool.NUMBERS, blank));

		//去除数字
		Assertions.assertEquals("公斤",ReUtil.delLast("\\d+", word));
		Assertions.assertEquals("公斤",ReUtil.delLast(PatternPool.NUMBERS, word));
		//去除汉字
		Assertions.assertEquals("180",ReUtil.delLast("[\u4E00-\u9FFF]+", word));
		Assertions.assertEquals("180",ReUtil.delLast(PatternPool.CHINESES, word));

		//多个匹配删除最后一个 判断是否不在包含最后的数字
		String s = ReUtil.delLast("\\d+", sentence);
		Assertions.assertEquals("10.商品KLS100021型号xxl适合身高180体重斤的用户", s);
		s = ReUtil.delLast(PatternPool.NUMBERS, sentence);
		Assertions.assertEquals("10.商品KLS100021型号xxl适合身高180体重斤的用户", s);

		//多个匹配删除最后一个 判断是否不在包含最后的数字
		Assertions.assertFalse(ReUtil.delLast("[\u4E00-\u9FFF]+", sentence).contains("斤的用户"));
		Assertions.assertFalse(ReUtil.delLast(PatternPool.CHINESES, sentence).contains("斤的用户"));
	}

	@Test
	public void delAllTest() {
		// 删除所有匹配到的内容
		String content = "发东方大厦eee![images]http://abc.com/2.gpg]好机会eee![images]http://abc.com/2.gpg]好机会";
		String resultDelAll = ReUtil.delAll("!\\[images\\][^\\u4e00-\\u9fa5\\\\s]*", content);
		Assertions.assertEquals("发东方大厦eee好机会eee好机会", resultDelAll);
	}

	@Test
	public void findAllTest() {
		// 查找所有匹配文本
		List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<>());
		ArrayList<String> expected = CollectionUtil.newArrayList("ZZ", "Za", "aa", "bb", "bc", "cc", "12", "34");
		Assertions.assertEquals(expected, resultFindAll);
	}

	@Test
	public void getFirstNumberTest() {
		// 找到匹配的第一个数字
		Integer resultGetFirstNumber = ReUtil.getFirstNumber(content);
		Assertions.assertEquals(Integer.valueOf(1234), resultGetFirstNumber);
	}

	@Test
	public void isMatchTest() {
		// 给定字符串是否匹配给定正则
		boolean isMatch = ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content);
		Assertions.assertTrue(isMatch);
	}

	@Test
	public void replaceAllTest() {
		//通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
		//此处把1234替换为 ->1234<-
		String replaceAll = ReUtil.replaceAll(content, "(\\d+)", "->$1<-");
		Assertions.assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
	}

	@Test
	public void replaceAllTest2() {
		//此处把1234替换为 ->1234<-
		String replaceAll = ReUtil.replaceAll(this.content, "(\\d+)", parameters -> "->" + parameters.group(1) + "<-");
		Assertions.assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
	}

	@Test
	public void replaceTest() {
		String str = "AAABBCCCBBDDDBB";
		String replace = StrUtil.replace(str, 0, "BB", "22", false);
		Assertions.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 3, "BB", "22", false);
		Assertions.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "BB", "22", false);
		Assertions.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "22", true);
		Assertions.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "", true);
		Assertions.assertEquals("AAABBCCCDDD", replace);

		replace = StrUtil.replace(str, 4, "bb", null, true);
		Assertions.assertEquals("AAABBCCCDDD", replace);
	}

	@Test
	public void escapeTest() {
		//转义给定字符串，为正则相关的特殊符号转义
		String escape = ReUtil.escape("我有个$符号{}");
		Assertions.assertEquals("我有个\\$符号\\{\\}", escape);
	}

	@Test
	public void getAllGroupsTest() {
		//转义给定字符串，为正则相关的特殊符号转义
		Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
		List<String> allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1");
		Assertions.assertEquals("192-168-1", allGroups.get(0));
		Assertions.assertEquals("192", allGroups.get(1));
		Assertions.assertEquals("168", allGroups.get(2));
		Assertions.assertEquals("1", allGroups.get(3));

		allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1", false);
		Assertions.assertEquals("192", allGroups.get(0));
		Assertions.assertEquals("168", allGroups.get(1));
		Assertions.assertEquals("1", allGroups.get(2));
	}

	@Test
	public void matchTest(){
		final boolean match = ReUtil.isMatch(
				"(.+?)省(.+?)市(.+?)区", "广东省深圳市南山区");
		Console.log(match);
	}

	@Test
	public void getByGroupNameTest() {
		String content = "2021-10-11";
		String regex = "(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)";
		String year = ReUtil.get(regex, content, "year");
		Assertions.assertEquals("2021", year);
		String month = ReUtil.get(regex, content, "month");
		Assertions.assertEquals("10", month);
		String day = ReUtil.get(regex, content, "day");
		Assertions.assertEquals("11", day);
	}

	@Test
	public void getAllGroupNamesTest() {
		String content = "2021-10-11";
		String regex = "(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)";
		Map<String, String> map = ReUtil.getAllGroupNames(PatternPool.get(regex, Pattern.DOTALL), content);
		Assertions.assertEquals(map.get("year"), "2021");
		Assertions.assertEquals(map.get("month"), "10");
		Assertions.assertEquals(map.get("day"), "11");
	}
}
