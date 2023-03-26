package cn.hutool.core.text;

import org.junit.Assert;
import org.junit.Test;

public class StrRegionMatcherTest {
	@Test
	public void matchPrefixTest() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, true);
		final boolean test = matcher.test("abcdef", "ab");
		Assert.assertTrue(test);
	}

	@Test
	public void matchSuffixTest() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, false);
		final boolean test = matcher.test("abcdef", "ef");
		Assert.assertTrue(test);
	}

	@Test
	public void matchOffsetTest1() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, 1);
		final boolean test = matcher.test("abcdef", "bc");
		Assert.assertTrue(test);
	}

	@Test
	public void matchOffsetTest2() {
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, -2);
		final boolean test = matcher.test("abcdef", "de");
		Assert.assertTrue(test);
	}

	@Test
	public void matchOffsetTest3() {
		// 部分越界
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, 5);
		final boolean test = matcher.test("abcdef", "de");
		Assert.assertFalse(test);
	}

	@Test
	public void matchOffsetTest4() {
		// 完全越界
		final StrRegionMatcher matcher = new StrRegionMatcher(
				false, false, 6);
		final boolean test = matcher.test("abcdef", "de");
		Assert.assertFalse(test);
	}
}
