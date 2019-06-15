package cn.hutool.core.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cn.hutool.core.collection.CollUtil.Hash;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Matcher;
import cn.hutool.core.map.MapUtil;

/**
 * 集合工具类单元测试
 * 
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
	public void disjunctionTest2() {
		// 任意一个集合为空，差集为另一个集合
		ArrayList<String> list1 = CollectionUtil.newArrayList();
		ArrayList<String> list2 = CollectionUtil.newArrayList("a", "b", "b", "b", "c", "d", "x2");

		Collection<String> disjunction = CollectionUtil.disjunction(list1, list2);
		Assert.assertEquals(list2, disjunction);
		Collection<String> disjunction2 = CollectionUtil.disjunction(list2, list1);
		Assert.assertEquals(list2, disjunction2);
	}

	@Test
	public void disjunctionTest3() {
		// 无交集下返回共同的元素
		ArrayList<String> list1 = CollectionUtil.newArrayList("1", "2", "3");
		ArrayList<String> list2 = CollectionUtil.newArrayList("a", "b", "c");

		Collection<String> disjunction = CollectionUtil.disjunction(list1, list2);
		Assert.assertTrue(disjunction.contains("1"));
		Assert.assertTrue(disjunction.contains("2"));
		Assert.assertTrue(disjunction.contains("3"));
		Assert.assertTrue(disjunction.contains("a"));
		Assert.assertTrue(disjunction.contains("b"));
		Assert.assertTrue(disjunction.contains("c"));
		Collection<String> disjunction2 = CollectionUtil.disjunction(list2, list1);
		Assert.assertTrue(disjunction2.contains("1"));
		Assert.assertTrue(disjunction2.contains("2"));
		Assert.assertTrue(disjunction2.contains("3"));
		Assert.assertTrue(disjunction2.contains("a"));
		Assert.assertTrue(disjunction2.contains("b"));
		Assert.assertTrue(disjunction2.contains("c"));
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
				if (key.equals("a")) {
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
				return t + 1;
			}
		});

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
				// 按照奇数偶数分类
				return Integer.parseInt(t) % 2;
			}

		});
		Assert.assertEquals(CollUtil.newArrayList("2", "4", "6"), group2.get(0));
		Assert.assertEquals(CollUtil.newArrayList("1", "3", "5"), group2.get(1));
	}

	@Test
	public void groupByFieldTest() {
		List<TestBean> list = CollUtil.newArrayList(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		List<List<TestBean>> groupByField = CollUtil.groupByField(list, "age");
		Assert.assertEquals("张三", groupByField.get(0).get(0).getName());
		Assert.assertEquals("王五", groupByField.get(0).get(1).getName());

		Assert.assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void sortByPropertyTest() {
		List<TestBean> list = CollUtil.newArrayList(new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
				new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
				new TestBean("王五", 12, DateUtil.parse("2018-04-01"))//
		);

		CollUtil.sortByProperty(list, "createTime");
		Assert.assertEquals("李四", list.get(0).getName());
		Assert.assertEquals("王五", list.get(1).getName());
		Assert.assertEquals("张三", list.get(2).getName());
	}

	public static class TestBean {
		private String name;
		private int age;
		private Date createTime;

		public TestBean(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public TestBean(String name, int age, Date createTime) {
			this.name = name;
			this.age = age;
			this.createTime = createTime;
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

		public Date getCreateTime() {
			return createTime;
		}

		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}

		@Override
		public String toString() {
			return "TestBeans [name=" + name + ", age=" + age + "]";
		}
	}

	@Test
	public void listTest() {
		List<Object> list1 = CollUtil.list(false);
		List<Object> list2 = CollUtil.list(true);

		Assert.assertTrue(list1 instanceof ArrayList);
		Assert.assertTrue(list2 instanceof LinkedList);
	}

	@Test
	public void listTest2() {
		List<String> list1 = CollUtil.list(false, "a", "b", "c");
		List<String> list2 = CollUtil.list(true, "a", "b", "c");
		Assert.assertEquals("[a, b, c]", list1.toString());
		Assert.assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void listTest3() {
		HashSet<String> set = new LinkedHashSet<>();
		set.add("a");
		set.add("b");
		set.add("c");

		List<String> list1 = CollUtil.list(false, set);
		List<String> list2 = CollUtil.list(true, set);
		Assert.assertEquals("[a, b, c]", list1.toString());
		Assert.assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void getTest() {
		HashSet<String> set = CollUtil.newHashSet(true, new String[] { "A", "B", "C", "D" });
		String str = CollUtil.get(set, 2);
		Assert.assertEquals("C", str);

		str = CollUtil.get(set, -1);
		Assert.assertEquals("D", str);
	}

	@Test
	public void addAllIfNotContainsTest() {
		ArrayList<String> list1 = new ArrayList<>();
		list1.add("1");
		list1.add("2");
		ArrayList<String> list2 = new ArrayList<>();
		list2.add("2");
		list2.add("3");
		CollUtil.addAllIfNotContains(list1, list2);

		Assert.assertEquals(3, list1.size());
		Assert.assertEquals("1", list1.get(0));
		Assert.assertEquals("2", list1.get(1));
		Assert.assertEquals("3", list1.get(2));
	}

	@Test
	public void subInput1PositiveNegativePositiveOutput1() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 3;
		final int end = -1;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		arrayList.add(null);
		Assert.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1ZeroPositivePositiveOutput1() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 0;
		final int end = 1;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);

		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		arrayList.add(null);
		Assert.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1PositiveZeroOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 1;
		final int end = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end);

		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		Assert.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput0ZeroZeroZeroOutputNull() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		final int start = 0;
		final int end = 0;
		final int step = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		Assert.assertNull(retval);
	}

	@Test
	public void subInput1PositiveNegativeZeroOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 1;
		final int end = -2_147_483_648;
		final int step = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		Assert.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1PositivePositivePositiveOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 2_147_483_647;
		final int end = 2_147_483_647;
		final int step = 1_073_741_824;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		Assert.assertEquals(arrayList, retval);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void subInput1PositiveNegativePositiveOutputArrayIndexOutOfBoundsException() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 2_147_483_643;
		final int end = -2_147_483_648;
		final int step = 2;

		// Act
		thrown.expect(ArrayIndexOutOfBoundsException.class);
		CollUtil.sub(list, start, end, step);
		// Method is not expected to return due to exception thrown
	}

	@Test
	public void subInput0ZeroPositiveNegativeOutputNull() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		final int start = 0;
		final int end = 1;
		final int step = -2_147_483_646;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		Assert.assertNull(retval);
	}

	@Test
	public void subInput1PositivePositivePositiveOutput02() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 2_147_483_643;
		final int end = 2_147_483_642;
		final int step = 1_073_741_824;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		Assert.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1ZeroZeroPositiveOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(0);
		final int start = 0;
		final int end = 0;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		Assert.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1NegativeZeroPositiveOutput0() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(0);
		final int start = -1;
		final int end = 0;
		final int step = 2;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end, step);
		// Assert result
		final List<Integer> arrayList = new ArrayList<>();
		Assert.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput0ZeroZeroOutputNull() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		final int start = 0;
		final int end = 0;
		// Act
		final List<Integer> retval = CollUtil.sub(list, start, end);
		// Assert result
		Assert.assertNull(retval);
	}

	@Test
	public void sortPageAllTest() {
		ArrayList<Integer> list = CollUtil.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		List<Integer> sortPageAll = CollUtil.sortPageAll(2, 5, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				// 反序
				return o2.compareTo(o1);
			}
		}, list);

		Assert.assertEquals(CollUtil.newArrayList(4, 3, 2, 1), sortPageAll);
	}
	
	@Test
	public void containsAnyTest() {
		ArrayList<Integer> list1 = CollUtil.newArrayList(1, 2, 3, 4, 5);
		ArrayList<Integer> list2 = CollUtil.newArrayList(5, 3, 1, 9, 11);
		
		Assert.assertTrue(CollUtil.containsAny(list1, list2));
	}

	@Test
	public void containsAllTest() {
		ArrayList<Integer> list1 = CollUtil.newArrayList(1, 2, 3, 4, 5);
		ArrayList<Integer> list2 = CollUtil.newArrayList(5, 3, 1);
		
		Assert.assertTrue(CollUtil.containsAll(list1, list2));
	}
}
