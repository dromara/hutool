package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Pr3507Test {
	@Test
	void writeClassTest() {
		final JSONObject set = JSONUtil.ofObj().set("name", Pr3507Test.class);
		Assertions.assertEquals("{\"name\":\"org.dromara.hutool.json.Pr3507Test\"}", set.toString());
	}
}
