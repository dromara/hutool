package cn.hutool.extra.emoji;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmojiUtilTest {

	@Test
	public void toUnicodeTest() {
		String emoji = EmojiUtil.toUnicode(":smile:");
		Assertions.assertEquals("😄", emoji);
	}

	@Test
	public void toAliasTest() {
		String alias = EmojiUtil.toAlias("😄");
		Assertions.assertEquals(":smile:", alias);
	}

	@Test
	public void containsEmojiTest() {
		boolean containsEmoji = EmojiUtil.containsEmoji("测试一下是否包含EMOJ:😄");
		Assertions.assertTrue(containsEmoji);
		boolean notContainsEmoji = EmojiUtil.containsEmoji("不包含EMOJ:^_^");
		Assertions.assertFalse(notContainsEmoji);

	}
}
