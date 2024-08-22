/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.cache.file;

import org.dromara.hutool.core.cache.Cache;
import org.dromara.hutool.core.cache.impl.LFUCache;

import java.io.File;

/**
 *  使用LFU缓存文件，以解决频繁读取文件引起的性能问题
 * @author Looly
 *
 */
public class LFUFileCache extends AbstractFileCache{
	private static final long serialVersionUID = 1L;

	/**
	 * 构造<br>
	 * 最大文件大小为缓存容量的一半<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 */
	public LFUFileCache(final int capacity) {
		this(capacity, capacity / 2, 0);
	}

	/**
	 * 构造<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 * @param maxFileSize 最大文件大小
	 */
	public LFUFileCache(final int capacity, final int maxFileSize) {
		this(capacity, maxFileSize, 0);
	}

	/**
	 * 构造
	 * @param capacity 缓存容量
	 * @param maxFileSize 文件最大大小
	 * @param timeout 默认超时时间，0表示无默认超时
	 */
	public LFUFileCache(final int capacity, final int maxFileSize, final long timeout) {
		super(capacity, maxFileSize, timeout);
	}

	@Override
	protected Cache<File, byte[]> initCache() {
		return new LFUCache<File, byte[]>(LFUFileCache.this.capacity, LFUFileCache.this.timeout) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isFull() {
				return LFUFileCache.this.usedSize > this.capacity;
			}

			@Override
			protected void onRemove(final File key, final byte[] cachedObject) {
				usedSize -= cachedObject.length;
			}
		};
	}

}
