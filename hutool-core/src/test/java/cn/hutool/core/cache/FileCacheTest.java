package cn.hutool.core.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cn.hutool.core.cache.file.LFUFileCache;

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
