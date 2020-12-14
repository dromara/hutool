package cn.hutool.core.collection;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wilson
 */
public class OptionalCollectionTest {

	@Test
	public void build() {
		Assert.assertThrows(NullPointerException.class, () -> OptionalCollection.of(null));
		Assert.assertFalse(OptionalCollection.ofEmpty(null).isPresent());
		Assert.assertTrue(OptionalCollection.ofEmpty(null).isEmpty());
		Assert.assertTrue(OptionalCollection.ofEmpty(Arrays.asList("1", "2")).isNotEmpty());
	}

	@Test
	public void orElse() {
		List<String> defaultList = Arrays.asList("1", "2", "3");
		List<String> nullList = null;
		List<String> testList = new ArrayList<>();
		// null orElse test
		Assert.assertEquals(OptionalCollection.ofEmpty(nullList)
				.orElse(defaultList), defaultList);
		Assert.assertEquals(OptionalCollection.ofEmpty(nullList)
				.orNullGet(() -> Arrays.asList("1", "2", "3")), defaultList);
		// empty orElse test
		Assert.assertNotEquals(OptionalCollection.ofEmpty(testList)
				.orElse(defaultList), defaultList);
		Assert.assertEquals(OptionalCollection.ofEmpty(testList)
				.orEmptyElse(defaultList), defaultList);
		Assert.assertNotEquals(OptionalCollection.ofEmpty(testList)
				.orNullGet(() -> Arrays.asList("1", "2", "3")), defaultList);
		Assert.assertEquals(OptionalCollection.ofEmpty(testList)
				.orEmptyGet(() -> Arrays.asList("1", "2", "3")), defaultList);

	}

	@Test
	public void throwException() {
		Assert.assertThrows(IllegalArgumentException.class, () ->
				OptionalCollection.ofEmpty(null)
						.orNullThrow(() -> new IllegalArgumentException("集合不能为null")));
		Assert.assertThrows(IllegalArgumentException.class, () ->
				OptionalCollection.ofEmpty(new ArrayList<>())
						.orEmptyThrow(() -> new IllegalArgumentException("集合不能为空")));
	}

	@Test
	public void map() {
		List<String> defaultList = Arrays.asList("1", "2", "3");
		Collection<Integer> resultList = OptionalCollection.ofEmpty(defaultList)
				.map(collection -> collection.stream()
						.map(Integer::valueOf)
						.collect(Collectors.toList()))
				.orElse(Arrays.asList(111, 222));
		int sum = resultList.stream().reduce(Integer::sum).orElse(100);
		Assert.assertEquals(sum, 6);

	}

	@Test
	public void stream() {
		List<String> defaultList = Arrays.asList("1", "2", "3");
		Assert.assertEquals("123", OptionalCollection.ofEmpty(defaultList)
				.stream()
				.reduce((a, b) -> a + b)
				.orElse("empty"));

		List<String> emptyList = new ArrayList<>();
		Assert.assertEquals("empty", OptionalCollection.ofEmpty(emptyList)
				.stream()
				.reduce((a, b) -> a + b)
				.orElse("empty"));

		List<String> nullList = null;
		Assert.assertEquals("empty", OptionalCollection.ofEmpty(nullList)
				.stream()
				.reduce((a, b) -> a + b)
				.orElse("empty"));
	}

	@Test
	public void filter() {
		List<String> defaultList = Arrays.asList("aa", "bb", "cc", "abcd");
		Assert.assertTrue(OptionalCollection.ofEmpty(defaultList)
				.filter(collection -> collection.contains("abcd"))
				.isNotEmpty());
		Assert.assertTrue(OptionalCollection.ofEmpty(defaultList)
				.filter(collection -> collection.contains("cd"))
				.isEmpty());
		Assert.assertTrue(OptionalCollection.ofEmpty(defaultList)
				.filterOrElse(collection -> collection.contains("cd"), Arrays.asList("cd", "ef"))
				.isNotEmpty());
		Assert.assertTrue(OptionalCollection.ofEmpty(defaultList)
				.filterOrElse(collection -> collection.contains("cd"), Arrays.asList("cd", "ef"))
				.get()
				.contains("cd"));
	}

	@Test
	public void ifPresentOrElse() {
		List<String> defaultList = Arrays.asList("a", "b", "c");
		String concat = OptionalCollection.ofEmpty(defaultList)
				.ifPresentOrElse(collection -> collection.stream()
						.reduce((a, b) -> a + "," + b)
						.orElse("abc"), "aaa");
		Assert.assertEquals(concat, "a,b,c");

		Assert.assertEquals("aaa", OptionalCollection.ofEmpty(null)
				.ifPresentOrElse(collection -> collection.stream()
						.reduce((a, b) -> a + "," + b)
						.orElse("abc"), "aaa"));

	}

	@Test
	public void ifNotEmpty() {
		List<String> defaultList = Arrays.asList("a", "b", "c");
		// print
		OptionalCollection.ofEmpty(defaultList)
				.ifNotEmpty(OptionalCollectionTest::deleteBatchIds);
		// won't print
		OptionalCollection.ofEmpty(null)
				.ifNotEmpty(OptionalCollectionTest::deleteBatchIds);
		OptionalCollection.ofEmpty(new ArrayList<>())
				.ifNotEmpty(OptionalCollectionTest::deleteBatchIds);
	}

	private static <V> void deleteBatchIds(Collection<V> collection) {
		System.out.println("delete batch ids: " + collection);
	}

	@Test
	public void ifNotEmptyOrElse() {
		List<String> defaultList = Arrays.asList("a", "b", "c");
		Assert.assertEquals("abc", OptionalCollection.ofEmpty(defaultList)
				.ifNotEmptyOrElse(collection -> collection.stream()
								.reduce((a, b) -> a + b)
								.get(),
						"abcdef"));

		List<String> nullList = null;
		Assert.assertEquals("abcdef", OptionalCollection.ofEmpty(nullList)
				.ifNotEmptyOrElse(collection -> collection.stream()
								.reduce((a, b) -> a + b)
								.orElse("abc"),
						"abcdef"));
	}

}