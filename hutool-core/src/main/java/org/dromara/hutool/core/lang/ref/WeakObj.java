/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.lang.ref;

import org.dromara.hutool.core.util.ObjUtil;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * 弱引用对象，在GC时发现弱引用会回收其对象
 *
 * @param <T> 键类型
 */
public class WeakObj<T> extends WeakReference<T> implements Ref<T>{
	private final int hashCode;

	/**
	 * 构造
	 *
	 * @param obj   原始对象
	 * @param queue {@link ReferenceQueue}
	 */
	public WeakObj(final T obj, final ReferenceQueue<? super T> queue) {
		super(obj, queue);
		hashCode = Objects.hashCode(obj);
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(final Object other) {
		if (other == this) {
			return true;
		} else if (other instanceof WeakObj) {
			return ObjUtil.equals(((WeakObj<?>) other).get(), get());
		}
		return false;
	}
}
