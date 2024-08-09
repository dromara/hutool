package cn.hutool.core.text;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class CharSequenceUtilTest {

	@Test
	public void replaceTest() {
		String actual = CharSequenceUtil.replace("SSM15930297701BeryAllen", Pattern.compile("[0-9]"), matcher -> "");
		assertEquals("SSMBeryAllen", actual);
	}

	@Test
	public void replaceTest2() {
		// https://gitee.com/dromara/hutool/issues/I4M16G
		String replace = "#{A}";
		String result = CharSequenceUtil.replace(replace, "#{AAAAAAA}", "1");
		assertEquals(replace, result);
	}

	@Test
	public void replaceByStrTest() {
		String replace = "SSM15930297701BeryAllen";
		String result = CharSequenceUtil.replaceByCodePoint(replace, 5, 12, "***");
		assertEquals("SSM15***01BeryAllen", result);
	}

	@Test
	public void addPrefixIfNotTest() {
		String str = "hutool";
		String result = CharSequenceUtil.addPrefixIfNot(str, "hu");
		assertEquals(str, result);

		result = CharSequenceUtil.addPrefixIfNot(str, "Good");
		assertEquals("Good" + str, result);
	}

	@Test
	public void addSuffixIfNotTest() {
		String str = "hutool";
		String result = CharSequenceUtil.addSuffixIfNot(str, "tool");
		assertEquals(str, result);

		result = CharSequenceUtil.addSuffixIfNot(str, " is Good");
		assertEquals(str + " is Good", result);

		// https://gitee.com/dromara/hutool/issues/I4NS0F
		result = CharSequenceUtil.addSuffixIfNot("", "/");
		assertEquals("/", result);
	}

	@Test
	public void normalizeTest() {
		// https://blog.csdn.net/oscar999/article/details/105326270

		String str1 = "\u00C1";
		String str2 = "\u0041\u0301";

		assertNotEquals(str1, str2);

		str1 = CharSequenceUtil.normalize(str1);
		str2 = CharSequenceUtil.normalize(str2);
		assertEquals(str1, str2);
	}

	@Test
	public void indexOfTest() {
		int index = CharSequenceUtil.indexOf("abc123", '1');
		assertEquals(3, index);
		index = CharSequenceUtil.indexOf("abc123", '3');
		assertEquals(5, index);
		index = CharSequenceUtil.indexOf("abc123", 'a');
		assertEquals(0, index);
	}

	@Test
	public void indexOfTest2() {
		int index = CharSequenceUtil.indexOf("abc123", '1', 0, 3);
		assertEquals(-1, index);

		index = CharSequenceUtil.indexOf("abc123", 'b', 0, 3);
		assertEquals(1, index);
	}

	@Test
	public void subPreGbkTest() {
		// https://gitee.com/dromara/hutool/issues/I4JO2E
		String s = "华硕K42Intel酷睿i31代2G以下独立显卡不含机械硬盘固态硬盘120GB-192GB4GB-6GB";

		String v = CharSequenceUtil.subPreGbk(s, 40, false);
		assertEquals(39, v.getBytes(CharsetUtil.CHARSET_GBK).length);

		v = CharSequenceUtil.subPreGbk(s, 40, true);
		assertEquals(41, v.getBytes(CharsetUtil.CHARSET_GBK).length);
	}

	@Test
	public void startWithTest() {
		// https://gitee.com/dromara/hutool/issues/I4MV7Q
		assertFalse(CharSequenceUtil.startWith("123", "123", false, true));
		assertFalse(CharSequenceUtil.startWith(null, null, false, true));
		assertFalse(CharSequenceUtil.startWith("abc", "abc", true, true));

		assertTrue(CharSequenceUtil.startWithIgnoreCase(null, null));
		assertFalse(CharSequenceUtil.startWithIgnoreCase(null, "abc"));
		assertFalse(CharSequenceUtil.startWithIgnoreCase("abcdef", null));
		assertTrue(CharSequenceUtil.startWithIgnoreCase("abcdef", "abc"));
		assertTrue(CharSequenceUtil.startWithIgnoreCase("ABCDEF", "abc"));
	}

	@Test
	public void endWithTest() {
		assertFalse(CharSequenceUtil.endWith("123", "123", false, true));
		assertFalse(CharSequenceUtil.endWith(null, null, false, true));
		assertFalse(CharSequenceUtil.endWith("abc", "abc", true, true));

		assertTrue(CharSequenceUtil.endWithIgnoreCase(null, null));
		assertFalse(CharSequenceUtil.endWithIgnoreCase(null, "abc"));
		assertFalse(CharSequenceUtil.endWithIgnoreCase("abcdef", null));
		assertTrue(CharSequenceUtil.endWithIgnoreCase("abcdef", "def"));
		assertTrue(CharSequenceUtil.endWithIgnoreCase("ABCDEF", "def"));
	}

	@Test
	public void removePrefixIgnoreCaseTest(){
		assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "abc"));
		assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABC"));
		assertEquals("de", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "Abc"));
		assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", ""));
		assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", null));
		assertEquals("", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCde"));
		assertEquals("ABCde", CharSequenceUtil.removePrefixIgnoreCase("ABCde", "ABCdef"));
		assertNull(CharSequenceUtil.removePrefixIgnoreCase(null, "ABCdef"));
	}

	@Test
	public void removeSuffixIgnoreCaseTest(){
		assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "cde"));
		assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "CDE"));
		assertEquals("AB", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "Cde"));
		assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", ""));
		assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", null));
		assertEquals("", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCde"));
		assertEquals("ABCde", CharSequenceUtil.removeSuffixIgnoreCase("ABCde", "ABCdef"));
		assertNull(CharSequenceUtil.removeSuffixIgnoreCase(null, "ABCdef"));
	}

	@Test
	public void trimToNullTest(){
		String a = "  ";
		assertNull(CharSequenceUtil.trimToNull(a));

		a = "";
		assertNull(CharSequenceUtil.trimToNull(a));

		a = null;
		assertNull(CharSequenceUtil.trimToNull(a));
	}

	@Test
	public void commonPrefixTest() throws Exception{

		// -------------------------- None match -----------------------

		assertEquals("", CharSequenceUtil.commonPrefix("", "abc"));
		assertEquals("", CharSequenceUtil.commonPrefix(null, "abc"));
		assertEquals("", CharSequenceUtil.commonPrefix("abc", null));
		assertEquals("", CharSequenceUtil.commonPrefix("abc", ""));

		assertEquals("", CharSequenceUtil.commonPrefix("azzzj", "bzzzj"));

		assertEquals("", CharSequenceUtil.commonPrefix("english中文", "french中文"));

		// -------------------------- Matched -----------------------

		assertEquals("name_", CharSequenceUtil.commonPrefix("name_abc", "name_efg"));

		assertEquals("zzzj", CharSequenceUtil.commonPrefix("zzzja", "zzzjb"));

		assertEquals("中文", CharSequenceUtil.commonPrefix("中文english", "中文french"));

		// { space * 10 } + "abc"
		final String str1 = CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 10) + "abc";

		// { space * 5 } + "efg"
		final String str2 = CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 5) + "efg";

		// Expect common prefix: { space * 5 }
		assertEquals(CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 5), CharSequenceUtil.commonPrefix(str1, str2));
	}

	@Test
	public void commonSuffixTest() throws Exception{

		// -------------------------- None match -----------------------

		assertEquals("", CharSequenceUtil.commonSuffix("", "abc"));
		assertEquals("", CharSequenceUtil.commonSuffix(null, "abc"));
		assertEquals("", CharSequenceUtil.commonSuffix("abc", null));
		assertEquals("", CharSequenceUtil.commonSuffix("abc", ""));

		assertEquals("", CharSequenceUtil.commonSuffix("zzzja", "zzzjb"));

		assertEquals("", CharSequenceUtil.commonSuffix("中文english", "中文Korean"));

		// -------------------------- Matched -----------------------

		assertEquals("_name", CharSequenceUtil.commonSuffix("abc_name", "efg_name"));

		assertEquals("zzzj", CharSequenceUtil.commonSuffix("abczzzj", "efgzzzj"));

		assertEquals("中文", CharSequenceUtil.commonSuffix("english中文", "Korean中文"));

		// "abc" + { space * 10 }
		final String str1 = "abc" + CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 10);

		// "efg" + { space * 15 }
		final String str2 = "efg" + CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 15);

		// Expect common suffix: { space * 10 }
		assertEquals(CharSequenceUtil.repeat(CharSequenceUtil.SPACE, 10), CharSequenceUtil.commonSuffix(str1, str2));
	}

	@Test
	public void testContainsOnly() {
		// 测试空字符串
		assertTrue(CharSequenceUtil.containsOnly("", 'a', 'b'));

		// 测试字符串只包含testChars中的字符
		assertTrue(CharSequenceUtil.containsOnly("asdf", 'a', 's', 'd', 'f'));

		// 测试字符串包含testChars中的字符和其它字符
		assertFalse(CharSequenceUtil.containsOnly("asdf123", 'a', 's', 'd', 'f'));

		// 测试字符串不包含testChars中的任何字符
		assertFalse(CharSequenceUtil.containsOnly("hello", 'a', 'b'));

		// 测试字符串为null
		assertTrue(CharSequenceUtil.containsOnly(null, 'a', 'b'));
	}

	@Test
	public void removeAllPrefixTest() {
		final String prefix = "ab";

		String str = "ababcdef";
		String result = CharSequenceUtil.removeAllPrefix(str, prefix);
		assertEquals("cdef", result);

		str = "abcdef";
		result = CharSequenceUtil.removeAllPrefix(str, prefix);
		assertEquals("cdef", result);

		str = "cdef";
		result = CharSequenceUtil.removeAllPrefix(str, prefix);
		assertEquals("cdef", result);

		str = "";
		result = CharSequenceUtil.removeAllPrefix(str, prefix);
		assertEquals("", result);

		str = null;
		result = CharSequenceUtil.removeAllPrefix(str, prefix);
		assertNull(result);
	}

	@Test
	public void removeAllSuffixTest() {
		final String prefix = "ab";

		String str = "cdefabab";
		String result = CharSequenceUtil.removeAllSuffix(str, prefix);
		assertEquals("cdef", result);

		str = "cdefab";
		result = CharSequenceUtil.removeAllSuffix(str, prefix);
		assertEquals("cdef", result);

		str = "cdef";
		result = CharSequenceUtil.removeAllSuffix(str, prefix);
		assertEquals("cdef", result);

		str = "";
		result = CharSequenceUtil.removeAllSuffix(str, prefix);
		assertEquals("", result);

		str = null;
		result = CharSequenceUtil.removeAllSuffix(str, prefix);
		assertNull(result);
	}

	@Test
	public void stripIgnoreCaseTest() {

		final String SOURCE_STRING = "aaa_STRIPPED_bbb";

		// ---------------------------- test strip ----------------------------

		// Normal test
		assertEquals("aa_STRIPPED_bbb", CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, "a"));
		assertEquals(SOURCE_STRING, CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, ""));
		assertEquals("aa_STRIPPED_bb", CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, "A", "b"));

		// test null param
		assertEquals(SOURCE_STRING, CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, null, null));
		assertEquals(SOURCE_STRING, CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, "", ""));
		assertEquals("aaa_STRIPPED_bb", CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, "", "B"));
		assertEquals("aaa_STRIPPED_bb", CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, null, "b"));
		assertEquals("aa_STRIPPED_bbb", CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, "a", ""));
		assertEquals("aa_STRIPPED_bbb", CharSequenceUtil.stripIgnoreCase(SOURCE_STRING, "a", null));
		// 本次提交前无法通过的 case
		assertEquals("", CharSequenceUtil.stripIgnoreCase("a", "a", "a"));

		// 前缀后缀有重叠，优先去掉前缀
		assertEquals("a", CharSequenceUtil.stripIgnoreCase("aba", "aB", "bB"));
	}

	@Test
	public void stripTest() {

		final String SOURCE_STRING = "aaa_STRIPPED_bbb";

		// ---------------------------- test strip ----------------------------

		// Normal test
		assertEquals("aa_STRIPPED_bbb", CharSequenceUtil.strip(SOURCE_STRING, "a"));
		assertEquals(SOURCE_STRING, CharSequenceUtil.strip(SOURCE_STRING, ""));
		assertEquals("aa_STRIPPED_bb", CharSequenceUtil.strip(SOURCE_STRING, "a", "b"));

		// test null param
		assertEquals(SOURCE_STRING, CharSequenceUtil.strip(SOURCE_STRING, null, null));
		assertEquals(SOURCE_STRING, CharSequenceUtil.strip(SOURCE_STRING, "", ""));
		assertEquals("aaa_STRIPPED_bb", CharSequenceUtil.strip(SOURCE_STRING, "", "b"));
		assertEquals("aaa_STRIPPED_bb", CharSequenceUtil.strip(SOURCE_STRING, null, "b"));
		assertEquals("aa_STRIPPED_bbb", CharSequenceUtil.strip(SOURCE_STRING, "a", ""));
		assertEquals("aa_STRIPPED_bbb", CharSequenceUtil.strip(SOURCE_STRING, "a", null));
		// 本次提交前无法通过的 case
		assertEquals("", CharSequenceUtil.strip("a", "a", "a"));

		// 前缀后缀有重叠，优先去掉前缀
		assertEquals("a", CharSequenceUtil.strip("aba", "ab", "ba"));
	}

	@Test
	public void stripAllTest() {
		final String SOURCE_STRING = "aaa_STRIPPED_bbb";

		// ---------------------------- test stripAll ----------------------------

		// Normal test
		assertEquals("_STRIPPED_bbb", CharSequenceUtil.stripAll(SOURCE_STRING, "a"));
		assertEquals(SOURCE_STRING, CharSequenceUtil.stripAll(SOURCE_STRING, ""));

		// test null param
		assertEquals("_STRIPPED_", CharSequenceUtil.stripAll(SOURCE_STRING, "a", "b"));
		assertEquals(SOURCE_STRING, CharSequenceUtil.stripAll(SOURCE_STRING, null, null));
		assertEquals(SOURCE_STRING, CharSequenceUtil.stripAll(SOURCE_STRING, "", ""));
		assertEquals("aaa_STRIPPED_", CharSequenceUtil.stripAll(SOURCE_STRING, "", "b"));
		assertEquals("aaa_STRIPPED_", CharSequenceUtil.stripAll(SOURCE_STRING, null, "b"));
		assertEquals("_STRIPPED_bbb", CharSequenceUtil.stripAll(SOURCE_STRING, "a", ""));
		assertEquals("_STRIPPED_bbb", CharSequenceUtil.stripAll(SOURCE_STRING, "a", null));

		// special test
		assertEquals("bbb", CharSequenceUtil.stripAll("aaaaaabbb", "aaa", null));
		assertEquals("abbb", CharSequenceUtil.stripAll("aaaaaaabbb", "aa", null));

		// aaaaaaaaa (9个a) 可以被看为 aaa_aaaa_aa
		assertEquals("", CharSequenceUtil.stripAll("aaaaaaaaa", "aaa", "aa"));
		// 第二次迭代后会出现 from 比 to 大的情况，原本代码是强行交换，但是回导致无法去除前后缀
		assertEquals("", CharSequenceUtil.stripAll("a", "a", "a"));

		// 前缀后缀有重叠，优先去掉前缀
		assertEquals("a", CharSequenceUtil.stripAll("aba", "ab", "ba"));
		assertEquals("a", CharSequenceUtil.stripAll("abababa", "ab", "ba"));
	}
}
