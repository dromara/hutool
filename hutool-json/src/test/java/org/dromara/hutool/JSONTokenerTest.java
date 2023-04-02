package org.dromara.hutool;

import org.dromara.hutool.io.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONTokenerTest {
	@Test
	public void parseTest() {
		final JSONObject jsonObject = JSONUtil.parseObj(ResourceUtil.getUtf8Reader("issue1200.json"));
		Assertions.assertNotNull(jsonObject);
	}
}
