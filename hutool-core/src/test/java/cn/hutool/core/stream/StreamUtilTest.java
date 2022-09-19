package cn.hutool.core.stream;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtilTest {

	@Test
	public void ofTest(){
		final Stream<Integer> stream = StreamUtil.of(2, x -> x * 2, 4);
		final String result = stream.collect(CollectorUtil.joining(","));
		Assert.assertEquals("2,4,8,16", result);
	}

	// === iterator ===
	@Test
	public void streamTestNullIterator() {
		Assert.assertThrows(IllegalArgumentException.class, () -> StreamUtil.of((Iterator<Object>) null));
	}

	@Test
	public void streamTestEmptyIterator() {
		assertStreamIsEmpty(StreamUtil.of(new ArrayList<>().iterator()));
	}

	@Test
	public void streamTestOrdinaryIterator() {
		ArrayList<Integer> arrayList = CollUtil.newArrayList(1, 2, 3);
		Assert.assertArrayEquals(new Integer[]{1, 2, 3}, StreamUtil.of(arrayList.iterator()).toArray());

		HashSet<Integer> hashSet = CollUtil.newHashSet(1, 2, 3);
		Assert.assertEquals(hashSet, StreamUtil.of(hashSet.iterator()).collect(Collectors.toSet()));
	}

	void assertStreamIsEmpty(Stream<?> stream) {
		Assert.assertNotNull(stream);
		Assert.assertEquals(0, stream.toArray().length);
	}
	// ================ stream test end ================
}
