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

import java.util.SortedMap;
import java.util.TreeMap;

public class Issue3058Test {
	@Test
	void toJsonStrTest() {
		final SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("attributes", "a");
				put("b", "b");
				put("c", "c");
			}};

		final String jsonStr = JSONUtil.toJsonStr(sortedMap);
		Assertions.assertEquals("{\"attributes\":\"a\",\"b\":\"b\",\"c\":\"c\"}", jsonStr);
	}
}
