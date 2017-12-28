package com.xiaoleilu.hutool.util;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.util.RandomUtil;

public class RandomUtilTest {
	@Test
	public void randomEleSetTest(){
		Set<Integer> set = RandomUtil.randomEleSet(CollectionUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		Assert.assertEquals(set.size(), 2);
	}
}
