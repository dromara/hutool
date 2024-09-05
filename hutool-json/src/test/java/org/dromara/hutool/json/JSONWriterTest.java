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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JSONWriterTest {

	@Test
	public void writeDateTest() {
		final JSONObject jsonObject = JSONUtil.ofObj(JSONConfig.of().setDateFormat("yyyy-MM-dd"))
				.set("date", DateUtil.parse("2022-09-30"));

		// 日期原样写入
		final Date date = jsonObject.getDate("date");
		Assertions.assertEquals("2022-09-30 00:00:00", date.toString());

		// 自定义日期格式生效
		Assertions.assertEquals("{\"date\":\"2022-09-30\"}", jsonObject.toString());

		// 自定义日期格式解析生效
		final JSONObject parse = JSONUtil.parseObj(jsonObject.toString(), JSONConfig.of().setDateFormat("yyyy-MM-dd"));
		Assertions.assertEquals(String.class, parse.get("date").getClass());
	}
}
