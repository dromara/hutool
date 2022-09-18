package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
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

		final HttpRequest request = HttpUtil.createGet("http://localhost:8888/formTest?" + queryParam);
		//request.setUrl("http://localhost:8888/formTest" + "?" + queryParam);
		//noinspection resource
		final HttpResponse execute = request.execute();
		Console.log(execute.body());
	}
}
