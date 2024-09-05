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

package org.dromara.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MapBuilderTest {

	@Test
	public void conditionPutTest() {
		final Map<String, String> map = MapBuilder.<String, String>of()
				.put(true, "a", "1")
				.put(false, "b", "2")
				.put(true, "c", () -> getValue(3))
				.put(false, "d", () -> getValue(4))
				.build();

		Assertions.assertEquals(map.get("a"), "1");
		Assertions.assertFalse(map.containsKey("b"));
		Assertions.assertEquals(map.get("c"), "3");
		Assertions.assertFalse(map.containsKey("d"));
	}

	public String getValue(final int value) {
		return String.valueOf(value);
	}
}
