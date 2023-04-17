package org.dromara.hutool.core.net;

import org.dromara.hutool.core.net.url.URLDecoder;
import org.dromara.hutool.core.net.url.URLEncoder;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class UrlDecoderTest {

	@Test
	void decodeForPathTest(){
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
}
