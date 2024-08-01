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

package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.map.MapUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class Issue3681Test {
	@Test
	void toJsonStrOfOptionalTest() {
		String abc = JSONUtil.toJsonStr(Optional.of("abc"));
		Assertions.assertEquals("\"abc\"", abc);

		abc = JSONUtil.toJsonStr(Optional.of("123"));
		Assertions.assertEquals("123", abc);
	}

	@Test
	void toJsonStrOfOptionalTest2() {
		final String abc = JSONUtil.toJsonStr(Optional.of(MapUtil.of("a", 1)));
		Assertions.assertEquals("{\"a\":1}", abc);
	}

	@Test
	void toJsonStrOfOptTest() {
		String abc = JSONUtil.toJsonStr(Opt.of("abc"));
		Assertions.assertEquals("\"abc\"", abc);

		abc = JSONUtil.toJsonStr(Opt.of("123"));
		Assertions.assertEquals("123", abc);
	}
}
