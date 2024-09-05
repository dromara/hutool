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
