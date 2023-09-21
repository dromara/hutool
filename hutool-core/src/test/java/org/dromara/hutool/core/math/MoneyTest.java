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

package org.dromara.hutool.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MoneyTest {

	@Test
	public void yuanToCentTest() {
		final Money money = new Money("1234.56");
		Assertions.assertEquals(123456, money.getCent());

		Assertions.assertEquals(123456, MathUtil.yuanToCent(1234.56));
	}

	@Test
	public void centToYuanTest() {
		final Money money = new Money(1234, 56);
		Assertions.assertEquals(1234.56D, money.getAmount().doubleValue(), 0);

		Assertions.assertEquals(1234.56D, MathUtil.centToYuan(123456), 0);
	}
}
