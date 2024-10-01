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

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FastJSONTest {
	@Test
	void prettyPrintTest() {
		final JSONEngine engine = JSONEngineFactory.createEngine("fastjson");
		engine.init(JSONEngineConfig.of().setPrettyPrint(true));

		final JSONEngineTest.TestBean testBean = new JSONEngineTest.TestBean("张三", 18, true);
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
	void toStringTest() {
		final String jsonStr = "{\"name\":\"张三\",\"age\":18,\"birthday\":\"2020-01-01\"}";
		final JSONObject jsonObject = JSON.parseObject(jsonStr);
		final JSONWriter writer = JSONWriter.of();
		writer.setRootObject(jsonObject);
		writer.write(jsonObject);

		final JSONWriter.Context context = writer.getContext();
		final ObjectWriter<?> objectWriter = context.getObjectWriter(jsonObject.getClass());
		Console.log(objectWriter.getClass());

		writer.close();
	}
}
