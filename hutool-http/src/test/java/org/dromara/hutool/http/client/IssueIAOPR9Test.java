package org.dromara.hutool.http.client;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.http.HttpUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAOPR9Test {
	@Test
	void getWithParamTest() {
		final String url = "https://hutool.cn";
		final Request request = HttpUtil.createGet(url)
			.form(MapUtil.builder("a", (Object) "1").put("b", "中文").build());

		Assertions.assertEquals("https://hutool.cn?a=1&b=%E4%B8%AD%E6%96%87", request.handledUrl().toString());
	}
}
