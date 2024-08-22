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

package org.dromara.hutool.core.collection.iter;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Iterator}对象转{@link Enumeration}
 * @author Looly
 *
 * @param <E> 元素类型
 * @since 3.0.8
 */
public class IteratorEnumeration<E> implements Enumeration<E>, Serializable{
	private static final long serialVersionUID = 1L;

	private final Iterator<E> iterator;



	/**
	 * 构造
	 * @param iterator {@link Iterator}对象
	 */
	public IteratorEnumeration(final Iterator<E> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasMoreElements() {
		return iterator.hasNext();
	}

	@Override
	public E nextElement() {
		return iterator.next();
	}

}
