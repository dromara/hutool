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

package org.dromara.hutool.core.stream;

import static java.util.Collections.singletonList;
import lombok.Builder;
import lombok.Data;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.math.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author VampireAchao
 */
public class EasyStreamTest {


	@Test
	void testIterateHierarchies() {
		// 创建一个三层的树结构，每个节点都有两个子节点
		Node node = new Node("1", Arrays.asList(
			new Node("1-1", Arrays.asList(
				new Node("1-1-1", null),
				new Node("1-1-2", null)
			)),
			new Node("1-2", Arrays.asList(
				new Node("1-2-1", null),
				new Node("1-2-2", null)
			))
		));

		// 按广度度优先遍历树结构
		List<String> allNodes = new ArrayList<>();
		EasyStream.iterateHierarchies(node, node1 -> node1.children)
			.forEach(node1 -> allNodes.add(node1.id));
		Assertions.assertEquals(Arrays.asList("1", "1-1", "1-2", "1-1-1", "1-1-2", "1-2-1", "1-2-2"), allNodes);

		// 按广度度优先遍历树结构，忽略id为1-1的节点与以其为根节点的子树
		List<String> filteredNodes = new ArrayList<>();
		EasyStream.iterateHierarchies(node, node1 -> node1.children, node1 -> !node1.id.equals("1-1"))
			.forEach(node1 -> filteredNodes.add(node1.id));
		Assertions.assertEquals(Arrays.asList("1", "1-2", "1-2-1", "1-2-2"), filteredNodes);
	}

	@Test
	public void testConcat() {
		final Stream<Integer> stream1 = Stream.of(1, 2);
		final Stream<Integer> stream2 = Stream.of(3, 4);
		Assertions.assertEquals(4, EasyStream.concat(stream1, stream2).count());
	}

	@Test
	public void testBuilder() {
		final List<Integer> list = EasyStream.<Integer>builder().add(1).add(2).add(3).build().toList();
		Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
	}

	@Test
	public void testGenerate() {
		final List<Integer> list = EasyStream.generate(() -> 0).limit(3).toList();
		Assertions.assertEquals(Arrays.asList(0, 0, 0), list);
	}

	@Test
	public void testOf() {
		Assertions.assertEquals(3, EasyStream.of(Arrays.asList(1, 2, 3), true).count());
		Assertions.assertEquals(3, EasyStream.of(1, 2, 3).count());
		Assertions.assertEquals(3, EasyStream.of(Stream.builder().add(1).add(2).add(3).build()).count());
	}

	@Test
	public void testSplit() {
		final List<Integer> list = EasyStream.split("1,2,3", ",").map(Integer::valueOf).toList();
		Assertions.assertEquals(Arrays.asList(1, 2, 3), list);
	}

	@Test
	public void testIterator() {
		final List<Integer> list = EasyStream.iterate(0, i -> i < 3, i -> ++i).toList();
		Assertions.assertEquals(Arrays.asList(0, 1, 2), list);
	}

	@Test
	public void testToColl() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final List<String> toCollection = EasyStream.of(list).map(String::valueOf).toColl(LinkedList::new);
		Assertions.assertEquals(Arrays.asList("1", "2", "3"), toCollection);
	}

	@Test
	public void testToList() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final List<String> toList = EasyStream.of(list).map(String::valueOf).toList();
		Assertions.assertEquals(Arrays.asList("1", "2", "3"), toList);
	}

	@Test
	public void testToSet() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final Set<String> toSet = EasyStream.of(list).map(String::valueOf).toSet();
		Assertions.assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), toSet);
	}

	@Test
	public void testToZip() {
		final List<Integer> orders = Arrays.asList(1, 2, 3, 2);
		final List<String> list = Arrays.asList("dromara", "guava", "sweet", "hutool");
		final Map<Integer, String> map = MapUtil.<Integer, String>builder()
				.put(1, "dromara")
				.put(2, "hutool")
				.put(3, "sweet")
				.build();

		final Map<Integer, String> toZip = EasyStream.of(orders).toZip(list);
		Assertions.assertEquals(map, toZip);

		final Map<Integer, String> toZipParallel = EasyStream.of(orders).parallel().nonNull().toZip(list);
		Assertions.assertEquals(map, toZipParallel);
	}

	@Test
	public void testJoin() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final String joining = EasyStream.of(list).join();
		Assertions.assertEquals("123", joining);
		Assertions.assertEquals("1,2,3", EasyStream.of(list).join(","));
		Assertions.assertEquals("(1,2,3)", EasyStream.of(list).join(",", "(", ")"));
	}

	@Test
	public void testToMap() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final Map<String, Integer> identityMap = EasyStream.of(list).toMap(String::valueOf);
		Assertions.assertEquals(new HashMap<String, Integer>() {
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
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final Map<String, List<Integer>> group = EasyStream.of(list).group(String::valueOf);
		Assertions.assertEquals(
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
		final List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		final List<String> mapIndex = EasyStream.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
		Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
	}

	@Test
	public void testMapMulti() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final List<Integer> mapMulti = EasyStream.of(list).<Integer>mapMulti((e, buffer) -> {
			for (int i = 0; i < e; i++) {
				buffer.accept(e);
			}
		}).toList();
		Assertions.assertEquals(Arrays.asList(1, 2, 2, 3, 3, 3), mapMulti);
	}

	@Test
	public void testMapNonNull() {
		final List<Integer> list = Arrays.asList(1, 2, 3, null);
		final List<String> mapNonNull = EasyStream.of(list).mapNonNull(String::valueOf).toList();
		Assertions.assertEquals(Arrays.asList("1", "2", "3"), mapNonNull);
	}

	@Test
	public void testDistinct() {
		final List<Integer> list = ListUtil.of(3, 2, 2, 1, null, null);
		for (int i = 0; i < 1000; i++) {
			list.add(i);
		}
		// 使用stream去重
		final List<Integer> collect1 = list.stream().distinct().collect(Collectors.toList());
		final List<Integer> collect2 = list.stream().parallel().distinct().collect(Collectors.toList());

		// 使用EasyStream去重
		final List<Integer> distinctBy1 = EasyStream.of(list).distinct().toList();
		final List<Integer> distinctBy2 = EasyStream.of(list).parallel().distinct(String::valueOf).toList();

		Assertions.assertEquals(collect1, distinctBy1);

		// 并行流测试
		Assertions.assertEquals(ListUtil.sort(collect2), ListUtil.sort(distinctBy2));

		Assertions.assertEquals(
				4, EasyStream.of(1, 2, 2, null, 3, null).parallel(true)
						.distinct(t -> Objects.isNull(t) ? null : t.toString()).sequential().count()
		);
	}

	@Test
	public void testForeachIdx() {
		final List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		final EasyStream.Builder<String> builder = EasyStream.builder();
		EasyStream.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
		Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
	}

	@Test
	public void testForEachOrderedIdx() {
		final List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		final EasyStream.Builder<String> builder = EasyStream.builder();
		EasyStream.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
		Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());

		final EasyStream.Builder<String> streamBuilder = EasyStream.builder();
		EasyStream.of(list).parallel().forEachOrderedIdx((e, i) -> streamBuilder.accept(i + 1 + "." + e));
		Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), streamBuilder.build().toList());

	}

	@Test
	public void testFlatMapIdx() {
		final List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		final List<String> mapIndex = EasyStream.of(list).flatMapIdx((e, i) -> EasyStream.of(i + 1 + "." + e)).toList();
		Assertions.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
	}

	@Test
	public void testPeek() {
		EasyStream.of("one", "two", "three", "four")
				.filter(e -> e.length() == 4)
				.peek(e -> Assertions.assertEquals("four", e))
				.map(String::toUpperCase)
				.peek(e -> Assertions.assertEquals("FOUR", e))
				.collect(Collectors.toList());
	}

	@Test
	public void testPeekIdx() {
		EasyStream.of("one", "two", "three", "four")
				.filter(e -> e.length() == 4)
				.peekIdx((e, i) -> Assertions.assertEquals("four:0", e + ":" + i))
				.map(String::toUpperCase)
				.peekIdx((e, i) -> Assertions.assertEquals("FOUR:0", e + ":" + i))
				.collect(Collectors.toList());
	}

	@Test
	public void testFlat() {
		final List<Integer> list = Arrays.asList(1, 2, 3);

		// 一个元素 扩散为 多个元素(迭代器)
		List<Integer> flat = EasyStream.of(list).flat(e -> Arrays.asList(e, e * 10)).toList();
		Assertions.assertEquals(ListUtil.of(1, 10, 2, 20, 3, 30), flat);

		// 过滤迭代器为null的元素
		flat = EasyStream.of(list).<Integer>flat(e -> null).toList();
		Assertions.assertEquals(Collections.emptyList(), flat);

		// 自动过滤null元素
		flat = EasyStream.of(list).flat(e -> Arrays.asList(e, e > 2 ? e : null)).toList();
		Assertions.assertEquals(ListUtil.of(1, null, 2, null, 3, 3), flat);
		// 不报npe测试
		Assertions.assertTrue(EasyStream.of(list).flat(e -> null).isEmpty());
	}

	@Test
	public void testFilter() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final List<Integer> filterIndex = EasyStream.of(list).filter(String::valueOf, "1").toList();
		Assertions.assertEquals(Collections.singletonList(1), filterIndex);
	}

	@Test
	public void testFilterIdx() {
		final List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		final List<String> filterIndex = EasyStream.of(list).filterIdx((e, i) -> i < 2).toList();
		Assertions.assertEquals(Arrays.asList("dromara", "hutool"), filterIndex);
	}

	@Test
	public void testNonNull() {
		final List<Integer> list = Arrays.asList(1, null, 2, 3);
		final List<Integer> nonNull = EasyStream.of(list).nonNull().toList();
		Assertions.assertEquals(Arrays.asList(1, 2, 3), nonNull);
	}

	@Test
	public void testParallel() {
		Assertions.assertTrue(EasyStream.of(1, 2, 3).parallel(true).isParallel());
		Assertions.assertFalse(EasyStream.of(1, 2, 3).parallel(false).isParallel());
	}

	@Test
	public void testPush() {
		final List<Integer> list = Arrays.asList(1, 2);
		final List<Integer> push = EasyStream.of(list).push(3).toList();
		Assertions.assertEquals(Arrays.asList(1, 2, 3), push);

		Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), EasyStream.of(list).push(3, 4).toList());
	}

	@Test
	public void testUnshift() {
		final List<Integer> list = Arrays.asList(2, 3);
		final List<Integer> unshift = EasyStream.of(list).unshift(1).toList();
		Assertions.assertEquals(Arrays.asList(1, 2, 3), unshift);

		Assertions.assertEquals(Arrays.asList(1, 2, 2, 3), EasyStream.of(list).unshift(1, 2).toList());
	}

	@Test
	public void testAt() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		Assertions.assertEquals(1, (Object) EasyStream.of(list).at(0).orElse(null));
		Assertions.assertEquals(2, (Object) EasyStream.of(list).at(1).orElse(null));
		Assertions.assertEquals(3, (Object) EasyStream.of(list).at(2).orElse(null));
		Assertions.assertEquals(1, (Object) EasyStream.of(list).at(-3).orElse(null));
		Assertions.assertEquals(3, (Object) EasyStream.of(list).at(-1).orElse(null));
		Assertions.assertNull(EasyStream.of(list).at(-4).orElse(null));
	}

	@Test
	public void testSplice() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		Assertions.assertEquals(Arrays.asList(1, 2, 2, 3), EasyStream.of(list).splice(1, 0, 2).toList());
		Assertions.assertEquals(Arrays.asList(1, 2, 3, 3), EasyStream.of(list).splice(3, 1, 3).toList());
		Assertions.assertEquals(Arrays.asList(1, 2, 4), EasyStream.of(list).splice(2, 1, 4).toList());
		Assertions.assertEquals(Arrays.asList(1, 2), EasyStream.of(list).splice(2, 1).toList());
		Assertions.assertEquals(Arrays.asList(1, 2, 3), EasyStream.of(list).splice(2, 0).toList());
		Assertions.assertEquals(Arrays.asList(1, 2), EasyStream.of(list).splice(-1, 1).toList());
		Assertions.assertEquals(Arrays.asList(1, 2, 3), EasyStream.of(list).splice(-2, 2, 2, 3).toList());
	}

	@Test
	public void testFindFirst() {
		final List<Integer> list = Arrays.asList(1, 2, 3);
		final Integer find = EasyStream.of(list).findFirst(Objects::nonNull).orElse(null);
		Assertions.assertEquals(1, (Object) find);
	}

	@Test
	public void testFindFirstIdx() {
		final List<Integer> list = Arrays.asList(null, 2, 3);
		Assertions.assertEquals(1, EasyStream.of(list).findFirstIdx(Objects::nonNull));
	}

	@Test
	public void testFindLast() {
		final List<Integer> list = ListUtil.of(1, 2, 4, 5, 6, 7, 8, 9, 10, 3);
		Assertions.assertEquals(3, (Object) EasyStream.of(list).findLast().orElse(null));
		Assertions.assertEquals(3, (Object) EasyStream.of(list).parallel().findLast().orElse(null));

		final List<Integer> list2 = ListUtil.of(1, 2, 4, 5, 6, 7, 8, 9, 10, 3, null);
		Assertions.assertEquals(3, (Object) EasyStream.of(list2).parallel().findLast(Objects::nonNull).orElse(null));

		Assertions.assertNull(EasyStream.of().parallel().findLast(Objects::nonNull).orElse(null));
		Assertions.assertNull(EasyStream.of((Object) null).parallel().findLast(Objects::nonNull).orElse(null));
	}

	@Test
	public void testFindLastIdx() {
		final List<Integer> list = Arrays.asList(1, null, 3);
		Assertions.assertEquals(2, (Object) EasyStream.of(list).findLastIdx(Objects::nonNull));
	}

	@Test
	public void testReverse() {
		final List<Integer> list = ListUtil.of(Stream.iterate(1, i -> i + 1).limit(1000).collect(Collectors.toList()));

		Assertions.assertEquals(ListUtil.reverseNew(list), EasyStream.of(list).reverse().toList());
		Assertions.assertEquals(ListUtil.empty(), EasyStream.of().reverse().toList());
		Assertions.assertEquals(ListUtil.of((Object) null), EasyStream.of((Object) null).reverse().toList());
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
		Assertions.assertEquals(Arrays.asList("4", "2"), res1);

		final List<Integer> res2 = EasyStream.iterate(1, i -> i + 1)
				.parallel()
				.takeWhile(e -> e < 5)
				.map(String::valueOf)
				.map(Integer::valueOf)
				.sorted(Comparator.naturalOrder())
				.toList();
		Assertions.assertEquals(Arrays.asList(1, 2, 3, 4), res2);
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
		Assertions.assertEquals(Arrays.asList("9", "7", "5"), res1);

		final List<Integer> res2 = EasyStream.of(list)
				.parallel()
				.dropWhile(e -> e < 5)
				// 过滤偶数
				.filter(e -> (e & 1) == 1)
				.map(String::valueOf)
				.map(Integer::valueOf)
				.sorted(Comparator.naturalOrder())
				.toList();
		Assertions.assertEquals(Arrays.asList(5, 7, 9), res2);
	}

	@Test
	public void testIsNotEmpty() {
		Assertions.assertTrue(EasyStream.of(1).isNotEmpty());
	}


	@Test
	public void testIntSumAndAvg() {
		//测试int类型的总和
		final int sum = EasyStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).sum(Integer::intValue);
		Assertions.assertEquals(55, sum);

		//测试为空
		final List<Integer> integerList = new ArrayList<>();
		final int emptySum = EasyStream.of(integerList).sum(Integer::intValue);
		Assertions.assertEquals(0, emptySum);

		//测试平均值
		final OptionalDouble avg = EasyStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).avg(Integer::intValue);
		Assertions.assertTrue(avg.isPresent());
		Assertions.assertEquals(5.5, avg.getAsDouble(), 2);

		//测试元素为空
		final OptionalDouble emptyAvg = EasyStream.of(integerList).avg(Integer::intValue);
		Assertions.assertFalse(emptyAvg.isPresent());

	}

	@Test
	public void testDoubleSumAndAvg() {
		//测试double类型的sum
		final double doubleSum = EasyStream.of(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10).sum(Double::doubleValue);
		Assertions.assertEquals(59.6, doubleSum, 2);

		//测试double类型的sum 无元素double
		final List<Double> doubleList = new ArrayList<>();
		final double emptySum = EasyStream.of(doubleList).sum(Double::doubleValue);
		Assertions.assertEquals(0.0, emptySum, 2);

		//测试double类型的avg
		final OptionalDouble doubleAvg = EasyStream.of(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10)
				.avg(Double::doubleValue);
		Assertions.assertTrue(doubleAvg.isPresent());
		Assertions.assertEquals(5.96, doubleAvg.getAsDouble(), 2);

		//测试double类型的 空元素的avg
		final OptionalDouble emptyDoubleAvg = EasyStream.of(doubleList).avg(Double::doubleValue);
		Assertions.assertFalse(emptyDoubleAvg.isPresent());

	}

	@Test
	public void testLongSumAndAvg() {
		//测试long类型的sum
		final long sum = EasyStream.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L).sum(Long::longValue);
		Assertions.assertEquals(55L, sum);

		//测试long类型的空元素 sum
		final List<Long> longList = new ArrayList<>();
		final long emptySum = EasyStream.of(longList).sum(Long::longValue);
		Assertions.assertEquals(0L, emptySum);

		//测试long类型的avg
		final OptionalDouble doubleAvg = EasyStream.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L).avg(Long::longValue);
		Assertions.assertTrue(doubleAvg.isPresent());
		Assertions.assertEquals(5.5, doubleAvg.getAsDouble(), 2);

		//测试long类型的avg 空元素
		final OptionalDouble emptyDoubleAvg = EasyStream.of(longList).avg(Long::longValue);
		Assertions.assertFalse(emptyDoubleAvg.isPresent());


	}

	@Test
	public void testBigDecimalSumAndAvg() {
		//测试bigDecimal的sum
		final BigDecimal sum = EasyStream.of(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10).map(NumberUtil::toBigDecimal)
				.sum(Function.identity());
		Assertions.assertEquals(NumberUtil.toBigDecimal(59.6), sum);

		//测试bigDecimal的sum 空元素
		final List<BigDecimal> bigDecimalEmptyList = new ArrayList<>();
		final BigDecimal emptySum = EasyStream.of(bigDecimalEmptyList).sum(Function.identity());
		Assertions.assertEquals(BigDecimal.ZERO, emptySum);

		//测试bigDecimal的avg全参
		final Opt<BigDecimal> bigDecimalAvgFullParam = EasyStream.of(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10)
				.map(NumberUtil::toBigDecimal)
				.avg(Function.identity(), 2, RoundingMode.HALF_UP);
		Assertions.assertEquals(NumberUtil.toBigDecimal(5.96), bigDecimalAvgFullParam.get());

		//测试bigDecimal的avg单参
		final Opt<BigDecimal> bigDecimalAvgOneParam = EasyStream.of(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10)
				.map(NumberUtil::toBigDecimal)
				.avg(Function.identity());
		Assertions.assertEquals(NumberUtil.toBigDecimal(5.96), bigDecimalAvgOneParam.get());

		//测试bigDecimal的avg双参
		final Opt<BigDecimal> bigDecimalAvgTwoParam = EasyStream.of(1.1, 2.2, 3.3, 4.4, 5.5, 6.6, 7.7, 8.8, 9.9, 10.10)
				.map(NumberUtil::toBigDecimal)
				.avg(Function.identity(), 2);
		Assertions.assertEquals(NumberUtil.toBigDecimal(5.96), bigDecimalAvgTwoParam.get());

		//测试bigDecimal的avg 空元素
		final Opt<BigDecimal> emptyBigDecimalAvg = EasyStream.of(bigDecimalEmptyList)
				.avg(Function.identity(), 2, RoundingMode.HALF_UP);
		Assertions.assertFalse(emptyBigDecimalAvg.isPresent());
	}

	/**
	 * https://github.com/dromara/hutool/pull/3128
	 */
	@Test
	void testStreamBigDecimal() {
		final List<BigDecimalTest> testList = ListUtil.of();
		for (int i = 0; i < 5; i++) {
			final BigDecimalTest test = BigDecimalTest.builder()
				.name("test" + i)
				.count(new BigDecimal(i+1))
				.build();
			testList.add(test);
		}

		final BigDecimal sum = EasyStream.of(testList).sum(BigDecimalTest::getCount);
		Assertions.assertEquals(15, sum.intValue());

		final BigDecimal avg = EasyStream.of(testList).avg(BigDecimalTest::getCount).get();
		Assertions.assertEquals(3, avg.intValue());
	}

	@Data
	@Builder
	private static class BigDecimalTest{
		private String name;
		private BigDecimal count;
	}

	/**
	 * 节点
	 */
	private static class Node {
		private final String id;
		private List<Node> children;
		private Node(String id, List<Node> children) {
			this.id = id;
			this.children = children;
		}
		public Node(String id) {
			this.id = id;
		}
	}
}
