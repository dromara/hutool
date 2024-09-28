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
import org.dromara.hutool.json.engine.fastjson.FastJSON2Engine;
import org.dromara.hutool.json.engine.gson.GsonEngine;
import org.dromara.hutool.json.engine.jackson.JacksonEngine;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONEngineFactoryTest {
	@Test
	void createDefaultEngineTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine();
		assertEquals(JacksonEngine.class, engine.getClass());
	}

	@Test
	void jacksonTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("jackson");
		assertEquals(JacksonEngine.class, engine.getClass());

		final TestBean testBean = new TestBean("张三", 18, true);
		final String jsonString = engine.toJsonString(testBean);

		final String resultJsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(resultJsonStr, jsonString);

		final TestBean testBean1 = engine.fromJsonString(resultJsonStr, TestBean.class);
		assertEquals(testBean, testBean1);
	}

	@Test
	void jacksonTest2() {
		final JSONEngine engine = JSONEngineFactory.createEngine("jackson");
		assertEquals(JacksonEngine.class, engine.getClass());

		final TestBean testBean = new TestBean("张三", 18, true);
		final String serialize = engine.toJsonString(testBean);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, serialize);

		final TestBean testBean1 = engine.fromJsonString(jsonStr, TestBean.class);
		assertEquals(testBean, testBean1);
	}

	@Test
	void GsonTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");
		assertEquals(GsonEngine.class, engine.getClass());

		final TestBean testBean = new TestBean("张三", 18, true);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, engine.toJsonString(testBean));

		final TestBean testBean1 = engine.deserialize(new StringReader(jsonStr), TestBean.class);
		assertEquals(testBean, testBean1);
	}

	@Test
	void fastJSON2Test() {
		final JSONEngine engine = JSONEngineFactory.createEngine("fastjson");
		assertEquals(FastJSON2Engine.class, engine.getClass());

		final TestBean testBean = new TestBean("张三", 18, true);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, engine.toJsonString(testBean));

		final TestBean testBean1 = engine.deserialize(new StringReader(jsonStr), TestBean.class);
		assertEquals(testBean, testBean1);
	}

	@Test
	void HutoolJSONTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("hutoolJSON");
		assertEquals(HutoolJSONEngine.class, engine.getClass());

		final TestBean testBean = new TestBean("张三", 18, true);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, engine.toJsonString(testBean));

		final TestBean testBean1 = engine.deserialize(new StringReader(jsonStr), TestBean.class);
		assertEquals(testBean, testBean1);
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
