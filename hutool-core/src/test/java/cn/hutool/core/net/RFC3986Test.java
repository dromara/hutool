package cn.hutool.core.net;

import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RFC3986Test {

	@Test
	public void encodeQueryTest(){
		final String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=b", CharsetUtil.CHARSET_UTF_8);
		Assertions.assertEquals("a=b", encode);
	}
}
