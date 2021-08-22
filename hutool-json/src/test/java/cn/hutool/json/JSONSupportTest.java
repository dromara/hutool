package cn.hutool.json;

import cn.hutool.core.lang.Console;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Test;

public class JSONSupportTest {

	@Test
	public void parseTest() {
		String jsonstr = "{\n" +
				"    \"location\": \"http://www.bejson.com\",\n" +
				"    \"message\": \"这是一条测试消息\",\n" +
				"    \"requestId\": \"123456789\",\n" +
				"    \"traceId\": \"987654321\"\n" +
				"}";


		final TestBean testBean = JSONUtil.toBean(jsonstr, TestBean.class);
		Console.log(testBean);
	}

	@EqualsAndHashCode(callSuper = true)
	@Data
	static class TestBean  extends JSONSupport{

		private String location;

		private String message;

		private String requestId;

		private String traceId;

	}
}
