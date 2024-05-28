/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.text;

import org.dromara.hutool.core.date.DatePattern;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.regex.Pattern;

public class CharSequenceUtilTest {

	@Test
	public void replaceTest() {
		final String actual = CharSequenceUtil.replace("SSM15930297701BeryAllen", Pattern.compile("[0-9]"), matcher -> "");
		Assertions.assertEquals("SSMBeryAllen", actual);
	}

	@Test
	public void replaceTest2() {
		// https://gitee.com/dromara/hutool/issues/I4M16G
		final String replace = "#{A}";
		final String result = CharSequenceUtil.replace(replace, "#{AAAAAAA}", "1");
		Assertions.assertEquals(replace, result);
	}

	@Test
	public void replaceByStrTest() {
		final String replace = "SSM15930297701BeryAllen";
		final String result = CharSequenceUtil.replaceByCodePoint(replace, 5, 12, "***");
		Assertions.assertEquals("SSM15***01BeryAllen", result);

		final String emoji = StrUtil.replaceByCodePoint("\uD83D\uDE00aabb\uD83D\uDE00ccdd", 2, 6, "***");
		Assertions.assertEquals("\uD83D\uDE00a***ccdd", emoji);
	}

	@Test
	public void addPrefixIfNotTest() {
		final String str = "hutool";
		String result = CharSequenceUtil.addPrefixIfNot(str, "hu");
		Assertions.assertEquals(str, result);

		result = CharSequenceUtil.addPrefixIfNot(str, "Good");
		Assertions.assertEquals("Good" + str, result);
	}

	@Test
	public void addSuffixIfNotTest() {
		final String str = "hutool";
		String result = CharSequenceUtil.addSuffixIfNot(str, "tool");
		Assertions.assertEquals(str, result);

		result = CharSequenceUtil.addSuffixIfNot(str, " is Good");
		Assertions.assertEquals(str + " is Good", result);

		// https://gitee.com/dromara/hutool/issues/I4NS0F
		result = CharSequenceUtil.addSuffixIfNot("", "/");
		Assertions.assertEquals("/", result);
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Test
	public void normalizeTest() {
		// https://blog.csdn.net/oscar999/article/details/105326270

		String str1 = "\u00C1";
		String str2 = "\u0041\u0301";

		Assertions.assertNotEquals(str1, str2);

		str1 = CharSequenceUtil.normalize(str1);
		str2 = CharSequenceUtil.normalize(str2);
		Assertions.assertEquals(str1, str2);
	}

	@Test
	public void indexOfTest() {
		int index = CharSequenceUtil.indexOf("abc123", '1');
		Assertions.assertEquals(3, index);
		index = CharSequenceUtil.indexOf("abc123", '3');
		Assertions.assertEquals(5, index);
		index = CharSequenceUtil.indexOf("abc123", 'a');
		Assertions.assertEquals(0, index);
	}

	@Test
	public void indexOfTest2() {
		int index = CharSequenceUtil.indexOf("abc123", '1', 0, 3);
		Assertions.assertEquals(-1, index);

		index = CharSequenceUtil.indexOf("abc123", 'b', 0, 3);
		Assertions.assertEquals(1, index);
	}

	@Test
	public void subPreGbkTest() {
		// https://gitee.com/dromara/hutool/issues/I4JO2E
		final String s = "华硕K42Intel酷睿i31代2G以下独立显卡不含机械硬盘固态硬盘120GB-192GB4GB-6GB";

		String v = CharSequenceUtil.subPreGbk(s, 40, false);
		Assertions.assertEquals(39, v.getBytes(CharsetUtil.GBK).length);

		v = CharSequenceUtil.subPreGbk(s, 40, true);
		Assertions.assertEquals(41, v.getBytes(CharsetUtil.GBK).length);
	}

	@Test
	void subPreTest() {
		final String pre = CharSequenceUtil.subPre("abc", 0);
		Assertions.assertEquals(StrUtil.EMPTY, pre);
	}

	@Test
	public void startWithTest() {
		// https://gitee.com/dromara/hutool/issues/I4MV7Q
		Assertions.assertFalse(CharSequenceUtil.startWith("123", "123", false, true));
		Assertions.assertFalse(CharSequenceUtil.startWith(null, null, false, true));
		Assertions.assertFalse(CharSequenceUtil.startWith("abc", "abc", true, true));

		Assertions.assertTrue(CharSequenceUtil.startWithIgnoreCase(null, null));
		Assertions.assertFalse(CharSequenceUtil.startWithIgnoreCase(null, "abc"));
		Assertions.assertFalse(CharSequenceUtil.startWithIgnoreCase("abcdef", null));
		Assertions.assertTrue(CharSequenceUtil.startWithIgnoreCase("abcdef", "abc"));
		Assertions.assertTrue(CharSequenceUtil.startWithIgnoreCase("ABCDEF", "abc"));
	}

	@Test
	public void endWithTest() {
		Assertions.assertFalse(CharSequenceUtil.endWith("123", "123", false, true));
		Assertions.assertFalse(CharSequenceUtil.endWith(null, null, false, true));
		Assertions.assertFalse(CharSequenceUtil.endWith("abc", "abc", true, true));

		Assertions.assertTrue(CharSequenceUtil.endWithIgnoreCase(null, null));
		Assertions.assertFalse(CharSequenceUtil.endWithIgnoreCase(null, "abc"));
		Assertions.assertFalse(CharSequenceUtil.endWithIgnoreCase("abcdef", null));
		Assertions.assertTrue(CharSequenceUtil.endWithIgnoreCase("abcdef", "def"));
		Assertions.assertTrue(CharSequenceUtil.endWithIgnoreCase("ABCDEF", "def"));
	}

	@Test
	public void removePrefixIgnoreCaseTest(){
		Assertions.assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "abc"));
		Assertions.assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABC"));
		Assertions.assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "Abc"));
		Assertions.assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", ""));
		Assertions.assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", null));
		Assertions.assertEquals("", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCde"));
		Assertions.assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCdef"));
		Assertions.assertNull(CharSequenceUtil.removePrefixIgnoreCase(null, "ABCdef"));
	}

	@Test
	public void removeSuffixIgnoreCaseTest(){
		Assertions.assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "cde"));
		Assertions.assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "CDE"));
		Assertions.assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "Cde"));
		Assertions.assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", ""));
		Assertions.assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", null));
		Assertions.assertEquals("", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCde"));
		Assertions.assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCdef"));
		Assertions.assertNull(CharSequenceUtil.removeSuffixIgnoreCase(null, "ABCdef"));
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void trimToNullTest(){
		String a = "  ";
		Assertions.assertNull(CharSequenceUtil.trimToNull(a));

		a = "";
		Assertions.assertNull(CharSequenceUtil.trimToNull(a));

		a = null;
		Assertions.assertNull(CharSequenceUtil.trimToNull(a));
	}

	@Test
	public void containsAllTest() {
		final String a = "2142342422423423";
		Assertions.assertTrue(StrUtil.containsAll(a, "214", "234"));
	}

	@Test
	public void defaultIfEmptyTest() {
		final String emptyValue = "";
		final Instant result1 = CharSequenceUtil.defaultIfEmpty(emptyValue,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assertions.assertNotNull(result1);

		final String dateStr = "2020-10-23 15:12:30";
		final Instant result2 = CharSequenceUtil.defaultIfEmpty(dateStr,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assertions.assertNotNull(result2);
	}

	@Test
	public void defaultIfBlankTest() {
		final String emptyValue = " ";
		final Instant result1 = CharSequenceUtil.defaultIfBlank(emptyValue,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assertions.assertNotNull(result1);

		final String dateStr = "2020-10-23 15:12:30";
		final Instant result2 = CharSequenceUtil.defaultIfBlank(dateStr,
				(v) -> DateUtil.parse(v, DatePattern.NORM_DATETIME_PATTERN).toInstant(), Instant::now);
		Assertions.assertNotNull(result2);
	}

	@Test
	public void replaceLastTest() {
		final String str = "i am jack and jack";
		final String result = StrUtil.replaceLast(str, "JACK", null, true);
		Assertions.assertEquals(result, "i am jack and ");
	}

	@Test
	public void replaceFirstTest() {
		final String str = "yes and yes i do";
		final String result = StrUtil.replaceFirst(str, "YES", "", true);
		Assertions.assertEquals(result, " and yes i do");
	}

	@Test
	public void issueI5YN49Test() {
		final String str = "A5E6005700000000000000000000000000000000000000090D0100000000000001003830";
		Assertions.assertEquals("38", StrUtil.subByLength(str,-2,2));
	}

	@Test
	public void commonPrefixTest() {

		// -------------------------- None match -----------------------

		Assertions.assertEquals("", CharSequenceUtil.commonPrefix("", "abc"));
		Assertions.assertEquals("", CharSequenceUtil.commonPrefix(null, "abc"));
		Assertions.assertEquals("", CharSequenceUtil.commonPrefix("abc", null));
		Assertions.assertEquals("", CharSequenceUtil.commonPrefix("abc", ""));

		Assertions.assertEquals("", CharSequenceUtil.commonPrefix("azzzj", "bzzzj"));

		Assertions.assertEquals("", CharSequenceUtil.commonPrefix("english中文", "french中文"));

		// -------------------------- Matched -----------------------

		Assertions.assertEquals("name_", CharSequenceUtil.commonPrefix("name_abc", "name_efg"));

		Assertions.assertEquals("zzzj", CharSequenceUtil.commonPrefix("zzzja", "zzzjb"));

		Assertions.assertEquals("中文", CharSequenceUtil.commonPrefix("中文english", "中文french"));

		// { space * 10 } + "abc"
		final String str1 = CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 10) + "abc";

		// { space * 5 } + "efg"
		final String str2 = CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 5) + "efg";

		// Expect common prefix: { space * 5 }
		Assertions.assertEquals(CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 5), CharSequenceUtil.commonPrefix(str1, str2));
	}

	@Test
	public void commonSuffixTest() {

		// -------------------------- None match -----------------------

		Assertions.assertEquals("", CharSequenceUtil.commonSuffix("", "abc"));
		Assertions.assertEquals("", CharSequenceUtil.commonSuffix(null, "abc"));
		Assertions.assertEquals("", CharSequenceUtil.commonSuffix("abc", null));
		Assertions.assertEquals("", CharSequenceUtil.commonSuffix("abc", ""));

		Assertions.assertEquals("", CharSequenceUtil.commonSuffix("zzzja", "zzzjb"));

		Assertions.assertEquals("", CharSequenceUtil.commonSuffix("中文english", "中文Korean"));

		// -------------------------- Matched -----------------------

		Assertions.assertEquals("_name", CharSequenceUtil.commonSuffix("abc_name", "efg_name"));

		Assertions.assertEquals("zzzj", CharSequenceUtil.commonSuffix("abczzzj", "efgzzzj"));

		Assertions.assertEquals("中文", CharSequenceUtil.commonSuffix("english中文", "Korean中文"));

		// "abc" + { space * 10 }
		final String str1 = "abc" + CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 10);

		// "efg" + { space * 15 }
		final String str2 = "efg" + CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 15);

		// Expect common suffix: { space * 10 }
		Assertions.assertEquals(CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 10), CharSequenceUtil.commonSuffix(str1, str2));
	}

	@Test
	void codeLengthTest() {
		final String a = "🍒🐽";
		final int i = StrUtil.codeLength(a);
		Assertions.assertEquals(4, a.length());
		Assertions.assertEquals(2, i);
	}

	@Test
	public void limitByteLengthUtf8Test() {
		final String str = "这是This一段中英文";
		String ret = StrUtil.limitByteLengthUtf8(str, 12,  true);
		Assertions.assertEquals("这是Thi...", ret);

		ret = StrUtil.limitByteLengthUtf8(str, 13, true);
		Assertions.assertEquals("这是This...", ret);

		ret = StrUtil.limitByteLengthUtf8(str, 14, true);
		Assertions.assertEquals("这是This...", ret);

		ret = StrUtil.limitByteLengthUtf8(str, 999, true);
		Assertions.assertEquals(str, ret);
	}

	@Test
	public void limitByteLengthUtf8Test2() {
		final String str = "这是This一";
		final String ret = StrUtil.limitByteLengthUtf8(str, 12, true);
		Assertions.assertEquals("这是Thi...", ret);
	}

	@Test
	public void limitByteLengthTest() {
		final String str = "This is English";
		final String ret = StrUtil.limitByteLength(str, CharsetUtil.ISO_8859_1,10, 1, false);
		Assertions.assertEquals("This is En", ret);

	}

	@Test
	void upperAtTest() {
		final StringBuilder sb = new StringBuilder("key");

		final String s1 = CharSequenceUtil.upperAt(sb, 0);
		Assertions.assertEquals("Key", s1);

		final String s2 = CharSequenceUtil.upperAt(sb, 1);
		Assertions.assertEquals("kEy", s2);

		final String s3 = CharSequenceUtil.upperAt(sb, 2);
		Assertions.assertEquals("keY", s3);

	}

	@Test
	void lowerAtTest() {
		final StringBuilder sb = new StringBuilder("KEY");

		final String s1 = CharSequenceUtil.lowerAt(sb, 0);
		Assertions.assertEquals("kEY", s1);

		final String s2 = CharSequenceUtil.lowerAt(sb, 1);
		Assertions.assertEquals("KeY", s2);

		final String s3 = CharSequenceUtil.lowerAt(sb, 2);
		Assertions.assertEquals("KEy", s3);
	}

	@Test
	public void testContainsOnly() {
		// 测试空字符串
		Assertions.assertTrue(CharSequenceUtil.containsOnly("", 'a', 'b'));

		// 测试字符串只包含testChars中的字符
		Assertions.assertTrue(CharSequenceUtil.containsOnly("asdf", 'a', 's', 'd', 'f'));

		// 测试字符串包含testChars中的字符和其它字符
		Assertions.assertFalse(CharSequenceUtil.containsOnly("asdf123", 'a', 's', 'd', 'f'));

		// 测试字符串不包含testChars中的任何字符
		Assertions.assertFalse(CharSequenceUtil.containsOnly("hello", 'a', 'b'));

		// 测试字符串为null
		Assertions.assertTrue(CharSequenceUtil.containsOnly(null, 'a', 'b'));
	}
}
