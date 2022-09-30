package cn.hutool.core.lang.range;

import org.junit.Assert;
import org.junit.Test;

/**
 * test for {@link BoundType}
 */
public class BoundTypeTest {

	@Test
	public void testIsDislocated() {
		Assert.assertTrue(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.CLOSE_UPPER_BOUND));
		Assert.assertTrue(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.OPEN_UPPER_BOUND));
		Assert.assertFalse(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.CLOSE_LOWER_BOUND));
		Assert.assertFalse(BoundType.CLOSE_LOWER_BOUND.isDislocated(BoundType.OPEN_LOWER_BOUND));
	}

	@Test
	public void testIsLowerBound() {
		Assert.assertFalse(BoundType.CLOSE_UPPER_BOUND.isLowerBound());
		Assert.assertFalse(BoundType.OPEN_UPPER_BOUND.isLowerBound());
		Assert.assertTrue(BoundType.CLOSE_LOWER_BOUND.isLowerBound());
		Assert.assertTrue(BoundType.OPEN_LOWER_BOUND.isLowerBound());
	}

	@Test
	public void testIsUpperBound() {
		Assert.assertTrue(BoundType.CLOSE_UPPER_BOUND.isUpperBound());
		Assert.assertTrue(BoundType.OPEN_UPPER_BOUND.isUpperBound());
		Assert.assertFalse(BoundType.CLOSE_LOWER_BOUND.isUpperBound());
		Assert.assertFalse(BoundType.OPEN_LOWER_BOUND.isUpperBound());
	}

	@Test
	public void testIsOpen() {
		Assert.assertFalse(BoundType.CLOSE_UPPER_BOUND.isOpen());
		Assert.assertTrue(BoundType.OPEN_UPPER_BOUND.isOpen());
		Assert.assertFalse(BoundType.CLOSE_LOWER_BOUND.isOpen());
		Assert.assertTrue(BoundType.OPEN_LOWER_BOUND.isOpen());
	}

	@Test
	public void testIsClose() {
		Assert.assertTrue(BoundType.CLOSE_UPPER_BOUND.isClose());
		Assert.assertFalse(BoundType.OPEN_UPPER_BOUND.isClose());
		Assert.assertTrue(BoundType.CLOSE_LOWER_BOUND.isClose());
		Assert.assertFalse(BoundType.OPEN_LOWER_BOUND.isClose());
	}

	@Test
	public void testNegate() {
		Assert.assertEquals(BoundType.CLOSE_UPPER_BOUND, BoundType.OPEN_LOWER_BOUND.negate());
		Assert.assertEquals(BoundType.OPEN_UPPER_BOUND, BoundType.CLOSE_LOWER_BOUND.negate());
		Assert.assertEquals(BoundType.OPEN_LOWER_BOUND, BoundType.CLOSE_UPPER_BOUND.negate());
		Assert.assertEquals(BoundType.CLOSE_LOWER_BOUND, BoundType.OPEN_UPPER_BOUND.negate());
	}

	@Test
	public void testGetSymbol() {
		Assert.assertEquals("]", BoundType.CLOSE_UPPER_BOUND.getSymbol());
		Assert.assertEquals(")", BoundType.OPEN_UPPER_BOUND.getSymbol());
		Assert.assertEquals("[", BoundType.CLOSE_LOWER_BOUND.getSymbol());
		Assert.assertEquals("(", BoundType.OPEN_LOWER_BOUND.getSymbol());
	}

	@Test
	public void testGetCode() {
		Assert.assertEquals(2, BoundType.CLOSE_UPPER_BOUND.getCode());
		Assert.assertEquals(1, BoundType.OPEN_UPPER_BOUND.getCode());
		Assert.assertEquals(-1, BoundType.OPEN_LOWER_BOUND.getCode());
		Assert.assertEquals(-2, BoundType.CLOSE_LOWER_BOUND.getCode());
	}

	@Test
	public void testGetOperator() {
		Assert.assertEquals("<=", BoundType.CLOSE_UPPER_BOUND.getOperator());
		Assert.assertEquals("<", BoundType.OPEN_UPPER_BOUND.getOperator());
		Assert.assertEquals(">", BoundType.OPEN_LOWER_BOUND.getOperator());
		Assert.assertEquals(">=", BoundType.CLOSE_LOWER_BOUND.getOperator());
	}

}
