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

package org.dromara.hutool.core.lang.range;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * test for {@link BoundType}
 */
public class BoundTypeTest {

	@Test
	public void testIsDislocated() {
		Assertions.assertTrue(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.CLOSE_UPPER_BOUND));
		Assertions.assertTrue(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.OPEN_UPPER_BOUND));
		Assertions.assertFalse(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.CLOSE_LOWER_BOUND));
		Assertions.assertFalse(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.OPEN_LOWER_BOUND));
	}

	@Test
	public void testIsLowerBound() {
		Assertions.assertFalse(BoundType.CLOSE_UPPER_BOUND.isLowerBound());
		Assertions.assertFalse(BoundType.OPEN_UPPER_BOUND.isLowerBound());
		Assertions.assertTrue(BoundType.CLOSE_LOWER_BOUND.isLowerBound());
		Assertions.assertTrue(BoundType.OPEN_LOWER_BOUND.isLowerBound());
	}

	@Test
	public void testIsUpperBound() {
		Assertions.assertTrue(BoundType.CLOSE_UPPER_BOUND.isUpperBound());
		Assertions.assertTrue(BoundType.OPEN_UPPER_BOUND.isUpperBound());
		Assertions.assertFalse(BoundType.CLOSE_LOWER_BOUND.isUpperBound());
		Assertions.assertFalse(BoundType.OPEN_LOWER_BOUND.isUpperBound());
	}

	@Test
	public void testIsOpen() {
		Assertions.assertFalse(BoundType.CLOSE_UPPER_BOUND.isOpen());
		Assertions.assertTrue(BoundType.OPEN_UPPER_BOUND.isOpen());
		Assertions.assertFalse(BoundType.CLOSE_LOWER_BOUND.isOpen());
		Assertions.assertTrue(BoundType.OPEN_LOWER_BOUND.isOpen());
	}

	@Test
	public void testIsClose() {
		Assertions.assertTrue(BoundType.CLOSE_UPPER_BOUND.isClose());
		Assertions.assertFalse(BoundType.OPEN_UPPER_BOUND.isClose());
		Assertions.assertTrue(BoundType.CLOSE_LOWER_BOUND.isClose());
		Assertions.assertFalse(BoundType.OPEN_LOWER_BOUND.isClose());
	}

	@Test
	public void testNegate() {
		Assertions.assertEquals(BoundType.CLOSE_UPPER_BOUND, BoundType.OPEN_LOWER_BOUND.negate());
		Assertions.assertEquals(BoundType.OPEN_UPPER_BOUND, BoundType.CLOSE_LOWER_BOUND.negate());
		Assertions.assertEquals(BoundType.OPEN_LOWER_BOUND, BoundType.CLOSE_UPPER_BOUND.negate());
		Assertions.assertEquals(BoundType.CLOSE_LOWER_BOUND, BoundType.OPEN_UPPER_BOUND.negate());
	}

	@Test
	public void testGetSymbol() {
		Assertions.assertEquals("]", BoundType.CLOSE_UPPER_BOUND.getSymbol());
		Assertions.assertEquals(")", BoundType.OPEN_UPPER_BOUND.getSymbol());
		Assertions.assertEquals("[", BoundType.CLOSE_LOWER_BOUND.getSymbol());
		Assertions.assertEquals("(", BoundType.OPEN_LOWER_BOUND.getSymbol());
	}

	@Test
	public void testGetCode() {
		Assertions.assertEquals(2, BoundType.CLOSE_UPPER_BOUND.getCode());
		Assertions.assertEquals(1, BoundType.OPEN_UPPER_BOUND.getCode());
		Assertions.assertEquals(-1, BoundType.OPEN_LOWER_BOUND.getCode());
		Assertions.assertEquals(-2, BoundType.CLOSE_LOWER_BOUND.getCode());
	}

	@Test
	public void testGetOperator() {
		Assertions.assertEquals("<=", BoundType.CLOSE_UPPER_BOUND.getOperator());
		Assertions.assertEquals("<", BoundType.OPEN_UPPER_BOUND.getOperator());
		Assertions.assertEquals(">", BoundType.OPEN_LOWER_BOUND.getOperator());
		Assertions.assertEquals(">=", BoundType.CLOSE_LOWER_BOUND.getOperator());
	}

}
