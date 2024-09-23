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

package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Issue2377Test {

	@Test
	void toListTest() {
		final String jsonStr = "[1,[10,11], \"报表.xlsx\"]";
		final JSONArray array = JSONUtil.parseArray(jsonStr);
		final List<Object> paramList = JSONUtil.toList(array, Object.class);
		Assertions.assertEquals(JSONArray.class, paramList.get(1).getClass());
	}

	@Test
	public void bytesTest() {
		final Object[] paramArray = new Object[]{1, new byte[]{10, 11}, "报表.xlsx"};
		final String paramsStr = JSONUtil.toJsonStr(paramArray);
		Assertions.assertEquals("[1,[10,11],\"报表.xlsx\"]", paramsStr);

		final JSONArray array = JSONUtil.parseArray(paramsStr);
		final List<Object> paramList = JSONUtil.toList(array, Object.class);

		Assertions.assertEquals(JSONArray.class, paramList.get(1).getClass());
		final String paramBytesStr = JSONUtil.toJsonStr(paramList.get(1));
		Assertions.assertEquals("[10,11]", paramBytesStr);

		final byte[] paramBytes = JSONUtil.toBean(paramBytesStr, byte[].class);
		Assertions.assertArrayEquals((byte[]) paramArray[1], paramBytes);
	}
}
