package cn.hutool.core.map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

public class Issue2349Test {

	@Test
	public void computeIfAbsentTest(){
		// https://blog.csdn.net/xiaochao_bos/article/details/103789991
		// 使用ConcurrentHashMap会造成死循环
		// SafeConcurrentHashMap用于修复此问题
		final ConcurrentHashMap<String,Integer> map=new SafeConcurrentHashMap<>(16);
		map.computeIfAbsent("AaAa", key->map.computeIfAbsent("BBBB",key2->42));

		Assertions.assertEquals(2, map.size());
		Assertions.assertEquals(Integer.valueOf(42), map.get("AaAa"));
		Assertions.assertEquals(Integer.valueOf(42), map.get("BBBB"));
	}
}
