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

package org.dromara.hutool.core.lang.intern;

import org.dromara.hutool.core.map.reference.WeakConcurrentMap;

import java.lang.ref.WeakReference;

/**
 * 使用WeakHashMap(线程安全)存储对象的规范化对象，注意此对象需单例使用！<br>
 *
 * @param <T> key 类型
 * @author looly
 * @since 5.4.3
 */
public class WeakIntern<T> implements Intern<T> {

	private final WeakConcurrentMap<T, WeakReference<T>> cache = new WeakConcurrentMap<>();

	@Override
	public T intern(final T sample) {
		if(null == sample){
			return null;
		}
		T val;
		// 循环避免刚创建就被回收的情况
		do {
			val = this.cache.computeIfAbsent(sample, WeakReference::new).get();
		} while (val == null);
		return val;
	}
}
