package org.dromara.hutool.http.server;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.meta.HeaderName;

public class RedirectServerTest {
	public static void main(final String[] args) {
		HttpUtil.createServer(8888).addAction("/redirect1", (request, response) -> {
			response.addHeader(HeaderName.LOCATION.getValue(),"http://localhost:8888/redirect2");
			response.addHeader(HeaderName.SET_COOKIE.getValue(),"redirect1=1; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect2", (request, response) -> {
			response.addHeader(HeaderName.LOCATION.getValue(),"http://localhost:8888/redirect3");
			response.addHeader(HeaderName.SET_COOKIE.getValue(), "redirect2=2; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect3", (request, response) -> {
			response.addHeader(HeaderName.LOCATION.getValue(),"http://localhost:8888/redirect4");
			response.addHeader(HeaderName.SET_COOKIE.getValue(),"redirect3=3; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect4", (request, response) -> {
			final String cookie = request.getHeader(HeaderName.COOKIE);
			Console.log(cookie);
			response.sendOk();
		}).start();
	}
}
