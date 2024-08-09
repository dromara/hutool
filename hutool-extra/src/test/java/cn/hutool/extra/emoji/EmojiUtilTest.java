package cn.hutool.extra.emoji;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EmojiUtilTest {

	@Test
	public void toUnicodeTest() {
		String emoji = EmojiUtil.toUnicode(":smile:");
		assertEquals("😄", emoji);
	}

	@Test
	public void toAliasTest() {
		String alias = EmojiUtil.toAlias("😄");
		assertEquals(":smile:", alias);
	}

	@Test
	public void containsEmojiTest() {
		boolean containsEmoji = EmojiUtil.containsEmoji("测试一下是否包含EMOJ:😄");
		assertTrue(containsEmoji);
		boolean notContainsEmoji = EmojiUtil.containsEmoji("不包含EMOJ:^_^");
		assertFalse(notContainsEmoji);

	}
}
