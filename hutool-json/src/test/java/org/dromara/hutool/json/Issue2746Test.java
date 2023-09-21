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

import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2746Test {

	@Test
	public void parseObjTest() {
		final String str = StrUtil.repeat("{", 1500) + StrUtil.repeat("}", 1500);
		try{
			JSONUtil.parseObj(str);
		} catch (final JSONException e){
			Assertions.assertTrue(e.getMessage().startsWith("A JSONObject can not directly nest another JSONObject or JSONArray"));
		}
	}

	@Test
	public void parseTest() {
		Assertions.assertThrows(JSONException.class, ()->{
			final String str = StrUtil.repeat("[", 1500) + StrUtil.repeat("]", 1500);
			JSONUtil.parseArray(str);
		});
	}
}
