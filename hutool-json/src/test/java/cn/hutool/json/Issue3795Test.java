package cn.hutool.json;

import cn.hutool.core.lang.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Issue3795Test {
	@Test
	void toBeanTest() {
		String fieldMapping = "[{\"lable\":\"id\",\"value\":\"id\"},{\"lable\":\"name\",\"value\":\"name\"},{\"lable\":\"age\",\"value\":\"age\"}]";
		Assertions.assertThrows(UnsupportedOperationException.class, ()->{
			JSONUtil.toBean(fieldMapping, new TypeReference<Map<String, String>>() {}, false);
		});
	}
}
