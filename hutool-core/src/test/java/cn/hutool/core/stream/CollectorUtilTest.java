package cn.hutool.core.stream;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

	@Data
	@Builder
	private static class BigDecimalTest{
		private String name;
		private BigDecimal count;
	}

	@Test
	public void testStreamBigDecimal(){

		List<BigDecimalTest> testList = ListUtil.toList();

		for (int i = 0; i < 5; i++) {
			BigDecimalTest test = BigDecimalTest.builder()
				.name("test" + i)
				.count(RandomUtil.randomBigDecimal().setScale(5, RoundingMode.HALF_UP))
				.build();
			testList.add(test);
		}

		testList.forEach(System.out::println);

		BigDecimal sum = testList.stream().collect(CollectorUtil.summingBigDecimal(BigDecimalTest::getCount));
		Assert.assertNotNull(sum);
		System.out.println("求和：" + sum);

		BigDecimal max = testList.stream().collect(CollectorUtil.maxByBigDecimal(BigDecimalTest::getCount));
		Assert.assertNotNull(max);
		System.out.println("最大值：" + max);

		BigDecimal min = testList.stream().collect(CollectorUtil.minByBigDecimal(BigDecimalTest::getCount));
		Assert.assertNotNull(min);
		System.out.println("最小值：" + min);

		BigDecimal avg = testList.stream().collect(CollectorUtil.averagingBigDecimal(BigDecimalTest::getCount, 2, RoundingMode.HALF_UP));
		Assert.assertNotNull(avg);
		System.out.println("平均值：" + avg);


	}
}
