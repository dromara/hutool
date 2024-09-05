/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
