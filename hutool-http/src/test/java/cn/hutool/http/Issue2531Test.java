package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue2531Test {

	@Test
	@Ignore
	public void getTest(){
		final Map<String,String> map = new HashMap<>();
		map.put("str","+123");

		final String queryParam = MapUtil.join(map, "&", "=");//返回str=+123
		Console.log(queryParam);

		final Request request = Request.of("http://localhost:8888/formTest?" + queryParam);
		//request.setUrl("http://localhost:8888/formTest" + "?" + queryParam);
		//noinspection resource
		final Response execute = request.send();
		Console.log(execute.body());
	}

	@Test
	public void encodePlusTest(){
		// 根据RFC3986规范，在URL中，"+"是安全字符，所以不会解码也不会编码
		UrlBuilder builder = UrlBuilder.ofHttp("https://hutool.cn/a=+");
		Assert.assertEquals("https://hutool.cn/a=+", builder.toString());

		// 由于+为安全字符无需编码，ofHttp方法会把%2B解码为+，但是编码的时候不会编码
		builder = UrlBuilder.ofHttp("https://hutool.cn/a=%2B");
		Assert.assertEquals("https://hutool.cn/a=+", builder.toString());

		// 如果你不想解码%2B，则charset传null表示不做解码，编码时候也被忽略
		builder = UrlBuilder.ofHttp("https://hutool.cn/a=%2B", null);
		Assert.assertEquals("https://hutool.cn/a=%2B", builder.toString());
	}
}
