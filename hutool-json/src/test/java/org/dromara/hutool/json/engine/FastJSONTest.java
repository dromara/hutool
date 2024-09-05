/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json.engine;

import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FastJSONTest {
	@Test
	void prettyPrintTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("fastjson");
		engine.init(JSONEngineConfig.of().setPrettyPrint(true));

		final JSONEngineFactoryTest.TestBean testBean = new JSONEngineFactoryTest.TestBean("张三", 18, true);
		String jsonString = engine.toJsonString(testBean);
		// 使用统一换行符
		jsonString = StrUtil.removeAll(jsonString, '\r');
		Assertions.assertEquals("{\n" +
			"	\"name\":\"张三\",\n" +
			"	\"age\":18,\n" +
			"	\"gender\":true\n" +
			"}", jsonString);
	}

	@Test
	void writeDateFormatTest() {
		final DateTime date = DateUtil.parse("2024-01-01 01:12:21");
		final BeanWithDate bean = new BeanWithDate(date, TimeUtil.of(date));
		final JSONEngine engine = JSONEngineFactory.createEngine("fastjson");

		final String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date1\":1704042741000,\"date2\":1704042741000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		Assertions.assertEquals("{\"date1\":\"2024-01-01 01:12:21\",\"date2\":\"2024-01-01 01:12:21\"}", engine.toJsonString(bean));
	}

	@Test
	void writeNullTest() {
		final BeanWithDate bean = new BeanWithDate(null, null);
		final JSONEngine engine = JSONEngineFactory.createEngine("fastjson");

		String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{}", jsonString);

		engine.init(JSONEngineConfig.of().setIgnoreNullValue(false));
		jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date1\":null,\"date2\":null}", jsonString);
	}
}
