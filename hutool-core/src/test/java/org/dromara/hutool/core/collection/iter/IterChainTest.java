package org.dromara.hutool.core.collection.iter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * test for {@link IterChain}
 */
public class IterChainTest {

	@Test
	public void testAddChain() {
		final Iterator<Integer> iter1 = Arrays.asList(1, 2).iterator();
		final Iterator<Integer> iter2 = Arrays.asList(3, 4).iterator();
		IterChain<Integer> iterChain = new IterChain<>();
		Assertions.assertSame(iterChain, iterChain.addChain(iter1));
		Assertions.assertSame(iterChain, iterChain.addChain(iter2));
		Assertions.assertEquals(2, iterChain.allIterators.size());

		iterChain = new IterChain<>(iter1, iter2);
		Assertions.assertEquals(2, iterChain.allIterators.size());
	}

	@Test
	public void testHasNext() {
		final IterChain<Integer> iterChain = new IterChain<>();
		Assertions.assertFalse(iterChain.hasNext());
		Assertions.assertFalse(iterChain.addChain(Collections.emptyIterator()).hasNext());
		Assertions.assertTrue(iterChain.addChain(Arrays.asList(3, 4).iterator()).hasNext());
	}

	@Test
	public void testNext() {
		final Iterator<Integer> iter1 = Arrays.asList(1, 2).iterator();
		final Iterator<Integer> iter2 = Arrays.asList(3, 4).iterator();
		final IterChain<Integer> iterChain = new IterChain<>();
		Assertions.assertSame(iterChain, iterChain.addChain(iter1));
		Assertions.assertSame(iterChain, iterChain.addChain(iter2));
		Assertions.assertEquals((Integer)1, iterChain.next());
		Assertions.assertEquals((Integer)2, iterChain.next());
		Assertions.assertEquals((Integer)3, iterChain.next());
		Assertions.assertEquals((Integer)4, iterChain.next());
	}

	@Test
	public void testRemove() {
		final IterChain<Integer> iterChain = new IterChain<>();
		iterChain.addChain(Arrays.asList(1, 2).iterator());
		Assertions.assertThrows(IllegalStateException.class, iterChain::remove);
	}

	@Test
	public void testIterator() {
		final Iterator<Integer> iter1 = Arrays.asList(1, 2).iterator();
		final Iterator<Integer> iter2 = Arrays.asList(3, 4).iterator();
		final IterChain<Integer> iterChain = new IterChain<>();
		Assertions.assertSame(iterChain, iterChain.addChain(iter1));
		Assertions.assertSame(iterChain, iterChain.addChain(iter2));

		final Iterator<Iterator<Integer>> iterators = iterChain.iterator();
		Assertions.assertSame(iter1, iterators.next());
		Assertions.assertSame(iter2, iterators.next());
	}

}
