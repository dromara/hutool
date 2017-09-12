package com.xiaoleilu.hutool.cache.test;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.cache.file.LFUFileCache;

public class FileCacheTest {
	@Test
	public void lfuFileCacheTest() {
		LFUFileCache cache = new LFUFileCache(1000, 500, 2000);
		Assert.assertNotNull(cache);
	}
}
