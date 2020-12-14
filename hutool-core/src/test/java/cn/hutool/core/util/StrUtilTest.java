package cn.hutool.core.util;

import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 字符串工具类单元测试
 *
 * @author Looly
 */
public class StrUtilTest {

	@Test
	public void isBlankTest() {
		String blank = "	  　";
		Assert.assertTrue(StrUtil.isBlank(blank));
	}

	@Test
	public void trimTest() {
		String blank = "	 哈哈 　";
		String trim = StrUtil.trim(blank);
		Assert.assertEquals("哈哈", trim);
	}

	@Test
	public void cleanBlankTest() {
		// 包含：制表符、英文空格、不间断空白符、全角空格
		String str = "	 你 好　";
		String cleanBlank = StrUtil.cleanBlank(str);
		Assert.assertEquals("你好", cleanBlank);
	}

	@Test
	public void cutTest() {
		String str = "aaabbbcccdddaadfdfsdfsdf0";
		String[] cut = StrUtil.cut(str, 4);
		Assert.assertArrayEquals(new String[]{"aaab", "bbcc", "cddd", "aadf", "dfsd", "fsdf", "0"}, cut);
	}

	@Test
	public void splitTest() {
		String str = "a,b ,c,d,,e";
		List<String> split = StrUtil.split(str, ',', -1, true, true);
		// 测试空是否被去掉
		Assert.assertEquals(5, split.size());
		// 测试去掉两边空白符是否生效
		Assert.assertEquals("b", split.get(1));

		final String[] strings = StrUtil.splitToArray("abc/", '/');
		Assert.assertEquals(2, strings.length);
	}

	@Test
	public void splitToLongTest() {
		String str = "1,2,3,4, 5";
		long[] longArray = StrUtil.splitToLong(str, ',');
		Assert.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);

		longArray = StrUtil.splitToLong(str, ",");
		Assert.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);
	}

	@Test
	public void splitToIntTest() {
		String str = "1,2,3,4, 5";
		int[] intArray = StrUtil.splitToInt(str, ',');
		Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);

		intArray = StrUtil.splitToInt(str, ",");
		Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);
	}

	@Test
	public void formatTest() {
		String template = "你好，我是{name}，我的电话是：{phone}";
		String result = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", "13888881111"));
		Assert.assertEquals("你好，我是张三，我的电话是：13888881111", result);

		String result2 = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", null));
		Assert.assertEquals("你好，我是张三，我的电话是：{phone}", result2);
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

		Assert.assertEquals("", StrUtil.strip("a", "a"));
		Assert.assertEquals("", StrUtil.strip("a", "a", "b"));
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
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("balabala", null, 0));
		Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("", "", 0));
		Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0));
		Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0));
		Assert.assertEquals(1, StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0));
		Assert.assertEquals(5, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3));
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9));
		Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1));
		Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "", 2));
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("abc", "", 9));
	}

	@Test
	public void lastIndexOfTest() {
		String a = "aabbccddcc";
		int lastIndexOf = StrUtil.lastIndexOf(a, "c", 0, false);
		Assert.assertEquals(-1, lastIndexOf);
	}

	@Test
	public void lastIndexOfIgnoreCaseTest() {
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase(null, "balabala", 0));
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("balabala", null));
		Assert.assertEquals(0, StrUtil.lastIndexOfIgnoreCase("", ""));
		Assert.assertEquals(7, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "A"));
		Assert.assertEquals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B"));
		Assert.assertEquals(4, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "AB"));
		Assert.assertEquals(2, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 3));
		Assert.assertEquals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 9));
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", -1));
		Assert.assertEquals(2, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "", 2));
		Assert.assertEquals(3, StrUtil.lastIndexOfIgnoreCase("abc", "", 9));
		Assert.assertEquals(0, StrUtil.lastIndexOfIgnoreCase("AAAcsd", "aaa"));
	}

	@Test
	public void replaceTest() {
		String string = StrUtil.replace("aabbccdd", 2, 6, '*');
		Assert.assertEquals("aa****dd", string);
		string = StrUtil.replace("aabbccdd", 2, 12, '*');
		Assert.assertEquals("aa******", string);
	}

	@Test
	public void replaceTest2() {
		String result = StrUtil.replace("123", "2", "3");
		Assert.assertEquals("133", result);
	}

	@Test
	public void replaceTest3() {
		String result = StrUtil.replace(",abcdef,", ",", "|");
		Assert.assertEquals("|abcdef|", result);
	}

	@Test
	public void replaceTest4() {
		String a = "1039";
		String result = StrUtil.padPre(a, 8, "0"); //在字符串1039前补4个0
		Assert.assertEquals("00001039", result);
	}

	@Test
	public void upperFirstTest() {
		StringBuilder sb = new StringBuilder("KEY");
		String s = StrUtil.upperFirst(sb);
		Assert.assertEquals(s, sb.toString());
	}

	@Test
	public void lowerFirstTest() {
		StringBuilder sb = new StringBuilder("KEY");
		String s = StrUtil.lowerFirst(sb);
		Assert.assertEquals("kEY", s);
	}

	@Test
	public void subTest() {
		String a = "abcderghigh";
		String pre = StrUtil.sub(a, -5, a.length());
		Assert.assertEquals("ghigh", pre);
	}

	@Test
	public void subByCodePointTest() {
		// 🤔👍🍓🤔
		String test = "\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53\uD83E\uDD14";

		// 不正确的子字符串
		String wrongAnswer = StrUtil.sub(test, 0, 3);
		Assert.assertNotEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", wrongAnswer);

		// 正确的子字符串
		String rightAnswer = StrUtil.subByCodePoint(test, 0, 3);
		Assert.assertEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", rightAnswer);
	}

	@Test
	public void subBeforeTest() {
		String a = "abcderghigh";
		String pre = StrUtil.subBefore(a, "d", false);
		Assert.assertEquals("abc", pre);
		pre = StrUtil.subBefore(a, 'd', false);
		Assert.assertEquals("abc", pre);
		pre = StrUtil.subBefore(a, 'a', false);
		Assert.assertEquals("", pre);

		//找不到返回原串
		pre = StrUtil.subBefore(a, 'k', false);
		Assert.assertEquals(a, pre);
		pre = StrUtil.subBefore(a, 'k', true);
		Assert.assertEquals(a, pre);
	}

	@Test
	public void subAfterTest() {
		String a = "abcderghigh";
		String pre = StrUtil.subAfter(a, "d", false);
		Assert.assertEquals("erghigh", pre);
		pre = StrUtil.subAfter(a, 'd', false);
		Assert.assertEquals("erghigh", pre);
		pre = StrUtil.subAfter(a, 'h', true);
		Assert.assertEquals("", pre);

		//找不到字符返回空串
		pre = StrUtil.subAfter(a, 'k', false);
		Assert.assertEquals("", pre);
		pre = StrUtil.subAfter(a, 'k', true);
		Assert.assertEquals("", pre);
	}

	@Test
	public void subSufByLengthTest() {
		Assert.assertEquals("cde", StrUtil.subSufByLength("abcde", 3));
		Assert.assertEquals("", StrUtil.subSufByLength("abcde", -1));
		Assert.assertEquals("", StrUtil.subSufByLength("abcde", 0));
		Assert.assertEquals("abcde", StrUtil.subSufByLength("abcde", 5));
		Assert.assertEquals("abcde", StrUtil.subSufByLength("abcde", 10));
	}

	@Test
	public void repeatAndJoinTest() {
		String repeatAndJoin = StrUtil.repeatAndJoin("?", 5, ",");
		Assert.assertEquals("?,?,?,?,?", repeatAndJoin);

		repeatAndJoin = StrUtil.repeatAndJoin("?", 0, ",");
		Assert.assertEquals("", repeatAndJoin);

		repeatAndJoin = StrUtil.repeatAndJoin("?", 5, null);
		Assert.assertEquals("?????", repeatAndJoin);
	}

	@Test
	public void moveTest() {
		String str = "aaaaaaa22222bbbbbbb";
		String result = StrUtil.move(str, 7, 12, -3);
		Assert.assertEquals("aaaa22222aaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -4);
		Assert.assertEquals("aaa22222aaaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -7);
		Assert.assertEquals("22222aaaaaaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -20);
		Assert.assertEquals("aaaaaa22222abbbbbbb", result);

		result = StrUtil.move(str, 7, 12, 3);
		Assert.assertEquals("aaaaaaabbb22222bbbb", result);
		result = StrUtil.move(str, 7, 12, 7);
		Assert.assertEquals("aaaaaaabbbbbbb22222", result);
		result = StrUtil.move(str, 7, 12, 20);
		Assert.assertEquals("aaaaaaab22222bbbbbb", result);

		result = StrUtil.move(str, 7, 12, 0);
		Assert.assertEquals("aaaaaaa22222bbbbbbb", result);
	}

	@Test
	public void removePrefixIgnorecaseTest() {
		String a = "aaabbb";
		String prefix = "aaa";
		Assert.assertEquals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

		prefix = "AAA";
		Assert.assertEquals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

		prefix = "AAABBB";
		Assert.assertEquals("", StrUtil.removePrefixIgnoreCase(a, prefix));
	}

	@Test
	public void maxLengthTest() {
		String text = "我是一段正文，很长的正文，需要截取的正文";
		String str = StrUtil.maxLength(text, 5);
		Assert.assertEquals("我是一段正...", str);
		str = StrUtil.maxLength(text, 21);
		Assert.assertEquals(text, str);
		str = StrUtil.maxLength(text, 50);
		Assert.assertEquals(text, str);
	}

	@Test
	public void toCamelCaseTest() {
		String str = "Table_Test_Of_day";
		String result = StrUtil.toCamelCase(str);
		Assert.assertEquals("tableTestOfDay", result);

		String str1 = "TableTestOfDay";
		String result1 = StrUtil.toCamelCase(str1);
		Assert.assertEquals("TableTestOfDay", result1);

		String abc1d = StrUtil.toCamelCase("abc_1d");
		Assert.assertEquals("abc1d", abc1d);
	}

	@Test
	public void toUnderLineCaseTest() {
		Dict.create()
				.set("Table_Test_Of_day", "table_test_of_day")
				.set("_Table_Test_Of_day_", "_table_test_of_day_")
				.set("_Table_Test_Of_DAY_", "_table_test_of_DAY_")
				.set("_TableTestOfDAYtoday", "_table_test_of_DAY_today")
				.set("HelloWorld_test", "hello_world_test")
				.set("H2", "H2")
				.set("H#case", "H#case")
				.forEach((key, value) -> Assert.assertEquals(value, StrUtil.toUnderlineCase(key)));
	}

	@Test
	public void containsAnyTest() {
		//字符
		boolean containsAny = StrUtil.containsAny("aaabbbccc", 'a', 'd');
		Assert.assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'e', 'd');
		Assert.assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'd', 'c');
		Assert.assertTrue(containsAny);

		//字符串
		containsAny = StrUtil.containsAny("aaabbbccc", "a", "d");
		Assert.assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "e", "d");
		Assert.assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "d", "c");
		Assert.assertTrue(containsAny);
	}

	@Test
	public void centerTest() {
		Assert.assertNull(StrUtil.center(null, 10));
		Assert.assertEquals("    ", StrUtil.center("", 4));
		Assert.assertEquals("ab", StrUtil.center("ab", -1));
		Assert.assertEquals(" ab ", StrUtil.center("ab", 4));
		Assert.assertEquals("abcd", StrUtil.center("abcd", 2));
		Assert.assertEquals(" a  ", StrUtil.center("a", 4));
	}

	@Test
	public void padPreTest() {
		Assert.assertNull(StrUtil.padPre(null, 10, ' '));
		Assert.assertEquals("001", StrUtil.padPre("1", 3, '0'));
		Assert.assertEquals("12", StrUtil.padPre("123", 2, '0'));

		Assert.assertNull(StrUtil.padPre(null, 10, "AA"));
		Assert.assertEquals("AB1", StrUtil.padPre("1", 3, "ABC"));
		Assert.assertEquals("12", StrUtil.padPre("123", 2, "ABC"));
	}

	@Test
	public void padAfterTest() {
		Assert.assertNull(StrUtil.padAfter(null, 10, ' '));
		Assert.assertEquals("100", StrUtil.padAfter("1", 3, '0'));
		Assert.assertEquals("23", StrUtil.padAfter("123", 2, '0'));

		Assert.assertNull(StrUtil.padAfter(null, 10, "ABC"));
		Assert.assertEquals("1AB", StrUtil.padAfter("1", 3, "ABC"));
		Assert.assertEquals("23", StrUtil.padAfter("123", 2, "ABC"));
	}

	@Test
	public void subBetweenAllTest() {
		Assert.assertArrayEquals(new String[]{"yz", "abc"}, StrUtil.subBetweenAll("saho[yz]fdsadp[abc]a", "[", "]"));
		Assert.assertArrayEquals(new String[]{"abc"}, StrUtil.subBetweenAll("saho[yzfdsadp[abc]a]", "[", "]"));
		Assert.assertArrayEquals(new String[]{"abc", "abc"}, StrUtil.subBetweenAll("yabczyabcz", "y", "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll(null, "y", "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll("", "y", "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", null, "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", "y", null));
	}

	@Test
	public void subBetweenAllTest2() {
		//issue#861@Github，起始不匹配的时候，应该直接空
		String src1 = "/* \n* hutool  */  asdas  /* \n* hutool  */";
		String src2 = "/ * hutool  */  asdas  / * hutool  */";

		String[] results1 = StrUtil.subBetweenAll(src1, "/**", "*/");
		Assert.assertEquals(0, results1.length);

		String[] results2 = StrUtil.subBetweenAll(src2, "/*", "*/");
		Assert.assertEquals(0, results2.length);
	}

	@Test
	public void subBetweenAllTest3() {
		String src1 = "'abc'and'123'";
		String[] strings = StrUtil.subBetweenAll(src1, "'", "'");
		Assert.assertEquals(2, strings.length);
		Assert.assertEquals("abc", strings[0]);
		Assert.assertEquals("123", strings[1]);

		String src2 = "'abc''123'";
		strings = StrUtil.subBetweenAll(src2, "'", "'");
		Assert.assertEquals(2, strings.length);
		Assert.assertEquals("abc", strings[0]);
		Assert.assertEquals("123", strings[1]);

		String src3 = "'abc'123'";
		strings = StrUtil.subBetweenAll(src3, "'", "'");
		Assert.assertEquals(1, strings.length);
		Assert.assertEquals("abc", strings[0]);
	}

	@Test
	public void briefTest() {
		String str = RandomUtil.randomString(1000);
		int maxLength = RandomUtil.randomInt(1000);
		String brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals(brief.length(), maxLength);
	}

	@Test
	public void filterTest() {
		final String filterNumber = StrUtil.filter("hutool678", CharUtil::isNumber);
		Assert.assertEquals("678", filterNumber);
		String cleanBlank = StrUtil.filter("	 你 好　", c -> !CharUtil.isBlankChar(c));
		Assert.assertEquals("你好", cleanBlank);
	}

	@Test
	public void wrapAllTest() {
		String[] strings = StrUtil.wrapAll("`", "`", StrUtil.splitToArray("1,2,3,4", ','));
		Assert.assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));

		strings = StrUtil.wrapAllWithPair("`", StrUtil.splitToArray("1,2,3,4", ','));
		Assert.assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));
	}

	@Test
	public void startWithTest(){
		String a = "123";
		String b = "123";

		Assert.assertTrue(StrUtil.startWith(a, b));
		Assert.assertFalse(StrUtil.startWithIgnoreEquals(a, b));
	}

	@Test
	public void indexedFormatTest() {
		final String ret = StrUtil.indexedFormat("this is {0} for {1}", "a", 1000);
		Assert.assertEquals("this is a for 1,000", ret);
	}
}
