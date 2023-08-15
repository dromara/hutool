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

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.MapBuilder;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssueI6ZF6KTest {

	@Test
	void buildQueryTest() {
		final String json = "{\"keyword\":\"国际 英语 c&b\",\"anyKeyword\":\"1\"}";
		final Map<String, Object> form = MapBuilder.<String, Object>of()
			.put("condition", json)
			.build();
		final String requestBody = URLUtil.buildQuery(form, CharsetUtil.UTF_8);
		Console.log(requestBody);
	}
}
