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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class URLEncoderTest {

	@Test
	void encodeTest() {
		final String body = "366466 - 副本.jpg";
		final String encode = UrlEncoder.encodeAll(body);
		Assertions.assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode);
		Assertions.assertEquals(body, UrlDecoder.decode(encode));

		final String encode2 = UrlEncoder.encodeQuery(body);
		Assertions.assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode2);
	}

	@Test
	void encodeQueryPlusTest() {
		final String body = "+";
		final String encode2 = UrlEncoder.encodeQuery(body);
		Assertions.assertEquals("+", encode2);
	}

	@Test
	void encodeEmojiTest() {
		final String emoji = "🐶😊😂🤣";
		final String encode = UrlEncoder.encodeAll(emoji);
		Assertions.assertEquals("%F0%9F%90%B6%F0%9F%98%8A%F0%9F%98%82%F0%9F%A4%A3", encode);
		Assertions.assertEquals(emoji, UrlDecoder.decode(encode));
	}
}
