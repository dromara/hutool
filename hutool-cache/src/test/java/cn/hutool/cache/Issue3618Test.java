package cn.hutool.cache;

import cn.hutool.cache.impl.FIFOCache;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Issue3618Test {
	@Test
	public void putTest() {
		FIFOCache<Object, Object> cache = CacheUtil.newFIFOCache(3);
		cache.put(1, 1);
		cache.put(2, 1);
		cache.put(3, 1);

		assertEquals(3, cache.size());

		// issue#3618 对于替换的键值对，不做满队列检查和清除
		cache.put(3, 2);

		assertEquals(3, cache.size());
	}
}
