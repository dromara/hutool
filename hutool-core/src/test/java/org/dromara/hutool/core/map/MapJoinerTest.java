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

public class MapJoinerTest {

	@Test
	public void joinMapTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, null);

		Assertions.assertEquals("id-12+name-张三+age-23", joiner.toString());
	}

	@Test
	public void joinMapWithPredicateTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, (entry)->"age".equals(entry.getKey()));

		Assertions.assertEquals("age-23", joiner.toString());
	}
}
