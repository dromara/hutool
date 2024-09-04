package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GsonTest {
	/**
	 * Gson默认缩进两个空格，使用\n换行符
	 */
	@Test
	void prettyPrintTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");
		engine.init(JSONEngineConfig.of().setPrettyPrint(true));

		final JSONEngineFactoryTest.TestBean testBean = new JSONEngineFactoryTest.TestBean("张三", 18, true);
		final String jsonString = engine.toJsonString(testBean);
		// 使用统一换行符
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
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");

		final String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date1\":1704042741000,\"date2\":1704042741000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		Assertions.assertEquals("{\"date1\":\"2024-01-01 01:12:21\",\"date2\":\"2024-01-01 01:12:21\"}", engine.toJsonString(bean));
	}

	@Test
	void writeNullTest() {
		final BeanWithDate bean = new BeanWithDate(null, null);
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");

		String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{}", jsonString);

		engine.init(JSONEngineConfig.of().setIgnoreNullValue(false));
		jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date1\":null,\"date2\":null}", jsonString);
	}
}
