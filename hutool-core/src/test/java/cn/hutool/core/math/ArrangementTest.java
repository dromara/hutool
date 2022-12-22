package cn.hutool.core.math;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
		Assert.assertEquals(12, result);
		
		result = Arrangement.count(4, 1);
		Assert.assertEquals(4, result);
		
		result = Arrangement.count(4, 0);
		Assert.assertEquals(1, result);
		
		long resultAll = Arrangement.countAll(4);
		Assert.assertEquals(64, resultAll);
	}
	
	@Test
	public void selectTest() {
		Arrangement arrangement = new Arrangement(new String[] { "1", "2", "3", "4" });
		List<String[]> list = arrangement.select(2);
		Assert.assertEquals(Arrangement.count(4, 2), list.size());
		Assert.assertArrayEquals(new String[] {"1", "2"}, list.get(0));
		Assert.assertArrayEquals(new String[] {"1", "3"}, list.get(1));
		Assert.assertArrayEquals(new String[] {"1", "4"}, list.get(2));
		Assert.assertArrayEquals(new String[] {"2", "1"}, list.get(3));
		Assert.assertArrayEquals(new String[] {"2", "3"}, list.get(4));
		Assert.assertArrayEquals(new String[] {"2", "4"}, list.get(5));
		Assert.assertArrayEquals(new String[] {"3", "1"}, list.get(6));
		Assert.assertArrayEquals(new String[] {"3", "2"}, list.get(7));
		Assert.assertArrayEquals(new String[] {"3", "4"}, list.get(8));
		Assert.assertArrayEquals(new String[] {"4", "1"}, list.get(9));
		Assert.assertArrayEquals(new String[] {"4", "2"}, list.get(10));
		Assert.assertArrayEquals(new String[] {"4", "3"}, list.get(11));
		
		List<String[]> selectAll = arrangement.selectAll();
		Assert.assertEquals(Arrangement.countAll(4), selectAll.size());
		
		List<String[]> list2 = arrangement.select(0);
		Assert.assertEquals(1, list2.size());
	}
	
	@Test
	@Ignore
	public void selectTest2() {
		List<String[]> list = MathUtil.arrangementSelect(new String[] { "1", "1", "3", "4" });
		for (String[] strings : list) {
			Console.log(strings);
		}
	}
}
