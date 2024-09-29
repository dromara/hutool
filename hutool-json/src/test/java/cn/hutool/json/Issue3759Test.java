package cn.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3759Test {
	@Test
	void parseTest() {
		String jsonArrayStr = "[null]";
		final JSONArray objects = JSONUtil.parseArray(jsonArrayStr,
			JSONConfig.create().setIgnoreNullValue(true));
		Assertions.assertTrue(objects.isEmpty());
	}
}
