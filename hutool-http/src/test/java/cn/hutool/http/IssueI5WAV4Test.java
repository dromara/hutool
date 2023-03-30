package cn.hutool.http;

import cn.hutool.http.client.Request;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueI5WAV4Test {

	@Test
	@Disabled
	public void getTest(){
		//测试代码
		final Map<String, Object> map = new HashMap<>();
		map.put("taskID", 370);
		map.put("flightID", 2879);


		@SuppressWarnings("resource")
		final String body = Request.of("http://localhost:8884/api/test/testHttpUtilGetWithBody").body(JSONUtil.toJsonStr(map)).send().bodyStr();
		System.out.println("使用hutool返回结果:" + body);
	}
}
