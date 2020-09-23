package cn.hutool.extra.emoji;

import org.junit.Assert;
import org.junit.Test;

public class EmojiUtilTest {
	
	@Test
	public void toUnicodeTest() {
		String emoji = EmojiUtil.toUnicode(":smile:");
		Assert.assertEquals("😄", emoji);
	}
	
	@Test
	public void toAliasTest() {
		String alias = EmojiUtil.toAlias("😄");
		Assert.assertEquals(":smile:", alias);
	}
	
	@Test
	public void containsEmojiTest() {
		boolean containsEmoji = EmojiUtil.containsEmoji("测试一下是否包含EMOJ:😄");
		Assert.assertEquals(containsEmoji, true);
		boolean notContainsEmoji = EmojiUtil.containsEmoji("不包含EMOJ:^_^");
		Assert.assertEquals(notContainsEmoji, false);

	}
}
