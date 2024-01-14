package org.dromara.hutool.json.xml;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2748Test {

	@Test
	void toJSONObjectTest() {
		final String s = StrUtil.repeat("<a>", 600);

		Assertions.assertThrows(JSONException.class, () -> {
			JSONXMLUtil.toJSONObject(s, ParseConfig.of().setMaxNestingDepth(512));
		});
	}
}
