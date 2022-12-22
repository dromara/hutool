package cn.hutool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * HexUtil单元测试
 * @author Looly
 *
 */
public class HexUtilTest {

	@Test
	public void hexStrTest(){
		final String str = "我是一个字符串";

		final String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);
		final String decodedStr = HexUtil.decodeHexStr(hex);

		Assert.assertEquals(str, decodedStr);
	}

	@Test
	public void issueI50MI6Test(){
		final String s = HexUtil.encodeHexStr("烟".getBytes(StandardCharsets.UTF_16BE));
		Assert.assertEquals("70df", s);
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
		Assert.assertTrue(HexUtil.isHexNumber(a));

		// https://gitee.com/dromara/hutool/issues/I62H7K
		a = "0x0000000000000001158e460913d00000";
		Assert.assertTrue(HexUtil.isHexNumber(a));

		// 错误的
		a = "0x0000001000T00001158e460913d00000";
		Assert.assertFalse(HexUtil.isHexNumber(a));
	}

	@Test
	public void decodeTest(){
		final String str = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		Assert.assertArrayEquals(HexUtil.decodeHex(str),
				HexUtil.decodeHex(str.toUpperCase()));
	}

	@Test
	public void formatHexTest(){
		final String hex = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		final String formatHex = HexUtil.format(hex);
		Assert.assertEquals("e8 c6 70 38 0c b2 20 09 52 68 f4 02 21 fc 74 8f a6 ac 39 d6 e9 30 e6 3c 30 da 68 ba d9 7f 88 5d", formatHex);
	}

	@Test
	public void decodeHexTest(){
		final String s = HexUtil.encodeHexStr("6");
		final String s1 = HexUtil.decodeHexStr(s);
		Assert.assertEquals("6", s1);
	}
}
