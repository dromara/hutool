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

package org.dromara.hutool.core.lang.range;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * test for {@link Bound}
 */
@SuppressWarnings("EqualsWithItself")
public class BoundTest {

	@Test
	public void testEquals() {
		final Bound<Integer> bound = new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND);
		Assertions.assertEquals(bound, bound);
		Assertions.assertEquals(bound, new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND));
		Assertions.assertNotEquals(bound, new FiniteBound<>(2, BoundType.OPEN_UPPER_BOUND));
		Assertions.assertNotEquals(bound, new FiniteBound<>(1, BoundType.OPEN_LOWER_BOUND));
		Assertions.assertNotEquals(bound, null);
	}

	@Test
	public void testHashCode() {
		final int hashCode = new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND).hashCode();
		Assertions.assertEquals(hashCode, new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND).hashCode());
		Assertions.assertNotEquals(hashCode, new FiniteBound<>(2, BoundType.OPEN_UPPER_BOUND).hashCode());
		Assertions.assertNotEquals(hashCode, new FiniteBound<>(1, BoundType.OPEN_LOWER_BOUND).hashCode());
	}

	@Test
	public void testNoneLowerBound() {
		final Bound<Integer> bound = Bound.noneLowerBound();
		// negate
		Assertions.assertEquals(bound, bound.negate());
		// test
		Assertions.assertTrue(bound.test(Integer.MAX_VALUE));
		// getType
		Assertions.assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());
		// getValue
		Assertions.assertNull(bound.getValue());
		// toString
		Assertions.assertEquals("(" + "-∞", bound.descBound());
		// compareTo
		Assertions.assertEquals(0, bound.compareTo(bound));
		Assertions.assertEquals(-1, bound.compareTo(Bound.atMost(1)));

		Assertions.assertEquals(BoundedRange.all(), bound.toRange());
		Assertions.assertEquals("{x | x > -∞}", bound.toString());
	}

	@Test
	public void testNoneUpperBound() {
		final Bound<Integer> bound = Bound.noneUpperBound();
		// negate
		Assertions.assertEquals(bound, bound.negate());
		// test
		Assertions.assertTrue(bound.test(Integer.MAX_VALUE));
		// getType
		Assertions.assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());
		// getValue
		Assertions.assertNull(bound.getValue());
		// toString
		Assertions.assertEquals("+∞" + ")", bound.descBound());
		// compareTo
		Assertions.assertEquals(0, bound.compareTo(bound));
		Assertions.assertEquals(1, bound.compareTo(Bound.atMost(1)));

		Assertions.assertEquals(BoundedRange.all(), bound.toRange());
		Assertions.assertEquals("{x | x < +∞}", bound.toString());
	}

	@Test
	public void testGreatThan() {
		// { x | x > 0}
		Bound<Integer> bound = Bound.greaterThan(0);

		// test
		Assertions.assertTrue(bound.test(1));
		Assertions.assertFalse(bound.test(0));
		Assertions.assertFalse(bound.test(-1));
		// getType
		Assertions.assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());
		// getValue
		Assertions.assertEquals((Integer)0, bound.getValue());
		// toString
		Assertions.assertEquals("(0", bound.descBound());
		Assertions.assertEquals("{x | x > 0}", bound.toString());

		// compareTo
		Assertions.assertEquals(0, bound.compareTo(bound));
		Assertions.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assertions.assertEquals(1, bound.compareTo(Bound.atLeast(-1)));
		Assertions.assertEquals(-1, bound.compareTo(Bound.atLeast(2)));
		Assertions.assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		Assertions.assertEquals(1, bound.compareTo(Bound.atMost(0)));
		Assertions.assertEquals(-1, bound.compareTo(Bound.atMost(2)));
		Assertions.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x >= 0}
		bound = bound.negate();
		Assertions.assertEquals((Integer)0, bound.getValue());
		Assertions.assertEquals(BoundType.CLOSE_UPPER_BOUND, bound.getType());

		Assertions.assertNotNull(bound.toRange());
	}

	@Test
	public void testAtLeast() {
		// { x | x >= 0}
		Bound<Integer> bound = Bound.atLeast(0);

		// test
		Assertions.assertTrue(bound.test(1));
		Assertions.assertTrue(bound.test(0));
		Assertions.assertFalse(bound.test(-1));
		// getType
		Assertions.assertEquals(BoundType.CLOSE_LOWER_BOUND, bound.getType());
		// getValue
		Assertions.assertEquals((Integer)0, bound.getValue());
		// toString
		Assertions.assertEquals("[0", bound.descBound());
		Assertions.assertEquals("{x | x >= 0}", bound.toString());

		// compareTo
		Assertions.assertEquals(0, bound.compareTo(bound));
		Assertions.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assertions.assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		Assertions.assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		Assertions.assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		Assertions.assertEquals(1, bound.compareTo(Bound.atMost(0)));
		Assertions.assertEquals(-1, bound.compareTo(Bound.atMost(2)));
		Assertions.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x < 0}
		bound = bound.negate();
		Assertions.assertEquals((Integer)0, bound.getValue());
		Assertions.assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());

		Assertions.assertNotNull(bound.toRange());
	}

	@Test
	public void testLessThan() {
		// { x | x < 0}
		Bound<Integer> bound = Bound.lessThan(0);

		// test
		Assertions.assertFalse(bound.test(1));
		Assertions.assertFalse(bound.test(0));
		Assertions.assertTrue(bound.test(-1));
		// getType
		Assertions.assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());
		// getValue
		Assertions.assertEquals((Integer)0, bound.getValue());
		// toString
		Assertions.assertEquals("0)", bound.descBound());
		Assertions.assertEquals("{x | x < 0}", bound.toString());

		// compareTo
		Assertions.assertEquals(0, bound.compareTo(bound));
		Assertions.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assertions.assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		Assertions.assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		Assertions.assertEquals(1, bound.compareTo(Bound.lessThan(-1)));
		Assertions.assertEquals(-1, bound.compareTo(Bound.atMost(0)));
		Assertions.assertEquals(1, bound.compareTo(Bound.atMost(-1)));
		Assertions.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x >= 0}
		bound = bound.negate();
		Assertions.assertEquals((Integer)0, bound.getValue());
		Assertions.assertEquals(BoundType.CLOSE_LOWER_BOUND, bound.getType());

		Assertions.assertNotNull(bound.toRange());
	}

	@Test
	public void testAtMost() {
		// { x | x <= 0}
		Bound<Integer> bound = Bound.atMost(0);

		// test
		Assertions.assertFalse(bound.test(1));
		Assertions.assertTrue(bound.test(0));
		Assertions.assertTrue(bound.test(-1));
		// getType
		Assertions.assertEquals(BoundType.CLOSE_UPPER_BOUND, bound.getType());
		// getValue
		Assertions.assertEquals((Integer)0, bound.getValue());
		// toString
		Assertions.assertEquals("0]", bound.descBound());
		Assertions.assertEquals("{x | x <= 0}", bound.toString());

		// compareTo
		Assertions.assertEquals(0, bound.compareTo(bound));
		Assertions.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assertions.assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		Assertions.assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		Assertions.assertEquals(1, bound.compareTo(Bound.atMost(-1)));
		Assertions.assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		Assertions.assertEquals(1, bound.compareTo(Bound.lessThan(-1)));
		Assertions.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x > 0}
		bound = bound.negate();
		Assertions.assertEquals((Integer)0, bound.getValue());
		Assertions.assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());

		Assertions.assertNotNull(bound.toRange());
	}

}
