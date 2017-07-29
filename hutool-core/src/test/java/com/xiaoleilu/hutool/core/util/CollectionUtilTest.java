package com.xiaoleilu.hutool.core.util;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Dict;
import com.xiaoleilu.hutool.util.CollectionUtil;

public class CollectionUtilTest {
	@Test
	public void newHashSetTest(){
		Set<String> set = CollectionUtil.newHashSet((String[])null);
		Assert.assertNotNull(set);
	}
	
	@Test
	public void valuesOfKeysTest() {
		Dict v1 = Dict.create().set("id", 12).set("name", "张三").set("age", 23);
		Dict v2 = Dict.create().set("age", 13).set("id", 15).set("name", "李四");
		
		final String[] keys = v1.keySet().toArray(new String[v1.size()]);
		ArrayList<Object> v1s = CollectionUtil.valuesOfKeys(v1, keys);
		Assert.assertEquals(v1s, CollectionUtil.newArrayList(12, 23, "张三"));

		ArrayList<Object> v2s = CollectionUtil.valuesOfKeys(v2, keys);
		Assert.assertEquals(v2s, CollectionUtil.newArrayList(15, 13, "李四"));
	}
}
