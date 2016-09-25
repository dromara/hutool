package com.xiaoleilu.hutool.demo.http;

import java.util.HashMap;

import com.xiaoleilu.hutool.http.HttpUtil;

public class HttpPostDemo {
	public static void main(String[] args) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("city", "北京");
		String post = HttpUtil.get("http://wthrcdn.etouch.cn/weather_mini", paramMap);
		System.out.println(post);
	}
}
