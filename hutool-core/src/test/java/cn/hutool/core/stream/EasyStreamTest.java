package cn.hutool.core.stream;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

/**
 * @author VampireAchao
 */
public class EasyStreamTest {

	@Test
	public void testBuilder() {
		List<Integer> list = EasyStream.<Integer>builder().add(1).add(2).add(3).build().toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), list);
	}

	@Test
	public void testGenerate() {
		List<Integer> list = EasyStream.generate(() -> 0).limit(3).toList();
		Assert.assertEquals(Arrays.asList(0, 0, 0), list);
	}

	@Test
	public void testOf() {
		Assert.assertEquals(3, EasyStream.of(Arrays.asList(1, 2, 3), true).count());
		Assert.assertEquals(3, EasyStream.of(1, 2, 3).count());
		Assert.assertEquals(3, EasyStream.of(Stream.builder().add(1).add(2).add(3).build()).count());
	}

	@Test
	public void testSplit() {
		List<Integer> list = EasyStream.split("1,2,3", ",").map(Integer::valueOf).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), list);
	}

	@Test
	public void testIterator() {
		List<Integer> list = EasyStream.iterate(0, i -> i < 3, i -> ++i).toList();
		Assert.assertEquals(Arrays.asList(0, 1, 2), list);
	}

	@Test
	public void testToCollection() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<String> toCollection = EasyStream.of(list).map(String::valueOf).toColl(LinkedList::new);
		Assert.assertEquals(Arrays.asList("1", "2", "3"), toCollection);
	}

	@Test
	public void testToList() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<String> toList = EasyStream.of(list).map(String::valueOf).toList();
		Assert.assertEquals(Arrays.asList("1", "2", "3"), toList);
	}

	@Test
	public void testToSet() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Set<String> toSet = EasyStream.of(list).map(String::valueOf).toSet();
		Assert.assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), toSet);
	}

	@Test
	public void testToZip() {
		List<Integer> orders = Arrays.asList(1, 2, 3, 2);
		List<String> list = Arrays.asList("dromara", "guava", "sweet", "hutool");
		final Map<Integer, String> map = MapUtil.<Integer, String>builder()
				.put(1, "dromara")
				.put(2, "hutool")
				.put(3, "sweet")
				.build();

		Map<Integer, String> toZip = EasyStream.of(orders).toZip(list);
		Assert.assertEquals(map, toZip);

		Map<Integer, String> toZipParallel = EasyStream.of(orders).parallel().nonNull().toZip(list);
		Assert.assertEquals(map, toZipParallel);
	}

	@Test
	public void testJoin() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		String joining = EasyStream.of(list).join();
		Assert.assertEquals("123", joining);
		Assert.assertEquals("1,2,3", EasyStream.of(list).join(","));
		Assert.assertEquals("(1,2,3)", EasyStream.of(list).join(",", "(", ")"));
	}

	@Test
	public void testToMap() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Map<String, Integer> identityMap = EasyStream.of(list).toMap(String::valueOf);
		Assert.assertEquals(new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;

			{
				put("1", 1);
				put("2", 2);
				put("3", 3);
			}
		}, identityMap);
	}

	@Test
	public void testGroup() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Map<String, List<Integer>> group = EasyStream.of(list).group(String::valueOf);
		Assert.assertEquals(
				new HashMap<String, List<Integer>>() {
					private static final long serialVersionUID = 1L;

					{
						put("1", singletonList(1));
						put("2", singletonList(2));
						put("3", singletonList(3));
					}
				}, group);
	}

	@Test
	public void testMapIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> mapIndex = EasyStream.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
		// 并行流时为-1
		Assert.assertEquals(Arrays.asList(-1, -1, -1), EasyStream.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
	}

	@Test
	public void testMapMulti() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<Integer> mapMulti = EasyStream.of(list).<Integer>mapMulti((e, buffer) -> {
			for (int i = 0; i < e; i++) {
				buffer.accept(e);
			}
		}).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 2, 3, 3, 3), mapMulti);
	}

	@Test
	public void testMapNonNull() {
		List<Integer> list = Arrays.asList(1, 2, 3, null);
		List<String> mapNonNull = EasyStream.of(list).mapNonNull(String::valueOf).toList();
		Assert.assertEquals(Arrays.asList("1", "2", "3"), mapNonNull);
	}

	@Test
	public void testDistinct() {
		List<Integer> list = ListUtil.of(3, 2, 2, 1, null, null);
		for (int i = 0; i < 1000; i++) {
			list.add(i);
		}
		// 使用stream去重
		List<Integer> collect1 = list.stream().distinct().collect(Collectors.toList());
		List<Integer> collect2 = list.stream().parallel().distinct().collect(Collectors.toList());

		// 使用FastStream去重
		List<Integer> distinctBy1 = EasyStream.of(list).distinct().toList();
		List<Integer> distinctBy2 = EasyStream.of(list).parallel().distinct(String::valueOf).toList();

		Assert.assertEquals(collect1, distinctBy1);
		Assert.assertEquals(collect2, distinctBy2);
	}

	@Test
	public void testForeachIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		EasyStream.FastStreamBuilder<String> builder = EasyStream.builder();
		EasyStream.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
		// 并行流时为-1
		EasyStream.of(1, 2, 3).parallel().forEachIdx((e, i) -> Assert.assertEquals(-1, (Object) i));
	}

	@Test
	public void testForEachOrderedIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		EasyStream.FastStreamBuilder<String> builder = EasyStream.builder();
		EasyStream.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());

		EasyStream.FastStreamBuilder<String> streamBuilder = EasyStream.builder();
		EasyStream.of(list).parallel().forEachOrderedIdx((e, i) -> streamBuilder.accept(i + 1 + "." + e));
		Assert.assertEquals(Arrays.asList("0.dromara", "0.hutool", "0.sweet"), streamBuilder.build().toList());

	}

	@Test
	public void testFlatMapIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> mapIndex = EasyStream.of(list).flatMapIdx((e, i) -> EasyStream.of(i + 1 + "." + e)).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
		// 并行流时为-1
		Assert.assertEquals(Arrays.asList(-1, -1, -1), EasyStream.of(1, 2, 3).parallel().flatMapIdx((e, i) -> EasyStream.of(i)).toList());
	}

	@Test
	public void testFlat() {
		List<Integer> list = Arrays.asList(1, 2, 3);

		// 一个元素 扩散为 多个元素(迭代器)
		List<Integer> flat = EasyStream.of(list).flat(e -> Arrays.asList(e, e * 10)).toList();
		Assert.assertEquals(ListUtil.of(1, 10, 2, 20, 3, 30), flat);

		// 过滤迭代器为null的元素
		flat = EasyStream.of(list).<Integer>flat(e -> null).toList();
		Assert.assertEquals(Collections.emptyList(), flat);

		// 自动过滤null元素
		flat = EasyStream.of(list).flat(e -> Arrays.asList(e, e > 2 ? e : null)).toList();
		Assert.assertEquals(ListUtil.of(1, null, 2, null, 3, 3), flat);
		// 不报npe测试
		Assert.assertTrue(EasyStream.of(list).flat(e -> null).isEmpty());
	}

	@Test
	public void testFilter() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<Integer> filterIndex = EasyStream.of(list).filter(String::valueOf, "1").toList();
		Assert.assertEquals(Collections.singletonList(1), filterIndex);
	}

	@Test
	public void testFilterIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> filterIndex = EasyStream.of(list).filterIdx((e, i) -> i < 2).toList();
		Assert.assertEquals(Arrays.asList("dromara", "hutool"), filterIndex);
		// 并行流时为-1
		Assert.assertEquals(3L, EasyStream.of(1, 2, 3).parallel().filterIdx((e, i) -> i == -1).count());
	}

	@Test
	public void testNonNull() {
		List<Integer> list = Arrays.asList(1, null, 2, 3);
		List<Integer> nonNull = EasyStream.of(list).nonNull().toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), nonNull);
	}

	@Test
	public void testParallel() {
		Assert.assertTrue(EasyStream.of(1, 2, 3).parallel(true).isParallel());
		Assert.assertFalse(EasyStream.of(1, 2, 3).parallel(false).isParallel());
	}

	@Test
	public void testPush() {
		List<Integer> list = Arrays.asList(1, 2);
		List<Integer> push = EasyStream.of(list).push(3).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), push);

		Assert.assertEquals(Arrays.asList(1, 2, 3, 4), EasyStream.of(list).push(3, 4).toList());
	}

	@Test
	public void testUnshift() {
		List<Integer> list = Arrays.asList(2, 3);
		List<Integer> unshift = EasyStream.of(list).unshift(1).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), unshift);

		Assert.assertEquals(Arrays.asList(1, 2, 2, 3), EasyStream.of(list).unshift(1, 2).toList());
	}

	@Test
	public void testAt() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Assert.assertEquals(1, (Object) EasyStream.of(list).at(0).orElse(null));
		Assert.assertEquals(2, (Object) EasyStream.of(list).at(1).orElse(null));
		Assert.assertEquals(3, (Object) EasyStream.of(list).at(2).orElse(null));
		Assert.assertEquals(1, (Object) EasyStream.of(list).at(-3).orElse(null));
		Assert.assertEquals(3, (Object) EasyStream.of(list).at(-1).orElse(null));
		Assert.assertNull(EasyStream.of(list).at(-4).orElse(null));
	}

	@Test
	public void testSplice() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Assert.assertEquals(Arrays.asList(1, 2, 2, 3), EasyStream.of(list).splice(1, 0, 2).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 3, 3), EasyStream.of(list).splice(3, 1, 3).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 4), EasyStream.of(list).splice(2, 1, 4).toList());
		Assert.assertEquals(Arrays.asList(1, 2), EasyStream.of(list).splice(2, 1).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 3), EasyStream.of(list).splice(2, 0).toList());
		Assert.assertEquals(Arrays.asList(1, 2), EasyStream.of(list).splice(-1, 1).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 3), EasyStream.of(list).splice(-2, 2, 2, 3).toList());
	}

	@Test
	public void testFindFirst() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Integer find = EasyStream.of(list).findFirst(Objects::nonNull).orElse(null);
		Assert.assertEquals(1, (Object) find);
	}

	@Test
	public void testFindFirstIdx() {
		List<Integer> list = Arrays.asList(null, 2, 3);
		Assert.assertEquals(1, EasyStream.of(list).findFirstIdx(Objects::nonNull));
		Assert.assertEquals(-1, (Object) EasyStream.of(list).parallel().findFirstIdx(Objects::nonNull));
	}

	@Test
	public void testFindLast() {
		List<Integer> list = ListUtil.of(1, 2, 4, 5, 6, 7, 8, 9, 10, 3);
		Assert.assertEquals(3, (Object) EasyStream.of(list).findLast().orElse(null));
		Assert.assertEquals(3, (Object) EasyStream.of(list).parallel().findLast().orElse(null));

		List<Integer> list2 = ListUtil.of(1, 2, 4, 5, 6, 7, 8, 9, 10, 3, null);
		Assert.assertEquals(3, (Object) EasyStream.of(list2).parallel().findLast(Objects::nonNull).orElse(null));

		Assert.assertNull(EasyStream.of().parallel().findLast(Objects::nonNull).orElse(null));
		Assert.assertNull(EasyStream.of((Object) null).parallel().findLast(Objects::nonNull).orElse(null));
	}

	@Test
	public void testFindLastIdx() {
		List<Integer> list = Arrays.asList(1, null, 3);
		Assert.assertEquals(2, (Object) EasyStream.of(list).findLastIdx(Objects::nonNull));
		Assert.assertEquals(-1, (Object) EasyStream.of(list).parallel().findLastIdx(Objects::nonNull));
	}

	@Test
	public void testReverse() {
		final List<Integer> list = ListUtil.of(Stream.iterate(1, i -> i + 1).limit(1000).collect(Collectors.toList()));

		Assert.assertEquals(ListUtil.reverseNew(list), EasyStream.of(list).reverse().toList());
		Assert.assertEquals(ListUtil.empty(), EasyStream.of().reverse().toList());
		Assert.assertEquals(ListUtil.of((Object) null), EasyStream.of((Object) null).reverse().toList());
	}

	@Test
	public void testZip() {
		List<Integer> orders = Arrays.asList(1, 2, 3);
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> zip = EasyStream.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);

		zip = EasyStream.iterate(1, i -> i + 1).zip(list, (e1, e2) -> e1 + "." + e2).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);
	}

	@Test
	public void testListSplit() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<List<Integer>> lists = EasyStream.of(list).split(2).map(EasyStream::toList).toList();
		Assert.assertEquals(ListUtil.split(list, 2), lists);

		// 指定长度 大于等于 列表长度
		lists = EasyStream.of(list).split(list.size()).map(EasyStream::toList).toList();
		Assert.assertEquals(singletonList(list), lists);
	}

	@Test
	public void testSplitList() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<List<Integer>> lists = EasyStream.of(list).splitList(2).toList();
		Assert.assertEquals(ListUtil.split(list, 2), lists);

		// 指定长度 大于等于 列表长度
		lists = EasyStream.of(list).splitList(list.size()).toList();
		Assert.assertEquals(singletonList(list), lists);
	}

	@Test
	public void testTakeWhile() {
		// 1 到 10
		final List<Integer> list = EasyStream.iterate(1, i -> i <= 10, i -> i + 1).toList();

		final List<String> res1 = EasyStream.of(list)
				// 舍弃 5
				.takeWhile(e -> e < 5)
				// 过滤奇数
				.filter(e -> (e & 1) == 0)
				// 反序
				.sorted(Comparator.reverseOrder())
				.map(String::valueOf)
				.toList();
		Assert.assertEquals(Arrays.asList("4", "2"), res1);

		final List<Integer> res2 = EasyStream.iterate(1, i -> i + 1)
				.parallel()
				.takeWhile(e -> e < 5)
				.map(String::valueOf)
				.map(Integer::valueOf)
				.sorted(Comparator.naturalOrder())
				.toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3, 4), res2);
	}

	@Test
	public void testDropWhile() {
		// 1 到 10
		final List<Integer> list = EasyStream.iterate(1, i -> i <= 10, i -> i + 1).toList();

		final List<String> res1 = EasyStream.of(list)
				// 舍弃 5之前的数字
				.dropWhile(e -> e < 5)
				// 过滤偶数
				.filter(e -> (e & 1) == 1)
				// 反序
				.sorted(Comparator.reverseOrder())
				.map(String::valueOf)
				.toList();
		Assert.assertEquals(Arrays.asList("9", "7", "5"), res1);

		final List<Integer> res2 = EasyStream.of(list)
				.parallel()
				.dropWhile(e -> e < 5)
				// 过滤偶数
				.filter(e -> (e & 1) == 1)
				.map(String::valueOf)
				.map(Integer::valueOf)
				.sorted(Comparator.naturalOrder())
				.toList();
		Assert.assertEquals(Arrays.asList(5, 7, 9), res2);
	}

	@Test
	public void testIsNotEmpty() {
		Assert.assertTrue(EasyStream.of(1).isNotEmpty());
	}

}
