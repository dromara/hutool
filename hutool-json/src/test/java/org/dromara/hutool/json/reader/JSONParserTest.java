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

package org.dromara.hutool.json.reader;

import org.dromara.hutool.json.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JSONParserTest {
	@Test
	void parseTest() {
		final String jsonStr = " {\"a\": 1} ";
		final JSONParser jsonParser = JSONParser.of(new JSONTokener(jsonStr, true), JSONFactory.getInstance());
		final JSON parse = jsonParser.parse();
		Assertions.assertEquals("{\"a\":1}", parse.toString());
	}

	@Test
	void parseToTest() {
		final String jsonStr = "{\"a\": 1}";

		final JSONObject jsonObject = JSONUtil.ofObj();
		JSONParser.of(new JSONTokener(jsonStr, true), JSONFactory.getInstance()).parseTo(jsonObject);
		Assertions.assertEquals("{\"a\":1}", jsonObject.toString());
	}

	@Test
	void parseToArrayTest() {
		final String jsonStr = "[{},2,3]";
		final JSONParser jsonParser = JSONParser.of(new JSONTokener(jsonStr, true), JSONFactory.getInstance());
		final JSONArray jsonArray = new JSONArray();
		jsonParser.parseTo(jsonArray);

		Assertions.assertEquals(jsonStr, jsonArray.toString());
	}
}
