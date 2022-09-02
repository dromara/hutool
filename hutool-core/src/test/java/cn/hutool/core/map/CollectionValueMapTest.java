package cn.hutool.core.map;

import cn.hutool.core.map.multi.CollectionValueMap;
import cn.hutool.core.map.multi.MultiValueMap;
import cn.hutool.core.text.StrUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CollectionValueMapTest {

	@Test
	public void putTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assert.assertNull(map.put(1, Arrays.asList("a", "b")));
		Collection<String> collection = map.put(1, Arrays.asList("c", "d"));
		Assert.assertEquals(Arrays.asList("a", "b"), collection);
	}

	@Test
	public void putAllTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Map<Integer, Collection<String>> source = new HashMap<>();
		source.put(1, Arrays.asList("a", "b", "c"));
		map.putAll(source);
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));
	}

	@Test
	public void putValueTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assert.assertTrue(map.putValue(1, "a"));
		Assert.assertTrue(map.putValue(1, "b"));
		Assert.assertTrue(map.putValue(1, "c"));
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));
	}

	@Test
	public void putAllValueTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assert.assertTrue(map.putAllValues(1, Arrays.asList("a", "b", "c")));
		Assert.assertEquals(1, map.size());
		Assert.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));

		Map<Integer, Collection<String>> source = new HashMap<>();
		Assert.assertTrue(map.putValue(1, "e"));
		Assert.assertTrue(map.putValue(1, "f"));
		map.putAllValues(source);
		Assert.assertEquals(Arrays.asList("a", "b", "c", "e", "f"), map.get(1));
	}

	@Test
	public void putValuesTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assert.assertTrue(map.putValues(1, "a", "b", "c"));
		Assert.assertEquals(Arrays.asList("a", "b", "c"), map.get(1));
	}

	@Test
	public void testFilterAllValues() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assert.assertTrue(map.putValues(1, "a", "b", "c"));
		Assert.assertTrue(map.putValues(2, "a", "b", "c"));

		Assert.assertEquals(map, map.filterAllValues((k, v) -> StrUtil.equals(v, "a")));
		Assert.assertEquals(Collections.singletonList("a"), map.getValues(1));
		Assert.assertEquals(Collections.singletonList("a"), map.getValues(2));

		Assert.assertEquals(map, map.filterAllValues(v -> !StrUtil.equals(v, "a")));
		Assert.assertEquals(Collections.emptyList(), map.getValues(1));
		Assert.assertEquals(Collections.emptyList(), map.getValues(2));
	}

	@Test
	public void testReplaceAllValues() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		Assert.assertTrue(map.putValues(1, "a", "b", "c"));
		Assert.assertTrue(map.putValues(2, "a", "b", "c"));

		Assert.assertEquals(map, map.replaceAllValues((k, v) -> v + "2"));
		Assert.assertEquals(Arrays.asList("a2", "b2", "c2"), map.getValues(1));
		Assert.assertEquals(Arrays.asList("a2", "b2", "c2"), map.getValues(2));

		Assert.assertEquals(map, map.replaceAllValues(v -> v + "3"));
		Assert.assertEquals(Arrays.asList("a23", "b23", "c23"), map.getValues(1));
		Assert.assertEquals(Arrays.asList("a23", "b23", "c23"), map.getValues(2));
	}

	@Test
	public void removeValueTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assert.assertFalse(map.removeValue(1, "d"));
		Assert.assertTrue(map.removeValue(1, "c"));
		Assert.assertEquals(Arrays.asList("a", "b"), map.get(1));
	}

	@Test
	public void removeAllValuesTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assert.assertFalse(map.removeAllValues(1, Arrays.asList("e", "f")));
		Assert.assertTrue(map.removeAllValues(1, Arrays.asList("b", "c")));
		Assert.assertEquals(Collections.singletonList("a"), map.get(1));
	}

	@Test
	public void removeValuesTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assert.assertFalse(map.removeValues(1, "e", "f"));
		Assert.assertTrue(map.removeValues(1, "b", "c"));
		Assert.assertEquals(Collections.singletonList("a"), map.get(1));
	}

	@Test
	public void getValuesTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assert.assertEquals(Collections.emptyList(), map.getValues(2));
		Assert.assertEquals(Arrays.asList("a", "b", "c"), map.getValues(1));
	}

	@Test
	public void sizeTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		Assert.assertEquals(0, map.size(2));
		Assert.assertEquals(3, map.size(1));
	}

	@Test
	public void allForEachTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>();
		map.putValues(1, "a", "b", "c");
		List<Integer> keys = new ArrayList<>();
		List<String> values = new ArrayList<>();
		map.allForEach((k, v) -> {
			keys.add(k);
			values.add(v);
		});
		Assert.assertEquals(Arrays.asList(1, 1, 1), keys);
		Assert.assertEquals(Arrays.asList("a", "b", "c"), values);
	}

	@Test
	public void allValuesTest() {
		MultiValueMap<Integer, String> map = new CollectionValueMap<>(new LinkedHashMap<>());
		map.putAllValues(1, Arrays.asList("a", "b", "c"));
		map.putAllValues(2, Arrays.asList("d", "e"));
		Assert.assertEquals(
			Arrays.asList("a", "b", "c", "d", "e"),
			map.allValues()
		);
	}

}
