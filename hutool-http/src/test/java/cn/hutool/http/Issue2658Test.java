package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.cookie.GlobalCookieManager;
import org.junit.Ignore;
import org.junit.Test;

import java.net.HttpCookie;
import java.util.List;

public class Issue2658Test {

	@SuppressWarnings("resource")
	@Test
	@Ignore
	public void getWithCookieTest(){
		HttpRequest.get("https://www.baidu.com/").execute();
		final List<HttpCookie> cookies = GlobalCookieManager.getCookieManager().getCookieStore().getCookies();
		Console.log("###" + cookies);
		HttpRequest.get("https://www.baidu.com/").execute();
	}
}
