package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

/**
 * test for {@link ArrayIter}
 */
public class ArrayIterTest {

	@Test
	public void testHasNext() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assert.assertTrue(iter.hasNext());
	}

	@Test
	public void testNext() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assert.assertEquals((Integer)1, iter.next());
		Assert.assertEquals((Integer)2, iter.next());
		Assert.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void testRemove() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assert.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testGetArray() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assert.assertEquals(arr, iter.getArray());
	}

	@Test
	public void testReset() {
		Integer[] arr = new Integer[]{ 1, 2, 3 };
		ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assert.assertEquals((Integer)1, iter.next());
		iter.reset();
		Assert.assertEquals((Integer)1, iter.next());
	}

}
