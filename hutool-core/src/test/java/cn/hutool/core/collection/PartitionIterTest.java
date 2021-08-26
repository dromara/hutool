package cn.hutool.core.collection;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.NumberUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PartitionIterTest {

	@Test
	public void iterTest() {
		final LineIter lineIter = new LineIter(ResourceUtil.getUtf8Reader("test_lines.csv"));
		final PartitionIter<String> iter = new PartitionIter<>(lineIter.iterator(), 3);
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
}
