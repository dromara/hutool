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
}
