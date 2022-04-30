package cn.hutool.core.net;

import org.junit.Assert;
import org.junit.Test;

public class URLEncoderTest {
	@Test
	public void encodeTest() {
		String body = "366466 - 副本.jpg";
		String encode = URLEncoder.encodeAll(body);
		Assert.assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode);
		Assert.assertEquals(body, URLDecoder.decode(encode));

		String encode2 = URLEncoder.encodeQuery(body);
		Assert.assertEquals("366466%20-%20%E5%89%AF%E6%9C%AC.jpg", encode2);
	}

	@Test
	public void encodeQueryPlusTest() {
		String body = "+";
		String encode2 = URLEncoder.encodeQuery(body);
		Assert.assertEquals("+", encode2);
	}

	@Test
	public void encodeEmojiTest(){
		String emoji = "🐶😊😂🤣";
		String encode = URLEncoder.encodeAll(emoji);
		Assert.assertEquals("%F0%9F%90%B6%F0%9F%98%8A%F0%9F%98%82%F0%9F%A4%A3", encode);
		Assert.assertEquals(emoji, URLDecoder.decode(encode));
	}
}
