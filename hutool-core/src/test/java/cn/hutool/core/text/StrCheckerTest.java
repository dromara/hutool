package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

public class StrCheckerTest {

	@Test
	public void isEmptyTest() {
		Assert.assertTrue(StrUtil.isEmpty(null));
		Assert.assertTrue(StrUtil.isEmpty(""));

		Assert.assertFalse(StrUtil.isEmpty(" \t\n"));
		Assert.assertFalse(StrUtil.isEmpty("abc"));
	}

	@Test
	public void isNotEmptyTest() {
		Assert.assertFalse(StrUtil.isNotEmpty(null));
		Assert.assertFalse(StrUtil.isNotEmpty(""));

		Assert.assertTrue(StrUtil.isNotEmpty(" \t\n"));
		Assert.assertTrue(StrUtil.isNotEmpty("abc"));
	}

	@Test
	public void isBlankTest() {
		Assert.assertTrue(StrUtil.isBlank(null));
		Assert.assertTrue(StrUtil.isBlank(""));
		Assert.assertTrue(StrUtil.isBlank(" \t\n"));

		Assert.assertFalse(StrUtil.isBlank("abc"));
	}

	@Test
	public void isNotBlankTest() {
		Assert.assertFalse(StrUtil.isNotBlank(null));
		Assert.assertFalse(StrUtil.isNotBlank(""));
		Assert.assertFalse(StrUtil.isNotBlank(" \t\n"));

		Assert.assertTrue(StrUtil.isNotBlank("abc"));
	}
}
