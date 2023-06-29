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

package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI7GPGXTest {
	@Test
	public void pairToBeanTest() {
		final Pair<String, Boolean> hutoolPair = new Pair<>("test1", true);
		final String a = JSONUtil.toJsonStr(hutoolPair);
		final Pair<String, Boolean> pair = JSONUtil.toBean(a, new TypeReference<Pair<String, Boolean>>() {});
		Assertions.assertEquals(hutoolPair, pair);
	}
}
