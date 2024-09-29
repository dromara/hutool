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

import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue3619Test {
	@Test
	public void parseObjTest() {
		final String json = "{\"@timestamp\":\"2024-06-14T00:02:06.438Z\",\"@version\":\"1\",\"int_arr\":[-4]}";
		final JSONConfig jsonConfig = JSONConfig.of().setKeyComparator(String.CASE_INSENSITIVE_ORDER);
		final JSONObject jsonObject = JSONUtil.parseObj(json, jsonConfig);

		final String jsonStr = jsonObject.toJSONString(0, pair -> {
			final Object key = pair.getKey();
			if(key instanceof String){
				// 只有key为String时才检查并过滤，其它类型的key，如int类型的key跳过
				return key.toString().equals("int_arr");
			}else{
				return true;
			}
		});

		Assertions.assertEquals("{\"int_arr\":[-4]}", jsonStr);
	}
}
