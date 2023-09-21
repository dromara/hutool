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

import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssuesI4V14NTest {

	@Test
	public void parseTest(){
		final String str = "{\"A\" : \"A\\nb\"}";
		final JSONObject jsonObject = JSONUtil.parseObj(str);
		Assertions.assertEquals("A\nb", jsonObject.getStr("A"));

		final Map<String, String> map = jsonObject.toBean(new TypeReference<Map<String, String>>() {});
		Assertions.assertEquals("A\nb", map.get("A"));
	}
}
