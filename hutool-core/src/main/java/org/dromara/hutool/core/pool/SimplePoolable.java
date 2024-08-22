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

package org.dromara.hutool.core.pool;

/**
 * 简单可池化对象，此对象会同时持有原始对象和所在的分区
 *
 * @param <T> 对象类型
 */
public class SimplePoolable<T> implements Poolable<T> {

	private final T raw;
	private long lastReturn;

	/**
	 * 构造
	 *
	 * @param raw       原始对象
	 */
	public SimplePoolable(final T raw) {
		this.raw = raw;
		this.lastReturn = System.currentTimeMillis();
	}

	@Override
	public T getRaw() {
		return this.raw;
	}

	@Override
	public long getLastReturn() {
		return lastReturn;
	}

	@Override
	public void setLastReturn(final long lastReturn) {
		this.lastReturn = lastReturn;
	}
}
