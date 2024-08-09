package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CharUtilTest {

	@Test
	public void trimTest() {
		//此字符串中的第一个字符为不可见字符: '\u202a'
		final String str = "‪C:/Users/maple/Desktop/tone.txt";
		assertEquals('\u202a', str.charAt(0));
		assertTrue(CharUtil.isBlankChar(str.charAt(0)));
	}

	@Test
	public void isEmojiTest() {
		final String a = "莉🌹";
		assertFalse(CharUtil.isEmoji(a.charAt(0)));
		assertTrue(CharUtil.isEmoji(a.charAt(1)));

	}

	@Test
	public void isCharTest(){
		final char a = 'a';
		assertTrue(CharUtil.isChar(a));
	}

	@Test
	public void isBlankCharTest(){
		final char a = '\u00A0';
		assertTrue(CharUtil.isBlankChar(a));

		final char a2 = '\u0020';
		assertTrue(CharUtil.isBlankChar(a2));

		final char a3 = '\u3000';
		assertTrue(CharUtil.isBlankChar(a3));

		final char a4 = '\u0000';
		assertTrue(CharUtil.isBlankChar(a4));

		final char a5 = ' ';
		assertTrue(CharUtil.isBlankChar(a5));
	}

	@Test
	public void toCloseCharTest(){
		assertEquals('②', CharUtil.toCloseChar('2'));
		assertEquals('Ⓜ', CharUtil.toCloseChar('M'));
		assertEquals('ⓡ', CharUtil.toCloseChar('r'));
	}

	@Test
	public void toCloseByNumberTest(){
		assertEquals('②', CharUtil.toCloseByNumber(2));
		assertEquals('⑫', CharUtil.toCloseByNumber(12));
		assertEquals('⑳', CharUtil.toCloseByNumber(20));
	}

	@Test
	public void issueI5UGSQTest(){
		char c = '\u3164';
		assertTrue(CharUtil.isBlankChar(c));

		c = '\u2800';
		assertTrue(CharUtil.isBlankChar(c));
	}
}
