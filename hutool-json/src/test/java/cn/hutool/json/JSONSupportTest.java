package cn.hutool.json;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.Assert;
import org.junit.Test;

public class JSONSupportTest {

	/**
	 * https://github.com/dromara/hutool/issues/1779
	 * 在JSONSupport的JSONBeanParse中，如果使用json.toBean，会导致JSONBeanParse.parse方法反复递归调用，最终栈溢出<br>
	 * 因此parse方法默认实现必须避开JSONBeanParse.parse调用。
	 */
	@Test
	public void parseTest() {
		String jsonstr = "{\n" +
				"    \"location\": \"https://hutool.cn\",\n" +
				"    \"message\": \"这是一条测试消息\",\n" +
				"    \"requestId\": \"123456789\",\n" +
				"    \"traceId\": \"987654321\"\n" +
				"}";


		final TestBean testBean = JSONUtil.toBean(jsonstr, TestBean.class);
		Assert.assertEquals("https://hutool.cn", testBean.getLocation());
		Assert.assertEquals("这是一条测试消息", testBean.getMessage());
		Assert.assertEquals("123456789", testBean.getRequestId());
		Assert.assertEquals("987654321", testBean.getTraceId());
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
