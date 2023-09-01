/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

public class IssueI7XMYWTest {
	@Test
	public void ageTest() {
		DateTime date1 = DateUtil.parse("2023-08-31");
		Assert.assertEquals(49, DateUtil.age(DateUtil.parse("1973-08-31"), date1));

		date1 = DateUtil.parse("2023-08-30");
		Assert.assertEquals(49, DateUtil.age(DateUtil.parse("1973-08-30"), date1));
	}
}
