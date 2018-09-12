package cn.hutool.core.util;

import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;

public class RandomUtilTest {
	
	@Test
	public void randomEleSetTest(){
		Set<Integer> set = RandomUtil.randomEleSet(CollectionUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		Assert.assertEquals(set.size(), 2);
	}
	
	@Test
	public void randomElesTest(){
		List<Integer> result = RandomUtil.randomEles(CollectionUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		Assert.assertEquals(result.size(), 2);
	}
	
	@Test
	public void randomDoubleTest() {
		double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
		Assert.assertTrue(randomDouble <= 1);
	}
	
	@Test
	public void randomUUIDTest() {
		String simpleUUID = RandomUtil.simpleUUID();
		Assert.assertEquals(32, simpleUUID.length());
		
		String randomUUID = RandomUtil.randomUUID();
		Assert.assertEquals(36, randomUUID.length());
	}
	
	/**
	 * UUID的性能测试
	 */
	@Test
	@Ignore
	public void benchTest() {
		TimeInterval timer = DateUtil.timer();
		for(int i =0; i< 1000000; i++) {
			RandomUtil.simpleUUID();
		}
		Console.log(timer.interval());
		
		timer.restart();
		for(int i =0; i< 1000000; i++) {
			UUID.randomUUID().toString().replace("-", "");
		}
		Console.log(timer.interval());
	}
}
