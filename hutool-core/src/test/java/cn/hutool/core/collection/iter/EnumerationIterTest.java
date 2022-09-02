package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author huangchengxing
 */
public class EnumerationIterTest {

	@Test
	public void testHasNext() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assert.assertTrue(iter.hasNext());
		Assert.assertFalse(new EnumerationIter<>(new IteratorEnumeration<>(Collections.emptyIterator())).hasNext());
	}

	@Test
	public void testNext() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assert.assertEquals((Integer)1, iter.next());
		Assert.assertEquals((Integer)2, iter.next());
		Assert.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void testRemove() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assert.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testIterator() {
		Enumeration<Integer> enumeration = new IteratorEnumeration<>(Arrays.asList(1, 2, 3).iterator());
		EnumerationIter<Integer> iter = new EnumerationIter<>(enumeration);
		Assert.assertSame(iter, iter.iterator());
	}

}
