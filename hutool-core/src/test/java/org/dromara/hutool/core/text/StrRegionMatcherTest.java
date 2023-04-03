package org.dromara.hutool.core.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrRegionMatcherTest {
	@Test
	public void matchPrefixTest() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, true);
		final boolean test = matcher.test("abcdef", "ab");
		Assertions.assertTrue(test);
	}

	@Test
	public void matchSuffixTest() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, false);
		final boolean test = matcher.test("abcdef", "ef");
		Assertions.assertTrue(test);
	}

	@Test
	public void matchOffsetTest1() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, 1);
		final boolean test = matcher.test("abcdef", "bc");
		Assertions.assertTrue(test);
	}

	@Test
	public void matchOffsetTest2() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, -2);
		final boolean test = matcher.test("abcdef", "de");
		Assertions.assertTrue(test);
	}

	@Test
	public void matchOffsetTest3() {
		// 部分越界
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, 5);
		final boolean test = matcher.test("abcdef", "de");
		Assertions.assertFalse(test);
	}

	@Test
	public void matchOffsetTest4() {
		// 完全越界
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, 6);
		final boolean test = matcher.test("abcdef", "de");
		Assertions.assertFalse(test);
	}
}
