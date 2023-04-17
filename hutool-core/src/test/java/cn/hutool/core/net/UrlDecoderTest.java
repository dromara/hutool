package cn.hutool.core.net;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class UrlDecoderTest {
	@Test
	public void decodeForPathTest(){
		Assert.assertEquals("+", URLDecoder.decodeForPath("+", CharsetUtil.CHARSET_UTF_8));
	}

	@Test
	public void issue3063Test() throws UnsupportedEncodingException {
		// https://github.com/dromara/hutool/issues/3063

		final String s = "测试";
		final String expectedDecode = "%FE%FF%6D%4B%8B%D5";

		final String s1 = URLUtil.encode(s, StandardCharsets.UTF_16);
		Assert.assertEquals(expectedDecode, s1);
		final String s2 = java.net.URLEncoder.encode(s, "UTF-16");
		Assert.assertEquals(expectedDecode, s2);

		final String decode = URLDecoder.decode(s1, StandardCharsets.UTF_16);
		Assert.assertEquals(s, decode);

		// 测试编码字符串和非编码字符串混合
		final String mixDecoded = expectedDecode + "你好";
		final String decode2 = URLDecoder.decode(mixDecoded, StandardCharsets.UTF_16);
		Assert.assertEquals("测试你好", decode2);

		Assert.assertEquals(
			java.net.URLDecoder.decode(mixDecoded, "UTF-16"),
			URLDecoder.decode(mixDecoded, StandardCharsets.UTF_16)
		);
	}
}
