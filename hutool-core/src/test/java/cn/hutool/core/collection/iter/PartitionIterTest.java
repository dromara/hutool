package cn.hutool.core.collection.iter;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * test for {@link PartitionIter}
 */
public class PartitionIterTest {

	@Test
	public void hasNext() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		PartitionIter<Integer> partitionIter = new PartitionIter<>(iter, 2);
		Assert.assertTrue(partitionIter.hasNext());
		Assert.assertFalse(new PartitionIter<>(Collections.emptyIterator(), 1).hasNext());
	}

	@Test
	public void next() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		PartitionIter<Integer> partitionIter = new PartitionIter<>(iter, 2);
		Assert.assertEquals(Arrays.asList(1, 2), partitionIter.next());
		Assert.assertEquals(Arrays.asList(3, 4), partitionIter.next());
	}

}
