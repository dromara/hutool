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

package org.dromara.hutool.extra.emoji;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmojiUtilTest {

	@Test
	public void toUnicodeTest() {
		final String emoji = EmojiUtil.toUnicode(":smile:");
		Assertions.assertEquals("😄", emoji);
	}

	@Test
	public void toAliasTest() {
		final String alias = EmojiUtil.toAlias("😄");
		Assertions.assertEquals(":smile:", alias);
	}

	@Test
	public void containsEmojiTest() {
		final boolean containsEmoji = EmojiUtil.containsEmoji("测试一下是否包含EMOJ:😄");
		Assertions.assertTrue(containsEmoji);
		final boolean notContainsEmoji = EmojiUtil.containsEmoji("不包含EMOJ:^_^");
		Assertions.assertFalse(notContainsEmoji);

	}
}
