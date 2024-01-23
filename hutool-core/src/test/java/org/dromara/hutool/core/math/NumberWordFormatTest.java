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

import org.dromara.hutool.core.math.NumberWordFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberWordFormatTest {

	@Test
	public void formatTest() {
		final String format = NumberWordFormatter.format(100.23);
		Assertions.assertEquals("ONE HUNDRED AND CENTS TWENTY THREE ONLY", format);

		final String format2 = NumberWordFormatter.format("2100.00");
		Assertions.assertEquals("TWO THOUSAND ONE HUNDRED AND CENTS  ONLY", format2);

		final String format3 = NumberWordFormatter.format("1234567890123.12");
		Assertions.assertEquals("ONE TRILLION TWO HUNDRED AND THIRTY FOUR BILLION FIVE HUNDRED AND SIXTY SEVEN MILLION EIGHT HUNDRED AND NINETY THOUSAND ONE HUNDRED AND TWENTY THREE AND CENTS TWELVE ONLY", format3);
	}

	@Test
	public void formatSimpleTest() {
		final String format1 = NumberWordFormatter.formatSimple(1200, false);
		Assertions.assertEquals("1.2k", format1);

		final String format2 = NumberWordFormatter.formatSimple(4384324, false);
		Assertions.assertEquals("4.38m", format2);

		final String format3 = NumberWordFormatter.formatSimple(4384324, true);
		Assertions.assertEquals("438.43w", format3);

		final String format4 = NumberWordFormatter.formatSimple(4384324);
		Assertions.assertEquals("438.43w", format4);

		final String format5 = NumberWordFormatter.formatSimple(438);
		Assertions.assertEquals("438", format5);
	}

	@Test
	public void formatSimpleTest2(){
		final String s = NumberWordFormatter.formatSimple(1000);
		Assertions.assertEquals("1k", s);
	}
}
