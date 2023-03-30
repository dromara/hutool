package cn.hutool.core.net;

import cn.hutool.core.net.url.RFC3986;
import cn.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RFC3986Test {

	@Test
	public void pacharTest(){
		final String encode = RFC3986.PCHAR.encode("=", CharsetUtil.UTF_8);
		Assertions.assertEquals("=", encode);
	}

	@Test
	public void encodeQueryTest(){
		String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a=b", encode);

		encode = RFC3986.QUERY_PARAM_VALUE.encode("a+1=b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a+1=b", encode);
	}

	@Test
	public void encodeQueryPercentTest(){
		final String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%b", CharsetUtil.UTF_8);
		Assertions.assertEquals("a=%25b", encode);
	}

	@Test
	public void encodeQueryWithSafeTest(){
		final String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%25", CharsetUtil.UTF_8, '%');
		Assertions.assertEquals("a=%25", encode);
	}
}
