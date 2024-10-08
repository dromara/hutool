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

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONEngineTest {

	private final String[] engineNames = {"jackson", "gson", "fastjson", "moshi", "hutool"};

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

	@Test
	void toStringOrFromStringTest() {
		Arrays.stream(engineNames).forEach(this::assertToStringOrFromString);
	}

	@Test
	void prettyPrintTest() {
		Arrays.stream(engineNames).forEach(this::assertPrettyPrint);
	}

	private void assertWriteDateFormat(final String engineName) {
		final DateTime date = DateUtil.parse("2024-01-01 01:12:21");
		final BeanWithDate bean = new BeanWithDate(date, TimeUtil.of(date));
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		final String jsonString = engine.toJsonString(bean);
		assertEquals("{\"date1\":1704042741000,\"date2\":1704042741000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		assertEquals("{\"date1\":\"2024-01-01 01:12:21\",\"date2\":\"2024-01-01 01:12:21\"}", engine.toJsonString(bean));
	}

	void assertWriteLocalDateFormat(final String engineName) {
		final DateTime date = DateUtil.parse("2024-01-01 01:12:21");
		final BeanWithLocalDate bean = new BeanWithLocalDate(TimeUtil.of(date).toLocalDate());
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		final String jsonString = engine.toJsonString(bean);
		assertEquals("{\"date\":1704038400000}", jsonString);

		engine.init(JSONEngineConfig.of().setDateFormat("yyyy-MM-dd HH:mm:ss"));
		assertEquals("{\"date\":\"2024-01-01 00:00:00\"}", engine.toJsonString(bean));
	}

	private void assertWriteNull(final String engineName) {
		final BeanWithDate bean = new BeanWithDate(null, null);
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		String jsonString = engine.toJsonString(bean);
		assertEquals("{}", jsonString);

		engine.init(JSONEngineConfig.of().setIgnoreNullValue(false));
		jsonString = engine.toJsonString(bean);
		assertEquals("{\"date1\":null,\"date2\":null}", jsonString);
	}

	private void assertWriteTimeZone(final String engineName) {
		final TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);

		String jsonString = engine.toJsonString(timeZone);
		assertEquals("\"GMT+08:00\"", jsonString);

		engine.init(JSONEngineConfig.of().setIgnoreNullValue(false));
		jsonString = engine.toJsonString(timeZone);
		assertEquals("\"GMT+08:00\"", jsonString);
	}

	private void assertEmptyBeanToJson(final String engineName){
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);
		final String jsonString = engine.toJsonString(new EmptyBean());
		assertEquals("{}", jsonString);
	}

	private void assertToStringOrFromString(final String engineName) {
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);
		final TestBean testBean = new TestBean("张三", 18, true);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		if("moshi".equals(engineName)){
			// TODO Moshi无法指定key的顺序
			assertEquals("{\"age\":18,\"gender\":true,\"name\":\"张三\"}", engine.toJsonString(testBean));
		}else{
			assertEquals(jsonStr, engine.toJsonString(testBean));
		}

		final TestBean testBean1 = engine.deserialize(new StringReader(jsonStr), TestBean.class);
		assertEquals(testBean, testBean1);
	}

	private void assertPrettyPrint(final String engineName){
		final JSONEngine engine = JSONEngineFactory.createEngine(engineName);
		engine.init(JSONEngineConfig.of().setPrettyPrint(true));

		final TestBean testBean = new TestBean("张三", 18, true);
		String jsonString = engine.toJsonString(testBean);
		if("jackson".equals(engineName)){
			jsonString = jsonString.replace(" : ", ": ");
			// 使用统一换行符
			jsonString = StrUtil.removeAll(jsonString, '\r');
		}
		if(engineName.startsWith("fastjson")){
			jsonString = jsonString.replace(":", ": ");
			jsonString = jsonString.replace("\t", "  ");
		}

		if("moshi".equals(engineName)){
			// Moshi顺序不同
			// 使用统一换行符
			assertEquals("{\n" +
				"  \"age\": 18,\n" +
				"  \"gender\": true,\n" +
				"  \"name\": \"张三\"\n" +
				"}", jsonString);
			return;
		}
		// 使用统一换行符
		assertEquals("{\n" +
			"  \"name\": \"张三\",\n" +
			"  \"age\": 18,\n" +
			"  \"gender\": true\n" +
			"}", jsonString);
	}

	@Data
	private static class EmptyBean{

	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class TestBean {
		// 解决输出顺序问题
		@JSONField(ordinal = 1)
		private String name;
		@JSONField(ordinal = 2)
		private int age;
		@JSONField(ordinal = 3)
		private boolean gender;
	}
}
