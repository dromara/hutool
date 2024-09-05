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

package org.dromara.hutool.core.date.chinese;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI97WU6Test {
	@Test
	public void getTermTest() {
		// 润十月没有三十，十月有三十
		final ChineseDate chineseDate = new ChineseDate(1984, 10, 30, false);
		Assertions.assertEquals("农历甲子鼠年寒月三十", chineseDate.toString());
		Assertions.assertEquals("小雪", chineseDate.getTerm());
	}
}
