package cn.hutool.core.stream;

import cn.hutool.core.map.MapUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CollectorUtilTest
 *
 * @author VampireAchao
 * @since 2022/7/3
 */
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
	public void testTransform() {
		Stream<Integer> stream = Stream.of(1, 2, 3, 4)
			.collect(CollectorUtil.transform(EasyStream::of));
		Assert.assertEquals(EasyStream.class, stream.getClass());

		stream = Stream.of(1, 2, 3, 4)
			.collect(CollectorUtil.transform(HashSet::new, EasyStream::of));
		Assert.assertEquals(EasyStream.class, stream.getClass());
	}

	@Test
	public void testToEasyStream() {
		Stream<Integer> stream =Stream.of(1, 2, 3, 4)
			.collect(CollectorUtil.toEasyStream());
		Assert.assertEquals(EasyStream.class, stream.getClass());
	}

}
