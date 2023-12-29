/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
