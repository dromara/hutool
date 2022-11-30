package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import cn.hutool.http.meta.Header;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI5TPSYTest {

	@Test
	@Ignore
	public void redirectTest() {
		final String url = "https://bsxt.gdzwfw.gov.cn/UnifiedReporting/auth/newIndex";
		final Response res = HttpUtil.send(Request.of(url)
						.setMaxRedirectCount(2)
				.header(Header.USER_AGENT, "PostmanRuntime/7.29.2")
				.cookie("jsessionid=s%3ANq6YTcIHQWrHkEqOSxiQNijDMhoFNV4_.h2MVD1CkW7sOZ60OSnPs7m4K%2FhENfYy%2FdzjKvSiZF4E"));
		Console.log(res.body());
	}
}
