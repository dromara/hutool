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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrStripperTest {

	@Test
	void stripAllPrefixTest() {
		final StrStripper prefixStripper = new StrStripper("a", null, false, true);
		assertEquals("_STRIPPED_bbb", prefixStripper.apply("aaa_STRIPPED_bbb"));
		// 不忽略大小写则不替换
		assertEquals("AAA_STRIPPED_bbb", prefixStripper.apply("AAA_STRIPPED_bbb"));
	}

	@Test
	void stripAllSuffixTest() {
		final StrStripper suffixStripper = new StrStripper(null, "b", false, true);
		assertEquals("aaa_STRIPPED_", suffixStripper.apply("aaa_STRIPPED_bbb"));
		// 不忽略大小写则不替换
		assertEquals("aaa_STRIPPED_BBB", suffixStripper.apply("aaa_STRIPPED_BBB"));
	}

	@Test
	void stripAllPrefixIgnoreCaseTest() {
		final StrStripper prefixStripper = new StrStripper("A", null, true, true);
		assertEquals("_STRIPPED_bbb", prefixStripper.apply("aaa_STRIPPED_bbb"));
		assertEquals("_STRIPPED_bbb", prefixStripper.apply("Aaa_STRIPPED_bbb"));
		assertEquals("_STRIPPED_bbb", prefixStripper.apply("AAA_STRIPPED_bbb"));
		assertEquals("_STRIPPED_bbb", prefixStripper.apply("aaA_STRIPPED_bbb"));
	}

	@Test
	void stripAllSuffixIgnoreCaseTest() {
		final StrStripper prefixStripper = new StrStripper(null, "B", true, true);
		assertEquals("aaa_STRIPPED_", prefixStripper.apply("aaa_STRIPPED_bbb"));
		assertEquals("aaa_STRIPPED_", prefixStripper.apply("aaa_STRIPPED_Bbb"));
		assertEquals("aaa_STRIPPED_", prefixStripper.apply("aaa_STRIPPED_BBB"));
		assertEquals("aaa_STRIPPED_", prefixStripper.apply("aaa_STRIPPED_bbB"));
	}
}

