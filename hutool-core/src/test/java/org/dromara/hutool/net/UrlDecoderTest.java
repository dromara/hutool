package org.dromara.hutool.net;

import org.dromara.hutool.net.url.URLDecoder;
import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UrlDecoderTest {
	@Test
	public void decodeForPathTest(){
		Assertions.assertEquals("+", URLDecoder.decodeForPath("+", CharsetUtil.UTF_8));
	}
}
