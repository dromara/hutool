package cn.hutool.extra.emoji;

import org.junit.Assert;
import org.junit.Test;

public class EmojiUtilTest {

	@Test
	public void toUnicodeTest() {
		final String emoji = EmojiUtil.toUnicode(":smile:");
		Assert.assertEquals("😄", emoji);
	}

	@Test
	public void toAliasTest() {
		final String alias = EmojiUtil.toAlias("😄");
		Assert.assertEquals(":smile:", alias);
	}

	@Test
	public void containsEmojiTest() {
		final boolean containsEmoji = EmojiUtil.containsEmoji("测试一下是否包含EMOJ:😄");
		Assert.assertTrue(containsEmoji);
		final boolean notContainsEmoji = EmojiUtil.containsEmoji("不包含EMOJ:^_^");
		Assert.assertFalse(notContainsEmoji);

	}
}
