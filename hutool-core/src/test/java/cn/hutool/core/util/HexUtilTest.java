package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * HexUtil单元测试
 * @author Looly
 *
 */
public class HexUtilTest {

	@Test
	public void hexStrTest(){
		String str = "我是一个字符串";

		String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);
		String decodedStr = HexUtil.decodeHexStr(hex);

		Assert.assertEquals(str, decodedStr);
	}

	@Test
	public void toUnicodeHexTest() {
		String unicodeHex = HexUtil.toUnicodeHex('\u2001');
		Assert.assertEquals("\\u2001", unicodeHex);

		unicodeHex = HexUtil.toUnicodeHex('你');
		Assert.assertEquals("\\u4f60", unicodeHex);
	}

	@Test
	public void isHexNumberTest() {
		String a = "0x3544534F444";
		boolean isHex = HexUtil.isHexNumber(a);
		Assert.assertTrue(isHex);
	}

	@Test
	public void decodeTest(){
		String str = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		Assert.assertArrayEquals(HexUtil.decodeHex(str),
				HexUtil.decodeHex(str.toUpperCase()));
	}

	@Test
	public void formatHexTest(){
		String hex = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		String formatHex = HexUtil.format(hex);
		Assert.assertEquals("e8 c6 70 38 0c b2 20 09 52 68 f4 02 21 fc 74 8f a6 ac 39 d6 e9 30 e6 3c 30 da 68 ba d9 7f 88 5d", formatHex);
	}

	@Test
	public void decodeHexTest(){
		String s = HexUtil.encodeHexStr("6");
		final String s1 = HexUtil.decodeHexStr(s);
		Assert.assertEquals("6", s1);
	}
}
