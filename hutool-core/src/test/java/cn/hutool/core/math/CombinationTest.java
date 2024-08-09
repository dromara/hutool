package cn.hutool.core.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 组合单元测试
 *
 * @author looly
 *
 */
public class CombinationTest {

	@Test
	public void countTest() {
		long result = Combination.count(5, 2);
		assertEquals(10, result);

		result = Combination.count(5, 5);
		assertEquals(1, result);

		result = Combination.count(5, 0);
		assertEquals(1, result);

		long resultAll = Combination.countAll(5);
		assertEquals(31, resultAll);
	}

	@Test
	public void selectTest() {
		Combination combination = new Combination(new String[] { "1", "2", "3", "4", "5" });
		List<String[]> list = combination.select(2);
		assertEquals(Combination.count(5, 2), list.size());

		assertArrayEquals(new String[] {"1", "2"}, list.get(0));
		assertArrayEquals(new String[] {"1", "3"}, list.get(1));
		assertArrayEquals(new String[] {"1", "4"}, list.get(2));
		assertArrayEquals(new String[] {"1", "5"}, list.get(3));
		assertArrayEquals(new String[] {"2", "3"}, list.get(4));
		assertArrayEquals(new String[] {"2", "4"}, list.get(5));
		assertArrayEquals(new String[] {"2", "5"}, list.get(6));
		assertArrayEquals(new String[] {"3", "4"}, list.get(7));
		assertArrayEquals(new String[] {"3", "5"}, list.get(8));
		assertArrayEquals(new String[] {"4", "5"}, list.get(9));

		List<String[]> selectAll = combination.selectAll();
		assertEquals(Combination.countAll(5), selectAll.size());

		List<String[]> list2 = combination.select(0);
		assertEquals(1, list2.size());
	}
}
