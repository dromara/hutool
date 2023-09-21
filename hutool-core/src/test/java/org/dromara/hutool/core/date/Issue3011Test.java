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

import java.util.Calendar;

public class Issue3011Test {
	@Test
	public void isSameMonthTest() {
		// https://github.com/dromara/hutool/issues/3011
		// 判断是否同一个月，还需考虑公元前和公元后的的情况
		// 此处公元前2020年和公元2021年返回年都是2021
		final Calendar calendar1 = Calendar.getInstance();
		calendar1.set(-2020, Calendar.FEBRUARY, 12);

		final Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2021, Calendar.FEBRUARY, 12);


		Assertions.assertFalse(DateUtil.isSameMonth(calendar1, calendar2));
	}
}
