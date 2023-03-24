package cn.hutool.http.server;

import cn.hutool.core.lang.Console;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;

public class RedirectServerTest {

	public static void main(String[] args) {
		HttpUtil.createServer(8888).addAction("/redirect1", (request, response) -> {
			response.addHeader(Header.LOCATION.getValue(), "http://localhost:8888/redirect2");
			response.addHeader(Header.SET_COOKIE.getValue(), "redirect1=1; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect2", (request, response) -> {
			response.addHeader(Header.LOCATION.getValue(), "http://localhost:8888/redirect3");
			response.addHeader(Header.SET_COOKIE.getValue(), "redirect2=2; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect3", (request, response) -> {
			response.addHeader(Header.LOCATION.getValue(), "http://localhost:8888/redirect4");
			response.addHeader(Header.SET_COOKIE.getValue(), "redirect3=3; path=/; HttpOnly");
			response.send(301);
		}).addAction("/redirect4", (request, response) -> {
			final String cookie = request.getHeader(Header.COOKIE);
			Console.log(cookie);
			response.sendOk();
		}).start();
	}

}
