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

package cn.hutool.json;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.TypeReference;
import org.junit.Assert;
import org.junit.Test;

public class IssueI7GPGXTest {

	@Test
	public void toBeanTest() {
		final Pair<String, Boolean> hutoolPair = new Pair<>("test1", true);
		final String a = JSONUtil.toJsonStr(hutoolPair);
		final Pair<String, Boolean> pair = JSONUtil.toBean(a, new TypeReference<Pair<String, Boolean>>() {}, false);
		Assert.assertEquals(hutoolPair, pair);
	}
}
