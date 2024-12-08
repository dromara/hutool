package org.dromara.hutool.json.reader;

import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3808Test {
	@Test
	void parseEscapeZeroWithCharTest() {
		final String str = ResourceUtil.readStr("issue3808.json", CharsetUtil.UTF_8);
		final JSON parse = JSONUtil.parse(str, JSONConfig.of().setIgnoreZeroWithChar(false));
		Assertions.assertEquals("{\"recommend_text\":\"✅宁波，\\u200c一座历史悠久的文化名城\\n你好\",\"，\\u200c一\":\"aaa\"}", parse.toString());
	}

	@Test
	void parseIgnoreZeroWithCharTest() {
		final String str = ResourceUtil.readStr("issue3808.json", CharsetUtil.UTF_8);
		final JSON parse = JSONUtil.parse(str, JSONConfig.of().setIgnoreZeroWithChar(true));
		Assertions.assertEquals("{\"recommend_text\":\"✅宁波，一座历史悠久的文化名城\\n你好\",\"，一\":\"aaa\"}", parse.toString());
	}
}
