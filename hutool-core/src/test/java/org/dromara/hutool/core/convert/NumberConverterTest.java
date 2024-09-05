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

import org.dromara.hutool.core.convert.impl.NumberConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberConverterTest {

	@Test
	public void toDoubleTest(){
		final NumberConverter numberConverter = new NumberConverter();
		final Number convert = numberConverter.convert(Double.class, "1,234.55", null);
		Assertions.assertEquals(1234.55D, convert);
	}

	@Test
	public void toIntegerTest(){
		final NumberConverter numberConverter = new NumberConverter();
		final Number convert = numberConverter.convert(Integer.class, "1,234.55", null);
		Assertions.assertEquals(1234, convert);
	}
}
