package cn.hutool.core.bean;


import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * https://github.com/dromara/hutool/issues/2697
 */
public class Issue2697Test {

	@Test
	public void mapToMapTest(){
		Map<String, String> mapA = new HashMap<>(16);
		mapA.put("12", "21");
		mapA.put("121", "21");
		mapA.put("122", "21");
		Map<String, String> mapB = new HashMap<>(16);
		BeanUtil.copyProperties(mapA, mapB, "12");
		System.out.println(mapB);
	}
}
