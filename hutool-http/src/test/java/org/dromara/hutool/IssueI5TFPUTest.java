package org.dromara.hutool;

import org.dromara.hutool.net.url.UrlBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI5TFPUTest {
	@Test
	public void urlBuilderTest() {
		final UrlBuilder urlBuilder = UrlBuilder.of("https://hutool.cn", null).addQuery("opt", "%");
		Assertions.assertEquals("https://hutool.cn?opt=%", urlBuilder.toString());
	}
}
