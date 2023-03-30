package cn.hutool.core.collection.iter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * test for {@link PartitionIter}
 */
public class PartitionIterTest {

	@Test
	public void testHasNext() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		PartitionIter<Integer> partitionIter = new PartitionIter<>(iter, 2);
		Assertions.assertTrue(partitionIter.hasNext());
		Assertions.assertFalse(new PartitionIter<>(Collections.emptyIterator(), 1).hasNext());
	}

	@Test
	public void testNext() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		PartitionIter<Integer> partitionIter = new PartitionIter<>(iter, 2);
		Assertions.assertEquals(Arrays.asList(1, 2), partitionIter.next());
		Assertions.assertEquals(Arrays.asList(3, 4), partitionIter.next());
	}

}
