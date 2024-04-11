package org.dromara.hutool.json.writer;

import lombok.Data;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3541Test {
	@Test
	void writeNumberJSTest() {
		final Demo demo = new Demo();
		// 超出长度
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		final String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.JS));
		Assertions.assertEquals("{\"id\":\"1227690722069581409\",\"name\":\"hutool\"}", jsonStr);

		// 未超出长度
		demo.setId(1227690722069581L);
		final String jsonStr2 = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.JS));
		Assertions.assertEquals("{\"id\":1227690722069581,\"name\":\"hutool\"}", jsonStr2);
	}

	@Test
	void writeNumberStringTest() {
		final Demo demo = new Demo();
		// 超出长度
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		final String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.STRING));
		Assertions.assertEquals("{\"id\":\"1227690722069581409\",\"name\":\"hutool\"}", jsonStr);

		// 未超出长度
		demo.setId(1227690722069581L);
		final String jsonStr2 = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.STRING));
		Assertions.assertEquals("{\"id\":\"1227690722069581\",\"name\":\"hutool\"}", jsonStr2);
	}

	@Test
	void writeNumberNormalTest() {
		final Demo demo = new Demo();
		// 超出长度
		demo.setId(1227690722069581409L);
		demo.setName("hutool");
		final String jsonStr = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.NORMAL));
		Assertions.assertEquals("{\"id\":1227690722069581409,\"name\":\"hutool\"}", jsonStr);

		// 未超出长度
		demo.setId(1227690722069581L);
		final String jsonStr2 = JSONUtil.toJsonStr(demo, JSONConfig.of().setNumberWriteMode(NumberWriteMode.NORMAL));
		Assertions.assertEquals("{\"id\":1227690722069581,\"name\":\"hutool\"}", jsonStr2);
	}

	@Data
	public static class Demo {
		private Long id;
		private String name;
	}
}
