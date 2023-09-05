package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3289Test {
	@Test
	void parseTest() {
		Assertions.assertThrows(JSONException.class, () -> {
			final String s = "{\"G\":00,[,,[0E5,6E9,6E5,6E9,6E5,6E9,6E5,6E9,6E9,6E5,true,6E5,6E9,6E5,6E9,6956,EE,5E9,6E5,RE,6E9,6E9,6E5,6E9,6E5,6E9,6E5,6E9,6E5,6E962756779,4141697],]}";
			JSONUtil.parse(s);
		});
	}
}
