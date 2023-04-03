package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.cache.file.LFUFileCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 文件缓存单元测试
 * @author looly
 *
 */
public class FileCacheTest {
	@Test
	public void lfuFileCacheTest() {
		final LFUFileCache cache = new LFUFileCache(1000, 500, 2000);
		Assertions.assertNotNull(cache);
	}
}
