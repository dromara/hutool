package com.xiaoleilu.hutool.http.test;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.date.TimeInterval;
import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpResponse;
import com.xiaoleilu.hutool.lang.Console;

/**
 * {@link HttpRequest}单元测试
 * @author Looly
 *
 */
public class HttpRequestTest {
	final String url = "http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2";
	
	@Test
	@Ignore
	public void asyncGetTest() {
		TimeInterval timer = DateUtil.timer();
		HttpResponse body = HttpRequest.get(url).charset("GBK").executeAsync();
		long interval = timer.interval();
		timer.restart();
		Console.log(body.body());
		long interval2 = timer.interval();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}
	
	@Test
	@Ignore
	public void syncGetTest() {
		TimeInterval timer = DateUtil.timer();
		HttpResponse body = HttpRequest.get(url).charset("GBK").execute();
		long interval = timer.interval();
		timer.restart();
		Console.log(body.body());
		long interval2 = timer.interval();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}
}
