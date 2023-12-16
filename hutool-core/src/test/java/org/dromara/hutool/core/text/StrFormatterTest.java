/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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

import org.dromara.hutool.core.text.placeholder.StrFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrFormatterTest {

	@Test
	public void formatTest() {
		//通常使用
		final String result1 = StrFormatter.format("this is {} for {}", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义{}
		final String result2 = StrFormatter.format("this is \\{} for {}", "a", "b");
		Assertions.assertEquals("this is {} for a", result2);

		//转义\
		final String result3 = StrFormatter.format("this is \\\\{} for {}", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}

	@Test
	public void formatWithTest() {
		//通常使用
		final String result1 = StrFormatter.formatWith("this is ? for ?", "?", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义?
		final String result2 = StrFormatter.formatWith("this is \\? for ?", "?", "a", "b");
		Assertions.assertEquals("this is ? for a", result2);

		//转义\
		final String result3 = StrFormatter.formatWith("this is \\\\? for ?", "?", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}

	@Test
	public void formatWithTest2() {
		//通常使用
		final String result1 = StrFormatter.formatWith("this is $$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is a for b", result1);

		//转义?
		final String result2 = StrFormatter.formatWith("this is \\$$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is $$$ for a", result2);

		//转义\
		final String result3 = StrFormatter.formatWith("this is \\\\$$$ for $$$", "$$$", "a", "b");
		Assertions.assertEquals("this is \\a for b", result3);
	}
}
