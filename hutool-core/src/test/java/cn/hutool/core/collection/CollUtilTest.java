package cn.hutool.core.collection;

import cn.hutool.core.collection.iter.IterUtil;
import cn.hutool.core.comparator.ComparableComparator;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

/**
 * 集合工具类单元测试
 *
 * @author looly
 */
public class CollUtilTest {

	@Test
	public void testPredicateContains() {
		final ArrayList<String> list = ListUtil.of("bbbbb", "aaaaa", "ccccc");
		Assert.assertTrue(CollUtil.contains(list, s -> s.startsWith("a")));
		Assert.assertFalse(CollUtil.contains(list, s -> s.startsWith("d")));
	}

	@Test
	public void testRemoveWithAddIf() {
		ArrayList<Integer> list = ListUtil.of(1, 2, 3);
		final ArrayList<Integer> exceptRemovedList = ListUtil.of(2, 3);
		final ArrayList<Integer> exceptResultList = ListUtil.of(1);

		List<Integer> resultList = CollUtil.removeWithAddIf(list, ele -> 1 == ele);
		Assert.assertEquals(list, exceptRemovedList);
		Assert.assertEquals(resultList, exceptResultList);

		list = ListUtil.of(1, 2, 3);
		resultList = new ArrayList<>();
		CollUtil.removeWithAddIf(list, resultList, ele -> 1 == ele);
		Assert.assertEquals(list, exceptRemovedList);
		Assert.assertEquals(resultList, exceptResultList);
	}

	@Test
	public void testPadLeft() {
		List<String> srcList = ListUtil.of();
		List<String> answerList = ListUtil.of("a", "b");
		CollUtil.padLeft(srcList, 1, "b");
		CollUtil.padLeft(srcList, 2, "a");
		Assert.assertEquals(srcList, answerList);

		srcList = ListUtil.of("a", "b");
		answerList = ListUtil.of("a", "b");
		CollUtil.padLeft(srcList, 2, "a");
		Assert.assertEquals(srcList, answerList);

		srcList = ListUtil.of("c");
		answerList = ListUtil.of("a", "a", "c");
		CollUtil.padLeft(srcList, 3, "a");
		Assert.assertEquals(srcList, answerList);
	}

	@Test
	public void testPadRight() {
		final List<String> srcList = ListUtil.of("a");
		final List<String> answerList = ListUtil.of("a", "b", "b", "b", "b");
		CollUtil.padRight(srcList, 5, "b");
		Assert.assertEquals(srcList, answerList);
	}

	@Test
	public void isNotEmptyTest() {
		Assert.assertFalse(CollUtil.isNotEmpty((Collection<?>) null));
	}

	@Test
	public void newHashSetTest() {
		final Set<String> set = SetUtil.of((String[]) null);
		Assert.assertNotNull(set);
	}

	@Test
	public void unionTest() {
		final ArrayList<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final ArrayList<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");

		final Collection<String> union = CollUtil.union(list1, list2);

		Assert.assertEquals(3, CollUtil.count(union, "b"::equals));
	}

	@Test
	public void intersectionTest() {
		final ArrayList<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final ArrayList<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");

		final Collection<String> intersection = CollUtil.intersection(list1, list2);
		Assert.assertEquals(2, CollUtil.count(intersection, "b"::equals));
	}

	@Test
	public void intersectionDistinctTest() {
		final ArrayList<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final ArrayList<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");
		final ArrayList<String> list3 = ListUtil.of();

		final Collection<String> intersectionDistinct = CollUtil.intersectionDistinct(list1, list2);
		Assert.assertEquals(SetUtil.ofLinked("a", "b", "c", "d"), intersectionDistinct);

		final Collection<String> intersectionDistinct2 = CollUtil.intersectionDistinct(list1, list2, list3);
		Assert.assertTrue(intersectionDistinct2.isEmpty());
	}

	@Test
	public void disjunctionTest() {
		final ArrayList<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final ArrayList<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		Assert.assertTrue(disjunction.contains("b"));
		Assert.assertTrue(disjunction.contains("x2"));
		Assert.assertTrue(disjunction.contains("x"));

		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		Assert.assertTrue(disjunction2.contains("b"));
		Assert.assertTrue(disjunction2.contains("x2"));
		Assert.assertTrue(disjunction2.contains("x"));
	}

	@Test
	public void disjunctionTest2() {
		// 任意一个集合为空，差集为另一个集合
		final ArrayList<String> list1 = ListUtil.of();
		final ArrayList<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		Assert.assertEquals(list2, disjunction);
		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		Assert.assertEquals(list2, disjunction2);
	}

	@Test
	public void disjunctionTest3() {
		// 无交集下返回共同的元素
		final ArrayList<String> list1 = ListUtil.of("1", "2", "3");
		final ArrayList<String> list2 = ListUtil.of("a", "b", "c");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		Assert.assertTrue(disjunction.contains("1"));
		Assert.assertTrue(disjunction.contains("2"));
		Assert.assertTrue(disjunction.contains("3"));
		Assert.assertTrue(disjunction.contains("a"));
		Assert.assertTrue(disjunction.contains("b"));
		Assert.assertTrue(disjunction.contains("c"));
		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		Assert.assertTrue(disjunction2.contains("1"));
		Assert.assertTrue(disjunction2.contains("2"));
		Assert.assertTrue(disjunction2.contains("3"));
		Assert.assertTrue(disjunction2.contains("a"));
		Assert.assertTrue(disjunction2.contains("b"));
		Assert.assertTrue(disjunction2.contains("c"));
	}

	@Test
	public void subtractTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");
		final Collection<String> subtract = CollUtil.subtract(list1, list2);
		Assert.assertEquals(1, subtract.size());
		Assert.assertEquals("x", subtract.iterator().next());
	}

	@Test
	public void subtractSetTest() {
		final HashMap<String, Object> map1 = MapUtil.newHashMap();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map1.put("1", "v1");
		map1.put("2", "v2");
		map2.put("2", "v2");
		final Collection<String> r2 = CollUtil.subtract(map1.keySet(), map2.keySet());
		Assert.assertEquals("[1]", r2.toString());
	}

	@Test
	public void subtractSetToListTest() {
		final HashMap<String, Object> map1 = MapUtil.newHashMap();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map1.put("1", "v1");
		map1.put("2", "v2");
		map2.put("2", "v2");
		final List<String> r2 = CollUtil.subtractToList(map1.keySet(), map2.keySet());
		Assert.assertEquals("[1]", r2.toString());
	}

	@Test
	public void toMapListAndToListMapTest() {
		final HashMap<String, String> map1 = new HashMap<>();
		map1.put("a", "值1");
		map1.put("b", "值1");

		final HashMap<String, String> map2 = new HashMap<>();
		map2.put("a", "值2");
		map2.put("c", "值3");

		// ----------------------------------------------------------------------------------------
		final ArrayList<HashMap<String, String>> list = ListUtil.of(map1, map2);
		final Map<String, List<String>> map = CollUtil.toListMap(list);
		Assert.assertEquals("值1", map.get("a").get(0));
		Assert.assertEquals("值2", map.get("a").get(1));

		// ----------------------------------------------------------------------------------------
		final List<Map<String, String>> listMap = CollUtil.toMapList(map);
		Assert.assertEquals("值1", listMap.get(0).get("a"));
		Assert.assertEquals("值2", listMap.get(1).get("a"));
	}

	@Test
	public void getFieldValuesTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final Dict v2 = Dict.of().set("age", 13).set("id", 15).set("name", "李四");
		final ArrayList<Dict> list = ListUtil.of(v1, v2);

		final List<Object> fieldValues = (List<Object>) CollUtil.getFieldValues(list, "name");

		Assert.assertEquals("张三", fieldValues.get(0));
		Assert.assertEquals("李四", fieldValues.get(1));
	}

	@Test
	public void splitTest() {
		final ArrayList<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<List<Integer>> split = CollUtil.split(list, 3);
		Assert.assertEquals(3, split.size());
		Assert.assertEquals(3, split.get(0).size());
	}

	@Test
	public void foreachTest() {
		final HashMap<String, String> map = MapUtil.newHashMap();
		map.put("a", "1");
		map.put("b", "2");
		map.put("c", "3");

		final String[] result = new String[1];
		final String a = "a";
		CollUtil.forEach(map, (key, value, index) -> {
			if (a.equals(key)) {
				result[0] = value;
			}
		});
		Assert.assertEquals("1", result[0]);
	}

	@Test
	public void editTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c");

		final Collection<String> filtered = CollUtil.edit(list, t -> t + 1);

		Assert.assertEquals(ListUtil.of("a1", "b1", "c1"), filtered);
	}

	@Test
	public void removeTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c");

		final ArrayList<String> filtered = CollUtil.remove(list, "a"::equals);

		// 原地过滤
		Assert.assertSame(list, filtered);
		Assert.assertEquals(ListUtil.of("b", "c"), filtered);
	}

	@Test
	public void removeForSetTest() {
		final Set<String> set = SetUtil.ofLinked("a", "b", "", "  ", "c");
		final Set<String> filtered = CollUtil.remove(set, StrUtil::isBlank);

		Assert.assertEquals(SetUtil.ofLinked("a", "b", "c"), filtered);
	}

	@Test
	public void filterRemoveTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c");

		final List<String> removed = new ArrayList<>();
		final ArrayList<String> filtered = CollUtil.remove(list, t -> {
			if("a".equals(t)){
				removed.add(t);
				return true;
			}
			return false;
		});

		Assert.assertEquals(1, removed.size());
		Assert.assertEquals("a", removed.get(0));

		// 原地过滤
		Assert.assertSame(list, filtered);
		Assert.assertEquals(ListUtil.of("b", "c"), filtered);
	}

	@Test
	public void removeNullTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final ArrayList<String> filtered = CollUtil.removeNull(list);

		// 原地过滤
		Assert.assertSame(list, filtered);
		Assert.assertEquals(ListUtil.of("a", "b", "c", "", "  "), filtered);
	}

	@Test
	public void removeEmptyTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final ArrayList<String> filtered = CollUtil.removeEmpty(list);

		// 原地过滤
		Assert.assertSame(list, filtered);
		Assert.assertEquals(ListUtil.of("a", "b", "c", "  "), filtered);
	}

	@Test
	public void removeBlankTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final ArrayList<String> filtered = CollUtil.removeBlank(list);

		// 原地过滤
		Assert.assertSame(list, filtered);
		Assert.assertEquals(ListUtil.of("a", "b", "c"), filtered);
	}

	@Test
	public void groupTest() {
		final List<String> list = ListUtil.of("1", "2", "3", "4", "5", "6");
		final List<List<String>> group = CollUtil.group(list, null);
		Assert.assertTrue(group.size() > 0);

		final List<List<String>> group2 = CollUtil.group(list, t -> {
			// 按照奇数偶数分类
			return Integer.parseInt(t) % 2;
		});
		Assert.assertEquals(ListUtil.of("2", "4", "6"), group2.get(0));
		Assert.assertEquals(ListUtil.of("1", "3", "5"), group2.get(1));
	}

	@Test
	public void groupByFieldTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		final List<List<TestBean>> groupByField = CollUtil.groupByField(list, "age");
		Assert.assertEquals("张三", groupByField.get(0).get(0).getName());
		Assert.assertEquals("王五", groupByField.get(0).get(1).getName());

		Assert.assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void sortByPropertyTest() {
		final List<TestBean> list = ListUtil.of(
				new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
				new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
				new TestBean("王五", 12, DateUtil.parse("2018-04-01"))//
		);

		CollUtil.sortByProperty(list, "createTime");
		Assert.assertEquals("李四", list.get(0).getName());
		Assert.assertEquals("王五", list.get(1).getName());
		Assert.assertEquals("张三", list.get(2).getName());
	}

	@Test
	public void sortByPropertyTest2() {
		final List<TestBean> list = ListUtil.of(
				new TestBean("张三", 0, DateUtil.parse("2018-05-01")), //
				new TestBean("李四", -12, DateUtil.parse("2018-03-01")), //
				new TestBean("王五", 23, DateUtil.parse("2018-04-01"))//
		);

		CollUtil.sortByProperty(list, "age");
		Assert.assertEquals("李四", list.get(0).getName());
		Assert.assertEquals("张三", list.get(1).getName());
		Assert.assertEquals("王五", list.get(2).getName());
	}

	@Test
	public void fieldValueMapTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
				new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
				new TestBean("王五", 12, DateUtil.parse("2018-04-01"))//
		);

		final Map<String, TestBean> map = CollUtil.fieldValueMap(list, "name");
		Assert.assertEquals("李四", map.get("李四").getName());
		Assert.assertEquals("王五", map.get("王五").getName());
		Assert.assertEquals("张三", map.get("张三").getName());
	}

	@Test
	public void fieldValueAsMapTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
				new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
				new TestBean("王五", 14, DateUtil.parse("2018-04-01"))//
		);

		final Map<String, Integer> map = CollUtil.fieldValueAsMap(list, "name", "age");
		Assert.assertEquals(new Integer(12), map.get("张三"));
		Assert.assertEquals(new Integer(13), map.get("李四"));
		Assert.assertEquals(new Integer(14), map.get("王五"));
	}

	@Test
	public void emptyTest() {
		final SortedSet<String> emptySortedSet = CollUtil.empty(SortedSet.class);
		Assert.assertEquals(Collections.emptySortedSet(), emptySortedSet);

		final Set<String> emptySet = CollUtil.empty(Set.class);
		Assert.assertEquals(Collections.emptySet(), emptySet);

		final List<String> emptyList = CollUtil.empty(List.class);
		Assert.assertEquals(Collections.emptyList(), emptyList);
	}

	@Data
	@AllArgsConstructor
	public static class TestBean {
		private String name;
		private int age;
		private Date createTime;

		public TestBean(final String name, final int age) {
			this.name = name;
			this.age = age;
		}
	}

	@Test
	public void listTest() {
		final List<Object> list1 = ListUtil.of(false);
		final List<Object> list2 = ListUtil.of(true);

		Assert.assertTrue(list1 instanceof ArrayList);
		Assert.assertTrue(list2 instanceof LinkedList);
	}

	@Test
	public void listTest2() {
		final List<String> list1 = ListUtil.of( "a", "b", "c");
		final List<String> list2 = ListUtil.ofLinked( "a", "b", "c");
		Assert.assertEquals("[a, b, c]", list1.toString());
		Assert.assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void listTest3() {
		final HashSet<String> set = new LinkedHashSet<>();
		set.add("a");
		set.add("b");
		set.add("c");

		final List<String> list1 = ListUtil.of(false, set);
		final List<String> list2 = ListUtil.of(true, set);
		Assert.assertEquals("[a, b, c]", list1.toString());
		Assert.assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void getTest() {
		final HashSet<String> set = SetUtil.ofLinked("A", "B", "C", "D");
		String str = CollUtil.get(set, 2);
		Assert.assertEquals("C", str);

		str = CollUtil.get(set, -1);
		Assert.assertEquals("D", str);
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
		Assert.assertTrue(retval.isEmpty());
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

	@Test(expected = IndexOutOfBoundsException.class)
	public void subInput1PositiveNegativePositiveOutputArrayIndexOutOfBoundsException() {
		// Arrange
		final List<Integer> list = new ArrayList<>();
		list.add(null);
		final int start = 2_147_483_643;
		final int end = -2_147_483_648;
		final int step = 2;

		// Act
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
		Assert.assertTrue(retval.isEmpty());
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
		Assert.assertTrue(retval.isEmpty());
	}

	@Test
	public void sortPageAllTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<Integer> sortPageAll = CollUtil.sortPageAll(1, 5, Comparator.reverseOrder(), list);

		Assert.assertEquals(ListUtil.of(4, 3, 2, 1), sortPageAll);
	}

	@Test
	public void containsAnyTest() {
		final ArrayList<Integer> list1 = ListUtil.of(1, 2, 3, 4, 5);
		final ArrayList<Integer> list2 = ListUtil.of(5, 3, 1, 9, 11);

		Assert.assertTrue(CollUtil.containsAny(list1, list2));
	}

	@Test
	public void containsAllTest() {
		final ArrayList<Integer> list1 = ListUtil.of(1, 2, 3, 4, 5);
		final ArrayList<Integer> list2 = ListUtil.of(5, 3, 1);
		Assert.assertTrue(CollUtil.containsAll(list1, list2));

		final ArrayList<Integer> list3 = ListUtil.of(1);
		final ArrayList<Integer> list4 = ListUtil.of();
		Assert.assertTrue(CollUtil.containsAll(list3, list4));
	}

	@Test
	public void getLastTest() {
		// 测试：空数组返回null而不是报错
		final List<String> test = ListUtil.of();
		final String last = CollUtil.getLast(test);
		Assert.assertNull(last);
	}

	@Test
	public void zipTest() {
		final Collection<String> keys = ListUtil.of("a", "b", "c", "d");
		final Collection<Integer> values = ListUtil.of(1, 2, 3, 4);

		final Map<String, Integer> map = CollUtil.zip(keys, values);

		Assert.assertEquals(4, Objects.requireNonNull(map).size());

		Assert.assertEquals(1, map.get("a").intValue());
		Assert.assertEquals(2, map.get("b").intValue());
		Assert.assertEquals(3, map.get("c").intValue());
		Assert.assertEquals(4, map.get("d").intValue());
	}

	@Test
	public void toMapTest() {
		final Collection<String> keys = ListUtil.of("a", "b", "c", "d");
		final Map<String, String> map = IterUtil.toMap(keys, (value) -> "key" + value);
		Assert.assertEquals("a", map.get("keya"));
		Assert.assertEquals("b", map.get("keyb"));
		Assert.assertEquals("c", map.get("keyc"));
		Assert.assertEquals("d", map.get("keyd"));
	}

	@Test
	public void mapToMapTest(){
		final HashMap<String, String> oldMap = new HashMap<>();
		oldMap.put("a", "1");
		oldMap.put("b", "12");
		oldMap.put("c", "134");

		final Map<String, Long> map = IterUtil.toMap(oldMap.entrySet(),
				Map.Entry::getKey,
				entry -> Long.parseLong(entry.getValue()));

		Assert.assertEquals(1L, (long)map.get("a"));
		Assert.assertEquals(12L, (long)map.get("b"));
		Assert.assertEquals(134L, (long)map.get("c"));
	}

	@Test
	public void countMapTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final Map<String, Integer> countMap = CollUtil.countMap(list);

		Assert.assertEquals(Integer.valueOf(2), countMap.get("a"));
		Assert.assertEquals(Integer.valueOf(2), countMap.get("b"));
		Assert.assertEquals(Integer.valueOf(2), countMap.get("c"));
		Assert.assertEquals(Integer.valueOf(1), countMap.get("d"));
	}

	@Test
	public void indexOfTest() {
		final ArrayList<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final int i = CollUtil.indexOf(list, (str) -> str.charAt(0) == 'c');
		Assert.assertEquals(2, i);
	}

	@Test
	public void lastIndexOfTest() {
		// List有优化
		final ArrayList<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final int i = CollUtil.lastIndexOf(list, (str) -> str.charAt(0) == 'c');
		Assert.assertEquals(3, i);
	}

	@Test
	public void lastIndexOfSetTest() {
		final Set<String> list = SetUtil.ofLinked("a", "b", "c", "c", "a", "b", "d");
		// 去重后c排第三
		final int i = CollUtil.lastIndexOf(list, (str) -> str.charAt(0) == 'c');
		Assert.assertEquals(2, i);
	}

	@Test
	public void pageTest() {
		final List<Dict> objects = ListUtil.of();
		for (int i = 0; i < 10; i++) {
			objects.add(Dict.of().set("name", "姓名：" + i));
		}

		Assert.assertEquals(0, CollUtil.page(3, 5, objects).size());
	}

	@Test
	public void subtractToListTest() {
		final List<Long> list1 = Arrays.asList(1L, 2L, 3L);
		final List<Long> list2 = Arrays.asList(2L, 3L);

		final List<Long> result = CollUtil.subtractToList(list1, list2);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(1L, result.get(0), 1);
	}

	@Test
	public void sortComparableTest() {
		final List<String> of = ListUtil.of("a", "c", "b");
		final List<String> sort = CollUtil.sort(of, new ComparableComparator<>());
		Assert.assertEquals("a,b,c", CollUtil.join(sort, ","));
	}

	@Test
	public void setValueByMapTest(){
		// https://gitee.com/dromara/hutool/pulls/482
		final List<Person> people = Arrays.asList(
				new Person("aa", 12, "man", 1),
				new Person("bb", 13, "woman", 2),
				new Person("cc", 14, "man", 3),
				new Person("dd", 15, "woman", 4),
				new Person("ee", 16, "woman", 5),
				new Person("ff", 17, "man", 6)
		);

		final Map<Integer, String> genderMap = new HashMap<>();
		genderMap.put(1, null);
		genderMap.put(2, "妇女");
		genderMap.put(3, "少女");
		genderMap.put(4, "女");
		genderMap.put(5, "小孩");
		genderMap.put(6, "男");

		Assert.assertEquals(people.get(1).getGender(), "woman");
		CollUtil.setValueByMap(people, genderMap, Person::getId, Person::setGender);
		Assert.assertEquals(people.get(1).getGender(), "妇女");

		final Map<Integer, Person> personMap = new HashMap<>();
		personMap.put(1, new Person("AA", 21, "男", 1));
		personMap.put(2, new Person("BB", 7, "小孩", 2));
		personMap.put(3, new Person("CC", 65, "老人", 3));
		personMap.put(4, new Person("DD", 35, "女人", 4));
		personMap.put(5, new Person("EE", 14, "少女", 5));
		personMap.put(6, null);

		CollUtil.setValueByMap(people, personMap, Person::getId, (x, y) -> {
			x.setGender(y.getGender());
			x.setName(y.getName());
			x.setAge(y.getAge());
		});

		Assert.assertEquals(people.get(1).getGender(), "小孩");
	}

	@Test
	public void distinctTest(){
		final ArrayList<Integer> distinct = CollUtil.distinct(ListUtil.view(5, 3, 10, 9, 0, 5, 10, 9));
		Assert.assertEquals(ListUtil.view(5, 3, 10, 9, 0), distinct);
	}

	@Test
	public void distinctByFunctionTest(){
		final List<Person> people = Arrays.asList(
				new Person("aa", 12, "man", 1),
				new Person("bb", 13, "woman", 2),
				new Person("cc", 14, "man", 3),
				new Person("dd", 15, "woman", 4),
				new Person("ee", 16, "woman", 5),
				new Person("ff", 17, "man", 6)
		);

		// 覆盖模式下ff覆盖了aa，ee覆盖了bb
		List<Person> distinct = CollUtil.distinct(people, Person::getGender, true);
		Assert.assertEquals(2, distinct.size());
		Assert.assertEquals("ff", distinct.get(0).getName());
		Assert.assertEquals("ee", distinct.get(1).getName());

		// 非覆盖模式下，保留了最早加入的aa和bb
		distinct = CollUtil.distinct(people, Person::getGender, false);
		Assert.assertEquals(2, distinct.size());
		Assert.assertEquals("aa", distinct.get(0).getName());
		Assert.assertEquals("bb", distinct.get(1).getName());
	}

	@Data
	@AllArgsConstructor
	static class Person {
		private String name;
		private Integer age;
		private String gender;
		private Integer id;
	}

	@Test
	public void mapTest(){
		final ArrayList<String> list = ListUtil.of("a", "b", "c");
		final List<Object> extract = CollUtil.map(list, (e) -> e + "_1");
		Assert.assertEquals(ListUtil.of("a_1",  "b_1", "c_1"), extract);
	}

	@Test
	public void mapBeanTest(){
		final List<Person> people = Arrays.asList(
				new Person("aa", 12, "man", 1),
				new Person("bb", 13, "woman", 2),
				new Person("cc", 14, "man", 3),
				new Person("dd", 15, "woman", 4)
		);

		final List<Object> extract = CollUtil.map(people, Person::getName);
		Assert.assertEquals(ListUtil.of("aa", "bb", "cc", "dd"), extract);
	}

	@Test
	public void createTest(){
		final Collection<Object> collection = CollUtil.create(Collections.emptyList().getClass());
		Console.log(collection.getClass());
		Assert.assertNotNull(collection);
	}

	@Test
	public void transTest(){
		final List<Person> people = Arrays.asList(
				new Person("aa", 12, "man", 1),
				new Person("bb", 13, "woman", 2),
				new Person("cc", 14, "man", 3),
				new Person("dd", 15, "woman", 4)
		);

		final Collection<String> trans = CollUtil.trans(people, Person::getName);
		Assert.assertEquals("[aa, bb, cc, dd]", trans.toString());
	}
}
