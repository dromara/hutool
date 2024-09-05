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

package org.dromara.hutool.core.math;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class Issue3423Test {
	@Test
	public void toBigDecimalOfNaNTest() {
		final BigDecimal naN = NumberUtil.toBigDecimal("NaN");
		Assertions.assertEquals(BigDecimal.ZERO, naN);
	}

	@Test
	@Disabled
	public void toBigDecimalOfNaNTest2() throws ParseException {
		final NumberFormat format = NumberFormat.getInstance();
		((DecimalFormat) format).setParseBigDecimal(true);
		final Number naN = format.parse("NaN");
		Console.log(naN.getClass());
	}
}
