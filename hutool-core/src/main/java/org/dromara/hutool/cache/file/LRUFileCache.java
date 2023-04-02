/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.cache.file;

import org.dromara.hutool.cache.Cache;
import org.dromara.hutool.cache.impl.LRUCache;

import java.io.File;

/**
 *  使用LRU缓存文件，以解决频繁读取文件引起的性能问题
 * @author Looly
 *
 */
public class LRUFileCache extends AbstractFileCache{
	private static final long serialVersionUID = 1L;

	/**
	 * 构造<br>
	 * 最大文件大小为缓存容量的一半<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 */
	public LRUFileCache(final int capacity) {
		this(capacity, capacity / 2, 0);
	}

	/**
	 * 构造<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 * @param maxFileSize 最大文件大小
	 */
	public LRUFileCache(final int capacity, final int maxFileSize) {
		this(capacity, maxFileSize, 0);
	}

	/**
	 * 构造
	 * @param capacity 缓存容量
	 * @param maxFileSize 文件最大大小
	 * @param timeout 默认超时时间，0表示无默认超时
	 */
	public LRUFileCache(final int capacity, final int maxFileSize, final long timeout) {
		super(capacity, maxFileSize, timeout);
	}

	@Override
	protected Cache<File, byte[]> initCache() {
		return new LRUCache<File, byte[]>(LRUFileCache.this.capacity, super.timeout) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isFull() {
				return LRUFileCache.this.usedSize > this.capacity;
			}

			@Override
			protected void onRemove(final File key, final byte[] cachedObject) {
				usedSize -= cachedObject.length;
			}
		};
	}

}
