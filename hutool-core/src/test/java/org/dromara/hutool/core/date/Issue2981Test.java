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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2981Test {
	/**
	 * https://github.com/dromara/hutool/issues/2981<br>
	 * 按照ISO8601规范，以Z结尾表示UTC时间，否则为当地时间
	 */
	@SuppressWarnings("DataFlowIssue")
	@Test
	public void parseUTCTest() {
		final String str1 = "2019-01-01T00:00:00.000Z";
		final String str2 = "2019-01-01T00:00:00.000";
		final String str3 = "2019-01-01 00:00:00.000";

		Assertions.assertEquals(1546300800000L, DateUtil.parse(str1).getTime());
		Assertions.assertEquals(1546272000000L, DateUtil.parse(str2).getTime());
		Assertions.assertEquals(1546272000000L, DateUtil.parse(str3).getTime());
	}
}
