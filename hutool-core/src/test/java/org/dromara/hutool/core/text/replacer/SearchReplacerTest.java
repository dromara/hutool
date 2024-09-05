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

package org.dromara.hutool.core.text.replacer;

import org.dromara.hutool.core.text.CharSequenceUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchReplacerTest {

	@Test
	public void replaceOnlyTest() {
		final String result = CharSequenceUtil.replace(",", ",", "|");
		Assertions.assertEquals("|", result);
	}

	@Test
	public void replaceTestAtBeginAndEnd() {
		final String result = CharSequenceUtil.replace(",abcdef,", ",", "|");
		Assertions.assertEquals("|abcdef|", result);
	}

	@Test
	public void replaceTest() {
		final String str = "AAABBCCCBBDDDBB";
		String replace = StrUtil.replace(str, 0, "BB", "22", false);
		Assertions.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 3, "BB", "22", false);
		Assertions.assertEquals("AAA22CCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "BB", "22", false);
		Assertions.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "22", true);
		Assertions.assertEquals("AAABBCCC22DDD22", replace);

		replace = StrUtil.replace(str, 4, "bb", "", true);
		Assertions.assertEquals("AAABBCCCDDD", replace);

		replace = StrUtil.replace(str, 4, "bb", null, true);
		Assertions.assertEquals("AAABBCCCDDD", replace);
	}
}
