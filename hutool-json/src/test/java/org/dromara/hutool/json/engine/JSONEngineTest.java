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

import lombok.Data;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.TimeZone;

public class JSONEngineTest {

	private final String[] engineNames = {"jackson", "gson", "fastjson", "hutool"};

	@Test
	void writeDateFormatTest() {
		Arrays.stream(engineNames).forEach(this::assertWriteDateFormat);
	}

	@Test
	void writeNullTest() {
		Arrays.stream(engineNames).forEach(this::assertWriteNull);
	}

	@Test
	void writeLocalDateFormatTest() {
		Arrays.stream(engineNames).forEach(this::assertWriteLocalDateFormat);
	}

	@Test
	void writeTimeZoneTest() {
		Arrays.stream(engineNames).forEach(this::assertWriteTimeZone);
	}

	@Test
	void writeEmptyBeanTest() {
		Arrays.stream(engineNames).forEach(this::assertEmptyBeanToJson);
	}

	private void assertWriteDateFormat(final String engineName) {
		final DateTime date = DateUtil.parse("2024-01-01 01:12:21");
		final BeanWithDate bean = new BeanWithDate(date, TimeUtil.of(date));
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		final String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date1\":1704042741000,\"date2\":1704042741000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		Assertions.assertEquals("{\"date1\":\"2024-01-01 01:12:21\",\"date2\":\"2024-01-01 01:12:21\"}", engine.toJsonString(bean));
	}

	void assertWriteLocalDateFormat(final String engineName) {
		final DateTime date = DateUtil.parse("2024-01-01 01:12:21");
		final BeanWithLocalDate bean = new BeanWithLocalDate(TimeUtil.of(date).toLocalDate());
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		final String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date\":1704038400000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		Assertions.assertEquals("{\"date\":\"2024-01-01 00:00:00\"}", engine.toJsonString(bean));
	}

	private void assertWriteNull(final String engineName) {
		final BeanWithDate bean = new BeanWithDate(null, null);
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		String jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{}", jsonString);

		engine.init(JSONEngineConfig.of().setIgnoreNullValue(false));
		jsonString = engine.toJsonString(bean);
		Assertions.assertEquals("{\"date1\":null,\"date2\":null}", jsonString);
	}

	private void assertWriteTimeZone(final String engineName) {
		final TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		String jsonString = engine.toJsonString(timeZone);
		Assertions.assertEquals("\"GMT+08:00\"", jsonString);

		engine.init(JSONEngineConfig.of().setIgnoreNullValue(false));
		jsonString = engine.toJsonString(timeZone);
		Assertions.assertEquals("\"GMT+08:00\"", jsonString);
	}

	private void assertEmptyBeanToJson(final String engineName){
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);
		final String jsonString = engine.toJsonString(new EmptyBean());
		Assertions.assertEquals("{}", jsonString);
	}

	@Data
	private static class EmptyBean{

	}
}
