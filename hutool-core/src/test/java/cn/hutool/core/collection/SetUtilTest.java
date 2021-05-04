package cn.hutool.core.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class SetUtilTest {
	@Test
	public void testIntersect() {
		Set<Integer> set1 = SetUtil.of(1, 2, 3);
		Set<Integer> set2 = SetUtil.of(3, 4, 5);
		Assert.assertTrue(SetUtil.intersect(set1, set2).size() == 1);
		Set<Integer> set3 = SetUtil.of();
		Assert.assertTrue(SetUtil.intersect(set1, set3).size() == 0);
		Assert.assertTrue(SetUtil.intersect(set3, set3).size() == 0);
	}

	@Test
	public void testConcat() {
		Set<Integer> set1 = SetUtil.of(1, 2, 3);
		Set<Integer> set2 = SetUtil.of(3, 4, 5);
		Assert.assertTrue(SetUtil.concat(set1, set2).size() == 5);
		Set<Integer> set3 = SetUtil.of();
		Assert.assertTrue(SetUtil.concat(set1, set3).size() == 3);
		Assert.assertTrue(SetUtil.concat(set3, set3).size() == 0);
		Assert.assertTrue(set1.size() == 3);
		Assert.assertTrue(set2.size() == 3);
	}
}
