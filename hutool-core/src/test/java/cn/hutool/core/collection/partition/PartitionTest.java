package cn.hutool.core.collection.partition;

import cn.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class PartitionTest {
	@Test
	public void sizeTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4, 5);
		final Partition<Integer> partition = new Partition<>(list, 4);
		Assertions.assertEquals(2, partition.size());
	}

	@Test
	public void getSizeTest() {
		List<Integer> mockedList = makingList(19);
		Partition<Integer> partition = new Partition<>(mockedList, 10);
		Assertions.assertEquals(2, partition.size());

		mockedList = makingList(11);
		partition = new Partition<>(mockedList, 10);
		Assertions.assertEquals(2, partition.size());

		mockedList = makingList(10);
		partition = new Partition<>(mockedList, 10);
		Assertions.assertEquals(1, partition.size());

		mockedList = makingList(9);
		partition = new Partition<>(mockedList, 10);
		Assertions.assertEquals(1, partition.size());

		mockedList = makingList(5);
		partition = new Partition<>(mockedList, 10);
		Assertions.assertEquals(1, partition.size());
	}

	@Test
	public void getZeroSizeTest() {
		final List<Integer> mockedList = makingList(0);
		final Partition<Integer> partition = new Partition<>(mockedList, 10);
		Assertions.assertEquals(0, partition.size());
	}

	private List<Integer> makingList(final int length) {
		final List<Integer> list = ListUtil.of();
		for (int i = 0; i < length; i++) {
			list.add(i);
		}

		return list;
	}

	@Test
	public void testSize() {
		final List<Integer> emptyList = Collections.emptyList();
		Partition<Integer> partition = new Partition<>(emptyList, 10);

		Assertions.assertTrue(partition.isEmpty());

		final List<Integer> singletonList = Collections.singletonList(1);
		partition = new Partition<>(singletonList, 10);
		Assertions.assertFalse(partition.isEmpty());
		Assertions.assertEquals(1, partition.size());
	}
}
