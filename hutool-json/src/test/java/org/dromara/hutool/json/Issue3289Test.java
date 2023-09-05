package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3289Test {
	@Test
	void parseTest() {
		Assertions.assertThrows(JSONException.class, () -> {
			String s = "{\"G\":00,[6E962756779]}";
			JSONUtil.parse(s);
		});
	}
}
