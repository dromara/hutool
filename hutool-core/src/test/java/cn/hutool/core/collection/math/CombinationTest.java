package cn.hutool.core.collection.math;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 组合单元测试
 * 
 * @author looly
 *
 */
public class CombinationTest {

	@Test
	public void arrangementTest() {
		long result = Combination.count(5, 2);
		Assert.assertEquals(10, result);
	}

	@Test
	public void selectTest() {
		Combination combination = new Combination(new String[] { "1", "2", "3", "4", "5" }, 2);
		List<String[]> list = combination.select();
		Assert.assertEquals(Combination.count(5, 2), list.size());
	}
}
