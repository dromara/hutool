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

package org.dromara.hutool.json.issues;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONUtil;
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
		final String str = StrUtil.repeat("[", 1500) + StrUtil.repeat("]", 1500);
		JSONUtil.parseArray(str);
	}
}