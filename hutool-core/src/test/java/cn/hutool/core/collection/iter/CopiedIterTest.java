package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * test for {@link CopiedIter}
 */
public class CopiedIterTest {

	@Test
	public void copyOf() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = list.iterator();
		Assert.assertEquals((Integer)1, iter.next());

		Assert.assertEquals((Integer)2, CopiedIter.copyOf(iter).next());
	}

	@Test
	public void hasNext() {
		Assert.assertTrue(CopiedIter.copyOf(Arrays.asList(1, 2, 3).iterator()).hasNext());
		Assert.assertFalse(CopiedIter.copyOf(Collections.emptyIterator()).hasNext());
	}

	@Test
	public void next() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = CopiedIter.copyOf(list.iterator());
		Assert.assertEquals((Integer)1, iter.next());
		Assert.assertEquals((Integer)2, iter.next());
		Assert.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void remove() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = CopiedIter.copyOf(list.iterator());
		Assert.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

}
