package org.dromara.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;

import java.util.concurrent.ConcurrentHashMap;

public class Issue2349Test {

	@Test
	@EnabledForJreRange(max = org.junit.jupiter.api.condition.JRE.JAVA_8)
	public void computeIfAbsentTest() {
		// https://blog.csdn.net/xiaochao_bos/article/details/103789991
		// 使用ConcurrentHashMap会造成死循环
		// SafeConcurrentHashMap用于修复此问题
		final ConcurrentHashMap<String, Integer> map = new SafeConcurrentHashMap<>(16);
		map.computeIfAbsent("AaAa", key -> map.computeIfAbsent("BBBB", key2 -> 42));

		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals(Integer.valueOf(42), map.get("AaAa"));
		Assertions.assertEquals(Integer.valueOf(42), map.get("BBBB"));
	}

	@Test
	@EnabledForJreRange(min = org.junit.jupiter.api.condition.JRE.JAVA_9)
	public void issue11986ForJava17Test() {
		// https://github.com/apache/dubbo/issues/11986
		final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

		// JDK9+ has been resolved JDK-8161372 bug, when cause dead then throw IllegalStateException
		Assertions.assertThrows(IllegalStateException.class, () -> {
			map.computeIfAbsent("AaAa", key -> map.computeIfAbsent("BBBB", key2 -> 42));
		});
	}
}
