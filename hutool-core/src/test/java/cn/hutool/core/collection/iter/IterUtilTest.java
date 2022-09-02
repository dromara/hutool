package cn.hutool.core.collection.iter;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.NodeList;

import java.util.*;
import java.util.function.Function;

/**
 * test for {@link IterUtil}
 */
public class IterUtilTest {

	@Test
	public void testGetIter() {
		Assert.assertNull(IterUtil.getIter(null));
		Assert.assertEquals(Collections.emptyIterator(), IterUtil.getIter(Collections.emptyList()));

		Assert.assertNull(IterUtil.getIter((Object)null));
		Assert.assertNotNull(IterUtil.getIter(Collections.emptyIterator()));
		Assert.assertNotNull(IterUtil.getIter((Object)Collections.emptyList()));
		Assert.assertNotNull(IterUtil.getIter(new Integer[0]));
		Assert.assertNotNull(IterUtil.getIter(Collections.emptyMap()));
		Assert.assertNull(IterUtil.getIter((NodeList)null));
	}

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(IterUtil.isEmpty(Collections.emptyIterator()));
		Assert.assertFalse(IterUtil.isEmpty(Arrays.asList(1, 2).iterator()));

		Assert.assertTrue(IterUtil.isEmpty(Collections.emptyList()));
		Assert.assertFalse(IterUtil.isEmpty(Arrays.asList(1, 2)));
	}

	@Test
	public void testIsNotEmpty() {
		Assert.assertFalse(IterUtil.isNotEmpty(Collections.emptyIterator()));
		Assert.assertTrue(IterUtil.isNotEmpty(Arrays.asList(1, 2).iterator()));

		Assert.assertFalse(IterUtil.isNotEmpty(Collections.emptyList()));
		Assert.assertTrue(IterUtil.isNotEmpty(Arrays.asList(1, 2)));
	}

	@Test
	public void testHasNull() {
		Assert.assertFalse(IterUtil.hasNull(Arrays.asList(1, 3, 2).iterator()));
		Assert.assertTrue(IterUtil.hasNull(Arrays.asList(1, null, 2).iterator()));
		Assert.assertFalse(IterUtil.hasNull(Collections.emptyIterator()));
		Assert.assertTrue(IterUtil.hasNull(null));
	}

	@Test
	public void testIsAllNull() {
		Assert.assertTrue(IterUtil.isAllNull(Arrays.asList(null, null)));
		Assert.assertFalse(IterUtil.isAllNull(Arrays.asList(1, null)));
		Assert.assertTrue(IterUtil.isAllNull((Iterable<?>)null));
		Assert.assertTrue(IterUtil.isAllNull(Arrays.asList(null, null).iterator()));
		Assert.assertFalse(IterUtil.isAllNull(Arrays.asList(1, null).iterator()));
		Assert.assertTrue(IterUtil.isAllNull((Iterator<?>)null));
	}

	@Test
	public void testCountMap() {
		Object o1 = new Object();
		Object o2 = new Object();
		Map<Object, Integer> countMap = IterUtil.countMap(Arrays.asList(o1, o2, o1, o1).iterator());
		Assert.assertEquals((Integer)3, countMap.get(o1));
		Assert.assertEquals((Integer)1, countMap.get(o2));
	}

	@Test
	public void testFieldValueMap() {
		Bean bean1 = new Bean(1, "A");
		Bean bean2 = new Bean(2, "B");
		Map<Integer, Bean> map = IterUtil.fieldValueMap(Arrays.asList(bean1, bean2).iterator(), "id");
		Assert.assertEquals(bean1, map.get(1));
		Assert.assertEquals(bean2, map.get(2));
	}

	@Test
	public void testFieldValueAsMap() {
		Bean bean1 = new Bean(1, "A");
		Bean bean2 = new Bean(2, "B");
		Map<Integer, String> map = IterUtil.fieldValueAsMap(
			Arrays.asList(bean1, bean2).iterator(), "id", "name"
		);
		Assert.assertEquals("A", map.get(1));
		Assert.assertEquals("B", map.get(2));
	}

	@Test
	public void testFieldValueList() {
		Bean bean1 = new Bean(1, "A");
		Bean bean2 = new Bean(2, "B");
		Assert.assertEquals(Arrays.asList(1, 2), IterUtil.fieldValueList(Arrays.asList(bean1, bean2), "id"));
		Assert.assertEquals(
			Arrays.asList(1, 2),
			IterUtil.fieldValueList(Arrays.asList(bean1, bean2).iterator(), "id")
		);
	}

	@Test
	public void testJoin() {
		List<String> stringList = Arrays.asList("1", "2", "3");
		Assert.assertEquals("123", IterUtil.join(stringList.iterator(), ""));
		Assert.assertEquals("-1--2--3-", IterUtil.join(stringList.iterator(), "", "-", "-"));
		Assert.assertEquals("123", IterUtil.join(stringList.iterator(), "", Function.identity()));
	}

	@Test
	public void testToMap() {
		List<Integer> keys = Arrays.asList(1, 2, 3);

		Map<Integer, Integer> map = IterUtil.toMap(keys, keys);
		Assert.assertEquals(keys, new ArrayList<>(map.keySet()));
		Assert.assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys.iterator(), keys.iterator());
		Assert.assertEquals(keys, new ArrayList<>(map.keySet()));
		Assert.assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys.iterator(), keys.iterator(), true);
		Assert.assertEquals(keys, new ArrayList<>(map.keySet()));
		Assert.assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys, keys, true);
		Assert.assertEquals(keys, new ArrayList<>(map.keySet()));
		Assert.assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys, Function.identity());
		Assert.assertEquals(keys, new ArrayList<>(map.keySet()));
		Assert.assertEquals(keys, new ArrayList<>(map.values()));

		map = IterUtil.toMap(keys, Function.identity(), Function.identity());
		Assert.assertEquals(keys, new ArrayList<>(map.keySet()));
		Assert.assertEquals(keys, new ArrayList<>(map.values()));
	}

	@Test
	public void testToListMap() {
		List<Integer> keys = Arrays.asList(1, 2, 3, 4);

		Map<Boolean, List<Integer>> map = IterUtil.toListMap(keys, i -> (i & 1) == 0, Function.identity());
		Assert.assertEquals(Arrays.asList(2, 4), map.get(true));
		Assert.assertEquals(Arrays.asList(1, 3), map.get(false));

		map = IterUtil.toListMap(keys, i -> (i & 1) == 0);
		Assert.assertEquals(Arrays.asList(2, 4), map.get(true));
		Assert.assertEquals(Arrays.asList(1, 3), map.get(false));

		map = new LinkedHashMap<>();
		Map<Boolean, List<Integer>> rawMap = IterUtil.toListMap(map, keys, i -> (i & 1) == 0, Function.identity());
		Assert.assertSame(rawMap, map);
		Assert.assertEquals(Arrays.asList(2, 4), rawMap.get(true));
		Assert.assertEquals(Arrays.asList(1, 3), rawMap.get(false));
	}

	@Test
	public void testAsIterable() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3).iterator();
		Assert.assertEquals(iter, IterUtil.asIterable(iter).iterator());
		Assert.assertNull(IterUtil.asIterable(null).iterator());

		Enumeration<Integer> enumeration = new IteratorEnumeration<>(iter);
		Iterator<Integer> iter2 = IterUtil.asIterator(enumeration);
		Assert.assertEquals((Integer)1, iter2.next());
		Assert.assertEquals((Integer)2, iter2.next());
		Assert.assertEquals((Integer)3, iter2.next());
	}

	@Test
	public void testGet() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		Assert.assertEquals((Integer)3, IterUtil.get(iter, 2));
		Assert.assertThrows(IllegalArgumentException.class, () -> IterUtil.get(iter, -1));
	}

	@Test
	public void testGetFirst() {
		Iterator<Integer> iter = Arrays.asList(1, 2, 3, 4).iterator();
		Assert.assertEquals((Integer)1, IterUtil.getFirst(iter));
		Assert.assertNull(IterUtil.getFirst(null));
		Assert.assertNull(IterUtil.getFirst(Collections.emptyIterator()));

		Assert.assertEquals((Integer)2, IterUtil.getFirst(iter, t -> (t & 1) == 0));
		Assert.assertNull(IterUtil.getFirst((Iterator<Integer>)null, t -> (t & 1) == 0));
		Assert.assertNull(IterUtil.getFirst(Collections.emptyIterator(), Objects::nonNull));
	}

	@Test
	public void testGetFirstNoneNull() {
		Iterator<Integer> iter = Arrays.asList(null, 2, null, 4).iterator();
		Assert.assertEquals((Integer)2, IterUtil.getFirstNoneNull(iter));
		Assert.assertNull(IterUtil.getFirstNoneNull(null));
		Assert.assertNull(IterUtil.getFirstNoneNull(Collections.emptyIterator()));
	}

	@Test
	public void testGetElementType() {
		List<Object> list = Arrays.asList(null, "str", null);
		Assert.assertEquals(String.class, IterUtil.getElementType(list));
		Assert.assertNull(IterUtil.getElementType((Iterable<?>)null));
		Assert.assertNull(IterUtil.getElementType(Collections.emptyList()));

		Assert.assertEquals(String.class, IterUtil.getElementType(list.iterator()));
		Assert.assertNull(IterUtil.getElementType((Iterator<?>)null));
		Assert.assertNull(IterUtil.getElementType(Collections.emptyIterator()));
	}

	@Test
	public void testEdit() {
		Assert.assertEquals(
			Collections.singletonList("str"),
			IterUtil.edit(Arrays.asList(null, "str", null).iterator(), t -> t)
		);
		Assert.assertEquals(
			Collections.singletonList("str"),
			IterUtil.edit(Arrays.asList(null, "str", null).iterator(), null)
		);
		Assert.assertEquals(Collections.emptyList(), IterUtil.edit(null, t -> t));
	}

	@Test
	public void testRemove() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, null, null, 3));
		IterUtil.remove(list.iterator(), Objects::isNull);
		Assert.assertEquals(Arrays.asList(1, 3), list);
	}

	@Test
	public void testFilterToList() {
		List<Integer> list1 = new ArrayList<>(Arrays.asList(1, null, null, 3));
		List<Integer> list2 = IterUtil.filterToList(list1.iterator(), Objects::nonNull);
		Assert.assertSame(list1, list1);
		Assert.assertEquals(Arrays.asList(1, 3), list2);
	}

	@Test
	public void testFiltered() {
		Assert.assertNotNull(IterUtil.filtered(Collections.emptyIterator(), t -> true));
	}

	@Test
	public void testEmpty() {
		Assert.assertSame(Collections.emptyIterator(), IterUtil.empty());
	}

	@Test
	public void testTrans() {
		Assert.assertNotNull(IterUtil.trans(Collections.emptyIterator(), t -> true));
	}

	@Test
	public void testSize() {
		Assert.assertEquals(0, IterUtil.size((Iterator<?>)null));
		Assert.assertEquals(0, IterUtil.size(Collections.emptyIterator()));
		Assert.assertEquals(3, IterUtil.size(Arrays.asList(1, 2, 3).iterator()));

		Assert.assertEquals(0, IterUtil.size((Iterable<?>)null));
		Assert.assertEquals(0, IterUtil.size(Collections.emptyList()));
		Assert.assertEquals(3, IterUtil.size(Arrays.asList(1, 2, 3)));
	}

	@Test
	public void testIsEqualList() {
		Assert.assertFalse(IterUtil.isEqualList(null, Collections.emptyList()));
		Assert.assertFalse(IterUtil.isEqualList(Arrays.asList(1, 2, 3), Collections.emptyList()));
		Assert.assertFalse(IterUtil.isEqualList(Arrays.asList(1, 2, 3), Arrays.asList(1, 2)));
		Assert.assertTrue(IterUtil.isEqualList(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3)));
		Assert.assertTrue(IterUtil.isEqualList(null, null));
		Assert.assertTrue(IterUtil.isEqualList(Collections.emptyList(), Collections.emptyList()));
	}

	@Test
	public void testClear() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
		IterUtil.clear(list.iterator());
		Assert.assertTrue(list.isEmpty());
		Assert.assertThrows(UnsupportedOperationException.class, () -> IterUtil.clear(Arrays.asList(1, 2).iterator()));
	}

	@Test
	public void testToStr() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Assert.assertEquals("[1, 2, 3]", IterUtil.toStr(list.iterator()));
		Assert.assertEquals("[1, 2, 3]", IterUtil.toStr(list.iterator(), Objects::toString));
		Assert.assertEquals("{1:2:3}", IterUtil.toStr(list.iterator(), Objects::toString, ":", "{", "}"));
	}

	@Test
	public void testForEach() {
		List<Integer> list = new ArrayList<>();
		IterUtil.forEach(Arrays.asList(1, 2, 3, 4).iterator(), list::add);
		Assert.assertEquals(Arrays.asList(1, 2, 3, 4), list);
	}

	@RequiredArgsConstructor
	private static class Bean {
		private final Integer id;
		private final String name;
	}

}
