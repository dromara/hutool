package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpResponse;

public class HttpTest {
	public static void main(String[] args) {
		HttpRequest request = HttpRequest.post("http://my.oschina.net/looly");
		HttpResponse response = request.execute();
		System.out.println(response.body());
	}
}
