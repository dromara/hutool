package cn.hutool.core.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * {@link OptionalCollection} 与 {@link  OptionalStream}用法测试
 *
 * @author Wilson-He
 */
public class OptionalCollectionTest {

	@Test
	public void empty() {
		List<String> defaultList = Arrays.asList("aaa", "bbb", "ccc");
		// test if empty, won't do anything
		List<String> list = null;
		OptionalCollection.ofEmpty(list)
				.ifNotEmpty(collection -> System.out.println("isPresent: " + collection));

		// test if null, subsequent operations won't throw NullPointerException
		OptionalCollection.ofEmpty(list)
				.filter(collection -> collection.contains("aaa"))
				.ifNotEmpty(collection -> System.out.println("filter: " + collection));

		// test if empty, won't do anything
		Assert.assertEquals(OptionalCollection.ofEmpty(list)
				.filter(collection -> collection.contains("aaa"))
				.orElse(defaultList), defaultList);
	}


	/**
	 * {@code OptionStream} usage test
	 */
	@Test
	public void stream() {
		List<String> defaultList = Arrays.asList("abc", "bac");

		// sorted list
		List<String> list = Arrays.asList("aaa", "bb", "ccaa", "ccbb", "aabb");
		Collection<String> sortedList = OptionalCollection.ofEmpty(list)
				.stream()
				.map(stream -> stream
						.filter(str -> str.contains("a"))
						.sorted())
				.toOptionalCollection()
				.orElse(defaultList);
		List<String> compareList = new ArrayList<>();
		compareList.add("aaa");
		compareList.add("aabb");
		compareList.add("ccaa");
		Assert.assertEquals(sortedList, compareList);

		// filter test
		boolean filter = OptionalCollection.ofEmpty(list)
				.stream()
				.optional(stream -> stream
						.filter(str -> str.contains("a"))
						.anyMatch(str -> str.contains("c")))
				.orElse(false);
		Assert.assertTrue(filter);

		filter = OptionalCollection.ofEmpty(list)
				.stream()
				.optional(stream -> stream
						.filter(str -> str.contains("a"))
						.anyMatch(str -> str.contains("d")))
				.orElse(false);
		Assert.assertFalse(filter);

		// ensure list not modified
		Assert.assertEquals(list, ListUtil.of("aaa", "bb", "ccaa", "ccbb", "aabb"));
	}


	@Test
	public void orElseThrow() {
		// test throw exception if empty
		List<String> list = null;
		String exceptionMsg = "Collection is empty";
		OptionalCollection.ofEmpty(list)
				.filter(collection -> collection.contains("aaa"))
				.orEmptyThrow(() -> new IllegalArgumentException(exceptionMsg));
		Assert.assertThrows(exceptionMsg, IllegalArgumentException.class,
				() -> OptionalCollection.ofEmpty(list)
						.filter(collection -> collection.contains("aaa"))
						.orEmptyThrow(() -> new IllegalArgumentException(exceptionMsg)));
	}
}