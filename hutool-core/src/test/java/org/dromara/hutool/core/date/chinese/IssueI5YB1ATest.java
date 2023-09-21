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

package org.dromara.hutool.core.date.chinese;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI5YB1ATest {
	@Test
	public void chineseDateTest() {
		// 四月非闰月，因此isLeapMonth参数无效
		final ChineseDate date = new ChineseDate(2023, 4, 8, true);
		Assertions.assertEquals("2023-05-26 00:00:00", date.getGregorianDate().toString());
	}
}
