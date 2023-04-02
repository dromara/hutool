package org.dromara.hutool.collection.iter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * test for {@link ArrayIter}
 */
public class ArrayIterTest {

	@Test
	public void testHasNext() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertTrue(iter.hasNext());
	}

	@Test
	public void testNext() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals((Integer)1, iter.next());
		Assertions.assertEquals((Integer)2, iter.next());
		Assertions.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void testRemove() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testGetArray() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals(arr, iter.getArray());
	}

	@Test
	public void testReset() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals((Integer)1, iter.next());
		iter.reset();
		Assertions.assertEquals((Integer)1, iter.next());
	}

}
