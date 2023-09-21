/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
