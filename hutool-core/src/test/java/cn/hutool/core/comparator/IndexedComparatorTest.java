package cn.hutool.core.comparator;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.StopWatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class IndexedComparatorTest {
	@Test
	public void sortTest() {
		final Object[] arr ={"a", "b", new User("9", null), "1",3,null,"2"};
		final Collection<Object> set = new HashSet<>(Arrays.asList(arr));


		final List<Object> sortSet = CollectionUtil.sort(set, new ArrayIndexedComparator<>(arr));

		Assert.assertEquals("a", sortSet.get(0));
		Assert.assertEquals( new User("9", null), sortSet.get(2));
		Assert.assertEquals(3, sortSet.get(4));
		Assert.assertNull(sortSet.get(5));
	}

	@Test
	public void reversedTest() {
		final Object[] arr ={"a", "b", new User("9", null), "1",3,null,"2"};
		final Collection<Object> set = new HashSet<>(Arrays.asList(arr));

		final List<Object> sortSet = CollectionUtil.sort(set, new ArrayIndexedComparator<>(arr).reversed());

		Assert.assertEquals("a", sortSet.get(6));
		Assert.assertNull(sortSet.get(1));
		Assert.assertEquals( new User("9", null), sortSet.get(4));
		Assert.assertEquals(3, sortSet.get(2));
	}

	@Test
	public void benchMarkSortTest() {
		final Object[] arr ={"a", "b", new User("9", null), "1",3,null,"2"};
		final Collection<Object> set = new HashSet<>(Arrays.asList(arr));

		final StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		for (int i = 0; i < 10_000_000; i++) {
			final List<Object> sortSet = CollectionUtil.sort(set, new IndexedComparator<>(arr));
		}
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());


		stopWatch.start();
		for (int i = 0; i < 10_000_000; i++) {
			final List<Object> sortSet = CollectionUtil.sort(set, new ArrayIndexedComparator<>(arr));
		}
		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}

	@Data
	@AllArgsConstructor
	static class User{
		private String a;
		private String b;
	}
}
