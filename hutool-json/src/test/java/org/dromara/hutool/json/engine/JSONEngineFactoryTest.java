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

import org.dromara.hutool.json.engine.fastjson.FastJSON2Engine;
import org.dromara.hutool.json.engine.gson.GsonEngine;
import org.dromara.hutool.json.engine.jackson.JacksonEngine;
import org.junit.jupiter.api.Test;

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
	}

	@Test
	void GsonTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("gson");
		assertEquals(GsonEngine.class, engine.getClass());
	}

	@Test
	void fastJSON2Test() {
		final JSONEngine engine = JSONEngineFactory.createEngine("fastjson");
		assertEquals(FastJSON2Engine.class, engine.getClass());
	}

	@Test
	void HutoolJSONTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("hutoolJSON");
		assertEquals(HutoolJSONEngine.class, engine.getClass());
	}
}
