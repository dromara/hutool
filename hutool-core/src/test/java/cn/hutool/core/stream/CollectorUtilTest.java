package cn.hutool.core.stream;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectorUtilTest {
	@Test
	public void reduceListMapTest() {
		final Set<Map<String, Integer>> nameScoreMapList = StreamUtil.of(
				// 集合内的第一个map，包含两个key value
				MapUtil.builder("苏格拉底", 1).put("特拉叙马霍斯", 3).build(),
				MapUtil.of("苏格拉底", 2),
				MapUtil.of("特拉叙马霍斯", 1),
				MapUtil.of("特拉叙马霍斯", 2)
		).collect(Collectors.toSet());
		// 执行聚合
		final Map<String, List<Integer>> nameScoresMap = nameScoreMapList.stream().collect(CollectorUtil.reduceListMap());

		Assert.assertEquals(MapUtil.builder("苏格拉底", Arrays.asList(1, 2))
						.put("特拉叙马霍斯", Arrays.asList(3, 1, 2)).build(),
				nameScoresMap);

		List<Map<String, String>> data = ListUtil.toList(
			MapUtil.builder("name", "sam").put("count", "80").map(),
			MapUtil.builder("name", "sam").put("count", "81").map(),
			MapUtil.builder("name", "sam").put("count", "82").map(),
			MapUtil.builder("name", "jack").put("count", "80").map(),
			MapUtil.builder("name", "jack").put("count", "90").map()
		);

		Map<String, Map<String, List<String>>> nameMap = data.stream()
			.collect(Collectors.groupingBy(e -> e.get("name"), CollectorUtil.reduceListMap()));
		Assert.assertEquals(MapUtil.builder("jack", MapUtil.builder("name", Arrays.asList("jack", "jack"))
				.put("count", Arrays.asList("80", "90")).build())
			.put("sam", MapUtil.builder("name", Arrays.asList("sam", "sam", "sam"))
				.put("count", Arrays.asList("80", "81", "82")).build())
			.build(), nameMap);
	}

	@Test
	public void testGroupingByAfterValueMapped() {
		List<Integer> list = Arrays.asList(1, 1, 2, 2, 3, 4);
		Map<Boolean, Set<String>> map = list.stream()
				.collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf, LinkedHashSet::new, LinkedHashMap::new));

		Assert.assertEquals(LinkedHashMap.class, map.getClass());
		Assert.assertEquals(new LinkedHashSet<>(Arrays.asList("2", "4")), map.get(Boolean.TRUE));
		Assert.assertEquals(new LinkedHashSet<>(Arrays.asList("1", "3")), map.get(Boolean.FALSE));

		map = list.stream()
				.collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf, LinkedHashSet::new));
		Assert.assertEquals(HashMap.class, map.getClass());
		Assert.assertEquals(new LinkedHashSet<>(Arrays.asList("2", "4")), map.get(Boolean.TRUE));
		Assert.assertEquals(new LinkedHashSet<>(Arrays.asList("1", "3")), map.get(Boolean.FALSE));

		final Map<Boolean, List<String>> map2 = list.stream()
				.collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf));
		Assert.assertEquals(Arrays.asList("2", "2", "4"), map2.get(Boolean.TRUE));
		Assert.assertEquals(Arrays.asList("1", "1", "3"), map2.get(Boolean.FALSE));

	}
}
