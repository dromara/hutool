package org.dromara.hutool.json.issues;

import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueIAW0ITTest {
	@Test
	void jsonArrayToStringTest() {
		final JSONArray jsonArray = JSONUtil.ofArray()
			.addValue(JSONUtil.ofObj().putValue("value", 0).putValue("empty", false));

		Assertions.assertEquals("[{\"value\":0,\"empty\":false}]", jsonArray.toString());
	}
}
