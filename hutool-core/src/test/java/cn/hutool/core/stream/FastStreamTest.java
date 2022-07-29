package cn.hutool.core.stream;


import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;

/**
 * @author VampireAchao
 */
public class FastStreamTest {

	@Test
	public void testBuilder() {
		List<Integer> list = FastStream.<Integer>builder().add(1).add(2).add(3).build().toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), list);
	}

	@Test
	public void testOf() {
		Assert.assertEquals(3, FastStream.of(Arrays.asList(1, 2, 3), true).count());
		Assert.assertEquals(3, FastStream.of(1, 2, 3).count());
		Assert.assertEquals(3, FastStream.of(Stream.builder().add(1).add(2).add(3).build()).count());
	}

	@Test
	public void testSplit() {
		List<Integer> list = FastStream.split("1,2,3", ",").map(Integer::valueOf).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), list);
	}

	@Test
	public void testIterator() {
		List<Integer> list = FastStream.iterate(0, i -> i < 3, i -> ++i).toList();
		Assert.assertEquals(Arrays.asList(0, 1, 2), list);
	}

	@Test
	public void testToCollection() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<String> toCollection = FastStream.of(list).map(String::valueOf).toColl(LinkedList::new);
		Assert.assertEquals(Arrays.asList("1", "2", "3"), toCollection);
	}

	@Test
	public void testToList() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<String> toList = FastStream.of(list).map(String::valueOf).toList();
		Assert.assertEquals(Arrays.asList("1", "2", "3"), toList);
	}

	@Test
	public void testToSet() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Set<String> toSet = FastStream.of(list).map(String::valueOf).toSet();
		Assert.assertEquals(new HashSet<>(Arrays.asList("1", "2", "3")), toSet);
	}

	@Test
	public void testToZip() {
		List<Integer> orders = Arrays.asList(1, 2, 3);
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		Map<Integer, String> toZip = FastStream.of(orders).toZip(list);
		Assert.assertEquals(new HashMap<Integer, String>() {
			private static final long serialVersionUID = 1L;

			{
			put(1, "dromara");
			put(2, "hutool");
			put(3, "sweet");
		}}, toZip);
	}

	@Test
	public void testJoin() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		String joining = FastStream.of(list).join();
		Assert.assertEquals("123", joining);
		Assert.assertEquals("1,2,3", FastStream.of(list).join(","));
		Assert.assertEquals("(1,2,3)", FastStream.of(list).join(",", "(", ")"));
	}

	@Test
	public void testToMap() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Map<String, Integer> identityMap = FastStream.of(list).toMap(String::valueOf);
		Assert.assertEquals(new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;

			{
			put("1", 1);
			put("2", 2);
			put("3", 3);
		}}, identityMap);
	}

	@Test
	public void testGroup() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Map<String, List<Integer>> group = FastStream.of(list).group(String::valueOf);
		Assert.assertEquals(
				new HashMap<String, List<Integer>>() {
					private static final long serialVersionUID = 1L;

					{
					put("1", singletonList(1));
					put("2", singletonList(2));
					put("3", singletonList(3));
				}}, group);
	}

	@Test
	public void testMapIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> mapIndex = FastStream.of(list).mapIdx((e, i) -> i + 1 + "." + e).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
		// 并行流时为-1
		Assert.assertEquals(Arrays.asList(-1, -1, -1), FastStream.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
	}

	@Test
	public void testMapMulti() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<Integer> mapMulti = FastStream.of(list).<Integer>mapMulti((e, buffer) -> {
			if (e % 2 == 0) {
				buffer.accept(e);
			}
			buffer.accept(e);
		}).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 2, 3), mapMulti);
	}

	@Test
	public void testDistinct() {
		List<Integer> list = Arrays.asList(1, 2, 2, 3);
		List<Integer> distinctBy = FastStream.of(list).distinct(String::valueOf).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), distinctBy);
	}

	@Test
	public void testForeachIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		FastStream.FastStreamBuilder<String> builder = FastStream.builder();
		FastStream.of(list).forEachIdx((e, i) -> builder.accept(i + 1 + "." + e));
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
		// 并行流时为-1
		FastStream.of(1, 2, 3).parallel().forEachIdx((e, i) -> Assert.assertEquals(-1, (Object) i));
	}

	@Test
	public void testForEachOrderedIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		FastStream.FastStreamBuilder<String> builder = FastStream.builder();
		FastStream.of(list).forEachOrderedIdx((e, i) -> builder.accept(i + 1 + "." + e));
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), builder.build().toList());
	}

	@Test
	public void testFlatMapIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> mapIndex = FastStream.of(list).flatMapIdx((e, i) -> FastStream.of(i + 1 + "." + e)).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), mapIndex);
		// 并行流时为-1
		Assert.assertEquals(Arrays.asList(-1, -1, -1), FastStream.of(1, 2, 3).parallel().mapIdx((e, i) -> i).toList());
	}

	@Test
	public void testFlatMapIter() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<Integer> flatMapIter = FastStream.of(list).<Integer>flatMapIter(e -> null).toList();
		Assert.assertEquals(Collections.emptyList(), flatMapIter);
	}

	@Test
	public void testFilter() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		List<Integer> filterIndex = FastStream.of(list).filter(String::valueOf, "1").toList();
		Assert.assertEquals(Collections.singletonList(1), filterIndex);
	}

	@Test
	public void testFilterIdx() {
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> filterIndex = FastStream.of(list).filterIdx((e, i) -> i < 2).toList();
		Assert.assertEquals(Arrays.asList("dromara", "hutool"), filterIndex);
		// 并行流时为-1
		Assert.assertEquals(3L, FastStream.of(1, 2, 3).parallel().filterIdx((e, i) -> i == -1).count());
	}

	@Test
	public void testNonNull() {
		List<Integer> list = Arrays.asList(1, null, 2, 3);
		List<Integer> nonNull = FastStream.of(list).nonNull().toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), nonNull);
	}

	@Test
	public void testParallel() {
		Assert.assertTrue(FastStream.of(1, 2, 3).parallel(true).isParallel());
		Assert.assertFalse(FastStream.of(1, 2, 3).parallel(false).isParallel());
	}

	@Test
	public void testPush() {
		List<Integer> list = Arrays.asList(1, 2);
		List<Integer> push = FastStream.of(list).push(3).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), push);
	}

	@Test
	public void testUnshift() {
		List<Integer> list = Arrays.asList(2, 3);
		List<Integer> unshift = FastStream.of(list).unshift(1).toList();
		Assert.assertEquals(Arrays.asList(1, 2, 3), unshift);
	}

	@Test
	public void testAt() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Assert.assertEquals(1, (Object) FastStream.of(list).at(0));
		Assert.assertEquals(1, (Object) FastStream.of(list).at(-3));
		Assert.assertEquals(3, (Object) FastStream.of(list).at(-1));
		Assert.assertNull(FastStream.of(list).at(-4));
	}

	@Test
	public void testSplice() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Assert.assertEquals(Arrays.asList(1, 2, 2, 3), FastStream.of(list).splice(1, 0, 2).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 3, 3), FastStream.of(list).splice(3, 1, 3).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 4), FastStream.of(list).splice(2, 1, 4).toList());
		Assert.assertEquals(Arrays.asList(1, 2), FastStream.of(list).splice(2, 1).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 3), FastStream.of(list).splice(2, 0).toList());
		Assert.assertEquals(Arrays.asList(1, 2), FastStream.of(list).splice(-1, 1).toList());
		Assert.assertEquals(Arrays.asList(1, 2, 3), FastStream.of(list).splice(-2, 2, 2, 3).toList());
	}

	@Test
	public void testFindFirst() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		Integer find = FastStream.of(list).findFirst(Objects::nonNull);
		Assert.assertEquals(1, (Object) find);
	}

	@Test
	public void testFindFirstIdx() {
		List<Integer> list = Arrays.asList(null, 2, 3);
		Integer idx = FastStream.of(list).findFirstIdx(Objects::nonNull);
		Assert.assertEquals(1, (Object) idx);
		Assert.assertEquals(-1, (Object) FastStream.of(list).parallel().findFirstIdx(Objects::nonNull));
	}

	@Test
	public void testFindLast() {
		List<Integer> list = Arrays.asList(1, null, 3);
		Integer find = FastStream.of(list).findLast(Objects::nonNull);
		Assert.assertEquals(3, (Object) find);
		Assert.assertEquals(3, (Object) FastStream.of(list).findLast().orElse(null));
	}

	@Test
	public void testFindLastIdx() {
		List<Integer> list = Arrays.asList(1, null, 3);
		Integer idx = FastStream.of(list).findLastIdx(Objects::nonNull);
		Assert.assertEquals(2, (Object) idx);
		Assert.assertEquals(-1, (Object) FastStream.of(list).parallel().findLastIdx(Objects::nonNull));
	}

	@Test
	public void testZip() {
		List<Integer> orders = Arrays.asList(1, 2, 3);
		List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> zip = FastStream.of(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);
	}

	@Test
	public void testSub() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<List<Integer>> lists = FastStream.of(list).sub(2).map(FastStream::toList).toList();
		Assert.assertEquals(Arrays.asList(Arrays.asList(1, 2),
				Arrays.asList(3, 4),
				singletonList(5)
		), lists);
	}

	@Test
	public void testSubList() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<List<Integer>> lists = FastStream.of(list).subList(2).toList();
		Assert.assertEquals(Arrays.asList(Arrays.asList(1, 2),
				Arrays.asList(3, 4),
				singletonList(5)
		), lists);
	}
}
