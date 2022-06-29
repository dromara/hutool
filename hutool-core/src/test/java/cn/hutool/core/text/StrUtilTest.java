package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class StrUtilTest {
	@Test
	public void testReplace2() {
		// https://gitee.com/dromara/hutool/issues/I4M16G
		final String replace = "#{A}";
		final String result = StrUtil.replace(replace, "#{AAAAAAA}", "1");
		Assert.assertEquals(replace, result);
	}

	@Test
	public void testReplaceByStr() {
		final String replace = "SSM15930297701BeryAllen";
		final String result = StrUtil.replace(replace, 5, 12, "***");
		Assert.assertEquals("SSM15***01BeryAllen", result);
	}

	@Test
	public void testAddPrefixIfNot() {
		final String str = "hutool";
		String result = StrUtil.addPrefixIfNot(str, "hu");
		Assert.assertEquals(str, result);

		result = StrUtil.addPrefixIfNot(str, "Good");
		Assert.assertEquals("Good" + str, result);
	}

	@Test
	public void testAddSuffixIfNot() {
		final String str = "hutool";
		String result = StrUtil.addSuffixIfNot(str, "tool");
		Assert.assertEquals(str, result);

		result = StrUtil.addSuffixIfNot(str, " is Good");
		Assert.assertEquals(str + " is Good", result);

		result = StrUtil.addSuffixIfNot("", "/");
		Assert.assertEquals("/", result);
	}

	@Test
	public void testIssAllBlank() {
		List<String> queue = new LinkedList<>();
		queue.add("apple");
		queue.add("banana");
		queue.add("cherry");
		queue.add("orange");
		queue.add("strawberry");
		queue.add("watermelon");
		Assert.assertFalse(StrUtil.isAllBlank(queue));

		Assert.assertTrue(CharSequenceUtil.isAllBlank(""));
		Assert.assertTrue(CharSequenceUtil.isAllBlank(" "));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\t"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\n"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\r"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\f"));
		Assert.assertFalse(CharSequenceUtil.isAllBlank("\b"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u00A0"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\uFEFF"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2000"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2001"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2002"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2003"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2004"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2005"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2006"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2007"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2008"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2009"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u200A"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u3000"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\uFEFF"));

		// 其他空白字符
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u000B"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u000C"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u00A0"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u1680"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u180E"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2000"));
		Assert.assertTrue(CharSequenceUtil.isAllBlank("\u2001"));
	}
}
