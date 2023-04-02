package org.dromara.hutool.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrCheckerTest {

	@Test
	public void isEmptyTest() {
		Assertions.assertTrue(StrUtil.isEmpty(null));
		Assertions.assertTrue(StrUtil.isEmpty(""));

		Assertions.assertFalse(StrUtil.isEmpty(" \t\n"));
		Assertions.assertFalse(StrUtil.isEmpty("abc"));
	}

	@Test
	public void isNotEmptyTest() {
		Assertions.assertFalse(StrUtil.isNotEmpty(null));
		Assertions.assertFalse(StrUtil.isNotEmpty(""));

		Assertions.assertTrue(StrUtil.isNotEmpty(" \t\n"));
		Assertions.assertTrue(StrUtil.isNotEmpty("abc"));
	}

	@Test
	public void isBlankTest() {
		Assertions.assertTrue(StrUtil.isBlank(null));
		Assertions.assertTrue(StrUtil.isBlank(""));
		Assertions.assertTrue(StrUtil.isBlank(" \t\n"));

		Assertions.assertFalse(StrUtil.isBlank("abc"));
	}

	@Test
	public void isNotBlankTest() {
		Assertions.assertFalse(StrUtil.isNotBlank(null));
		Assertions.assertFalse(StrUtil.isNotBlank(""));
		Assertions.assertFalse(StrUtil.isNotBlank(" \t\n"));

		Assertions.assertTrue(StrUtil.isNotBlank("abc"));
	}
}
