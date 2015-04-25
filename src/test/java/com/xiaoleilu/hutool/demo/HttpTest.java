package com.xiaoleilu.hutool.demo;

import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpResponse;

public class HttpTest {
	public static void main(String[] args) {
		HttpRequest request = HttpRequest.get("http://my.oschina.net/xinxingegeya/blog");
		HttpResponse response = request.execute();
		System.out.println(response.body());
	}
}
