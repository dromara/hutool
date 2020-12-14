package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

public class RandomUtilTest {
	
	@Test
	public void randomEleSetTest(){
		Set<Integer> set = RandomUtil.randomEleSet(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		Assert.assertEquals(set.size(), 2);
	}
	
	@Test
	public void randomElesTest(){
		List<Integer> result = RandomUtil.randomEles(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		Assert.assertEquals(result.size(), 2);
	}
	
	@Test
	public void randomDoubleTest() {
		double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
		Assert.assertTrue(randomDouble <= 1);
	}
	
	@Test
	@Ignore
	public void randomBooleanTest() {
		Console.log(RandomUtil.randomBoolean());
	}

	@Test
	public void randomNumberTest() {
		final char c = RandomUtil.randomNumber();
		Assert.assertTrue(c <= '9');
	}
}
