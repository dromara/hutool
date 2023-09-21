/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
