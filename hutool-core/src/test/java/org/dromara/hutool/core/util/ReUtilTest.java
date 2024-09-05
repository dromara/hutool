/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReUtilTest {

	static final String content = "ZZZaaabbbccc中文1234";

	@Test
	public void getTest() {
		final String resultGet = ReUtil.get("\\w{2}", content, 0);
		assertEquals("ZZ", resultGet);
	}

	@Test
	public void extractMultiTest() {
		// 抽取多个分组然后把它们拼接起来
		final String resultExtractMulti = ReUtil.extractMulti("(\\w)aa(\\w)", content, "$1-$2");
		assertEquals("Z-a", resultExtractMulti);
	}

	@Test
	public void extractMultiTest2() {
		// 抽取多个分组然后把它们拼接起来
		final String resultExtractMulti = ReUtil.extractMulti("(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)(\\w)", content, "$1-$2-$3-$4-$5-$6-$7-$8-$9-$10");
		assertEquals("Z-Z-Z-a-a-a-b-b-b-c", resultExtractMulti);
	}

	@Test
	public void delFirstTest() {
		// 删除第一个匹配到的内容
		final String resultDelFirst = ReUtil.delFirst("(\\w)aa(\\w)", content);
		assertEquals("ZZbbbccc中文1234", resultDelFirst);
	}

	@Test
	public void delLastTest(){
		final String blank = "";
		final String word = "180公斤";
		final String sentence = "10.商品KLS100021型号xxl适合身高180体重130斤的用户";
		//空字符串兼容
		assertEquals(blank,ReUtil.delLast("\\d+", blank));
		assertEquals(blank,ReUtil.delLast(PatternPool.NUMBERS, blank));

		//去除数字
		assertEquals("公斤",ReUtil.delLast("\\d+", word));
		assertEquals("公斤",ReUtil.delLast(PatternPool.NUMBERS, word));
		//去除汉字
		//noinspection UnnecessaryUnicodeEscape
		assertEquals("180",ReUtil.delLast("[\u4E00-\u9FFF]+", word));
		assertEquals("180",ReUtil.delLast(PatternPool.CHINESES, word));

		//多个匹配删除最后一个 判断是否不在包含最后的数字
		String s = ReUtil.delLast("\\d+", sentence);
		assertEquals("10.商品KLS100021型号xxl适合身高180体重斤的用户", s);
		s = ReUtil.delLast(PatternPool.NUMBERS, sentence);
		assertEquals("10.商品KLS100021型号xxl适合身高180体重斤的用户", s);

		//多个匹配删除最后一个 判断是否不在包含最后的数字
		//noinspection UnnecessaryUnicodeEscape
		Assertions.assertFalse(ReUtil.delLast("[\u4E00-\u9FFF]+", sentence).contains("斤的用户"));
		Assertions.assertFalse(ReUtil.delLast(PatternPool.CHINESES, sentence).contains("斤的用户"));
	}

	@Test
	public void delAllTest() {
		// 删除所有匹配到的内容
		final String content = "发东方大厦eee![images]http://abc.com/2.gpg]好机会eee![images]http://abc.com/2.gpg]好机会";
		final String resultDelAll = ReUtil.delAll("!\\[images\\][^\\u4e00-\\u9fa5\\\\s]*", content);
		assertEquals("发东方大厦eee好机会eee好机会", resultDelAll);
	}

	@Test
	public void findAllTest() {
		// 查找所有匹配文本
		final List<String> resultFindAll = ReUtil.findAll("\\w{2}", content, 0, new ArrayList<>());
		final List<String> expected = ListUtil.of("ZZ", "Za", "aa", "bb", "bc", "cc", "12", "34");
		assertEquals(expected, resultFindAll);
	}

	@Test
	public void getFirstNumberTest() {
		// 找到匹配的第一个数字
		final Integer resultGetFirstNumber = ReUtil.getFirstNumber(content);
		assertEquals(Integer.valueOf(1234), resultGetFirstNumber);
	}

	@Test
	public void isMatchTest() {
		// 给定字符串是否匹配给定正则
		//noinspection UnnecessaryUnicodeEscape
		final boolean isMatch = ReUtil.isMatch("\\w+[\u4E00-\u9FFF]+\\d+", content);
		Assertions.assertTrue(isMatch);
	}

	@Test
	public void replaceAllTest() {
		//通过正则查找到字符串，然后把匹配到的字符串加入到replacementTemplate中，$1表示分组1的字符串
		//此处把1234替换为 ->1234<-
		final String replaceAll = ReUtil.replaceAll(content, "(\\d+)", "->$1<-");
		assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
	}

	@Test
	public void replaceAllTest2() {
		//此处把1234替换为 ->1234<-
		final String replaceAll = ReUtil.replaceAll(content, "(\\d+)", parameters -> "->" + parameters.group(1) + "<-");
		assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);
	}

	@Test
	public void replaceAllTest3() {
		// 修改前：ReUtil.replaceAll()方法，当replacementTemplate为null对象时，出现空指针异常
		final String str = null;
		final Pattern pattern = Pattern.compile("(\\d+)");
		// Assertions.assertThrows(NullPointerException.class, () -> ReUtil.replaceAll(content, pattern, str));

		// 修改后：测试正常的方法访问是否有效
		final String replaceAll = ReUtil.replaceAll(content, pattern, parameters -> "->" + parameters.group(1) + "<-");
		assertEquals("ZZZaaabbbccc中文->1234<-", replaceAll);

		// 修改后：判断ReUtil.replaceAll()方法，当replacementTemplate为null对象时按照""处理，表示去除匹配
		String s = ReUtil.replaceAll(content, pattern, str);
		assertEquals("ZZZaaabbbccc中文", s);
		s = ReUtil.replaceAll(content, pattern, "");
		assertEquals("ZZZaaabbbccc中文", s);
	}

	@Test
	public void escapeTest() {
		//转义给定字符串，为正则相关的特殊符号转义
		final String escape = ReUtil.escape("我有个$符号{}");
		assertEquals("我有个\\$符号\\{\\}", escape);
	}

	@Test
	public void escapeTest2(){
		final String str = "a[bbbc";
		final String re = "[";
		final String s = ReUtil.get(ReUtil.escape(re), str, 0);
		assertEquals("[", s);
	}

	@Test
	public void escapeTest3(){
		final String context = "{prefix}_";
		final String regex = "{prefix}_";
		final boolean b = ReUtil.isMatch(ReUtil.escape(regex), context);
		Assertions.assertTrue(b);
	}

	@Test
	public void getAllGroupsTest() {
		//转义给定字符串，为正则相关的特殊符号转义
		final Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+)");
		List<String> allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1");
		assertEquals("192-168-1", allGroups.get(0));
		assertEquals("192", allGroups.get(1));
		assertEquals("168", allGroups.get(2));
		assertEquals("1", allGroups.get(3));

		allGroups = ReUtil.getAllGroups(pattern, "192-168-1-1", false);
		assertEquals("192", allGroups.get(0));
		assertEquals("168", allGroups.get(1));
		assertEquals("1", allGroups.get(2));
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
		assertEquals("2021", year);
		final String month = ReUtil.get(regex, content, "month");
		assertEquals("10", month);
		final String day = ReUtil.get(regex, content, "day");
		assertEquals("11", day);
	}

	@Test
	@EnabledForJreRange(max = org.junit.jupiter.api.condition.JRE.JAVA_8)
	public void getAllGroupNamesTest() {
		final String content = "2021-10-11";
		final String regex = "(?<year>\\d+)-(?<month>\\d+)-(?<day>\\d+)";
		final Map<String, String> map = ReUtil.getAllGroupNames(PatternPool.get(regex, Pattern.DOTALL), content);
		assertEquals(map.get("year"), "2021");
		assertEquals(map.get("month"), "10");
		assertEquals(map.get("day"), "11");
	}

	@Test
	public void issuesI5TQDRTest(){
		final Pattern patternIp = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))"
				+ "|[0-1]?\\d{1,2})\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})");
		final String s = ReUtil.replaceAll("1.2.3.4", patternIp, "$1.**.**.$10");
		assertEquals("1.**.**.4", s);
	}

	@Test
	public void issueI6GIMTTest(){
		assertEquals(StrUtil.EMPTY, ReUtil.delAll("[\\s]*", " "));
	}

	@Test
	public void issueI9T1TGTest() {
		String regex = "^model";
		String content = "model-v";
		final String result = ReUtil.get(regex, content, 0);
		assertEquals("model", result);

		regex = "^model.*?";
		content = "model-v";
		final boolean match = ReUtil.isMatch(regex, content);
		assertTrue(match);
	}

	@Test
	void getEmailAddressTest() {
		String mail = "姓名<a.b@Hutool.cn>";
		String s = ReUtil.get(PatternPool.EMAIL, mail, 0);
		assertEquals("a.b@Hutool.cn", s);

		mail = "姓名 <a.b@Hutool.cn>";
		s = ReUtil.get(PatternPool.EMAIL, mail, 0);
		assertEquals("a.b@Hutool.cn", s);

		mail = "a.b@Hutool.cn";
		s = ReUtil.get(PatternPool.EMAIL, mail, 0);
		assertEquals("a.b@Hutool.cn", s);
	}
}
