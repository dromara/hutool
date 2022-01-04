package cn.hutool.core.net;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UrlDecoderTest {
	@Test
	public void decodeForPathTest(){
		Assertions.assertEquals("+", URLDecoder.decodeForPath("+", CharsetUtil.CHARSET_UTF_8));
	}
}
