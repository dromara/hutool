package cn.hutool.core.stream;

import cn.hutool.core.map.multi.Table;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EntryStreamTest {

	@Test
	public void testMerge() {
		Assert.assertEquals(0, EntryStream.merge(null, null).count());
		Assert.assertEquals(
			Arrays.asList(2, 4, 6),
			EntryStream.merge(Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3))
				.map(Integer::sum)
				.toList()
		);
		Assert.assertEquals(
			Arrays.asList(1, 2, null),
			EntryStream.merge(Arrays.asList(1, 2, 3), Arrays.asList(1, 2))
				.collectValues(Collectors.toList())
		);
		Assert.assertEquals(
			Arrays.asList(1, 2, null),
			EntryStream.merge(Arrays.asList(1, 2), Arrays.asList(1, 2, 3))
				.collectKeys(Collectors.toList())
		);

		Assert.assertEquals(
			Arrays.asList(1, 2),
			EntryStream.merge(null, Arrays.asList(1, 2))
				.collectValues(Collectors.toList())
		);
		Assert.assertEquals(
			Arrays.asList(1, 2),
			EntryStream.merge(Arrays.asList(1, 2), null)
				.collectKeys(Collectors.toList())
		);
	}

	@Test
	public void testOf() {
		Map<String, String> map = new HashMap<>();
		map.put("1", "1");
		Assert.assertEquals(1, EntryStream.of(map).count());
		Assert.assertEquals(0, EntryStream.of((Map<String, String>)null).count());

		Set<Map.Entry<Integer, Integer>> entries = new HashSet<>();
		entries.add(new Entry<>(1, 1));
		entries.add(null);
		Assert.assertEquals(2, EntryStream.of(entries).count());
		Assert.assertEquals(0, EntryStream.of((Set<Map.Entry<Integer, Integer>>)null).count());
		Assert.assertEquals(2, EntryStream.of(entries.stream()).count());
		Assert.assertEquals(0, EntryStream.of((Stream<Map.Entry<Integer, Integer>>)null).count());
		Assert.assertEquals(2, new EntryStream<>(entries.stream()).count());
		Assert.assertThrows(NullPointerException.class, () -> new EntryStream<>(null));

		Iterable<Integer> iterable = Arrays.asList(1, 2, null);
		Assert.assertEquals(3, EntryStream.of(iterable, Function.identity(), Function.identity()).count());
		Assert.assertEquals(0, EntryStream.of(null, Function.identity(), Function.identity()).count());
	}

	@Test
	public void testEmpty() {
		Assert.assertEquals(0, EntryStream.empty().count());
	}

	@Test
	public void testDistinctByKey() {
		long count = EntryStream.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.distinctByKey()
			.count();
		Assert.assertEquals(2, count);
	}

	@Test
	public void testDistinctByValue() {
		long count = EntryStream.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.distinctByValue()
			.count();
		Assert.assertEquals(2, count);
	}

	@Test
	public void testFilter() {
		long count = EntryStream.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.filter((k, v) -> k == 1 && v == 1)
			.count();
		Assert.assertEquals(1, count);
	}

	@Test
	public void testFilterByKey() {
		long count = EntryStream.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.filterByKey(k -> k == 1)
			.count();
		Assert.assertEquals(2, count);
	}

	@Test
	public void testFilterByValue() {
		long count = EntryStream.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.filterByValue(v -> v == 1)
			.count();
		Assert.assertEquals(2, count);
	}

	@Test
	public void testPeekKey() {
		List<Integer> keys = new ArrayList<>();
		EntryStream.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.peekKey(keys::add)
			.count();
		Assert.assertEquals(Arrays.asList(1, 1, 2, 2), keys);
	}

	@Test
	public void testPeekValue() {
		List<Integer> values = new ArrayList<>();
		EntryStream.of(Arrays.asList(new Entry<>(1, 1), new Entry<>(1, 2), new Entry<>(2, 1), new Entry<>(2, 2)))
			.peekValue(values::add)
			.count();
		Assert.assertEquals(Arrays.asList(1, 2, 1, 2), values);
	}

	@Test
	public void testPush() {
		Assert.assertEquals(
			5,
			EntryStream.of(Arrays.asList(1, 2, 3), Function.identity(), Function.identity())
				.push(4, 4)
				.push(5, 5)
				.count()
		);
	}

	@Test
	public void testUnshift() {
		Assert.assertEquals(
			5,
			EntryStream.of(Arrays.asList(1, 2, 3), Function.identity(), Function.identity())
				.unshift(4, 4)
				.unshift(5, 5)
				.count()
		);
	}

	@Test
	public void testAppend() {
		Map<Integer, Integer> map1 = new HashMap<Integer, Integer>(){{
			put(1, 1);
			put(2, 2);
		}};
		Map<Integer, Integer> map2 = new HashMap<Integer, Integer>(){{
			put(3, 3);
			put(4, 4);
		}};
		Assert.assertEquals(
			new ArrayList<Map.Entry<Integer, Integer>>(){{
				addAll(map1.entrySet());
				addAll(map2.entrySet());
			}},
			EntryStream.of(map1).append(map2.entrySet()).toList()
		);
		Assert.assertEquals(
			new ArrayList<>(map1.entrySet()), EntryStream.of(map1).append(null).toList()
		);
	}

	@Test
	public void testPrepend() {
		Map<Integer, Integer> map1 = new HashMap<Integer, Integer>(){{
			put(1, 1);
			put(2, 2);
		}};
		Map<Integer, Integer> map2 = new HashMap<Integer, Integer>(){{
			put(3, 3);
			put(4, 4);
		}};
		Assert.assertEquals(
			new ArrayList<Map.Entry<Integer, Integer>>(){{
				addAll(map2.entrySet());
				addAll(map1.entrySet());
			}},
			EntryStream.of(map1).prepend(map2.entrySet()).toList()
		);
		Assert.assertEquals(
			new ArrayList<>(map1.entrySet()), EntryStream.of(map1).prepend(null).toList()
		);
	}

	@Test
	public void testSortByKey() {
		List<Map.Entry<Integer, Integer>> entries = EntryStream.of(Arrays.asList(new Entry<>(3, 1), new Entry<>(2, 1), new Entry<>(4, 1), new Entry<>(1, 1)))
			.sortByKey(Comparator.comparingInt(Integer::intValue))
			.collect(Collectors.toList());
		Assert.assertEquals(
			Arrays.asList(1, 2, 3, 4),
			entries.stream().map(Map.Entry::getKey).collect(Collectors.toList())
		);
	}

	@Test
	public void testSortByValue() {
		List<Map.Entry<Integer, Integer>> entries = EntryStream.of(Arrays.asList(new Entry<>(4, 4), new Entry<>(2, 2), new Entry<>(1, 1), new Entry<>(3, 3)))
			.sortByValue(Comparator.comparingInt(Integer::intValue))
			.collect(Collectors.toList());
		Assert.assertEquals(
			Arrays.asList(1, 2, 3, 4),
			entries.stream().map(Map.Entry::getValue).collect(Collectors.toList())
		);
	}

	@Test
	public void testToValueStream() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assert.assertEquals(
			new ArrayList<>(map.values()), EntryStream.of(map).toValueStream().collect(Collectors.toList())
		);
	}

	@Test
	public void testToKeyStream() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assert.assertEquals(
			new ArrayList<>(map.keySet()), EntryStream.of(map).toKeyStream().collect(Collectors.toList())
		);
	}

	@Test
	public void testCollectKey() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		List<Integer> keys = EntryStream.of(map).collectKeys(Collectors.toList());
		Assert.assertEquals(new ArrayList<>(map.keySet()), keys);
	}

	@Test
	public void testCollectValue() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		List<Integer> keys = EntryStream.of(map).collectValues(Collectors.toList());
		Assert.assertEquals(new ArrayList<>(map.keySet()), keys);
	}

	@Test
	public void testMapKeys() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assert.assertEquals(
			Arrays.asList("1", "2", "3"),
			EntryStream.of(map)
				.mapKeys(String::valueOf)
				.toKeyStream()
				.collect(Collectors.toList())
		);
	}

	@Test
	public void testMapValues() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assert.assertEquals(
			Arrays.asList("1", "2", "3"),
			EntryStream.of(map)
				.mapValues(String::valueOf)
				.toValueStream()
				.collect(Collectors.toList())
		);
	}

	@Test
	public void testMap() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		Assert.assertEquals(
			Arrays.asList("11", "22", "33"),
			EntryStream.of(map)
				.map((k, v) -> k.toString() + v.toString())
				.collect(Collectors.toList())
		);
		Assert.assertEquals(
			Arrays.asList("11", "22", "33"),
			EntryStream.of(map)
				.map(e -> e.getKey().toString() + e.getValue().toString())
				.collect(Collectors.toList())
		);
	}

	@Test
	public void testFlatMap() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		List<Integer> list = EntryStream.of(map)
			.flatMap(e -> Stream.of(e.getKey(), e.getKey() + 1))
			.collect(Collectors.toList());
		Assert.assertEquals(Arrays.asList(1, 2, 2, 3, 3, 4), list);
	}

	@Test
	public void testFlatMapValue() {
		Map<String, Integer> map = new HashMap<>();
		map.put("class1", 1);
		map.put("class2", 2);
		map.put("class3", 3);
		List<String> values = EntryStream.of(map)
			.flatMapKey(k -> Stream.of(k + "'s student1", k + "'s student2"))
			.map((k, v) -> v + "=" + k)
			.sorted()
			.collect(Collectors.toList());
		Assert.assertEquals(
			Arrays.asList(
				"1=class1's student1", "1=class1's student2",
				"2=class2's student1", "2=class2's student2",
				"3=class3's student1", "3=class3's student2"
			),
			values
		);
	}

	@Test
	public void testInverse() {
		Map<String, String> map = new HashMap<>();
		map.put("key1", "value1");
		map.put("key2", "value2");
		map.put("key3", "value3");
		List<String> results = EntryStream.of(map)
			.inverse()
			.map((k, v) -> k + "=" + v)
			.collect(Collectors.toList());
		Assert.assertEquals(
			Arrays.asList("value1=key1", "value2=key2", "value3=key3"),
			results
		);
	}

	@Test
	public void testFlatMapKey() {
		Map<Integer, String> map = new HashMap<>();
		map.put(1, "class1");
		map.put(2, "class2");
		map.put(3, "class3");
		List<String> values = EntryStream.of(map)
			.flatMapValue(v -> Stream.of(v + "'s student1", v + "'s student2"))
			.map((k, v) -> k + "=" + v)
			.collect(Collectors.toList());
		Assert.assertEquals(
			Arrays.asList(
				"1=class1's student1", "1=class1's student2",
				"2=class2's student1", "2=class2's student2",
				"3=class3's student1", "3=class3's student2"
			),
			values
		);
	}

	@Test
	public void testForEach() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);

		List<Integer> keys = new ArrayList<>();
		List<Integer> values = new ArrayList<>();
		EntryStream.of(map).forEach((k ,v) -> {
			keys.add(k);
			values.add(v);
		});
		Assert.assertEquals(Arrays.asList(1, 2, 3), keys);
		Assert.assertEquals(Arrays.asList(1, 2, 3), values);
	}

	@Test
	public void testToMap() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);

		Map<Integer, Integer> result = EntryStream.of(map).toMap();
		Assert.assertEquals(map, result);

		result = EntryStream.of(map).toMap((Supplier<Map<Integer, Integer>>)LinkedHashMap::new);
		Assert.assertEquals(new LinkedHashMap<>(map), result);

		result = EntryStream.of(map).toMap(LinkedHashMap::new, (t1, t2) -> t1);
		Assert.assertEquals(new LinkedHashMap<>(map), result);
	}

	@Test
	public void testToTable() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);

		// 按是否偶数分组
		Table<Boolean, Integer, Integer> table = EntryStream.of(map).toTable(
			(k ,v) -> (k & 1) == 0, HashMap::new, (t1, t2) -> t1
		);
		Assert.assertEquals((Integer)1, table.get(false, 1));
		Assert.assertEquals((Integer)3, table.get(false, 3));
		Assert.assertEquals((Integer)2, table.get(true, 2));
		Assert.assertEquals((Integer)4, table.get(true, 4));

		table = EntryStream.of(map).toTable((k ,v) -> (k & 1) == 0);
		Assert.assertEquals((Integer)1, table.get(false, 1));
		Assert.assertEquals((Integer)3, table.get(false, 3));
		Assert.assertEquals((Integer)2, table.get(true, 2));
		Assert.assertEquals((Integer)4, table.get(true, 4));
	}

	@Test
	public void testToTableByKey() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);

		// 按是否偶数分组
		Table<Boolean, Integer, Integer> table = EntryStream.of(map).toTableByKey(
			k -> (k & 1) == 0, HashMap::new, (t1, t2) -> t1
		);
		Assert.assertEquals((Integer)1, table.get(false, 1));
		Assert.assertEquals((Integer)3, table.get(false, 3));
		Assert.assertEquals((Integer)2, table.get(true, 2));
		Assert.assertEquals((Integer)4, table.get(true, 4));

		table = EntryStream.of(map).toTableByKey(k -> (k & 1) == 0);
		Assert.assertEquals((Integer)1, table.get(false, 1));
		Assert.assertEquals((Integer)3, table.get(false, 3));
		Assert.assertEquals((Integer)2, table.get(true, 2));
		Assert.assertEquals((Integer)4, table.get(true, 4));
	}

	@Test
	public void testToTableByValue() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);

		// 按是否偶数分组
		Table<Boolean, Integer, Integer> table = EntryStream.of(map).toTableByValue(
			v -> (v & 1) == 0, HashMap::new, (t1, t2) -> t1
		);
		Assert.assertEquals((Integer)1, table.get(false, 1));
		Assert.assertEquals((Integer)3, table.get(false, 3));
		Assert.assertEquals((Integer)2, table.get(true, 2));
		Assert.assertEquals((Integer)4, table.get(true, 4));

		table = EntryStream.of(map).toTableByValue(v -> (v & 1) == 0);
		Assert.assertEquals((Integer)1, table.get(false, 1));
		Assert.assertEquals((Integer)3, table.get(false, 3));
		Assert.assertEquals((Integer)2, table.get(true, 2));
		Assert.assertEquals((Integer)4, table.get(true, 4));
	}

	@Test
	public void testGroupByKey() {
		Map<Integer, List<Integer>> map1 = EntryStream.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.groupByKey();
		Assert.assertEquals(2, map1.size());
		Assert.assertEquals(Arrays.asList(1, 1), map1.get(1));
		Assert.assertEquals(Arrays.asList(2, 2), map1.get(2));

		Map<Integer, Set<Integer>> map2 = EntryStream.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.groupByKey(Collectors.toSet());
		Assert.assertEquals(2, map2.size());
		Assert.assertEquals(Collections.singleton(1), map2.get(1));
		Assert.assertEquals(Collections.singleton(2), map2.get(2));

		Map<Integer, Set<Integer>> map3 = EntryStream.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.groupByKey(LinkedHashMap::new, Collectors.toSet());
		Assert.assertEquals(2, map3.size());
		Assert.assertEquals(Collections.singleton(1), map3.get(1));
		Assert.assertEquals(Collections.singleton(2), map3.get(2));
	}

	@Test
	public void testAnyMatch() {
		Assert.assertTrue(EntryStream.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.anyMatch((k, v) -> (k & 1) == 1));
		Assert.assertFalse(EntryStream.of(Arrays.asList(2, 2, 2, 2), Function.identity(), Function.identity())
			.anyMatch((k, v) -> (k & 1) == 1));
	}

	@Test
	public void testAllMatch() {
		Assert.assertFalse(EntryStream.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.allMatch((k, v) -> (k & 1) == 1));
		Assert.assertTrue(EntryStream.of(Arrays.asList(2, 2, 2, 2), Function.identity(), Function.identity())
			.allMatch((k, v) -> (k & 1) == 0));
	}

	@Test
	public void testNoneMatch() {
		Assert.assertFalse(EntryStream.of(Arrays.asList(1, 1, 2, 2), Function.identity(), Function.identity())
			.noneMatch((k, v) -> (k & 1) == 1));
		Assert.assertTrue(EntryStream.of(Arrays.asList(2, 2, 2, 2), Function.identity(), Function.identity())
			.noneMatch((k, v) -> (k & 1) == 1));
	}

	@Test
	public void testNonNull() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, null);
		map.put(null, 1);
		Assert.assertEquals(0, EntryStream.of(map).nonNullKeyValue().count());
	}

	@Test
	public void testKeyNonNull() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, null);
		map.put(null, 1);
		Assert.assertEquals(1, EntryStream.of(map).nonNullKey().count());
	}

	@Test
	public void testValueNonNull() {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, null);
		map.put(null, 1);
		Assert.assertEquals(1, EntryStream.of(map).nonNullValue().count());
	}

	private static class Entry<K, V> implements Map.Entry<K, V> {

		private final K key;
		private final V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			return null;
		}
	}

}

