package org.dromara.hutool.core.collection.iter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		Assertions.assertEquals((Integer)1, iter.next());

		Assertions.assertEquals((Integer)2, CopiedIter.copyOf(iter).next());
	}

	@Test
	public void hasNext() {
		Assertions.assertTrue(CopiedIter.copyOf(Arrays.asList(1, 2, 3).iterator()).hasNext());
		Assertions.assertFalse(CopiedIter.copyOf(Collections.emptyIterator()).hasNext());
	}

	@Test
	public void next() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = CopiedIter.copyOf(list.iterator());
		Assertions.assertEquals((Integer)1, iter.next());
		Assertions.assertEquals((Integer)2, iter.next());
		Assertions.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void remove() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Iterator<Integer> iter = CopiedIter.copyOf(list.iterator());
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

}
