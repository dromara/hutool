package cn.hutool.json;

import lombok.Data;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Issue3541Test {
	@Test
	public void longToStringTest() {
		Demo demo = new Demo();
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.create().setWriteLongAsString(true));
		assertEquals("{\"id\":\"1227690722069581409\",\"name\":\"hutool\"}", jsonStr);
	}

	@Data
	public static class Demo {
		private Long id;
		private String name;
	}
}
