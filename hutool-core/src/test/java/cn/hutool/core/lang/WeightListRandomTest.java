package cn.hutool.core.lang;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeightListRandomTest {

	@Test
	@Disabled
	public void nextTest() {
		Map<Integer, Times> timesMap = new HashMap<>();
		int size = 100;
		double sumWeight = 0.0;
		WeightListRandom<Integer> pool = new WeightListRandom<>(size);
		for (int i = 0; i < size; i++) {
			double weight = RandomUtil.randomDouble(100);
			pool.add(i, weight);
			sumWeight += weight;
			timesMap.put(i, new Times(weight));
		}

		double d = 0.0001;// 随机误差
		int times = 100000000;// 随机次数
		for (int i = 0; i < times; i++) {
			timesMap.get(pool.next()).num++;
		}
		double finalSumWeight = sumWeight;
		timesMap.forEach((key, times1) -> {
			double expected = times1.weight / finalSumWeight;// 期望概率
			double actual = timesMap.get(key).num * 1.0 / times;// 真实随机概率
			assertTrue(Math.abs(actual - expected) < d);// 检验随机误差是否在误差范围内
		});
	}

	private static class Times {
		int num;
		double weight;

		public Times(double weight) {
			this.weight = weight;
		}
	}
}
