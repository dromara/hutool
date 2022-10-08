package cn.hutool.core.lang.range;

import org.junit.Assert;
import org.junit.Test;

/**
 * test for {@link BoundedRange}
 */
public class BoundedRangeTest {

	@Test
	public void testEquals() {
		final BoundedRange<Integer> range = new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		);
		Assert.assertEquals(range, range);
		Assert.assertNotEquals(range, null);
		Assert.assertEquals(range, new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		));
		Assert.assertNotEquals(range, new BoundedRange<>(
			Bound.greaterThan(1), Bound.lessThan(10)
		));
	}

	@Test
	public void testHashCode() {
		final int hasCode = new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		).hashCode();
		Assert.assertEquals(hasCode, new BoundedRange<>(
			Bound.greaterThan(0), Bound.lessThan(10)
		).hashCode());
		Assert.assertNotEquals(hasCode, new BoundedRange<>(
			Bound.greaterThan(1), Bound.lessThan(10)
		).hashCode());
	}

	@Test
	public void testAll() {
		final BoundedRange<Integer> range = BoundedRange.all();
		Assert.assertEquals("(-∞, +∞)", range.toString());

		// getBound
		Assert.assertFalse(range.hasLowerBound());
		Assert.assertFalse(range.hasUpperBound());
		Assert.assertNull(range.getLowerBoundValue());
		Assert.assertNull(range.getUpperBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertTrue(range.test(Integer.MAX_VALUE));
		Assert.assertTrue(range.test(Integer.MIN_VALUE));

		// isXXX
		Assert.assertFalse(range.isDisjoint(BoundedRange.open(0, 5)));
		Assert.assertTrue(range.isEquals(range));
		Assert.assertFalse(range.isEquals(BoundedRange.open(0, 5)));
		Assert.assertTrue(range.isIntersected(BoundedRange.open(0, 5)));
		Assert.assertTrue(range.isIntersected(range));
		Assert.assertFalse(range.isSubset(BoundedRange.open(0, 5)));
		Assert.assertTrue(range.isSubset(range));
		Assert.assertFalse(range.isProperSubset(BoundedRange.open(0, 5)));
		Assert.assertFalse(range.isProperSubset(range));
		Assert.assertTrue(range.isSuperset(BoundedRange.open(0, 5)));
		Assert.assertTrue(range.isSuperset(range));
		Assert.assertTrue(range.isProperSuperset(BoundedRange.open(0, 5)));
		Assert.assertFalse(range.isProperSuperset(range));

		// operate
		Assert.assertEquals(range, range.unionIfIntersected(BoundedRange.open(0, 5)));
		Assert.assertEquals(range, range.span(BoundedRange.open(0, 5)));
		Assert.assertNull(range.gap(BoundedRange.open(0, 5)));
		Assert.assertEquals(range, range.intersection(range));
		Assert.assertEquals(BoundedRange.open(0, 5), range.intersection(BoundedRange.open(0, 5)));

		// sub
		Assert.assertEquals("(0, +∞)", range.subGreatThan(0).toString());
		Assert.assertEquals("[0, +∞)", range.subAtLeast(0).toString());
		Assert.assertEquals("(-∞, 0)", range.subLessThan(0).toString());
		Assert.assertEquals("(-∞, 0]", range.subAtMost(0).toString());
	}

	@Test
	public void testOpen() {
		final BoundedRange<Integer> range = BoundedRange.open(0, 5);
		Assert.assertEquals("(0, 5)", range.toString());

		// getBound
		Assert.assertTrue(range.hasLowerBound());
		Assert.assertTrue(range.hasUpperBound());
		Assert.assertEquals((Integer)0, range.getLowerBoundValue());
		Assert.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertFalse(range.test(6));
		Assert.assertFalse(range.test(5));
		Assert.assertTrue(range.test(3));
		Assert.assertFalse(range.test(0));
		Assert.assertFalse(range.test(-1));

		// isXXX
		Assert.assertTrue(range.isEquals(range));
		Assert.assertTrue(range.isDisjoint(BoundedRange.open(-5, 0))); // (-5, 0)
		Assert.assertTrue(range.isDisjoint(BoundedRange.close(-5, 0))); // [-5, 0]
		Assert.assertTrue(range.isIntersected(BoundedRange.close(-5, 1))); // [-5, 1]
		Assert.assertTrue(range.isSubset(BoundedRange.close(0, 5))); // [0, 5]
		Assert.assertTrue(range.isProperSubset(BoundedRange.close(0, 5))); // [0, 5]
		Assert.assertFalse(range.isSuperset(BoundedRange.close(0, 5))); // [0, 5]
		Assert.assertFalse(range.isProperSuperset(BoundedRange.close(0, 5))); // [0, 5]

		// operate
		Assert.assertEquals("(0, 10]", range.unionIfIntersected(BoundedRange.close(4, 10)).toString());
		Assert.assertEquals("(0, 10)", range.span(BoundedRange.open(9, 10)).toString());
		Assert.assertEquals("(-2, 0]", range.gap(BoundedRange.close(-10, -2)).toString());
		Assert.assertEquals("(3, 5)", range.intersection(BoundedRange.open(3, 10)).toString());

		// sub
		Assert.assertEquals("(3, 5)", range.subGreatThan(3).toString());
		Assert.assertEquals("[3, 5)", range.subAtLeast(3).toString());
		Assert.assertEquals("(0, 3)", range.subLessThan(3).toString());
		Assert.assertEquals("(0, 3]", range.subAtMost(3).toString());
	}

	@Test
	public void testClose() {
		final BoundedRange<Integer> range = BoundedRange.close(0, 5);
		Assert.assertEquals("[0, 5]", range.toString());

		// getBound
		Assert.assertTrue(range.hasLowerBound());
		Assert.assertTrue(range.hasUpperBound());
		Assert.assertEquals((Integer)0, range.getLowerBoundValue());
		Assert.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertFalse(range.test(6));
		Assert.assertTrue(range.test(5));
		Assert.assertTrue(range.test(0));
		Assert.assertFalse(range.test(-1));

		// isXXX
		Assert.assertTrue(range.isEquals(range));
		Assert.assertTrue(range.isDisjoint(BoundedRange.open(-5, 0))); // (-5, 0)
		Assert.assertTrue(range.isDisjoint(BoundedRange.close(-5, 0))); // [-5, 0]
		Assert.assertTrue(range.isIntersected(BoundedRange.open(-5, 1))); // [-5, 1]
		Assert.assertFalse(range.isSubset(BoundedRange.open(0, 5))); // (0, 5)
		Assert.assertFalse(range.isProperSubset(BoundedRange.open(0, 5))); // (0, 5)
		Assert.assertTrue(range.isSuperset(BoundedRange.open(0, 5))); // (0, 5)
		Assert.assertTrue(range.isProperSuperset(BoundedRange.open(0, 5))); // (0, 5)

		// operate
		Assert.assertEquals("[0, 5]", range.unionIfIntersected(BoundedRange.open(5, 10)).toString());
		Assert.assertEquals("[0, 10]", range.unionIfIntersected(BoundedRange.close(4, 10)).toString());
		Assert.assertEquals("[0, 10)", range.span(BoundedRange.open(9, 10)).toString());
		Assert.assertEquals("(-2, 0)", range.gap(BoundedRange.close(-10, -2)).toString());
		Assert.assertEquals("(3, 5]", range.intersection(BoundedRange.open(3, 10)).toString());
		Assert.assertNull(range.intersection(BoundedRange.open(5, 10)));

		// sub
		Assert.assertEquals("(3, 5]", range.subGreatThan(3).toString());
		Assert.assertEquals("[3, 5]", range.subAtLeast(3).toString());
		Assert.assertEquals("[0, 3)", range.subLessThan(3).toString());
		Assert.assertEquals("[0, 3]", range.subAtMost(3).toString());
	}

	@Test
	public void testOpenClose() {
		final BoundedRange<Integer> range = BoundedRange.openClose(0, 5);
		Assert.assertEquals("(0, 5]", range.toString());

		// getBound
		Assert.assertTrue(range.hasLowerBound());
		Assert.assertTrue(range.hasUpperBound());
		Assert.assertEquals((Integer)0, range.getLowerBoundValue());
		Assert.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertFalse(range.test(6));
		Assert.assertTrue(range.test(5));
		Assert.assertFalse(range.test(0));
		Assert.assertFalse(range.test(-1));
	}

	@Test
	public void testCloseOpen() {
		final BoundedRange<Integer> range = BoundedRange.closeOpen(0, 5);
		Assert.assertEquals("[0, 5)", range.toString());

		// getBound
		Assert.assertTrue(range.hasLowerBound());
		Assert.assertTrue(range.hasUpperBound());
		Assert.assertEquals((Integer)0, range.getLowerBoundValue());
		Assert.assertEquals((Integer)5, range.getUpperBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertFalse(range.test(6));
		Assert.assertFalse(range.test(5));
		Assert.assertTrue(range.test(0));
		Assert.assertFalse(range.test(-1));
	}

	@Test
	public void testGreatThan() {
		final BoundedRange<Integer> range = BoundedRange.greaterThan(0);
		Assert.assertEquals("(0, +∞)", range.toString());

		// getBound
		Assert.assertTrue(range.hasLowerBound());
		Assert.assertFalse(range.hasUpperBound());
		Assert.assertEquals((Integer)0, range.getLowerBoundValue());
		Assert.assertNull(range.getUpperBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertTrue(range.test(6));
		Assert.assertTrue(range.test(5));
		Assert.assertFalse(range.test(0));
		Assert.assertFalse(range.test(-1));
	}

	@Test
	public void testAtLeast() {
		final BoundedRange<Integer> range = BoundedRange.atLeast(0);
		Assert.assertEquals("[0, +∞)", range.toString());

		// getBound
		Assert.assertTrue(range.hasLowerBound());
		Assert.assertFalse(range.hasUpperBound());
		Assert.assertEquals((Integer)0, range.getLowerBoundValue());
		Assert.assertNull(range.getUpperBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertTrue(range.test(6));
		Assert.assertTrue(range.test(5));
		Assert.assertTrue(range.test(0));
		Assert.assertFalse(range.test(-1));
	}

	@Test
	public void testLessThan() {
		final BoundedRange<Integer> range = BoundedRange.lessThan(5);
		Assert.assertEquals("(-∞, 5)", range.toString());

		// getBound
		Assert.assertTrue(range.hasUpperBound());
		Assert.assertFalse(range.hasLowerBound());
		Assert.assertEquals((Integer)5, range.getUpperBoundValue());
		Assert.assertNull(range.getLowerBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertFalse(range.test(6));
		Assert.assertFalse(range.test(5));
		Assert.assertTrue(range.test(0));
		Assert.assertTrue(range.test(-1));
	}

	@Test
	public void testAtMost() {
		final BoundedRange<Integer> range = BoundedRange.atMost(5);
		Assert.assertEquals("(-∞, 5]", range.toString());

		// getBound
		Assert.assertTrue(range.hasUpperBound());
		Assert.assertFalse(range.hasLowerBound());
		Assert.assertEquals((Integer)5, range.getUpperBoundValue());
		Assert.assertNull(range.getLowerBoundValue());

		// test
		Assert.assertFalse(range.isEmpty());
		Assert.assertFalse(range.test(6));
		Assert.assertTrue(range.test(5));
		Assert.assertTrue(range.test(0));
		Assert.assertTrue(range.test(-1));
	}

}
