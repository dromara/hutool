/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.net;

import org.dromara.hutool.core.net.url.UrlDecoder;
import org.dromara.hutool.core.net.url.UrlEncoder;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

class UrlDecoderTest {

	@Test
	void decodeForPathTest() {
		Assertions.assertEquals("+", UrlDecoder.decodeForPath("+", CharsetUtil.UTF_8));
	}

	@Test
	public void decodePlusTest() {
		final String decode = UrlDecoder.decode("+", CharsetUtil.UTF_8);
		Assertions.assertEquals(" ", decode);
	}

	@Test
	void issue3063Test() throws UnsupportedEncodingException {
		// https://github.com/dromara/hutool/issues/3063

		final String s = "测试";
		final String expectedDecode = "%FE%FF%6D%4B%8B%D5";

		final String s1 = UrlEncoder.encodeAll(s, StandardCharsets.UTF_16);
		Assertions.assertEquals(expectedDecode, s1);
		final String s2 = java.net.URLEncoder.encode(s, "UTF-16");
		Assertions.assertEquals(expectedDecode, s2);

		final String decode = UrlDecoder.decode(s1, StandardCharsets.UTF_16);
		Assertions.assertEquals(s, decode);

		// 测试编码字符串和非编码字符串混合
		final String mixDecoded = expectedDecode + "你好";
		final String decode2 = UrlDecoder.decode(mixDecoded, StandardCharsets.UTF_16);
		Assertions.assertEquals("测试你好", decode2);

		Assertions.assertEquals(
			java.net.URLDecoder.decode(mixDecoded, "UTF-16"),
			UrlDecoder.decode(mixDecoded, StandardCharsets.UTF_16)
		);
	}

	@Test
	void decodeCharSetIsNullToStrTest() {
		final String hello = "你好";
		final String decode = UrlDecoder.decode(hello, null, true);
		Assertions.assertEquals(hello, decode);
	}

	@Test
	void decodeStrIsEmptyToStrTest() {
		final String strEmpty = "";
		final String decode = UrlDecoder.decode(strEmpty, StandardCharsets.UTF_8, true);
		Assertions.assertEquals(strEmpty, decode);
	}

	@Test
	void decodeStrWithUTF8ToStrTest() {
		final String exceptedDecode = "你好";
		final String encode = "%E4%BD%A0%E5%A5%BD";
		final String s1 = UrlDecoder.decode(encode);
		Assertions.assertEquals(exceptedDecode, s1);

		final String s2 = UrlDecoder.decode(encode, StandardCharsets.UTF_8);
		Assertions.assertEquals(exceptedDecode, s2);

		final String s3 = UrlDecoder.decode(encode, true);
		Assertions.assertEquals(exceptedDecode, s3);

		final String s4 = UrlDecoder.decode(encode + "+", false);
		Assertions.assertEquals(exceptedDecode + "+", s4);

		final String s5 = UrlDecoder.decode(encode, StandardCharsets.UTF_8, false);
		Assertions.assertEquals(exceptedDecode, s5);
	}

	@Test
	void decodeStrWithUTF8ToByteTest(){
		final String exceptedDecode = "你好";
		final String encode = "%E4%BD%A0%E5%A5%BD";
		final byte[] decode = UrlDecoder.decode(encode.getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals(exceptedDecode, new String(decode,StandardCharsets.UTF_8));

		final byte[] decode1 = UrlDecoder.decode((encode + "+").getBytes(StandardCharsets.UTF_8));
		Assertions.assertEquals(exceptedDecode+" ",new String(decode1,StandardCharsets.UTF_8));
	}
}
