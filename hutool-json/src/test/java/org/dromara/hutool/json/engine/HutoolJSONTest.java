package org.dromara.hutool.json.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HutoolJSONTest {
	@Test
	void prettyPrintTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("hutoolJSON");
		engine.init(JSONEngineConfig.of().setPrettyPrint(true));

		final JSONEngineFactoryTest.TestBean testBean = new JSONEngineFactoryTest.TestBean("张三", 18, true);
		final String jsonString = engine.toJsonString(testBean);
		Assertions.assertEquals("{\n" +
			"  \"name\": \"张三\",\n" +
			"  \"age\": 18,\n" +
			"  \"gender\": true\n" +
			"}", jsonString);
	}
}
