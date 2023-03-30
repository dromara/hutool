package cn.hutool.extra.emoji;

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
