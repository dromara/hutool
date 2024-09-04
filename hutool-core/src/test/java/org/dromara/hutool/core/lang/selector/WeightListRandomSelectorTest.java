package org.dromara.hutool.core.lang.selector;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class WeightListRandomSelectorTest {

	@Test
	@Disabled
	public void weightListRandomTest() {
		final Map<Integer, Times> timesMap = new HashMap<>();
		final int size = 100;
		int sumWeight = 0;
		final WeightListRandomSelector<Integer> pool = new WeightListRandomSelector<>(size);
		for (int i = 0; i < size; i++) {
			final int weight = RandomUtil.randomInt(1, 100);
			pool.add(i, weight);
			sumWeight += weight;
			timesMap.put(i, new Times(weight));
		}

		final int times = 100000000;// 随机次数
		for (int i = 0; i < times; i++) {
			timesMap.get(pool.select()).num++;
		}

		final int finalSumWeight = sumWeight;
		timesMap.forEach((key, times1) -> {
			final double expected = times1.weight / finalSumWeight;// 期望概率
			final double actual = (double) timesMap.get(key).num / times;// 真实随机概率
			Console.log(expected, actual);
		});
	}

	private static class Times {
		int num;
		double weight;

		public Times(final double weight) {
			this.weight = weight;
		}
	}

	@Test
	public void weightRandomBenchTest() {
		final int size = 100;
		final WeightListRandomSelector<Integer> pool = new WeightListRandomSelector<>(size);
		final WeightRandomSelector<Integer> pool2 = new WeightRandomSelector<>();
		for (int i = 0; i < size; i++) {
			final int weight = RandomUtil.randomInt(1, 100);
			pool.add(i, weight);
			pool2.add(i, weight);
		}

		final int count = 1000;
		final StopWatch stopWatch = DateUtil.createStopWatch();
		stopWatch.start("WeightListRandomSelector");
		for (int i = 0; i < count; i++) {
			pool.select();
		}
		stopWatch.stop();
		stopWatch.start("WeightRandomSelector");
		for (int i = 0; i < count; i++) {
			pool2.select();
		}
		stopWatch.stop();

		//Console.log(stopWatch.prettyPrint());
	}
}
