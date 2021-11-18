package cn.hutool.cache;

import cn.hutool.cache.impl.WeakCache;
import org.junit.Assert;
import org.junit.Test;

public class WeakCacheTest {

	@Test
	public void removeTest(){
		final WeakCache<String, String> cache = new WeakCache<>(-1);
		cache.put("abc", "123");
		cache.put("def", "456");

		Assert.assertEquals(2, cache.size());

		cache.remove("abc");

		Assert.assertEquals(1, cache.size());
	}
}
