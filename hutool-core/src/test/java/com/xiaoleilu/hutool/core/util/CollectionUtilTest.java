package com.xiaoleilu.hutool.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		Assert.assertTrue(v1s.contains(12));
		Assert.assertTrue(v1s.contains(23));
		Assert.assertTrue(v1s.contains("张三"));

		ArrayList<Object> v2s = CollectionUtil.valuesOfKeys(v2, keys);
		Assert.assertTrue(v2s.contains(15));
		Assert.assertTrue(v2s.contains(13));
		Assert.assertTrue(v2s.contains("李四"));
	}
	
	@Test
	public void unionTest() {
		ArrayList<String> list1 = CollectionUtil.newArrayList("a", "b", "b", "c", "d", "x");
		ArrayList<String> list2 = CollectionUtil.newArrayList("a", "b", "b", "b", "c", "d");
		
		Collection<String> union = CollectionUtil.union(list1, list2);
		Assert.assertEquals("[d, b, b, b, c, a, x]", union.toString());
	}
	
	@Test
	public void intersectionTest() {
		ArrayList<String> list1 = CollectionUtil.newArrayList("a", "b", "b", "c", "d", "x");
		ArrayList<String> list2 = CollectionUtil.newArrayList("a", "b", "b", "b", "c", "d");
		
		Collection<String> union = CollectionUtil.intersection(list1, list2);
		Assert.assertEquals("[d, b, b, c, a]", union.toString());
	}
	
	@Test
	public void disjunctionTest() {
		ArrayList<String> list1 = CollectionUtil.newArrayList("a", "b", "b", "c", "d", "x");
		ArrayList<String> list2 = CollectionUtil.newArrayList("a", "b", "b", "b", "c", "d", "x2");
		
		Collection<String> disjunction = CollectionUtil.disjunction(list1, list2);
		Assert.assertEquals("[b, x2, x]", disjunction.toString());
		
		Collection<String> disjunction2 = CollectionUtil.disjunction(list2, list1);
		Assert.assertEquals("[b, x2, x]", disjunction2.toString());
	}
	
	@Test
	public void toMapListAndToListMapTest() {
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("a", "值1");
		map1.put("b", "值1");
		
		HashMap<String, String> map2 = new HashMap<>();
		map2.put("a", "值2");
		map2.put("c", "值3");
		
		//----------------------------------------------------------------------------------------
		ArrayList<HashMap<String, String>> list = CollectionUtil.newArrayList(map1, map2);
		Map<String, List<String>> map = CollectionUtil.toListMap(list);
		Assert.assertEquals("值1", map.get("a").get(0));
		Assert.assertEquals("值2", map.get("a").get(1));
		
		//----------------------------------------------------------------------------------------
		List<Map<String,String>> listMap = CollectionUtil.toMapList(map);
		Assert.assertEquals("值1", listMap.get(0).get("a"));
		Assert.assertEquals("值2", listMap.get(1).get("a"));
	}
	
	@Test
	public void getFieldValuesTest() {
		Dict v1 = Dict.create().set("id", 12).set("name", "张三").set("age", 23);
		Dict v2 = Dict.create().set("age", 13).set("id", 15).set("name", "李四");
		ArrayList<Dict> list = CollectionUtil.newArrayList(v1, v2);
		
		List<Object> fieldValues = CollectionUtil.getFieldValues(list, "name");
		Assert.assertEquals("张三", fieldValues.get(0));
		Assert.assertEquals("李四", fieldValues.get(1));
	}
}
