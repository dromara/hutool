package cn.hutool.core.lang.range;

import org.junit.Assert;
import org.junit.Test;

/**
 * test for {@link Bound}
 */
@SuppressWarnings("EqualsWithItself")
public class BoundTest {

	@Test
	public void testEquals() {
		final Bound<Integer> bound = new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND);
		Assert.assertEquals(bound, bound);
		Assert.assertEquals(bound, new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND));
		Assert.assertNotEquals(bound, new FiniteBound<>(2, BoundType.OPEN_UPPER_BOUND));
		Assert.assertNotEquals(bound, new FiniteBound<>(1, BoundType.OPEN_LOWER_BOUND));
		Assert.assertNotEquals(bound, null);
	}

	@Test
	public void testHashCode() {
		final int hashCode = new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND).hashCode();
		Assert.assertEquals(hashCode, new FiniteBound<>(1, BoundType.OPEN_UPPER_BOUND).hashCode());
		Assert.assertNotEquals(hashCode, new FiniteBound<>(2, BoundType.OPEN_UPPER_BOUND).hashCode());
		Assert.assertNotEquals(hashCode, new FiniteBound<>(1, BoundType.OPEN_LOWER_BOUND).hashCode());
	}

	@Test
	public void testNoneLowerBound() {
		final Bound<Integer> bound = Bound.noneLowerBound();
		// negate
		Assert.assertEquals(bound, bound.negate());
		// test
		Assert.assertTrue(bound.test(Integer.MAX_VALUE));
		// getType
		Assert.assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());
		// getValue
		Assert.assertNull(bound.getValue());
		// toString
		Assert.assertEquals("(" + "-\u221e", bound.descBound());
		// compareTo
		Assert.assertEquals(0, bound.compareTo(bound));
		Assert.assertEquals(-1, bound.compareTo(Bound.atMost(1)));

		Assert.assertEquals(BoundedRange.all(), bound.toRange());
		Assert.assertEquals("{x | x > -\u221e}", bound.toString());
	}

	@Test
	public void testNoneUpperBound() {
		final Bound<Integer> bound = Bound.noneUpperBound();
		// negate
		Assert.assertEquals(bound, bound.negate());
		// test
		Assert.assertTrue(bound.test(Integer.MAX_VALUE));
		// getType
		Assert.assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());
		// getValue
		Assert.assertNull(bound.getValue());
		// toString
		Assert.assertEquals("+\u221e" + ")", bound.descBound());
		// compareTo
		Assert.assertEquals(0, bound.compareTo(bound));
		Assert.assertEquals(1, bound.compareTo(Bound.atMost(1)));

		Assert.assertEquals(BoundedRange.all(), bound.toRange());
		Assert.assertEquals("{x | x < +\u221e}", bound.toString());
	}

	@Test
	public void testGreatThan() {
		// { x | x > 0}
		Bound<Integer> bound = Bound.greaterThan(0);

		// test
		Assert.assertTrue(bound.test(1));
		Assert.assertFalse(bound.test(0));
		Assert.assertFalse(bound.test(-1));
		// getType
		Assert.assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());
		// getValue
		Assert.assertEquals((Integer)0, bound.getValue());
		// toString
		Assert.assertEquals("(0", bound.descBound());
		Assert.assertEquals("{x | x > 0}", bound.toString());

		// compareTo
		Assert.assertEquals(0, bound.compareTo(bound));
		Assert.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assert.assertEquals(1, bound.compareTo(Bound.atLeast(-1)));
		Assert.assertEquals(-1, bound.compareTo(Bound.atLeast(2)));
		Assert.assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		Assert.assertEquals(1, bound.compareTo(Bound.atMost(0)));
		Assert.assertEquals(-1, bound.compareTo(Bound.atMost(2)));
		Assert.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x >= 0}
		bound = bound.negate();
		Assert.assertEquals((Integer)0, bound.getValue());
		Assert.assertEquals(BoundType.CLOSE_UPPER_BOUND, bound.getType());

		Assert.assertNotNull(bound.toRange());
	}

	@Test
	public void testAtLeast() {
		// { x | x >= 0}
		Bound<Integer> bound = Bound.atLeast(0);

		// test
		Assert.assertTrue(bound.test(1));
		Assert.assertTrue(bound.test(0));
		Assert.assertFalse(bound.test(-1));
		// getType
		Assert.assertEquals(BoundType.CLOSE_LOWER_BOUND, bound.getType());
		// getValue
		Assert.assertEquals((Integer)0, bound.getValue());
		// toString
		Assert.assertEquals("[0", bound.descBound());
		Assert.assertEquals("{x | x >= 0}", bound.toString());

		// compareTo
		Assert.assertEquals(0, bound.compareTo(bound));
		Assert.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assert.assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		Assert.assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		Assert.assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		Assert.assertEquals(1, bound.compareTo(Bound.atMost(0)));
		Assert.assertEquals(-1, bound.compareTo(Bound.atMost(2)));
		Assert.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x < 0}
		bound = bound.negate();
		Assert.assertEquals((Integer)0, bound.getValue());
		Assert.assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());

		Assert.assertNotNull(bound.toRange());
	}

	@Test
	public void testLessThan() {
		// { x | x < 0}
		Bound<Integer> bound = Bound.lessThan(0);

		// test
		Assert.assertFalse(bound.test(1));
		Assert.assertFalse(bound.test(0));
		Assert.assertTrue(bound.test(-1));
		// getType
		Assert.assertEquals(BoundType.OPEN_UPPER_BOUND, bound.getType());
		// getValue
		Assert.assertEquals((Integer)0, bound.getValue());
		// toString
		Assert.assertEquals("0)", bound.descBound());
		Assert.assertEquals("{x | x < 0}", bound.toString());

		// compareTo
		Assert.assertEquals(0, bound.compareTo(bound));
		Assert.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assert.assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		Assert.assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		Assert.assertEquals(1, bound.compareTo(Bound.lessThan(-1)));
		Assert.assertEquals(-1, bound.compareTo(Bound.atMost(0)));
		Assert.assertEquals(1, bound.compareTo(Bound.atMost(-1)));
		Assert.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x >= 0}
		bound = bound.negate();
		Assert.assertEquals((Integer)0, bound.getValue());
		Assert.assertEquals(BoundType.CLOSE_LOWER_BOUND, bound.getType());

		Assert.assertNotNull(bound.toRange());
	}

	@Test
	public void testAtMost() {
		// { x | x <= 0}
		Bound<Integer> bound = Bound.atMost(0);

		// test
		Assert.assertFalse(bound.test(1));
		Assert.assertTrue(bound.test(0));
		Assert.assertTrue(bound.test(-1));
		// getType
		Assert.assertEquals(BoundType.CLOSE_UPPER_BOUND, bound.getType());
		// getValue
		Assert.assertEquals((Integer)0, bound.getValue());
		// toString
		Assert.assertEquals("0]", bound.descBound());
		Assert.assertEquals("{x | x <= 0}", bound.toString());

		// compareTo
		Assert.assertEquals(0, bound.compareTo(bound));
		Assert.assertEquals(-1, bound.compareTo(Bound.noneUpperBound()));
		Assert.assertEquals(1, bound.compareTo(Bound.greaterThan(-1)));
		Assert.assertEquals(-1, bound.compareTo(Bound.greaterThan(0)));
		Assert.assertEquals(1, bound.compareTo(Bound.atMost(-1)));
		Assert.assertEquals(1, bound.compareTo(Bound.lessThan(0)));
		Assert.assertEquals(1, bound.compareTo(Bound.lessThan(-1)));
		Assert.assertEquals(1, bound.compareTo(Bound.noneLowerBound()));

		// { x | x > 0}
		bound = bound.negate();
		Assert.assertEquals((Integer)0, bound.getValue());
		Assert.assertEquals(BoundType.OPEN_LOWER_BOUND, bound.getType());

		Assert.assertNotNull(bound.toRange());
	}

}
