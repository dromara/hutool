package org.dromara.hutool.math;

import org.dromara.hutool.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 排列单元测试
 * @author looly
 *
 */
public class ArrangementTest {

	@Test
	public void arrangementTest() {
		long result = Arrangement.count(4, 2);
		Assertions.assertEquals(12, result);

		result = Arrangement.count(4, 1);
		Assertions.assertEquals(4, result);

		result = Arrangement.count(4, 0);
		Assertions.assertEquals(1, result);

		final long resultAll = Arrangement.countAll(4);
		Assertions.assertEquals(64, resultAll);
	}

	@Test
	public void selectTest() {
		final Arrangement arrangement = new Arrangement(new String[] { "1", "2", "3", "4" });
		final List<String[]> list = arrangement.select(2);
		Assertions.assertEquals(Arrangement.count(4, 2), list.size());
		Assertions.assertArrayEquals(new String[] {"1", "2"}, list.get(0));
		Assertions.assertArrayEquals(new String[] {"1", "3"}, list.get(1));
		Assertions.assertArrayEquals(new String[] {"1", "4"}, list.get(2));
		Assertions.assertArrayEquals(new String[] {"2", "1"}, list.get(3));
		Assertions.assertArrayEquals(new String[] {"2", "3"}, list.get(4));
		Assertions.assertArrayEquals(new String[] {"2", "4"}, list.get(5));
		Assertions.assertArrayEquals(new String[] {"3", "1"}, list.get(6));
		Assertions.assertArrayEquals(new String[] {"3", "2"}, list.get(7));
		Assertions.assertArrayEquals(new String[] {"3", "4"}, list.get(8));
		Assertions.assertArrayEquals(new String[] {"4", "1"}, list.get(9));
		Assertions.assertArrayEquals(new String[] {"4", "2"}, list.get(10));
		Assertions.assertArrayEquals(new String[] {"4", "3"}, list.get(11));

		final List<String[]> selectAll = arrangement.selectAll();
		Assertions.assertEquals(Arrangement.countAll(4), selectAll.size());

		final List<String[]> list2 = arrangement.select(0);
		Assertions.assertEquals(1, list2.size());
	}

	@Test
	@Disabled
	public void selectTest2() {
		final List<String[]> list = MathUtil.arrangementSelect(new String[] { "1", "1", "3", "4" });
		for (final String[] strings : list) {
			Console.log(strings);
		}
	}
}
