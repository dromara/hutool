package com.xiaoleilu.hutool.demo.http;

import java.util.HashMap;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.lang.Console;

/**
 * Post请求样例
 * @author Looly
 *
 */
public class HttpPostDemo {
	public static void main(String[] args) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("city", "北京");
		String result1 = HttpUtil.post("http://wthrcdn.etouch.cn/weather_mini", paramMap);
		Console.log(result1);
	}
}
