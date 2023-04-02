package org.dromara.hutool.core.collection;

import org.dromara.hutool.core.collection.iter.LineIter;
import org.dromara.hutool.core.collection.iter.PartitionIter;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PartitionIterTest {

	@Test
	public void iterTest() {
		final LineIter lineIter = new LineIter(ResourceUtil.getUtf8Reader("test_lines.csv"));
		final PartitionIter<String> iter = new PartitionIter<>(lineIter, 3);
		for (final List<String> lines : iter) {
			Assertions.assertTrue(lines.size() > 0);
		}
	}

	@Test
	public void iterMaxTest() {
		final List<Integer> list = ListUtil.view(1, 2, 3, 4, 5, 6, 7, 8, 9, 9, 0, 12, 45, 12);
		final PartitionIter<Integer> iter = new PartitionIter<>(list.iterator(), 3);
		int max = 0;
		for (final List<Integer> lines : iter) {
			max = ArrayUtil.max(max, ArrayUtil.max(lines.toArray(new Integer[0])));
		}
		Assertions.assertEquals(45, max);
	}
}
