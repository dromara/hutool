package cn.hutool.core.stream;

import cn.hutool.core.collection.CollUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtilTest {

	@Test
	public void ofTest(){
		final Stream<Integer> stream = StreamUtil.of(2, x -> x * 2, 4);
		final String result = stream.collect(CollectorUtil.joining(","));
		assertEquals("2,4,8,16", result);
	}

	// === iterator ===
	@Test
	public void streamTestNullIterator() {
		assertThrows(IllegalArgumentException.class, () -> StreamUtil.of((Iterator<Object>) null));
	}

	@SuppressWarnings({"RedundantOperationOnEmptyContainer", "RedundantCollectionOperation"})
	@Test
	public void streamTestEmptyListToIterator() {
		assertStreamIsEmpty(StreamUtil.of(new ArrayList<>().iterator()));
	}

	@Test
	public void streamTestEmptyIterator() {
		assertStreamIsEmpty(StreamUtil.of(Collections.emptyIterator()));
	}

	@Test
	public void streamTestOrdinaryIterator() {
		final ArrayList<Integer> arrayList = CollUtil.newArrayList(1, 2, 3);
		assertArrayEquals(new Integer[]{1, 2, 3}, StreamUtil.of(arrayList.iterator()).toArray());

		final HashSet<Integer> hashSet = CollUtil.newHashSet(1, 2, 3);
		assertEquals(hashSet, StreamUtil.of(hashSet.iterator()).collect(Collectors.toSet()));
	}

	void assertStreamIsEmpty(final Stream<?> stream) {
		assertNotNull(stream);
		assertEquals(0, stream.toArray().length);
	}
	// ================ stream test end ================
}
