package cn.hutool.cache;

import cn.hutool.cache.impl.WeakCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WeakCacheTest {

	@Test
	public void removeTest(){
		final WeakCache<String, String> cache = new WeakCache<>(-1);
		cache.put("abc", "123");
		cache.put("def", "456");

		Assertions.assertEquals(2, cache.size());

		cache.remove("abc");

		Assertions.assertEquals(1, cache.size());
	}
}
