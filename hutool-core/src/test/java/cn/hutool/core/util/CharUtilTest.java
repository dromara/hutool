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

	@Test
	public void isCharTest(){
		char a = 'a';
		Assert.assertTrue(CharUtil.isChar(a));
	}

	@Test
	public void isBlankCharTest(){
		char a = '\u00A0';
		Assert.assertTrue(CharUtil.isBlankChar(a));

		char a2 = '\u0020';
		Assert.assertTrue(CharUtil.isBlankChar(a2));

		char a3 = '\u3000';
		Assert.assertTrue(CharUtil.isBlankChar(a3));

		char a4 = '\u0000';
		Assert.assertTrue(CharUtil.isBlankChar(a4));
	}

	@Test
	public void toCloseCharTest(){
		Assert.assertEquals('②', CharUtil.toCloseChar('2'));
		Assert.assertEquals('Ⓜ', CharUtil.toCloseChar('M'));
		Assert.assertEquals('ⓡ', CharUtil.toCloseChar('r'));
	}

	@Test
	public void toCloseByNumberTest(){
		Assert.assertEquals('②', CharUtil.toCloseByNumber(2));
		Assert.assertEquals('⑫', CharUtil.toCloseByNumber(12));
		Assert.assertEquals('⑳', CharUtil.toCloseByNumber(20));
	}
}
