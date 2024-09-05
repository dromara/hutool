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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NumberParserTest {
	@Test
	void parseLongTest() {
		final long value = NumberParser.INSTANCE.parseLong("0.a");
		Assertions.assertEquals(0L, value);
	}

	@Test
	void testParseIntWithValidInputs() {
		// Test with various valid inputs
		assertEquals(123, NumberParser.INSTANCE.parseInt("123".toCharArray(), 10));
		assertEquals(-123, NumberParser.INSTANCE.parseInt("-123".toCharArray(), 10));
		assertEquals(123, NumberParser.INSTANCE.parseInt("+123".toCharArray(), 10));
		assertEquals(0, NumberParser.INSTANCE.parseInt("0".toCharArray(), 10));
		assertEquals(255, NumberParser.INSTANCE.parseInt("ff".toCharArray(), 16));
		assertEquals(-255, NumberParser.INSTANCE.parseInt("-ff".toCharArray(), 16));
	}

	@Test
	void testParseIntWithInvalidInputs() {
		// Test with various invalid inputs
		assertThrows(IllegalArgumentException.class, () -> NumberParser.INSTANCE.parseInt("".toCharArray(), 10));
		assertThrows(NumberFormatException.class, () -> NumberParser.INSTANCE.parseInt("1234".toCharArray(), 2));
		assertThrows(NumberFormatException.class, () -> NumberParser.INSTANCE.parseInt("abc".toCharArray(), 10));
		assertThrows(NumberFormatException.class, () -> NumberParser.INSTANCE.parseInt("--123".toCharArray(), 10));
		assertThrows(NumberFormatException.class, () -> NumberParser.INSTANCE.parseInt("++123".toCharArray(), 10));
		assertThrows(NumberFormatException.class, () -> NumberParser.INSTANCE.parseInt("123".toCharArray(), 1));
	}

	@Test
	void testParseIntWithLeadingAndTrailingWhitespace() {
		// Test with leading and trailing whitespace
		assertEquals(42, NumberParser.INSTANCE.parseInt("  42  ".toCharArray(), 10));
	}

	@Test
	void testParseIntWithMaxAndMinInt() {
		// Test with values at the edge of int range
		final char[] maxIntStr = Integer.toString(Integer.MAX_VALUE).toCharArray();
		final char[] minIntStr = Integer.toString(Integer.MIN_VALUE).toCharArray();

		assertEquals(Integer.MAX_VALUE, NumberParser.INSTANCE.parseInt(maxIntStr, 10));
		assertEquals(Integer.MIN_VALUE, NumberParser.INSTANCE.parseInt(minIntStr, 10));
	}
}
