package cn.hutool.core.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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

		assertEquals(str, decodedStr);
	}

	@Test
	public void issueI50MI6Test(){
		final String s = HexUtil.encodeHexStr("烟".getBytes(StandardCharsets.UTF_16BE));
		assertEquals("70df", s);
	}

	@Test
	public void toUnicodeHexTest() {
		String unicodeHex = HexUtil.toUnicodeHex('\u2001');
		assertEquals("\\u2001", unicodeHex);

		unicodeHex = HexUtil.toUnicodeHex('你');
		assertEquals("\\u4f60", unicodeHex);
	}

	@Test
	public void isHexNumberTest() {
		assertTrue(HexUtil.isHexNumber("0"));
		assertTrue(HexUtil.isHexNumber("002c"));

		String a = "0x3544534F444";
		assertTrue(HexUtil.isHexNumber(a));

		// https://gitee.com/dromara/hutool/issues/I62H7K
		a = "0x0000000000000001158e460913d00000";
		assertTrue(HexUtil.isHexNumber(a));

		// 错误的
		a = "0x0000001000T00001158e460913d00000";
		assertFalse(HexUtil.isHexNumber(a));

		// 错误的,https://github.com/dromara/hutool/issues/2857
		a = "-1";
		assertFalse(HexUtil.isHexNumber(a));
	}

	@Test
	public void decodeTest(){
		final String str = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		assertArrayEquals(HexUtil.decodeHex(str),
				HexUtil.decodeHex(str.toUpperCase()));
	}

	@Test
	public void formatHexTest(){
		final String hex = "e8c670380cb220095268f40221fc748fa6ac39d6e930e63c30da68bad97f885d";
		final String formatHex = HexUtil.format(hex);
		assertEquals("e8 c6 70 38 0c b2 20 09 52 68 f4 02 21 fc 74 8f a6 ac 39 d6 e9 30 e6 3c 30 da 68 ba d9 7f 88 5d", formatHex);
	}

	@Test
	public void formatHexTest2(){
		final String hex = "e8c670380cb220095268f40221fc748fa6";
		final String formatHex = HexUtil.format(hex, "0x");
		assertEquals("0xe8 0xc6 0x70 0x38 0x0c 0xb2 0x20 0x09 0x52 0x68 0xf4 0x02 0x21 0xfc 0x74 0x8f 0xa6", formatHex);
	}

	@Test
	public void decodeHexTest(){
		final String s = HexUtil.encodeHexStr("6");
		final String s1 = HexUtil.decodeHexStr(s);
		assertEquals("6", s1);
	}
}
