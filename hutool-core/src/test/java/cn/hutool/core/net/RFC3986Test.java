package cn.hutool.core.net;

import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

public class RFC3986Test {

	@Test
	public void pacharTest(){
		final String encode = RFC3986.PCHAR.encode("=", CharsetUtil.UTF_8);
		Assert.assertEquals("=", encode);
	}

	@Test
	public void encodeQueryTest(){
		String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=b", CharsetUtil.UTF_8);
		Assert.assertEquals("a=b", encode);

		encode = RFC3986.QUERY_PARAM_VALUE.encode("a+1=b", CharsetUtil.UTF_8);
		Assert.assertEquals("a+1=b", encode);
	}

	@Test
	public void encodeQueryPercentTest(){
		String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%b", CharsetUtil.UTF_8);
		Assert.assertEquals("a=%25b", encode);
	}

	@Test
	public void encodeQueryWithSafeTest(){
		String encode = RFC3986.QUERY_PARAM_VALUE.encode("a=%25", CharsetUtil.UTF_8, '%');
		Assert.assertEquals("a=%25", encode);
	}
}
