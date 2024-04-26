package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class BeanToJsonTest {
	@Test
	public void toJsonStrTest() {
		final ReadParam readParam = new ReadParam();
		readParam.setInitSpikeMac("a");
		readParam.setMac("b");
		readParam.setSpikeMac("c");
		readParam.setBag("d");
		readParam.setProjectId(123);

		//Console.log(JSONUtil.toJsonStr(readParam));
		Assert.assertEquals("{\"initSpikeMac\":\"a\",\"mac\":\"b\",\"spikeMac\":\"c\",\"bag\":\"d\",\"projectId\":123}", JSONUtil.toJsonStr(readParam));
	}

	@Data
	private static class ReadParam{
		private String initSpikeMac;
		private String mac;
		private String spikeMac;
		private String bag;
		private Integer projectId;
	}
}
