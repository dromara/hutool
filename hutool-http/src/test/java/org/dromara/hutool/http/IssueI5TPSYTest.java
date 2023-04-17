package org.dromara.hutool.http;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.meta.HeaderName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI5TPSYTest {

	@Test
	@Disabled
	public void redirectTest() {
		final String url = "https://bsxt.gdzwfw.gov.cn/UnifiedReporting/auth/newIndex";
		final Response res = HttpUtil.send(Request.of(url)
						.setMaxRedirectCount(2)
				.header(HeaderName.USER_AGENT, "PostmanRuntime/7.29.2")
				.cookie("jsessionid=s%3ANq6YTcIHQWrHkEqOSxiQNijDMhoFNV4_.h2MVD1CkW7sOZ60OSnPs7m4K%2FhENfYy%2FdzjKvSiZF4E"));
		Console.log(res.body());
	}
}
