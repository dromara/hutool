package cn.hutool.core.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class PartitionTest {
	@Test
	public void testSize() {
		List<Integer> emptyList = Collections.emptyList();
		Partition<Integer> partition = new Partition<>(emptyList, 10);

		Assert.assertTrue(partition.isEmpty());
		Assert.assertEquals(0, partition.size());

		List<Integer> singletonList = Collections.singletonList(1);
		partition = new Partition<>(singletonList, 10);
		Assert.assertFalse(partition.isEmpty());
		Assert.assertEquals(1, partition.size());
	}
}
