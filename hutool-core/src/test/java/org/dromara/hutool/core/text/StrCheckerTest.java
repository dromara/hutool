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
