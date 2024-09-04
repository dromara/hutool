package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
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

	@Test
	void writeDateFormatTest() {
		final DateTime date = DateUtil.parse("2024-01-01 01:12:21");
		final BeanWithDate bean = new BeanWithDate(date, TimeUtil.of(date));
		final JSONEngine engine = JSONEngineFactory.createEngine("hutool");

		final String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date1\":1704042741000,\"date2\":1704042741000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		Assertions.assertEquals("{\"date1\":\"2024-01-01 01:12:21\",\"date2\":\"2024-01-01 01:12:21\"}", engine.toJsonString(bean));
	}
}
