/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json.engine;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;

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

		final StringWriter stringWriter = new StringWriter();
		final TestBean testBean = new TestBean("张三", 18, true);
		engine.serialize(testBean, stringWriter);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, stringWriter.toString());

		final TestBean testBean1 = engine.deserialize(new StringReader(jsonStr), TestBean.class);
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

		final StringWriter stringWriter = new StringWriter();
		final TestBean testBean = new TestBean("张三", 18, true);
		engine.serialize(testBean, stringWriter);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, stringWriter.toString());

		final TestBean testBean1 = engine.deserialize(new StringReader(jsonStr), TestBean.class);
		assertEquals(testBean, testBean1);
	}

	@Test
	void fastJSON2Test() {
		final JSONEngine engine = JSONEngineFactory.createEngine("fastjson");
		assertEquals(FastJSON2Engine.class, engine.getClass());

		final StringWriter stringWriter = new StringWriter();
		final TestBean testBean = new TestBean("张三", 18, true);
		engine.serialize(testBean, stringWriter);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, stringWriter.toString());

		final TestBean testBean1 = engine.deserialize(new StringReader(jsonStr), TestBean.class);
		assertEquals(testBean, testBean1);
	}

	@Test
	void HutoolJSONTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("hutoolJSON");
		assertEquals(HutoolJSONEngine.class, engine.getClass());

		final StringWriter stringWriter = new StringWriter();
		final TestBean testBean = new TestBean("张三", 18, true);
		engine.serialize(testBean, stringWriter);

		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"gender\":true}";
		assertEquals(jsonStr, stringWriter.toString());

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
