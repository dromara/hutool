package org.dromara.hutool.json.reader;

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3808Test {
	@Test
	void parseTest() {
		final String str = ResourceUtil.readStr("issue3808.json", CharsetUtil.UTF_8);
		final JSON parse = JSONUtil.parse(str);
		Assertions.assertNotNull(parse);
	}
}
