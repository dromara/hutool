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
 * test for {@link BoundedRange}
 */
public class BoundedRangeTest {

	@Test
	public void testEquals() {
		final BoundedRange<Integer> range = new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		);
		Assertions.assertNotEquals(range, null);
		Assertions.assertEquals(range, new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		));
		Assertions.assertNotEquals(range, new BoundedRange<>(
			Bound.greaterThan(1), Bound.lessThan(10)
		));
	}

	@Test
	public void testHashCode() {
		final int hasCode = new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		).hashCode();
		Assertions.assertEquals(hasCode, new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		).hashCode());
		Assertions.assertNotEquals(hasCode, new BoundedRange<>(
			Bound.greaterThan(1), Bound.lessThan(10)
		).hashCode());
	}

	@Test
	public void testAll() {
		final BoundedRange<Integer> range = BoundedRange.all();
		Assertions.assertEquals("(-∞, +∞)", range.toString());

		// getBound
		Assertions.assertFalse(range.hasLowerBound());
		Assertions.assertFalse(range.hasUpperBound());
		Assertions.assertNull(range.getLowerBoundValue());
		Assertions.assertNull(range.getUpperBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertTrue(range.test(Integer.MAX_VALUE));
		Assertions.assertTrue(range.test(Integer.MIN_VALUE));

		// isXXX
		Assertions.assertFalse(range.isDisjoint(BoundedRange.open(0, 5)));
		Assertions.assertNotEquals(range, BoundedRange.open(0, 5));
		Assertions.assertTrue(range.isIntersected(BoundedRange.open(0, 5)));
		Assertions.assertTrue(range.isIntersected(range));
		Assertions.assertFalse(range.isSubset(BoundedRange.open(0, 5)));
		Assertions.assertTrue(range.isSubset(range));
		Assertions.assertFalse(range.isProperSubset(BoundedRange.open(0, 5)));
		Assertions.assertFalse(range.isProperSubset(range));
		Assertions.assertTrue(range.isSuperset(BoundedRange.open(0, 5)));
		Assertions.assertTrue(range.isSuperset(range));
		Assertions.assertTrue(range.isProperSuperset(BoundedRange.open(0, 5)));
		Assertions.assertFalse(range.isProperSuperset(range));

		// operate
		Assertions.assertEquals(range, range.unionIfIntersected(BoundedRange.open(0, 5)));
		Assertions.assertEquals(range, range.span(BoundedRange.open(0, 5)));
		Assertions.assertNull(range.gap(BoundedRange.open(0, 5)));
		Assertions.assertEquals(range, range.intersection(range));
		Assertions.assertEquals(BoundedRange.open(0, 5), range.intersection(BoundedRange.open(0, 5)));

		// sub
		Assertions.assertEquals("(0, +∞)", range.subGreatThan(0).toString());
		Assertions.assertEquals("[0, +∞)", range.subAtLeast(0).toString());
		Assertions.assertEquals("(-∞, 0)", range.subLessThan(0).toString());
		Assertions.assertEquals("(-∞, 0]", range.subAtMost(0).toString());
	}

	@Test
	public void testOpen() {
		final BoundedRange<Integer> range = BoundedRange.open(0, 5);
		Assertions.assertEquals("(0, 5)", range.toString());

		// getBound
		Assertions.assertTrue(range.hasLowerBound());
		Assertions.assertTrue(range.hasUpperBound());
		Assertions.assertEquals((Integer)0, range.getLowerBoundValue());
		Assertions.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertFalse(range.test(6));
		Assertions.assertFalse(range.test(5));
		Assertions.assertTrue(range.test(3));
		Assertions.assertFalse(range.test(0));
		Assertions.assertFalse(range.test(-1));

		// isXXX
		Assertions.assertTrue(range.isDisjoint(BoundedRange.open(-5, 0))); // (-5, 0)
		Assertions.assertTrue(range.isDisjoint(BoundedRange.close(-5, 0))); // [-5, 0]
		Assertions.assertTrue(range.isIntersected(BoundedRange.close(-5, 1))); // [-5, 1]
		Assertions.assertTrue(range.isSubset(BoundedRange.close(0, 5))); // [0, 5]
		Assertions.assertTrue(range.isProperSubset(BoundedRange.close(0, 5))); // [0, 5]
		Assertions.assertFalse(range.isSuperset(BoundedRange.close(0, 5))); // [0, 5]
		Assertions.assertFalse(range.isProperSuperset(BoundedRange.close(0, 5))); // [0, 5]

		// operate
		Assertions.assertEquals("(0, 10]", range.unionIfIntersected(BoundedRange.close(4, 10)).toString());
		Assertions.assertEquals("(0, 10)", range.span(BoundedRange.open(9, 10)).toString());
		Assertions.assertEquals("(-2, 0]", range.gap(BoundedRange.close(-10, -2)).toString());
		Assertions.assertEquals("(3, 5)", range.intersection(BoundedRange.open(3, 10)).toString());

		// sub
		Assertions.assertEquals("(3, 5)", range.subGreatThan(3).toString());
		Assertions.assertEquals("[3, 5)", range.subAtLeast(3).toString());
		Assertions.assertEquals("(0, 3)", range.subLessThan(3).toString());
		Assertions.assertEquals("(0, 3]", range.subAtMost(3).toString());
	}

	@Test
	public void testClose() {
		final BoundedRange<Integer> range = BoundedRange.close(0, 5);
		Assertions.assertEquals("[0, 5]", range.toString());

		// getBound
		Assertions.assertTrue(range.hasLowerBound());
		Assertions.assertTrue(range.hasUpperBound());
		Assertions.assertEquals((Integer)0, range.getLowerBoundValue());
		Assertions.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertFalse(range.test(6));
		Assertions.assertTrue(range.test(5));
		Assertions.assertTrue(range.test(0));
		Assertions.assertFalse(range.test(-1));

		// isXXX
		Assertions.assertTrue(range.isDisjoint(BoundedRange.open(-5, 0))); // (-5, 0)
		Assertions.assertTrue(range.isDisjoint(BoundedRange.close(-5, 0))); // [-5, 0]
		Assertions.assertTrue(range.isIntersected(BoundedRange.open(-5, 1))); // [-5, 1]
		Assertions.assertFalse(range.isSubset(BoundedRange.open(0, 5))); // (0, 5)
		Assertions.assertFalse(range.isProperSubset(BoundedRange.open(0, 5))); // (0, 5)
		Assertions.assertTrue(range.isSuperset(BoundedRange.open(0, 5))); // (0, 5)
		Assertions.assertTrue(range.isProperSuperset(BoundedRange.open(0, 5))); // (0, 5)

		// operate
		Assertions.assertEquals("[0, 5]", range.unionIfIntersected(BoundedRange.open(5, 10)).toString());
		Assertions.assertEquals("[0, 10]", range.unionIfIntersected(BoundedRange.close(4, 10)).toString());
		Assertions.assertEquals("[0, 10)", range.span(BoundedRange.open(9, 10)).toString());
		Assertions.assertEquals("(-2, 0)", range.gap(BoundedRange.close(-10, -2)).toString());
		Assertions.assertEquals("(3, 5]", range.intersection(BoundedRange.open(3, 10)).toString());
		Assertions.assertNull(range.intersection(BoundedRange.open(5, 10)));

		// sub
		Assertions.assertEquals("(3, 5]", range.subGreatThan(3).toString());
		Assertions.assertEquals("[3, 5]", range.subAtLeast(3).toString());
		Assertions.assertEquals("[0, 3)", range.subLessThan(3).toString());
		Assertions.assertEquals("[0, 3]", range.subAtMost(3).toString());
	}

	@Test
	public void testOpenClose() {
		final BoundedRange<Integer> range = BoundedRange.openClose(0, 5);
		Assertions.assertEquals("(0, 5]", range.toString());

		// getBound
		Assertions.assertTrue(range.hasLowerBound());
		Assertions.assertTrue(range.hasUpperBound());
		Assertions.assertEquals((Integer)0, range.getLowerBoundValue());
		Assertions.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertFalse(range.test(6));
		Assertions.assertTrue(range.test(5));
		Assertions.assertFalse(range.test(0));
		Assertions.assertFalse(range.test(-1));
	}

	@Test
	public void testCloseOpen() {
		final BoundedRange<Integer> range = BoundedRange.closeOpen(0, 5);
		Assertions.assertEquals("[0, 5)", range.toString());

		// getBound
		Assertions.assertTrue(range.hasLowerBound());
		Assertions.assertTrue(range.hasUpperBound());
		Assertions.assertEquals((Integer)0, range.getLowerBoundValue());
		Assertions.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertFalse(range.test(6));
		Assertions.assertFalse(range.test(5));
		Assertions.assertTrue(range.test(0));
		Assertions.assertFalse(range.test(-1));
	}

	@Test
	public void testGreatThan() {
		final BoundedRange<Integer> range = BoundedRange.greaterThan(0);
		Assertions.assertEquals("(0, +∞)", range.toString());

		// getBound
		Assertions.assertTrue(range.hasLowerBound());
		Assertions.assertFalse(range.hasUpperBound());
		Assertions.assertEquals((Integer)0, range.getLowerBoundValue());
		Assertions.assertNull(range.getUpperBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertTrue(range.test(6));
		Assertions.assertTrue(range.test(5));
		Assertions.assertFalse(range.test(0));
		Assertions.assertFalse(range.test(-1));
	}

	@Test
	public void testAtLeast() {
		final BoundedRange<Integer> range = BoundedRange.atLeast(0);
		Assertions.assertEquals("[0, +∞)", range.toString());

		// getBound
		Assertions.assertTrue(range.hasLowerBound());
		Assertions.assertFalse(range.hasUpperBound());
		Assertions.assertEquals((Integer)0, range.getLowerBoundValue());
		Assertions.assertNull(range.getUpperBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertTrue(range.test(6));
		Assertions.assertTrue(range.test(5));
		Assertions.assertTrue(range.test(0));
		Assertions.assertFalse(range.test(-1));
	}

	@Test
	public void testLessThan() {
		final BoundedRange<Integer> range = BoundedRange.lessThan(5);
		Assertions.assertEquals("(-∞, 5)", range.toString());

		// getBound
		Assertions.assertTrue(range.hasUpperBound());
		Assertions.assertFalse(range.hasLowerBound());
		Assertions.assertEquals((Integer)5, range.getUpperBoundValue());
		Assertions.assertNull(range.getLowerBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertFalse(range.test(6));
		Assertions.assertFalse(range.test(5));
		Assertions.assertTrue(range.test(0));
		Assertions.assertTrue(range.test(-1));
	}

	@Test
	public void testAtMost() {
		final BoundedRange<Integer> range = BoundedRange.atMost(5);
		Assertions.assertEquals("(-∞, 5]", range.toString());

		// getBound
		Assertions.assertTrue(range.hasUpperBound());
		Assertions.assertFalse(range.hasLowerBound());
		Assertions.assertEquals((Integer)5, range.getUpperBoundValue());
		Assertions.assertNull(range.getLowerBoundValue());

		// test
		Assertions.assertFalse(range.isEmpty());
		Assertions.assertFalse(range.test(6));
		Assertions.assertTrue(range.test(5));
		Assertions.assertTrue(range.test(0));
		Assertions.assertTrue(range.test(-1));
	}

}
