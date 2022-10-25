package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.client.engine.jdk.HttpResponse;
import cn.hutool.http.meta.Header;
import org.junit.Ignore;
import org.junit.Test;

public class IssueI5TPSYTest {

	@Test
	@Ignore
	public void redirectTest() {
		final String url = "https://bsxt.gdzwfw.gov.cn/UnifiedReporting/auth/newIndex";
		final HttpResponse res = HttpUtil.createGet(url).setFollowRedirects(true)
				.header(Header.USER_AGENT, "PostmanRuntime/7.29.2")
				.cookie("jsessionid=s%3ANq6YTcIHQWrHkEqOSxiQNijDMhoFNV4_.h2MVD1CkW7sOZ60OSnPs7m4K%2FhENfYy%2FdzjKvSiZF4E")
				.execute();
		Console.log(res.body());
	}
}
