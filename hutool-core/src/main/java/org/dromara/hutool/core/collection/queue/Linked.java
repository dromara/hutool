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

package org.dromara.hutool.core.collection.queue;

import java.util.Deque;

/**
 * An element that is linked on the {@link Deque}.
 *
 * @param <T> 值类型
 */
public interface Linked<T extends Linked<T>> {

	/**
	 * Retrieves the previous element or {@code null} if either the element is
	 * unlinked or the first element on the deque.
	 *
	 * @return 前一个值
	 */
	T getPrevious();

	/**
	 * Sets the previous element or {@code null} if there is no link.
	 *
	 * @param prev 前一个值
	 */
	void setPrevious(T prev);

	/**
	 * Retrieves the next element or {@code null} if either the element is
	 * unlinked or the last element on the deque.
	 *
	 * @return 下一个值
	 */
	T getNext();

	/**
	 * Sets the next element or {@code null} if there is no link.
	 *
	 * @param next 下一个值
	 */
	void setNext(T next);
}
