package cn.hutool.http;

import cn.hutool.core.net.url.UrlBuilder;
import org.junit.Assert;
import org.junit.Test;

public class IssueI5TFPUTest {
	@Test
	public void urlBuilderTest() {
		final UrlBuilder urlBuilder = UrlBuilder.of("https://hutool.cn", null).addQuery("opt", "%");
		Assert.assertEquals("https://hutool.cn?opt=%", urlBuilder.toString());
	}
}
