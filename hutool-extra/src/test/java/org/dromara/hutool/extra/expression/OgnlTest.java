/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.expression;

import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.extra.expression.engine.ognl.OgnlEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OgnlTest {

	@Test
	void evalTest() {
		final ExpressionEngine engine = new OgnlEngine();
		final Dict dict = Dict.of()
			.set("a", 100.3)
			.set("b", 45)
			.set("c", -199.100);
		final Object eval = engine.eval("#a-(#b-#c)", dict);
		Assertions.assertEquals(-143.8, (double)eval, 0);
	}
}
