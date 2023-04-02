package org.dromara.hutool.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharUtilTest {

	@Test
	public void trimTest() {
		//此字符串中的第一个字符为不可见字符: '\u202a'
		final String str = "‪C:/Users/maple/Desktop/tone.txt";
		Assertions.assertEquals('\u202a', str.charAt(0));
		Assertions.assertTrue(CharUtil.isBlankChar(str.charAt(0)));
	}

	@Test
	public void isEmojiTest() {
		final String a = "莉🌹";
		Assertions.assertFalse(CharUtil.isEmoji(a.charAt(0)));
		Assertions.assertTrue(CharUtil.isEmoji(a.charAt(1)));

	}

	@Test
	public void isCharTest(){
		final char a = 'a';
		Assertions.assertTrue(CharUtil.isChar(a));
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Test
	public void isBlankCharTest(){
		final char a = '\u00A0';
		Assertions.assertTrue(CharUtil.isBlankChar(a));

		final char a2 = '\u0020';
		Assertions.assertTrue(CharUtil.isBlankChar(a2));

		final char a3 = '\u3000';
		Assertions.assertTrue(CharUtil.isBlankChar(a3));

		final char a4 = '\u0000';
		Assertions.assertTrue(CharUtil.isBlankChar(a4));
	}

	@Test
	public void toCloseCharTest(){
		Assertions.assertEquals('②', CharUtil.toCloseChar('2'));
		Assertions.assertEquals('Ⓜ', CharUtil.toCloseChar('M'));
		Assertions.assertEquals('ⓡ', CharUtil.toCloseChar('r'));
	}

	@Test
	public void toCloseByNumberTest(){
		Assertions.assertEquals('②', CharUtil.toCloseByNumber(2));
		Assertions.assertEquals('⑫', CharUtil.toCloseByNumber(12));
		Assertions.assertEquals('⑳', CharUtil.toCloseByNumber(20));
	}

	@SuppressWarnings("UnnecessaryUnicodeEscape")
	@Test
	public void issueI5UGSQTest(){
		char c = '\u3164';
		Assertions.assertTrue(CharUtil.isBlankChar(c));

		c = '\u2800';
		Assertions.assertTrue(CharUtil.isBlankChar(c));
	}
}
