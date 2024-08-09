package cn.hutool.core.math;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cn.hutool.core.lang.Console;

/**
 * 排列单元测试
 * @author looly
 *
 */
public class ArrangementTest {

	@Test
	public void arrangementTest() {
		long result = Arrangement.count(4, 2);
		assertEquals(12, result);

		result = Arrangement.count(4, 1);
		assertEquals(4, result);

		result = Arrangement.count(4, 0);
		assertEquals(1, result);

		long resultAll = Arrangement.countAll(4);
		assertEquals(64, resultAll);
	}

	@Test
	public void selectTest() {
		Arrangement arrangement = new Arrangement(new String[] { "1", "2", "3", "4" });
		List<String[]> list = arrangement.select(2);
		assertEquals(Arrangement.count(4, 2), list.size());
		assertArrayEquals(new String[] {"1", "2"}, list.get(0));
		assertArrayEquals(new String[] {"1", "3"}, list.get(1));
		assertArrayEquals(new String[] {"1", "4"}, list.get(2));
		assertArrayEquals(new String[] {"2", "1"}, list.get(3));
		assertArrayEquals(new String[] {"2", "3"}, list.get(4));
		assertArrayEquals(new String[] {"2", "4"}, list.get(5));
		assertArrayEquals(new String[] {"3", "1"}, list.get(6));
		assertArrayEquals(new String[] {"3", "2"}, list.get(7));
		assertArrayEquals(new String[] {"3", "4"}, list.get(8));
		assertArrayEquals(new String[] {"4", "1"}, list.get(9));
		assertArrayEquals(new String[] {"4", "2"}, list.get(10));
		assertArrayEquals(new String[] {"4", "3"}, list.get(11));

		List<String[]> selectAll = arrangement.selectAll();
		assertEquals(Arrangement.countAll(4), selectAll.size());

		List<String[]> list2 = arrangement.select(0);
		assertEquals(1, list2.size());
	}

	@Test
	@Disabled
	public void selectTest2() {
		List<String[]> list = MathUtil.arrangementSelect(new String[] { "1", "1", "3", "4" });
		for (String[] strings : list) {
			Console.log(strings);
		}
	}
}
