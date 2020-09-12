package cn.hutool.core.net;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class UrlQueryTest {

	@Test
	public void parseTest(){
		String queryStr = "a=1&b=111==";
		UrlQuery q = new UrlQuery();
		UrlQuery parse = q.parse(queryStr, Charset.defaultCharset());
		Assert.assertEquals("111==", parse.get("b"));
		Assert.assertEquals("a=1&b=111==", parse.toString());
	}

	@Test
	public void ofHttpWithoutEncodeTest(){
		// charset为null表示不做编码
		String url = "https://img-cloud.voc.com.cn/140/2020/09/03/c3d41b93e0d32138574af8e8b50928b376ca5ba61599127028157.png?imageMogr2/auto-orient/thumbnail/500&pid=259848";
		final UrlBuilder urlBuilder = UrlBuilder.ofHttpWithoutEncode(url);
		final String queryStr = urlBuilder.getQueryStr();
		Assert.assertEquals("imageMogr2/auto-orient/thumbnail/500&pid=259848", queryStr);
	}
}
