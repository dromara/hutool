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

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.json.writer.NumberWriteMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueIALQ0NTest {

	@Test
	void toJsonStrTest() {
		final Map<String, Object> map = new HashMap<>();
		map.put("id", 1826166955313201152L);
		map.put("createdDate", DateUtil.parse("2024-08-24"));
		final String jsonStr = JSONUtil.toJsonStr(map, JSONConfig.of()
			.setDateFormat("yyyy-MM-dd HH:mm:ss")
			.setNumberWriteMode(NumberWriteMode.STRING));
		Assertions.assertEquals("{\"createdDate\":\"2024-08-24 00:00:00\",\"id\":\"1826166955313201152\"}", jsonStr);
	}
}
