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

package org.dromara.hutool.core.collection.iter;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Enumeration}对象转{@link Iterator}对象
 * @author Looly
 *
 * @param <E> 元素类型
 * @since 4.1.1
 */
public class EnumerationIter<E> implements IterableIter<E>, Serializable{
	private static final long serialVersionUID = 1L;

	private final Enumeration<E> e;

	/**
	 * 构造
	 * @param enumeration {@link Enumeration}对象
	 */
	public EnumerationIter(final Enumeration<E> enumeration) {
		this.e = enumeration;
	}

	@Override
	public boolean hasNext() {
		return e.hasMoreElements();
	}

	@Override
	public E next() {
		return e.nextElement();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
