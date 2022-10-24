package cn.hutool.core.collection;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.NumberUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PartitionIterTest {

	@Test
	public void iterTest() {
		final LineIter lineIter = new LineIter(ResourceUtil.getUtf8Reader("test_lines.csv"));
		final PartitionIter<String> iter = new PartitionIter<>(lineIter, 3);
		for (List<String> lines : iter) {
			Assert.assertTrue(lines.size() > 0);
		}
	}

	@Test
	public void iterMaxTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 0, 12, 45, 12);
		final PartitionIter<Integer> iter = new PartitionIter<>(list.iterator(), 3);
		int max = 0;
		for (List<Integer> lines : iter) {
			max = NumberUtil.max(max, NumberUtil.max(lines.toArray(new Integer[0])));
		}
		Assert.assertEquals(45, max);
	}

	@Test
	public void getSize() {
		List<Integer> mockedList = makingList(19);
		Partition<Integer> partition = new Partition<>(mockedList, 10);
		Assert.assertEquals(2, partition.size());

		mockedList = makingList(11);
		partition = new Partition<>(mockedList, 10);
		Assert.assertEquals(2, partition.size());

		mockedList = makingList(10);
		partition = new Partition<>(mockedList, 10);
		Assert.assertEquals(1, partition.size());

		mockedList = makingList(9);
		partition = new Partition<>(mockedList, 10);
		Assert.assertEquals(1, partition.size());

		mockedList = makingList(5);
		partition = new Partition<>(mockedList, 10);
		Assert.assertEquals(1, partition.size());

		mockedList = makingList(0);
		partition = new Partition<>(mockedList, 10);
		Assert.assertEquals(0, partition.size());
	}

	private List<Integer> makingList(int length) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			list.add(i);
		}

		return list;
	}
}
