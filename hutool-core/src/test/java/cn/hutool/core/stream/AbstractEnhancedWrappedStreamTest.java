package cn.hutool.core.stream;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * {@link AbstractEnhancedWrappedStream}、{@link TerminableWrappedStream}、{@link TransformableWrappedStream}的测试用例。
 * 此用例用于保证通过{@link AbstractEnhancedWrappedStream}获得的默认方法，在子类不重写的情况下能够按照预期效果生效
 *
 * @author huangchengxing
 */
public class AbstractEnhancedWrappedStreamTest {

	@Test
	public void testToList() {
		final List<Integer> list = asList(1, 2, 3);
		final List<Integer> toList = wrap(list).toList();
		Assert.assertEquals(list, toList);
	}

	@Test
	public void testToUnmodifiableList() {
		final List<Integer> list = wrap(1, 2, 3)
				.toUnmodifiableList();
		Assert.assertThrows(UnsupportedOperationException.class, () -> list.remove(0));
	}

	@Test
	public void testToSet() {
		final List<Integer> list = asList(1, 2, 3);
		final Set<String> toSet = wrap(list).map(String::valueOf).toSet();
		Assert.assertEquals(new HashSet<>(asList("1", "2", "3")), toSet);
	}

	@Test
	public void testToUnmodifiableSet() {
		final Set<Integer> set = wrap(1, 2, 3)
				.toUnmodifiableSet();
		Assert.assertThrows(UnsupportedOperationException.class, () -> set.remove(0));
	}

	@Test
	public void testToCollection() {
		final List<Integer> list = asList(1, 2, 3);
		final List<String> toCollection = wrap(list).map(String::valueOf).toColl(LinkedList::new);
		Assert.assertEquals(asList("1", "2", "3"), toCollection);
	}

	@Test
	public void testToMap() {
		final List<Integer> list = asList(1, 2, 3);
		final Map<String, Integer> identityMap = wrap(list).toMap(String::valueOf);
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
	public void testToUnmodifiableMap() {
		final Map<Integer, Integer> map1 = wrap(1, 2, 3).toUnmodifiableMap(Function.identity(), Function.identity());
		Assert.assertThrows(UnsupportedOperationException.class, () -> map1.remove(1));
		final Map<Integer, Integer> map2 = wrap(1, 2, 3).toUnmodifiableMap(Function.identity(), Function.identity(), (t1, t2) -> t1);
		Assert.assertThrows(UnsupportedOperationException.class, () -> map2.remove(1));
	}

	@Test
	public void testToZip() {
		final List<Integer> orders = asList(1, 2, 3);
		final List<String> list = asList("dromara", "hutool", "sweet");
		final Map<Integer, String> toZip = wrap(orders).toZip(list);
		Assert.assertEquals(new HashMap<Integer, String>() {
			private static final long serialVersionUID = 1L;

			{
				put(1, "dromara");
				put(2, "hutool");
				put(3, "sweet");
			}
		}, toZip);
	}

	@Test
	public void testTransform() {
		final List<Integer> list = wrap(1, 2, 3).transform(Wrapper::toList).orElse(null);
		Assert.assertEquals(asList(1, 2, 3), list);
	}

	@Test
	public void testFindFirst() {
		final List<Integer> list = asList(1, 2, 3);
		Assert.assertEquals((Integer) 1, wrap(list).findFirst(t -> (t & 1) == 1).orElse(null));
		Assert.assertEquals((Integer) 1, wrap(list).filter(t -> (t & 1) == 1).findFirst().orElse(null));
	}

	@Test
	public void testFindFirstIdx() {
		final List<Integer> list = asList(1, 2, 3);
		Assert.assertEquals(1, wrap(list).findFirstIdx(t -> (t & 1) == 0));
	}

	@Test
	public void testFindLast() {
		final List<Integer> list = asList(1, 2, 3);
		Assert.assertEquals((Integer) 3, wrap(list).findLast(t -> (t & 1) == 1).orElse(null));
	}

	@Test
	public void testFindLastIdx() {
		final List<Integer> list = asList(1, 2, 3);
		Assert.assertEquals(1, wrap(list).findLastIdx(t -> (t & 1) == 0));
	}

	@Test
	public void testAt() {
		final List<Integer> list = asList(1, 2, 3);
		Assert.assertEquals((Integer) 3, wrap(list).at(2).orElse(null));
	}

	@Test
	public void testIsEmpty() {
		Assert.assertTrue(wrap(Collections.emptyList()).isEmpty());
		Assert.assertFalse(wrap(asList(1, 2, 3)).isEmpty());
	}

	@Test
	public void testIsNotEmpty() {
		Assert.assertFalse(wrap(Collections.emptyList()).isNotEmpty());
		Assert.assertTrue(wrap(asList(1, 2, 3)).isNotEmpty());
	}

	@Test
	public void testJoining() {
		final List<Integer> list = asList(1, 2, 3);
		final String joining = wrap(list).join();
		Assert.assertEquals("123", joining);
		Assert.assertEquals("1,2,3", wrap(list).join(","));
		Assert.assertEquals("(1,2,3)", wrap(list).join(",", "(", ")"));
	}

	@Test
	public void testGrouping() {
		final List<Integer> list = asList(1, 2, 3);
		final Map<String, List<Integer>> map = new HashMap<String, List<Integer>>() {
			private static final long serialVersionUID = 1L;

			{
				put("1", singletonList(1));
				put("2", singletonList(2));
				put("3", singletonList(3));
			}
		};

		Map<String, List<Integer>> group = wrap(list).group(String::valueOf, HashMap::new, Collectors.toList());
		Assert.assertEquals(map, group);
		group = wrap(list).group(String::valueOf, Collectors.toList());
		Assert.assertEquals(map, group);
		group = wrap(list).group(String::valueOf);
		Assert.assertEquals(map, group);
	}

	@Test
	public void testPartitioning() {
		final List<Integer> list = asList(1, 2, 3);
		final Map<Boolean, List<Integer>> map = new HashMap<Boolean, List<Integer>>() {
			private static final long serialVersionUID = 1L;
			{
				put(Boolean.TRUE, singletonList(2));
				put(Boolean.FALSE, asList(1, 3));
			}
		};

		Map<Boolean, List<Integer>> partition = wrap(list).partition(t -> (t & 1) == 0, Collectors.toList());
		Assert.assertEquals(map, partition);
		partition = wrap(list).partition(t -> (t & 1) == 0);
		Assert.assertEquals(map, partition);
	}

	@Test
	public void testForEachIdx() {
		final List<Integer> elements = new ArrayList<>();
		final List<Integer> indexes = new ArrayList<>();
		wrap(1, 2, 3).forEachIdx((t, i) -> {
			elements.add(t);
			indexes.add(i);
		});
		Assert.assertEquals(asList(1, 2, 3), elements);
		Assert.assertEquals(asList(0, 1, 2), indexes);
	}

	@Test
	public void testForEachOrderedIdx() {
		final List<Integer> elements = new ArrayList<>();
		final List<Integer> indexes = new ArrayList<>();
		wrap(1, 2, 3).forEachOrderedIdx((t, i) -> {
			elements.add(t);
			indexes.add(i);
		});
		Assert.assertEquals(asList(1, 2, 3), elements);
		Assert.assertEquals(asList(0, 1, 2), indexes);
	}

	@Test
	public void testForEachOrdered() {
		final List<Integer> elements = new ArrayList<>();
		wrap(1, 2, 3).forEachOrdered(elements::add);
		Assert.assertEquals(asList(1, 2, 3), elements);
	}

	@Test
	public void testForEach() {
		final List<Integer> elements = new ArrayList<>();
		wrap(1, 2, 3).forEach(elements::add);
		Assert.assertEquals(asList(1, 2, 3), elements);
	}

	@Test
	public void testMapToInt() {
		final int[] array = wrap(1, 2, 3).mapToInt(Integer::intValue).toArray();
		Assert.assertArrayEquals(new int[]{1, 2, 3}, array);
	}

	@Test
	public void testMapToLong() {
		final long[] array = wrap(1L, 2L, 3L).mapToLong(Long::intValue).toArray();
		Assert.assertArrayEquals(new long[]{1L, 2L, 3L}, array);
	}

	@Test
	public void testMapToDouble() {
		final double[] array = wrap(1d, 2d, 3d).mapToDouble(Double::intValue).toArray();
		Assert.assertEquals(1d, array[0], 0);
		Assert.assertEquals(2d, array[1], 0);
		Assert.assertEquals(3d, array[2], 0);
	}

	@Test
	public void testFlatMapToInt() {
		final int[] array = wrap(1, 2, 3).flatMapToInt(IntStream::of).toArray();
		Assert.assertArrayEquals(new int[]{1, 2, 3}, array);
	}

	@Test
	public void testFlatMapToLong() {
		final long[] array = wrap(1L, 2L, 3L).flatMapToLong(LongStream::of).toArray();
		Assert.assertArrayEquals(new long[]{1L, 2L, 3L}, array);
	}

	@Test
	public void testFlatMapToDouble() {
		final double[] array = wrap(1d, 2d, 3d).flatMapToDouble(DoubleStream::of).toArray();
		Assert.assertEquals(1d, array[0], 0);
		Assert.assertEquals(2d, array[1], 0);
		Assert.assertEquals(3d, array[2], 0);
	}

	@Test
	public void testSorted() {
		final List<Integer> list = wrap(3, 1, 2).sorted().toList();
		Assert.assertEquals(asList(1, 2, 3), list);
	}

	@Test
	public void testPeek() {
		final List<Integer> elements = new ArrayList<>();
		wrap(1, 2, 3).peek(elements::add).exec();
		Assert.assertEquals(asList(1, 2, 3), elements);
	}

	@Test
	public void testPeekIdx() {
		final List<Integer> elements = new ArrayList<>();
		final List<Integer> indexes = new ArrayList<>();
		wrap(1, 2, 3).peekIdx((t, i) -> {
			elements.add(t);
			indexes.add(i);
		}).exec();
		Assert.assertEquals(asList(1, 2, 3), elements);
		Assert.assertEquals(asList(0, 1, 2), indexes);

		final Set<Integer> elements2 = new HashSet<>();
		final Set<Integer> indexes2 = new HashSet<>();
		wrap(1, 2, null).parallel().peekIdx((t, i) -> {
			elements2.add(t);
			indexes2.add(i);
		}).exec();
		Assert.assertEquals(new HashSet<>(asList(1, null, 2)).toString(), elements2.toString());
		Assert.assertEquals(new HashSet<>(asList(-1, -1, -1)).toString(), indexes2.toString());
	}

	@Test
	public void testLimit() {
		final List<Integer> list = wrap(1, 2, 3).limit(2L).toList();
		Assert.assertEquals(asList(1, 2), list);
	}

	@Test
	public void testSkip() {
		final List<Integer> list = wrap(1, 2, 3).skip(1L).toList();
		Assert.assertEquals(asList(2, 3), list);
	}

	@Test
	public void testToArray() {
		Object[] array1 = wrap(1, 2, 3).toArray();
		Assert.assertArrayEquals(new Object[]{1, 2, 3}, array1);
		array1 = wrap(1, 2, 3).toArray(Object[]::new);
		Assert.assertArrayEquals(new Object[]{1, 2, 3}, array1);
	}

	@Test
	public void testReduce() {
		Assert.assertEquals((Integer) 6, wrap(1, 2, 3).reduce(Integer::sum).orElse(null));
		Assert.assertEquals((Integer) 6, wrap(1, 2, 3).reduce(0, Integer::sum));
		Assert.assertEquals((Integer) 6, wrap(1, 2, 3).reduce(0, Integer::sum, Integer::sum));
	}

	@Test
	public void testCollect() {
		Assert.assertEquals(asList(1, 2, 3), wrap(1, 2, 3).collect(Collectors.toList()));
		Assert.assertEquals(
				asList(1, 2, 3),
				wrap(1, 2, 3).collect(ArrayList::new, List::add, List::addAll)
		);
	}

	@Test
	public void testMin() {
		Assert.assertEquals((Integer) 1, wrap(1, 2, 3).min(Comparator.comparingInt(Integer::intValue)).orElse(null));
	}

	@Test
	public void testMax() {
		Assert.assertEquals((Integer) 3, wrap(1, 2, 3).max(Comparator.comparingInt(Integer::intValue)).orElse(null));
	}

	@Test
	public void testCount() {
		Assert.assertEquals(3, wrap(1, 2, 3).count());
	}

	@Test
	public void testAnyMatch() {
		Assert.assertTrue(wrap(1, 2, 3).anyMatch(t -> (t & 1) == 0));
		Assert.assertFalse(wrap(1, 3).anyMatch(t -> (t & 1) == 0));
	}

	@Test
	public void testAllMatch() {
		Assert.assertFalse(wrap(1, 2, 3).allMatch(t -> (t & 1) == 0));
		Assert.assertTrue(wrap(2, 4).anyMatch(t -> (t & 1) == 0));
	}

	@Test
	public void testNoneMatch() {
		Assert.assertFalse(wrap(1, 2, 3).noneMatch(t -> (t & 1) == 0));
		Assert.assertTrue(wrap(1, 3).noneMatch(t -> (t & 1) == 0));
	}

	@Test
	public void testFindAny() {
		Assert.assertNotNull(wrap(1, 2, 3).findAny());
	}

	@Test
	public void testIterator() {
		final Iterator<Integer> iter1 = Stream.of(1, 2, 3).iterator();
		final Iterator<Integer> iter2 = wrap(1, 2, 3).iterator();
		while (iter1.hasNext() && iter2.hasNext()) {
			Assert.assertEquals(iter1.next(), iter2.next());
		}
	}

	@Test
	public void testSpliterator() {
		final Spliterator<Integer> iter1 = Stream.of(1, 2, 3).spliterator();
		final Spliterator<Integer> iter2 = wrap(1, 2, 3).spliterator();
		Assert.assertEquals(iter1.trySplit().estimateSize(), iter2.trySplit().estimateSize());
	}

	@Test
	public void testIsParallel() {
		Assert.assertTrue(wrap(Stream.of(1, 2, 3).parallel()).isParallel());
	}

	@Test
	public void testSequential() {
		Assert.assertFalse(wrap(Stream.of(1, 2, 3).parallel()).sequential().isParallel());
	}

	@Test
	public void testUnordered() {
		Assert.assertNotNull(wrap(Stream.of(1, 2, 3)).unordered());
	}

	@Test
	public void testOnClose() {
		final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
		wrap(Stream.of(1, 2, 3).onClose(() -> atomicBoolean.set(true))).close();
		Assert.assertTrue(atomicBoolean.get());
	}

	@Test
	public void testClose() {
		final Wrapper<Integer> stream = wrap(Stream.of(1, 2, 3));
		stream.close();
		Assert.assertThrows(IllegalStateException.class, stream::exec);
	}

	@Test
	public void testReverse() {
		Assert.assertEquals(
				asList(3, 2, 1), wrap(1, 2, 3).reverse().toList()
		);
	}

	@Test
	public void testParallel() {
		Assert.assertTrue(wrap(1, 2, 3).parallel().isParallel());
	}

	@Test
	public void testSplice() {
		Assert.assertEquals(
				asList(1, 4, 5), wrap(1, 2, 3).splice(1, 2, 4, 5).toList()
		);
	}

	@Test
	public void testTakeWhile() {
		Assert.assertEquals(
				asList(1, 2),
				wrap(1, 2, 3, 4).takeWhile(i -> !Objects.equals(i, 3)).toList()
		);

	}

	@Test
	public void testDropWhile() {
		Assert.assertEquals(
				asList(3, 4),
				wrap(1, 2, 3, 4).dropWhile(i -> !Objects.equals(i, 3)).toList()
		);
	}

	@Test
	public void testDistinct() {
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1, 1, 2, 3).distinct().toList()
		);
	}

	@Test
	public void testLog() {
		Assert.assertNotNull(wrap(1, 2, 3).log().toList());
	}

	@Test
	public void testPush() {
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1).push(2, 3).toList()
		);
	}

	@Test
	public void testUnshift() {
		Assert.assertEquals(
				asList(1, 2, 3), wrap(3).unshift(1, 2).toList()
		);
	}

	@Test
	public void testAppend() {
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1).append(asList(2, 3)).toList()
		);
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1, 2, 3).append(null).toList()
		);
	}

	@Test
	public void testPrepend() {
		Assert.assertEquals(
				asList(1, 2, 3), wrap(3).prepend(asList(1, 2)).toList()
		);
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1, 2, 3).prepend(null).toList()
		);
	}

	@Test
	public void testNonNull() {
		Assert.assertEquals(
				asList(1, 3), wrap(1, null, 3).nonNull().toList()
		);
	}

	@Test
	public void testFilterIdx() {
		final List<Integer> indexes = new ArrayList<>();
		Assert.assertEquals(
				asList(1, 3),
				wrap(1, 2, 3).filterIdx((t, i) -> {
					indexes.add(i);
					return (t & 1) == 1;
				}).toList()
		);
		Assert.assertEquals(asList(0, 1, 2), indexes);
	}

	@Test
	public void testFilter() {
		Assert.assertEquals(
				asList(1, 3), wrap(1, 2, 3).filter(i -> (i & 1) == 1).toList()
		);
	}

	@Test
	public void testFlatMap() {
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1, 2, 3).flatMap(Stream::of).toList()
		);
	}

	@Test
	public void testFlatMapIdx() {
		final List<Integer> indexes = new ArrayList<>();
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1, 2, 3).flatMapIdx((t, i) -> {
					indexes.add(i);
					return Stream.of(t);
				}).toList()
		);
		Assert.assertEquals(asList(0, 1, 2), indexes);
	}

	@Test
	public void testFlat() {
		Assert.assertEquals(
				asList(1, 2, 3), wrap(1, 2, 3).flat(Collections::singletonList).toList()
		);
	}

	@Test
	public void testFlatNonNull() {
		Assert.assertEquals(
				asList(2, 3), wrap(null, 2, 3).flatNonNull(Collections::singletonList).toList()
		);
	}

	@Test
	public void testFlatTree() {
		final Tree root = new Tree(1, ListUtil.of(new Tree(2, ListUtil.of(new Tree(3, Collections.emptyList())))));
		Assert.assertEquals(3L, wrap(root).flatTree(Tree::getChildren, Tree::setChildren).count());
	}

	@Test
	public void testMap() {
		Assert.assertEquals(
				asList("1", "2", "3"), wrap(1, 2, 3).map(String::valueOf).toList()
		);
	}

	@Test
	public void testMapNonNull() {
		Assert.assertEquals(
				ListUtil.of("3"), wrap(null, 2, 3, 4).mapNonNull(t -> ((t & 1) == 0) ? null : String.valueOf(t)).toList()
		);
	}

	@Test
	public void testMapIdx() {
		final List<Integer> indexes = new ArrayList<>();
		Assert.assertEquals(
				asList("1", "2", "3"), wrap(1, 2, 3).mapIdx((t, i) -> {
					indexes.add(i);
					return String.valueOf(t);
				}).toList()
		);
		Assert.assertEquals(asList(0, 1, 2), indexes);
	}

	@Test
	public void testMapMulti() {
		Assert.assertEquals(
				asList(1, 2, 3),
				wrap(1, 2, 3).mapMulti((t, builder) -> {
					builder.accept(t);
				}).toList()
		);
	}

	@Test
	public void testHashCode() {
		final Stream<Integer> stream = Stream.of(1, 2, 3);
		Assert.assertEquals(stream.hashCode(), wrap(stream).hashCode());
	}

	@Test
	public void testEquals() {
		final Stream<Integer> stream = Stream.of(1, 2, 3);
		Assert.assertEquals(wrap(stream), stream);
	}

	@Test
	public void testToString() {
		final Stream<Integer> stream = Stream.of(1, 2, 3);
		Assert.assertEquals(stream.toString(), wrap(stream).toString());
	}

	@Test
	public void testToEntries() {
		final Map<Integer, Integer> expect = new HashMap<Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			{
			put(1, 1);
			put(2, 2);
			put(3, 3);
		}};
		Map<Integer, Integer> map = EasyStream.of(1, 2, 3)
				.toEntries(Function.identity(), Function.identity())
				.toMap();
		Assert.assertEquals(expect, map);

		map = EasyStream.of(1, 2, 3)
				.toEntries(Function.identity())
				.toMap();
		Assert.assertEquals(expect, map);
	}

	@Test
	public void testZip() {
		final List<Integer> orders = Arrays.asList(1, 2, 3);
		final List<String> list = Arrays.asList("dromara", "hutool", "sweet");
		List<String> zip = wrap(orders).zip(list, (e1, e2) -> e1 + "." + e2).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);

		zip = this.wrap((Stream<Integer>) EasyStream.iterate(1, i -> i + 1)).limit(10).zip(list, (e1, e2) -> e1 + "." + e2).toList();
		Assert.assertEquals(Arrays.asList("1.dromara", "2.hutool", "3.sweet"), zip);
	}

	@Test
	public void testListSplit() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<List<Integer>> lists = wrap(list).split(2).map(TerminableWrappedStream::toList).toList();
		Assert.assertEquals(ListUtil.split(list, 2), lists);

		// 指定长度 大于等于 列表长度
		lists = wrap(list).split(list.size()).map(TerminableWrappedStream::toList).toList();
		Assert.assertEquals(singletonList(list), lists);
	}

	@Test
	public void testSplitList() {
		final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<List<Integer>> lists = wrap(list).splitList(2).toList();
		Assert.assertEquals(ListUtil.split(list, 2), lists);

		// 指定长度 大于等于 列表长度
		lists = wrap(list).splitList(list.size()).toList();
		Assert.assertEquals(singletonList(list), lists);
	}

	@SafeVarargs
	private final <T> Wrapper<T> wrap(final T... array) {
		return new Wrapper<>(Stream.of(array));
	}

	private <T> Wrapper<T> wrap(final Iterable<T> iterable) {
		return new Wrapper<>(StreamSupport.stream(iterable.spliterator(), false));
	}

	private <T> Wrapper<T> wrap(final Stream<T> stream) {
		return new Wrapper<>(stream);
	}

	private static class Wrapper<T> extends AbstractEnhancedWrappedStream<T, Wrapper<T>> {

		/**
		 * 创建一个流包装器
		 *
		 * @param stream 包装的流对象
		 * @throws NullPointerException 当{@code unwrap}为{@code null}时抛出
		 */
		protected Wrapper(final Stream<T> stream) {
			super(stream);
		}

		@Override
		public Wrapper<T> wrap(final Stream<T> source) {
			return new Wrapper<>(source);
		}

	}

	@Setter
	@Getter
	@AllArgsConstructor
	private static class Tree {
		private final Integer id;
		private List<Tree> children;
	}

}
