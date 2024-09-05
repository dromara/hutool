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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class ConvertToNumberTest {
	@Test
	public void dateToLongTest(){
		final Date date = DateUtil.parse("2020-05-17 12:32:00");
		final Long dateLong = ConvertUtil.toLong(date);
		assert date != null;
		Assertions.assertEquals(date.getTime(), dateLong.longValue());
	}

	@Test
	public void dateToIntTest(){
		final Date date = DateUtil.parse("2020-05-17 12:32:00");
		final Integer dateInt = ConvertUtil.toInt(date);
		assert date != null;
		Assertions.assertEquals((int)date.getTime(), dateInt.intValue());
	}

	@Test
	public void dateToAtomicLongTest(){
		final Date date = DateUtil.parse("2020-05-17 12:32:00");
		final AtomicLong dateLong = ConvertUtil.convert(AtomicLong.class, date);
		assert date != null;
		Assertions.assertEquals(date.getTime(), dateLong.longValue());
	}

	@Test
	public void toBigDecimalTest(){
		BigDecimal bigDecimal = ConvertUtil.toBigDecimal("1.1f");
		Assertions.assertEquals(1.1f, bigDecimal.floatValue(), 0);

		bigDecimal = ConvertUtil.toBigDecimal("1L");
		Assertions.assertEquals(1L, bigDecimal.longValue());
	}

	@Test
	public void toNumberTest(){
		// 直接转换为抽象Number，默认使用BigDecimal实现
		final Number number = ConvertUtil.toNumber("1");
		Assertions.assertEquals(BigDecimal.class, number.getClass());
	}
}
