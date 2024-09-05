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

package org.dromara.hutool.core.data.id;

import org.dromara.hutool.core.data.id.ObjectId;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

/**
 * ObjectId单元测试
 *
 * @author looly
 *
 */
public class ObjectIdTest {

	@Test
	public void distinctTest() {
		//生成10000个id测试是否重复
		final HashSet<String> set = new HashSet<>();
		for(int i = 0; i < 10000; i++) {
			set.add(ObjectId.next());
		}

		Assertions.assertEquals(10000, set.size());
	}

	@Test
	@Disabled
	public void nextTest() {
		Console.log(ObjectId.next());
	}
}
