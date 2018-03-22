package com.xiaoleilu.hutool.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.collection.CollUtil.Hash;
import com.xiaoleilu.hutool.lang.Dict;
import com.xiaoleilu.hutool.lang.Editor;
import com.xiaoleilu.hutool.lang.Matcher;
import com.xiaoleilu.hutool.map.MapUtil;

/**
 * 集合工具类单元测试
 * @author looly
 *
 */
public class CollUtilTest {
	
	@Test
	public void newHashSetTest() {
		Set<String> set = CollectionUtil.newHashSet((String[]) null);
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
		
		Assert.assertEquals(3, CollectionUtil.count(union, new Matcher<String>() {

			@Override
			public boolean match(String t) {
				return t.equals("b");
			}
			
		}));
	}

	@Test
	public void intersectionTest() {
		ArrayList<String> list1 = CollectionUtil.newArrayList("a", "b", "b", "c", "d", "x");
		ArrayList<String> list2 = CollectionUtil.newArrayList("a", "b", "b", "b", "c", "d");

		Collection<String> union = CollectionUtil.intersection(list1, list2);
		Assert.assertEquals(2, CollectionUtil.count(union, new Matcher<String>() {

			@Override
			public boolean match(String t) {
				return t.equals("b");
			}
			
		}));
	}

	@Test
	public void disjunctionTest() {
		ArrayList<String> list1 = CollectionUtil.newArrayList("a", "b", "b", "c", "d", "x");
		ArrayList<String> list2 = CollectionUtil.newArrayList("a", "b", "b", "b", "c", "d", "x2");

		Collection<String> disjunction = CollectionUtil.disjunction(list1, list2);
		Assert.assertTrue(disjunction.contains("b"));
		Assert.assertTrue(disjunction.contains("x2"));
		Assert.assertTrue(disjunction.contains("x"));

		Collection<String> disjunction2 = CollectionUtil.disjunction(list2, list1);
		Assert.assertTrue(disjunction2.contains("b"));
		Assert.assertTrue(disjunction2.contains("x2"));
		Assert.assertTrue(disjunction2.contains("x"));
	}

	@Test
	public void toMapListAndToListMapTest() {
		HashMap<String, String> map1 = new HashMap<>();
		map1.put("a", "值1");
		map1.put("b", "值1");

		HashMap<String, String> map2 = new HashMap<>();
		map2.put("a", "值2");
		map2.put("c", "值3");

		// ----------------------------------------------------------------------------------------
		ArrayList<HashMap<String, String>> list = CollectionUtil.newArrayList(map1, map2);
		Map<String, List<String>> map = CollectionUtil.toListMap(list);
		Assert.assertEquals("值1", map.get("a").get(0));
		Assert.assertEquals("值2", map.get("a").get(1));

		// ----------------------------------------------------------------------------------------
		List<Map<String, String>> listMap = CollectionUtil.toMapList(map);
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

	@Test
	public void splitTest() {
		final ArrayList<Integer> list = CollectionUtil.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		List<List<Integer>> split = CollectionUtil.split(list, 3);
		Assert.assertEquals(3, split.size());
		Assert.assertEquals(3, split.get(0).size());
	}
	
	@Test
	public void foreachTest() {
		HashMap<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");

		final String[] result = new String[1];
		CollectionUtil.forEach(map, new CollUtil.KVConsumer<String, String>() {
			@Override
			public void accept(String key, String value, int index) {
				if(key.equals("a")) {
					result[0] = value;
				}
			}
		});
		Assert.assertEquals("1", result[0]);
	}
	
	@Test
	public void filterTest() {
		ArrayList<String> list = CollUtil.newArrayList("a", "b", "c");
		
		Collection<String> filtered = CollUtil.filter(list, new Editor<String>() {
			@Override
			public String edit(String t) {
				return t +1;
			}});
		
		Assert.assertEquals(CollUtil.newArrayList("a1", "b1", "c1"), filtered);
	}
	
	@Test
	public void groupTest() {
		List<String> list = CollUtil.newArrayList("1", "2", "3", "4", "5", "6");
		List<List<String>> group = CollectionUtil.group(list, null);
		Assert.assertTrue(group.size() > 0);
		
		List<List<String>> group2 = CollectionUtil.group(list, new Hash<String>() {
			@Override
			public int hash(String t) {
				//按照奇数偶数分类
				return Integer.parseInt(t) % 2;
			}
			
		});
		Assert.assertEquals(CollUtil.newArrayList("2", "4", "6"), group2.get(0));
		Assert.assertEquals(CollUtil.newArrayList("1", "3", "5"), group2.get(1));
	}
	
	@Test
	public void groupByFieldTest() {
		List<TestBeans> list = CollUtil.newArrayList(new TestBeans("张三", 12), new TestBeans("李四", 13), new TestBeans("王五", 12));
		List<List<TestBeans>> groupByField = CollUtil.groupByField(list, "age");
		Assert.assertEquals("张三", groupByField.get(0).get(0).getName());
		Assert.assertEquals("王五", groupByField.get(0).get(1).getName());
		
		Assert.assertEquals("李四", groupByField.get(1).get(0).getName());
	}
	
	public static class TestBeans {
		private String name;
		private int age;
		
		public TestBeans(String name, int age) {
			this.name = name;
			this.age = age;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "TestBeans [name=" + name + ", age=" + age + "]";
		}
	}
}
