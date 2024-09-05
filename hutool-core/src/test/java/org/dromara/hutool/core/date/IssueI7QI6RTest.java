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

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI7QI6RTest {

	// TODO 2023-8-4-1解析错误解决
	@Test
	@Disabled
	void parseTest() {
		Console.log(DateUtil.parse("2023-8-4-1"));
		Assertions.assertThrows(DateException.class, () -> DateUtil.parse("2023-8-4-1"));
		Assertions.assertThrows(DateException.class, () -> DateUtil.parse("2023-8-4 1"));
	}
}
