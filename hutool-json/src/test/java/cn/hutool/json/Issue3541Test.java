package cn.hutool.json;

import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

public class Issue3541Test {
	@Test
	public void longToStringTest() {
		Demo demo = new Demo();
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.create().setWriteLongAsString(true));
		Assert.assertEquals("{\"id\":\"1227690722069581409\",\"name\":\"hutool\"}", jsonStr);
	}

	@Data
	public static class Demo {
		private Long id;
		private String name;
	}
}
