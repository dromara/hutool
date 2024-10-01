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

package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONFactoryTest {
	@Test
	void parseFromStringBuilderTest() {
		final String jsonStr = "{\"name\":\"张三\"}";
		final JSON parse = JSONFactory.getInstance().parse(new StringBuilder(jsonStr));
		Assertions.assertEquals(JSONObject.class, parse.getClass());
	}

	@Test
	void parseFromStringTest() {
		final String jsonStr = "{\"name\":\"张三\"}";
		final JSON parse = JSONFactory.getInstance().parse(jsonStr);
		Assertions.assertEquals(JSONObject.class, parse.getClass());
	}
}
