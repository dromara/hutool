package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class StrUtilTest {
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

	}
}
