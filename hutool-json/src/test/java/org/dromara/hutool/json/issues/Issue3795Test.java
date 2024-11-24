package org.dromara.hutool.json.issues;

import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Issue3795Test {
	@Test
	void toBeanTest() {
		final String fieldMapping = "[{\"lable\":\"id\",\"value\":\"id\"},{\"lable\":\"name\",\"value\":\"name\"},{\"lable\":\"age\",\"value\":\"age\"}]";
		Assertions.assertThrows(JSONException.class, ()->{
			JSONUtil.toBean(fieldMapping, new TypeReference<Map<String, String>>() {});
		});
	}
}
