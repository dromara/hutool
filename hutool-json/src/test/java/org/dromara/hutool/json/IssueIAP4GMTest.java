package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAP4GMTest {
	@Test
	void parse() {
		final String res = "{\"uid\":\"asdf\\n\"}";
		final JSONObject entries = JSONUtil.parseObj(res);
		Assertions.assertEquals("asdf\n", entries.getStr("uid"));
	}
}
