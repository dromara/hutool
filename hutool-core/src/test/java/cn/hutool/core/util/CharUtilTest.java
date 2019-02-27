package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

public class CharUtilTest {
	
	@Test
	public void trimTest() {
		//此字符串中的第一个字符为不可见字符: '\u202a'
		String str = "‪C:/Users/maple/Desktop/tone.txt";
		Assert.assertEquals('\u202a', str.charAt(0));
		Assert.assertTrue(CharUtil.isBlankChar(str.charAt(0)));
	}
	
	@Test
	public void isEmojiTest() {
		String a = "莉🌹";
		Assert.assertFalse(CharUtil.isEmoji(a.charAt(0)));
		Assert.assertTrue(CharUtil.isEmoji(a.charAt(1)));
		
	}
}
