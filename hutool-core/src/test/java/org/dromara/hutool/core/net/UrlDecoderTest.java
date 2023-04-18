package org.dromara.hutool.core.net;

import org.dromara.hutool.core.net.url.URLDecoder;
import org.dromara.hutool.core.net.url.URLEncoder;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

class UrlDecoderTest {

	@Test
	void decodeForPathTest() {
		Assertions.assertEquals("+", URLDecoder.decodeForPath("+", CharsetUtil.UTF_8));
	}

	@Test
	void issue3063Test() throws UnsupportedEncodingException {
		// https://github.com/dromara/hutool/issues/3063

		final String s = "测试";
		final String expectedDecode = "%FE%FF%6D%4B%8B%D5";

		final String s1 = URLEncoder.encodeAll(s, StandardCharsets.UTF_16);
		Assertions.assertEquals(expectedDecode, s1);
		final String s2 = java.net.URLEncoder.encode(s, "UTF-16");
		Assertions.assertEquals(expectedDecode, s2);

		final String decode = URLDecoder.decode(s1, StandardCharsets.UTF_16);
		Assertions.assertEquals(s, decode);

		// 测试编码字符串和非编码字符串混合
		final String mixDecoded = expectedDecode + "你好";
		final String decode2 = URLDecoder.decode(mixDecoded, StandardCharsets.UTF_16);
		Assertions.assertEquals("测试你好", decode2);

		Assertions.assertEquals(
			java.net.URLDecoder.decode(mixDecoded, "UTF-16"),
			URLDecoder.decode(mixDecoded, StandardCharsets.UTF_16)
		);
	}

	@Test
	void decodeCharSetIsNullToStrTest() {
		final String hello = "你好";
		String decode = URLDecoder.decode(hello, null, true);
		Assertions.assertEquals(hello, decode);
	}

	@Test
	void decodeStrIsEmptyToStrTest() {
		final String strEmpty = "";
		String decode = URLDecoder.decode(strEmpty, StandardCharsets.UTF_8, true);
		Assertions.assertEquals(strEmpty, decode);
	}

	@Test
	void decodeStrWithUTF8ToStrTest() {
		final String exceptedDecode = "你好";
		final String encode = "%E4%BD%A0%E5%A5%BD";
		String s1 = URLDecoder.decode(encode);
		Assertions.assertEquals(exceptedDecode, s1);

		String s2 = URLDecoder.decode(encode, StandardCharsets.UTF_8);
		Assertions.assertEquals(exceptedDecode, s2);

		String s3 = URLDecoder.decode(encode, true);
		Assertions.assertEquals(exceptedDecode, s3);

		String s4 = URLDecoder.decode(encode + "+", false);
		Assertions.assertEquals(exceptedDecode + "+", s4);

		String s5 = URLDecoder.decode(encode, StandardCharsets.UTF_8, false);
		Assertions.assertEquals(exceptedDecode, s5);
	}

	@Test
	void decodeStrWithUTF8ToByteTest(){
		final String exceptedDecode = "你好";
		final String encode = "%E4%BD%A0%E5%A5%BD";
		byte[] decode = URLDecoder.decode(encode.getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals(exceptedDecode, new String(decode,StandardCharsets.UTF_8));

		byte[] decode1 = URLDecoder.decode((encode + "+").getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals(exceptedDecode+" ",new String(decode1,StandardCharsets.UTF_8));
	}
}
