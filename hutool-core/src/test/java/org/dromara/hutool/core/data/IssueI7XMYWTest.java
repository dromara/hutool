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

package org.dromara.hutool.core.data;

import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI7XMYWTest {

	@Test
	public void ageTest() {
		DateTime date1 = DateUtil.parse("2023-08-31");
		Assertions.assertEquals(49, DateUtil.age(DateUtil.parse("1973-08-31"), date1));

		date1 = DateUtil.parse("2023-08-30");
		Assertions.assertEquals(49, DateUtil.age(DateUtil.parse("1973-08-30"), date1));
	}
}
