package com.xiaoleilu.hutool.core.util;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.util.CollectionUtil;

public class CollectionUtilTest {
	@Test
	public void newHashSetTest(){
		Set<String> set = CollectionUtil.newHashSet((String[])null);
		Assert.assertNotNull(set);
	}
}
