/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.collection;

import lombok.*;
import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.comparator.CompareUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.Dict;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

/**
 * 集合工具类单元测试
 *
 * @author looly
 */
public class CollUtilTest {

	@SuppressWarnings("ConstantConditions")
	@Test
	public void emptyIfNullTest() {
		final Set<?> set = null;
		final Set<?> set1 = CollUtil.emptyIfNull(set);
		Assertions.assertEquals(SetUtil.empty(), set1);

		final List<?> list = null;
		final List<?> list1 = CollUtil.emptyIfNull(list);
		Assertions.assertEquals(ListUtil.empty(), list1);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void hasNullTest() {
		List<Object> list = null;
		Assertions.assertTrue(CollUtil.hasNull(list));

		list = ListUtil.of();
		Assertions.assertFalse(CollUtil.hasNull(list));

		list = ListUtil.of("");
		Assertions.assertFalse(CollUtil.hasNull(list));

		list = ListUtil.of("", null);
		Assertions.assertTrue(CollUtil.hasNull(list));
	}

	@Test
	public void defaultIfEmpty() {
		List<String> strings = CollUtil.defaultIfEmpty(ListUtil.of(), ListUtil.of("1"));
		Assertions.assertEquals(ListUtil.of("1"), strings);

		strings = CollUtil.defaultIfEmpty(null, ListUtil.of("1"));
		Assertions.assertEquals(ListUtil.of("1"), strings);
	}

	@Test
	public void defaultIfEmpty2() {
		final List<String> strings = CollUtil.defaultIfEmpty(ListUtil.of(), Function.identity(), () -> ListUtil.of("1"));
		Assertions.assertEquals(ListUtil.of("1"), strings);
	}

	@Test
	public void testPredicateContains() {
		final List<String> list = ListUtil.of("bbbbb", "aaaaa", "ccccc");
		Assertions.assertTrue(CollUtil.contains(list, s -> s.startsWith("a")));
		Assertions.assertFalse(CollUtil.contains(list, s -> s.startsWith("d")));
	}

	@Test
	public void testRemoveWithAddIf() {
		List<Integer> list = ListUtil.of(1, 2, 3);
		final List<Integer> exceptRemovedList = ListUtil.of(2, 3);
		final List<Integer> exceptResultList = ListUtil.of(1);

		List<Integer> resultList = CollUtil.removeWithAddIf(list, ele -> 1 == ele);
		Assertions.assertEquals(list, exceptRemovedList);
		Assertions.assertEquals(resultList, exceptResultList);

		list = ListUtil.of(1, 2, 3);
		resultList = new ArrayList<>();
		CollUtil.removeWithAddIf(list, resultList, ele -> 1 == ele);
		Assertions.assertEquals(list, exceptRemovedList);
		Assertions.assertEquals(resultList, exceptResultList);
	}

	@Test
	public void testPadLeft() {
		List<String> srcList = ListUtil.of();
		List<String> answerList = ListUtil.of("a", "b");
		CollUtil.padLeft(srcList, 1, "b");
		CollUtil.padLeft(srcList, 2, "a");
		Assertions.assertEquals(srcList, answerList);

		srcList = ListUtil.of("a", "b");
		answerList = ListUtil.of("a", "b");
		CollUtil.padLeft(srcList, 2, "a");
		Assertions.assertEquals(srcList, answerList);

		srcList = ListUtil.of("c");
		answerList = ListUtil.of("a", "a", "c");
		CollUtil.padLeft(srcList, 3, "a");
		Assertions.assertEquals(srcList, answerList);
	}

	@Test
	public void testPadRight() {
		final List<String> srcList = ListUtil.of("a");
		final List<String> answerList = ListUtil.of("a", "b", "b", "b", "b");
		CollUtil.padRight(srcList, 5, "b");
		Assertions.assertEquals(srcList, answerList);
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void isNotEmptyTest() {
		Assertions.assertFalse(CollUtil.isNotEmpty((Collection<?>) null));
	}

	@Test
	public void newHashSetTest() {
		final Set<String> set = SetUtil.of((String[]) null);
		Assertions.assertNotNull(set);
	}

	@Test
	public void unionTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");

		final Collection<String> union = CollUtil.union(list1, list2);

		Assertions.assertEquals(3, CollUtil.count(union, "b"::equals));
	}

	@Test
	public void intersectionTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");

		final Collection<String> intersection = CollUtil.intersection(list1, list2);
		Assertions.assertEquals(2, CollUtil.count(intersection, "b"::equals));
		Assertions.assertEquals(0, CollUtil.count(intersection, "x"::equals));
	}

	@Test
	public void intersectionDistinctTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d");
		final List<String> list3 = ListUtil.of();

		final Collection<String> intersectionDistinct = CollUtil.intersectionDistinct(list1, list2);
		Assertions.assertEquals(SetUtil.ofLinked("a", "b", "c", "d"), intersectionDistinct);

		final Collection<String> intersectionDistinct2 = CollUtil.intersectionDistinct(list1, list2, list3);
		Console.log(intersectionDistinct2);
		Assertions.assertTrue(intersectionDistinct2.isEmpty());
	}

	@Test
	public void disjunctionTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		Assertions.assertTrue(disjunction.contains("b"));
		Assertions.assertTrue(disjunction.contains("x2"));
		Assertions.assertTrue(disjunction.contains("x"));

		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		Assertions.assertTrue(disjunction2.contains("b"));
		Assertions.assertTrue(disjunction2.contains("x2"));
		Assertions.assertTrue(disjunction2.contains("x"));
	}

	@Test
	public void disjunctionTest2() {
		// 任意一个集合为空，差集为另一个集合
		final List<String> list1 = ListUtil.of();
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		Assertions.assertEquals(list2, disjunction);
		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		Assertions.assertEquals(list2, disjunction2);
	}

	@Test
	public void disjunctionTest3() {
		// 无交集下返回共同的元素
		final List<String> list1 = ListUtil.of("1", "2", "3");
		final List<String> list2 = ListUtil.of("a", "b", "c");

		final Collection<String> disjunction = CollUtil.disjunction(list1, list2);
		Assertions.assertTrue(disjunction.contains("1"));
		Assertions.assertTrue(disjunction.contains("2"));
		Assertions.assertTrue(disjunction.contains("3"));
		Assertions.assertTrue(disjunction.contains("a"));
		Assertions.assertTrue(disjunction.contains("b"));
		Assertions.assertTrue(disjunction.contains("c"));
		final Collection<String> disjunction2 = CollUtil.disjunction(list2, list1);
		Assertions.assertTrue(disjunction2.contains("1"));
		Assertions.assertTrue(disjunction2.contains("2"));
		Assertions.assertTrue(disjunction2.contains("3"));
		Assertions.assertTrue(disjunction2.contains("a"));
		Assertions.assertTrue(disjunction2.contains("b"));
		Assertions.assertTrue(disjunction2.contains("c"));
	}

	@Test
	public void subtractTest() {
		final List<String> list1 = ListUtil.of("a", "b", "b", "c", "d", "x");
		final List<String> list2 = ListUtil.of("a", "b", "b", "b", "c", "d", "x2");
		final Collection<String> subtract = CollUtil.subtract(list1, list2);
		Assertions.assertEquals(1, subtract.size());
		Assertions.assertEquals("x", subtract.iterator().next());
	}

	@Test
	public void subtractSetTest() {
		final HashMap<String, Object> map1 = MapUtil.newHashMap();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map1.put("1", "v1");
		map1.put("2", "v2");
		map2.put("2", "v2");
		final Collection<String> r2 = CollUtil.subtract(map1.keySet(), map2.keySet());
		Assertions.assertEquals("[1]", r2.toString());
	}

	@Test
	public void subtractSetToListTest() {
		final HashMap<String, Object> map1 = MapUtil.newHashMap();
		final HashMap<String, Object> map2 = MapUtil.newHashMap();
		map1.put("1", "v1");
		map1.put("2", "v2");
		map2.put("2", "v2");
		final List<String> r2 = CollUtil.subtractToList(map1.keySet(), map2.keySet());
		Assertions.assertEquals("[1]", r2.toString());
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
		final List<HashMap<String, String>> list = ListUtil.of(map1, map2);
		final Map<String, List<String>> map = CollUtil.toListMap(list);
		Assertions.assertEquals("值1", map.get("a").get(0));
		Assertions.assertEquals("值2", map.get("a").get(1));

		// ----------------------------------------------------------------------------------------
		final List<Map<String, String>> listMap = CollUtil.toMapList(map);
		Assertions.assertEquals("值1", listMap.get(0).get("a"));
		Assertions.assertEquals("值2", listMap.get(1).get("a"));
	}

	@Test
	public void getFieldValuesTest() {
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final Dict v2 = Dict.of().set("age", 13).set("id", 15).set("name", "李四");
		final List<Dict> list = ListUtil.of(v1, v2);

		final List<Object> fieldValues = (List<Object>) CollUtil.getFieldValues(list, "name");

		Assertions.assertEquals("张三", fieldValues.get(0));
		Assertions.assertEquals("李四", fieldValues.get(1));
	}

	@Test
	public void partitionTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<List<Integer>> split = CollUtil.partition(list, 3);
		Assertions.assertEquals(3, split.size());
		Assertions.assertEquals(3, split.get(0).size());
	}

	@Test
	public void partitionTest2() {
		final ArrayList<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<List<Integer>> split = CollUtil.partition(list, Integer.MAX_VALUE);
		Assertions.assertEquals(1, split.size());
		Assertions.assertEquals(9, split.get(0).size());
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
		Assertions.assertEquals("1", result[0]);
	}

	@Test
	public void editTest() {
		final List<String> list = ListUtil.of("a", "b", "c");

		final Collection<String> filtered = CollUtil.edit(list, t -> t + 1);

		Assertions.assertEquals(ListUtil.of("a1", "b1", "c1"), filtered);
	}

	@Test
	public void removeTest() {
		final List<String> list = ListUtil.of("a", "b", "c");

		final List<String> filtered = CollUtil.remove(list, "a"::equals);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		Assertions.assertEquals(ListUtil.of("b", "c"), filtered);
	}

	@Test
	public void removeForSetTest() {
		final Set<String> set = SetUtil.ofLinked("a", "b", "", "  ", "c");
		final Set<String> filtered = CollUtil.remove(set, StrUtil::isBlank);

		Assertions.assertEquals(SetUtil.ofLinked("a", "b", "c"), filtered);
	}

	@Test
	public void filterRemoveTest() {
		final List<String> list = ListUtil.of("a", "b", "c");

		final List<String> removed = new ArrayList<>();
		final List<String> filtered = CollUtil.remove(list, t -> {
			if ("a".equals(t)) {
				removed.add(t);
				return true;
			}
			return false;
		});

		Assertions.assertEquals(1, removed.size());
		Assertions.assertEquals("a", removed.get(0));

		// 原地过滤
		Assertions.assertSame(list, filtered);
		Assertions.assertEquals(ListUtil.of("b", "c"), filtered);
	}

	@Test
	public void removeNullTest() {
		final List<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final List<String> filtered = CollUtil.removeNull(list);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		Assertions.assertEquals(ListUtil.of("a", "b", "c", "", "  "), filtered);
	}

	@Test
	public void removeEmptyTest() {
		final List<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final List<String> filtered = CollUtil.removeEmpty(list);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		Assertions.assertEquals(ListUtil.of("a", "b", "c", "  "), filtered);
	}

	@Test
	public void removeBlankTest() {
		final List<String> list = ListUtil.of("a", "b", "c", null, "", "  ");

		final List<String> filtered = CollUtil.removeBlank(list);

		// 原地过滤
		Assertions.assertSame(list, filtered);
		Assertions.assertEquals(ListUtil.of("a", "b", "c"), filtered);
	}

	@Test
	public void groupTest() {
		final List<String> list = ListUtil.of("1", "2", "3", "4", "5", "6");
		final List<List<String>> group = CollUtil.group(list, null);
		Assertions.assertFalse(group.isEmpty());

		final List<List<String>> group2 = CollUtil.group(list, t -> {
			// 按照奇数偶数分类
			return Integer.parseInt(t) % 2;
		});
		Assertions.assertEquals(ListUtil.of("2", "4", "6"), group2.get(0));
		Assertions.assertEquals(ListUtil.of("1", "3", "5"), group2.get(1));
	}

	@Test
	public void groupByFieldTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		final List<List<TestBean>> groupByField = CollUtil.groupByField(list, "age");
		Assertions.assertEquals("张三", groupByField.get(0).get(0).getName());
		Assertions.assertEquals("王五", groupByField.get(0).get(1).getName());

		Assertions.assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void groupByFuncTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		final List<List<TestBean>> groupByField = CollUtil.groupByFunc(list, TestBean::getAge);
		Assertions.assertEquals("张三", groupByField.get(0).get(0).getName());
		Assertions.assertEquals("王五", groupByField.get(0).get(1).getName());

		Assertions.assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void groupByFunc2Test() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12), new TestBean("李四", 13), new TestBean("王五", 12));
		final List<List<TestBean>> groupByField = CollUtil.groupByFunc(list, a -> a.getAge() > 12);
		Assertions.assertEquals("张三", groupByField.get(0).get(0).getName());
		Assertions.assertEquals("王五", groupByField.get(0).get(1).getName());

		Assertions.assertEquals("李四", groupByField.get(1).get(0).getName());
	}

	@Test
	public void sortByPropertyTest() {
		final List<TestBean> list = ListUtil.of(
			new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 12, DateUtil.parse("2018-04-01"))//
		);

		CollUtil.sortByProperty(list, "createTime");
		Assertions.assertEquals("李四", list.get(0).getName());
		Assertions.assertEquals("王五", list.get(1).getName());
		Assertions.assertEquals("张三", list.get(2).getName());
	}

	@Test
	public void sortByPropertyTest2() {
		final List<TestBean> list = ListUtil.of(
			new TestBean("张三", 0, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", -12, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 23, DateUtil.parse("2018-04-01"))//
		);

		CollUtil.sortByProperty(list, "age");
		Assertions.assertEquals("李四", list.get(0).getName());
		Assertions.assertEquals("张三", list.get(1).getName());
		Assertions.assertEquals("王五", list.get(2).getName());
	}

	@Test
	public void fieldValueMapTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 12, DateUtil.parse("2018-04-01"))//
		);

		final Map<String, TestBean> map = CollUtil.fieldValueMap(list, "name");
		Assertions.assertEquals("李四", map.get("李四").getName());
		Assertions.assertEquals("王五", map.get("王五").getName());
		Assertions.assertEquals("张三", map.get("张三").getName());
	}

	@Test
	public void fieldValueAsMapTest() {
		final List<TestBean> list = ListUtil.of(new TestBean("张三", 12, DateUtil.parse("2018-05-01")), //
			new TestBean("李四", 13, DateUtil.parse("2018-03-01")), //
			new TestBean("王五", 14, DateUtil.parse("2018-04-01"))//
		);

		final Map<String, Integer> map = CollUtil.fieldValueAsMap(list, "name", "age");
		Assertions.assertEquals(Integer.valueOf(12), map.get("张三"));
		Assertions.assertEquals(Integer.valueOf(13), map.get("李四"));
		Assertions.assertEquals(Integer.valueOf(14), map.get("王五"));
	}

	@Test
	public void emptyTest() {
		final SortedSet<String> emptySortedSet = CollUtil.empty(SortedSet.class);
		Assertions.assertEquals(Collections.emptySortedSet(), emptySortedSet);

		final Set<String> emptySet = CollUtil.empty(Set.class);
		Assertions.assertEquals(Collections.emptySet(), emptySet);

		final List<String> emptyList = CollUtil.empty(List.class);
		Assertions.assertEquals(Collections.emptyList(), emptyList);
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

		Assertions.assertInstanceOf(ArrayList.class, list1);
		Assertions.assertInstanceOf(LinkedList.class, list2);
	}

	@Test
	public void listTest2() {
		final List<String> list1 = ListUtil.of("a", "b", "c");
		final List<String> list2 = ListUtil.ofLinked("a", "b", "c");
		Assertions.assertEquals("[a, b, c]", list1.toString());
		Assertions.assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void listTest3() {
		final HashSet<String> set = new LinkedHashSet<>();
		set.add("a");
		set.add("b");
		set.add("c");

		final List<String> list1 = ListUtil.of(false, set);
		final List<String> list2 = ListUtil.of(true, set);
		Assertions.assertEquals("[a, b, c]", list1.toString());
		Assertions.assertEquals("[a, b, c]", list2.toString());
	}

	@Test
	public void getTest() {
		final HashSet<String> set = SetUtil.ofLinked("A", "B", "C", "D");
		String str = CollUtil.get(set, 2);
		Assertions.assertEquals("C", str);

		str = CollUtil.get(set, -1);
		Assertions.assertEquals("D", str);
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
		Assertions.assertEquals(arrayList, retval);
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
		Assertions.assertEquals(arrayList, retval);
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
		Assertions.assertEquals(arrayList, retval);
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
		Assertions.assertTrue(retval.isEmpty());
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
		Assertions.assertEquals(arrayList, retval);
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
		Assertions.assertEquals(arrayList, retval);
	}

	@Test
	public void subInput1PositiveNegativePositiveOutputArrayIndexOutOfBoundsException() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			// Arrange
			final List<Integer> list = new ArrayList<>();
			list.add(null);
			final int start = 2_147_483_643;
			final int end = -2_147_483_648;
			final int step = 2;

			// Act
			CollUtil.sub(list, start, end, step);
			// Method is not expected to return due to exception thrown
		});
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
		Assertions.assertTrue(retval.isEmpty());
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
		Assertions.assertEquals(arrayList, retval);
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
		Assertions.assertEquals(arrayList, retval);
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
		Assertions.assertEquals(arrayList, retval);
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
		Assertions.assertTrue(retval.isEmpty());
	}

	@Test
	public void sortPageAllTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
		final List<Integer> sortPageAll = CollUtil.sortPageAll(1, 5, Comparator.reverseOrder(), list);

		Assertions.assertEquals(ListUtil.of(4, 3, 2, 1), sortPageAll);
	}

	@Test
	public void containsAnyTest() {
		final List<Integer> list1 = ListUtil.of(1, 2, 3, 4, 5);
		final List<Integer> list2 = ListUtil.of(5, 3, 1, 9, 11);

		Assertions.assertTrue(CollUtil.containsAny(list1, list2));
	}

	@Test
	public void containsAllTest() {
		final List<Integer> list1 = ListUtil.of(1, 2, 3, 4, 5);
		final List<Integer> list2 = ListUtil.of(5, 3, 1);
		Assertions.assertTrue(CollUtil.containsAll(list1, list2));

		final List<Integer> list3 = ListUtil.of(1);
		final List<Integer> list4 = ListUtil.of();
		Assertions.assertTrue(CollUtil.containsAll(list3, list4));
	}

	@Test
	public void getLastTest() {
		// 测试：空数组返回null而不是报错
		final List<String> test = ListUtil.of();
		final String last = CollUtil.getLast(test);
		Assertions.assertNull(last);
	}

	@Test
	public void zipTest() {
		final Collection<String> keys = ListUtil.of("a", "b", "c", "d");
		final Collection<Integer> values = ListUtil.of(1, 2, 3, 4);

		final Map<String, Integer> map = CollUtil.zip(keys, values);

		Assertions.assertEquals(4, Objects.requireNonNull(map).size());

		Assertions.assertEquals(1, map.get("a").intValue());
		Assertions.assertEquals(2, map.get("b").intValue());
		Assertions.assertEquals(3, map.get("c").intValue());
		Assertions.assertEquals(4, map.get("d").intValue());
	}

	@Test
	public void toMapTest() {
		final Collection<String> keys = ListUtil.of("a", "b", "c", "d");
		final Map<String, String> map = IterUtil.toMap(keys, (value) -> "key" + value);
		Assertions.assertEquals("a", map.get("keya"));
		Assertions.assertEquals("b", map.get("keyb"));
		Assertions.assertEquals("c", map.get("keyc"));
		Assertions.assertEquals("d", map.get("keyd"));
	}

	@Test
	public void mapToMapTest() {
		final HashMap<String, String> oldMap = new HashMap<>();
		oldMap.put("a", "1");
		oldMap.put("b", "12");
		oldMap.put("c", "134");

		final Map<String, Long> map = IterUtil.toMap(oldMap.entrySet(),
			Map.Entry::getKey,
			entry -> Long.parseLong(entry.getValue()));

		Assertions.assertEquals(1L, (long) map.get("a"));
		Assertions.assertEquals(12L, (long) map.get("b"));
		Assertions.assertEquals(134L, (long) map.get("c"));
	}

	@Test
	public void countMapTest() {
		final List<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final Map<String, Integer> countMap = CollUtil.countMap(list);

		Assertions.assertEquals(Integer.valueOf(2), countMap.get("a"));
		Assertions.assertEquals(Integer.valueOf(2), countMap.get("b"));
		Assertions.assertEquals(Integer.valueOf(2), countMap.get("c"));
		Assertions.assertEquals(Integer.valueOf(1), countMap.get("d"));
	}

	@Test
	public void indexOfTest() {
		final List<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final int i = CollUtil.indexOf(list, (str) -> str.charAt(0) == 'c');
		Assertions.assertEquals(2, i);
	}

	@Test
	public void lastIndexOfTest() {
		// List有优化
		final List<String> list = ListUtil.of("a", "b", "c", "c", "a", "b", "d");
		final int i = CollUtil.lastIndexOf(list, (str) -> str.charAt(0) == 'a');
		Assertions.assertEquals(4, i);

		final Queue<Integer> set = new ArrayDeque<>(Arrays.asList(1, 2, 3, 3, 2, 1));
		Assertions.assertEquals(5, CollUtil.lastIndexOf(set, num -> num.equals(1)));
		Assertions.assertEquals(4, CollUtil.lastIndexOf(set, num -> num.equals(2)));
		Assertions.assertEquals(3, CollUtil.lastIndexOf(set, num -> num.equals(3)));
		Assertions.assertEquals(-1, CollUtil.lastIndexOf(set, num -> num.equals(4)));
	}

	@Test
	public void lastIndexOfSetTest() {
		final Set<String> list = SetUtil.ofLinked("a", "b", "c", "c", "a", "b", "d");
		// 去重后c排第三
		final int i = CollUtil.lastIndexOf(list, (str) -> str.charAt(0) == 'c');
		Assertions.assertEquals(2, i);
	}

	@Test
	public void pageTest() {
		final List<Dict> objects = ListUtil.of();
		for (int i = 0; i < 10; i++) {
			objects.add(Dict.of().set("name", "姓名：" + i));
		}

		Assertions.assertEquals(0, ListUtil.page(objects, 3, 5).size());
	}

	@Test
	public void subtractToListTest() {
		final List<Long> list1 = Arrays.asList(1L, 2L, 3L);
		final List<Long> list2 = Arrays.asList(2L, 3L);

		final List<Long> result = CollUtil.subtractToList(list1, list2);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(1L, (long) result.get(0));
	}

	@Test
	public void sortNaturalTest() {
		final List<String> of = ListUtil.of("a", "c", "b");
		final List<String> sort = CollUtil.sort(of, CompareUtil.natural());
		Assertions.assertEquals("a,b,c", CollUtil.join(sort, ","));
	}

	@Test
	public void setValueByMapTest() {
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

		Assertions.assertEquals(people.get(1).getGender(), "woman");
		CollUtil.setValueByMap(people, genderMap, Person::getId, Person::setGender);
		Assertions.assertEquals(people.get(1).getGender(), "妇女");

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

		Assertions.assertEquals(people.get(1).getGender(), "小孩");
	}

	@Test
	public void distinctTest() {
		final List<Integer> distinct = CollUtil.distinct(ListUtil.view(5, 3, 10, 9, 0, 5, 10, 9));
		Assertions.assertEquals(ListUtil.view(5, 3, 10, 9, 0), distinct);
	}

	@Test
	public void distinctByFunctionTest() {
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
		Assertions.assertEquals(2, distinct.size());
		Assertions.assertEquals("ff", distinct.get(0).getName());
		Assertions.assertEquals("ee", distinct.get(1).getName());

		// 非覆盖模式下，保留了最早加入的aa和bb
		distinct = CollUtil.distinct(people, Person::getGender, false);
		Assertions.assertEquals(2, distinct.size());
		Assertions.assertEquals("aa", distinct.get(0).getName());
		Assertions.assertEquals("bb", distinct.get(1).getName());
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
	public void mapTest() {
		final List<String> list = ListUtil.of("a", "b", "c");
		final List<Object> extract = CollUtil.map(list, (e) -> e + "_1");
		Assertions.assertEquals(ListUtil.of("a_1", "b_1", "c_1"), extract);
	}

	@Test
	public void mapBeanTest() {
		final List<Person> people = Arrays.asList(
			new Person("aa", 12, "man", 1),
			new Person("bb", 13, "woman", 2),
			new Person("cc", 14, "man", 3),
			new Person("dd", 15, "woman", 4)
		);

		final List<Object> extract = CollUtil.map(people, Person::getName);
		Assertions.assertEquals(ListUtil.of("aa", "bb", "cc", "dd"), extract);
	}

	@Test
	public void createTest() {
		final Collection<Object> collection = CollUtil.create(Collections.emptyList().getClass());
		Console.log(collection.getClass());
		Assertions.assertNotNull(collection);
	}

	@Test
	public void transTest() {
		final List<Person> people = Arrays.asList(
			new Person("aa", 12, "man", 1),
			new Person("bb", 13, "woman", 2),
			new Person("cc", 14, "man", 3),
			new Person("dd", 15, "woman", 4)
		);

		final Collection<String> trans = CollUtil.trans(people, Person::getName);
		Assertions.assertEquals("[aa, bb, cc, dd]", trans.toString());
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void unionNullTest() {
		final List<String> list1 = new ArrayList<>();
		final List<String> list2 = null;
		final List<String> list3 = null;
		final Collection<String> union = CollUtil.union(list1, list2, list3);
		Assertions.assertNotNull(union);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void unionDistinctNullTest() {
		final List<String> list1 = new ArrayList<>();
		final List<String> list2 = null;
		final List<String> list3 = null;
		final Set<String> set = CollUtil.unionDistinct(list1, list2, list3);
		Assertions.assertNotNull(set);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void unionAllNullTest() {
		final List<String> list1 = new ArrayList<>();
		final List<String> list2 = null;
		final List<String> list3 = null;
		final List<String> list = CollUtil.unionAll(list1, list2, list3);
		Assertions.assertNotNull(list);

		Assertions.assertEquals(
			ListUtil.of(1, 2, 3, 4),
			CollUtil.unionAll(ListUtil.of(1), ListUtil.of(2), ListUtil.of(3), ListUtil.of(4))
		);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void intersectionNullTest() {
		final List<String> list1 = new ArrayList<>();
		list1.add("aa");
		final List<String> list2 = new ArrayList<>();
		list2.add("aa");
		final List<String> list3 = null;
		final Collection<String> collection = CollUtil.intersection(list1, list2, list3);
		Assertions.assertNotNull(collection);
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void intersectionDistinctNullTest() {
		final List<String> list1 = new ArrayList<>();
		list1.add("aa");
		final List<String> list2 = null;
		// list2.add("aa");
		final List<String> list3 = null;
		final Collection<String> collection = CollUtil.intersectionDistinct(list1, list2, list3);
		Assertions.assertNotNull(collection);
	}

	@Test
	public void addIfAbsentTest() {
		// 为false的情况
		Assertions.assertFalse(CollUtil.addIfAbsent(null, null));
		Assertions.assertFalse(CollUtil.addIfAbsent(ListUtil.of(), null));
		Assertions.assertFalse(CollUtil.addIfAbsent(null, "123"));
		Assertions.assertFalse(CollUtil.addIfAbsent(ListUtil.of("123"), "123"));
		Assertions.assertFalse(CollUtil.addIfAbsent(ListUtil.of(new Animal("jack", 20)),
			new Animal("jack", 20)));

		// 正常情况
		Assertions.assertTrue(CollUtil.addIfAbsent(ListUtil.of("456"), "123"));
		Assertions.assertTrue(CollUtil.addIfAbsent(ListUtil.of(new Animal("jack", 20)),
			new Dog("jack", 20)));
		Assertions.assertTrue(CollUtil.addIfAbsent(ListUtil.of(new Animal("jack", 20)),
			new Animal("tom", 20)));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class Animal {
		private String name;
		private Integer age;
	}

	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	@Data
	static class Dog extends Animal {
		public Dog(final String name, final Integer age) {
			super(name, age);
		}
	}

	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	@Data
	static class Cat extends Animal {

		public Cat(final String name, final Integer age) {
			super(name, age);
		}
	}

	@ToString(callSuper = true)
	@EqualsAndHashCode(callSuper = true)
	@Data
	static class Pig extends Animal {

		public Pig(final String name, final Integer age) {
			super(name, age);
		}
	}

	@Test
	public void getFirstTest() {
		Assertions.assertNull(CollUtil.getFirst(null));
		Assertions.assertNull(CollUtil.getFirst(ListUtil.of()));

		Assertions.assertEquals("1", CollUtil.getFirst(ListUtil.of("1", "2", "3")));
		final ArrayDeque<String> deque = new ArrayDeque<>();
		deque.add("3");
		deque.add("4");
		Assertions.assertEquals("3", CollUtil.getFirst(deque));
	}

	@Test
	public void popPartTest() {
		final Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < 10; i++) {
			stack.push(i);
		}
		final List<Integer> popPart1 = CollUtil.popPart(stack, 3);
		Assertions.assertEquals(ListUtil.of(9, 8, 7), popPart1);
		Assertions.assertEquals(7, stack.size());

		final ArrayDeque<Integer> queue = new ArrayDeque<>();
		for (int i = 0; i < 10; i++) {
			queue.push(i);
		}
		final List<Integer> popPart2 = CollUtil.popPart(queue, 3);
		Assertions.assertEquals(ListUtil.of(9, 8, 7), popPart2);
		Assertions.assertEquals(7, queue.size());
	}

	@Test
	public void isEqualListTest() {
		final List<Integer> list = ListUtil.of(1, 2, 3, 4);
		Assertions.assertTrue(CollUtil.isEqualList(null, null));
		Assertions.assertTrue(CollUtil.isEqualList(ListUtil.of(), ListUtil.of()));
		Assertions.assertTrue(CollUtil.isEqualList(list, list));
		Assertions.assertTrue(CollUtil.isEqualList(list, ListUtil.of(1, 2, 3, 4)));

		Assertions.assertFalse(CollUtil.isEqualList(null, ListUtil.of()));
		Assertions.assertFalse(CollUtil.isEqualList(list, ListUtil.of(1, 2, 3, 3)));
		Assertions.assertFalse(CollUtil.isEqualList(list, ListUtil.of(1, 2, 3)));
		Assertions.assertFalse(CollUtil.isEqualList(list, ListUtil.of(4, 3, 2, 1)));
	}

	@Test
	public void testMatch() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		Assertions.assertTrue(CollUtil.anyMatch(list, i -> i == 1));
		Assertions.assertFalse(CollUtil.anyMatch(list, i -> i > 6));
		Assertions.assertFalse(CollUtil.allMatch(list, i -> i == 1));
		Assertions.assertTrue(CollUtil.allMatch(list, i -> i <= 6));
	}

	@Test
	public void maxTest() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
		Assertions.assertEquals((Integer) 6, CollUtil.max(list));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	public void maxEmptyTest() {
		final List<? extends Comparable> emptyList = Collections.emptyList();
		Assertions.assertNull(CollUtil.max(emptyList));
	}

	@Test
	public void minNullTest() {
		Assertions.assertNull(CollUtil.max(null));
	}

	@Test
	public void unionExtendTest() {
		final List<Dog> dog = Arrays.asList(new Dog("dog1", 12), new Dog("dog2", 12));
		final List<Cat> cat = Arrays.asList(new Cat("cat1", 12), new Cat("cat2", 12));
		Assertions.assertEquals(CollUtil.union(dog, cat).size(), dog.size() + cat.size());
	}

	@Test
	public void unionAllExtendTest() {
		final List<Dog> dog = Arrays.asList(new Dog("dog1", 12), new Dog("dog2", 12));
		final List<Cat> cat = Arrays.asList(new Cat("cat1", 12), new Cat("cat2", 12));
		final List<Pig> pig = Arrays.asList(new Pig("pig1", 12), new Pig("pig2", 12));
		Assertions.assertEquals(CollUtil.unionAll(dog, cat, pig).size(), dog.size() + cat.size() + pig.size());
	}

	@Test
	public void unionDistinctExtendTest() {
		final List<Dog> dog = Arrays.asList(new Dog("dog1", 12), new Dog("dog1", 12)); // same
		final List<Cat> cat = Arrays.asList(new Cat("cat1", 12), new Cat("cat2", 12));
		final List<Pig> pig = Arrays.asList(new Pig("pig1", 12), new Pig("pig2", 12));
		Assertions.assertEquals(CollUtil.unionDistinct(dog, cat, pig).size(), 5);
	}


	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	@Test
	public void flatListTest1() {
		final List<List<List<String>>> list = Arrays.asList(Arrays.asList(Arrays.asList("1", "2", "3"), Arrays.asList("5", "6", "7")));

		final List<Object> objects = CollUtil.flat(list);

		Assertions.assertArrayEquals(new String[]{"1", "2", "3", "5", "6", "7"}, objects.toArray());
	}


	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	@Test
	public void flatListTest2() {
		final List<List<List<String>>> list = Arrays.asList(
			Arrays.asList(
				Arrays.asList("a"),
				Arrays.asList("b", "c"),
				Arrays.asList("d", "e", "f")
			),
			Arrays.asList(
				Arrays.asList("g", "h", "i"),
				Arrays.asList("j", "k", "l")
			)
		);
		final List<Object> flat = CollUtil.flat(list);
		Assertions.assertArrayEquals(new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"}, flat.toArray());

	}


	@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
	@Test
	void flatListTest3() {
		final List<List<List<String>>> list = Arrays.asList(
			Arrays.asList(
				Arrays.asList("a"),
				Arrays.asList("b", "c", null),
				Arrays.asList("d", "e", "f")
			),
			Arrays.asList(
				Arrays.asList("g", "h", "i"),
				Arrays.asList("j", "k", "l")
			)
		);
		final List<Object> flat = CollUtil.flat(list, false);
		Assertions.assertArrayEquals(new String[]{"a", "b", "c", null, "d", "e", "f", "g", "h", "i", "j", "k", "l"}, flat.toArray());
	}

	@Test
	void issueI8Z2Q4Test() {
		final ArrayList<String> coll1 = new ArrayList<>();
		coll1.add("1");
		coll1.add("2");
		coll1.add("3");
		coll1.add("4");
		final ArrayList<String> coll2 = new ArrayList<>();
		coll2.add("1");
		coll2.add("1");
		coll2.add("1");
		coll2.add("1");
		coll2.add("1");

		Assertions.assertTrue(CollUtil.containsAll(coll1, coll2));
	}
}
