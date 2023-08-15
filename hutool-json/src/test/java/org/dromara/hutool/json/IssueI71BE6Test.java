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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI71BE6Test {

	@Test
	void toArrayTest() {
		final String jsonStr = "[50.0,50.0,50.0,50.0]";

		final Double[] doubles = (Double[]) JSONUtil.parseArray(jsonStr).toArray(Double[].class);
		Assertions.assertArrayEquals(new Double[]{50.0,50.0,50.0,50.0}, doubles);
	}

	@Test
	void toBeanTest() {
		final String jsonStr = "[50.0,50.0,50.0,50.0]";

		final Double[] doubles = JSONUtil.toBean(jsonStr, Double[].class);
		Assertions.assertArrayEquals(new Double[]{50.0,50.0,50.0,50.0}, doubles);
	}
}
