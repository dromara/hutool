/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
