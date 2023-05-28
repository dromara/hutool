package org.dromara.hutool.core.net;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.net.url.URLDecoder;
import org.dromara.hutool.core.net.url.URLEncoder;
import org.dromara.hutool.core.net.url.UrlQuery;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class URLEncoderTest {

	@Test
	void encodeTest() {
		final String body = "366466 - 副本.jpg";
		final String encode = URLEncoder.encodeAll(body);
		Assertions.assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode);
		Assertions.assertEquals(body, URLDecoder.decode(encode));

		final String encode2 = URLEncoder.encodeQuery(body);
		Assertions.assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode2);
	}

	@Test
	void encodeQueryPlusTest() {
		final String body = "+";
		final String encode2 = URLEncoder.encodeQuery(body);
		Assertions.assertEquals("+", encode2);
	}

	@Test
	void encodeEmojiTest() {
		final String emoji = "🐶😊😂🤣";
		final String encode = URLEncoder.encodeAll(emoji);
		Assertions.assertEquals("%F0%9F%90%B6%F0%9F%98%8A%F0%9F%98%82%F0%9F%A4%A3", encode);
		Assertions.assertEquals(emoji, URLDecoder.decode(encode));
	}
}
