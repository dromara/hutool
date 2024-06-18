package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.cache.impl.FIFOCache;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3618Test {
	@Test
	public void putTest() {
		final FIFOCache<Object, Object> cache = CacheUtil.newFIFOCache(3);
		cache.put(1, 1);
		cache.put(2, 1);
		cache.put(3, 1);

		assertEquals(3, cache.size());

		// issue#3618 对于替换的键值对，不做满队列检查和清除
		cache.put(3, 2);

		assertEquals(3, cache.size());
	}
}
