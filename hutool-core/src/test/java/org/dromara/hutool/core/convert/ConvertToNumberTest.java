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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class ConvertToNumberTest {
	@Test
	public void dateToLongTest(){
		final DateTime date = DateUtil.parse("2020-05-17 12:32:00");
		final Long dateLong = Convert.toLong(date);
		assert date != null;
		Assertions.assertEquals(date.getTime(), dateLong.longValue());
	}

	@Test
	public void dateToIntTest(){
		final DateTime date = DateUtil.parse("2020-05-17 12:32:00");
		final Integer dateInt = Convert.toInt(date);
		assert date != null;
		Assertions.assertEquals((int)date.getTime(), dateInt.intValue());
	}

	@Test
	public void dateToAtomicLongTest(){
		final DateTime date = DateUtil.parse("2020-05-17 12:32:00");
		final AtomicLong dateLong = Convert.convert(AtomicLong.class, date);
		assert date != null;
		Assertions.assertEquals(date.getTime(), dateLong.longValue());
	}

	@Test
	public void toBigDecimalTest(){
		BigDecimal bigDecimal = Convert.toBigDecimal("1.1f");
		Assertions.assertEquals(1.1f, bigDecimal.floatValue(), 0);

		bigDecimal = Convert.toBigDecimal("1L");
		Assertions.assertEquals(1L, bigDecimal.longValue());
	}

	@Test
	public void toNumberTest(){
		// 直接转换为抽象Number，默认使用BigDecimal实现
		final Number number = Convert.toNumber("1");
		Assertions.assertEquals(BigDecimal.class, number.getClass());
	}
}
