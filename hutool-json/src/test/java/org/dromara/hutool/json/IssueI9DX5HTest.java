package org.dromara.hutool.json;

import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI9DX5HTest {

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	@Test
	void xmlToJSONTest() {
		final String xml = "<GoodMsg>你好</GoodMsg>";
		final JSONObject jsonObject = new JSONObject(xml, JSONConfig.of(), entry -> {
			entry.setKey(StrUtil.toUnderlineCase(entry.getKey()));
			return true;
		});

		Assertions.assertEquals("{\"good_msg\":\"你好\"}", jsonObject.toString());
	}
}
