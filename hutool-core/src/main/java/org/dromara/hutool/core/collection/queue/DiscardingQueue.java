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

import java.util.AbstractQueue;
import java.util.Collections;
import java.util.Iterator;
import java.util.Queue;

/**
 * 始终为空的队列，所有新增节点都丢弃
 *
 * @author Guava, looly
 * @since 6.0.0
 */
public class DiscardingQueue extends AbstractQueue<Object> {
	private static final DiscardingQueue INSTANCE = new DiscardingQueue();

	/**
	 * 获取单例的空队列
	 * @return DiscardingQueue
	 * @param <E> 节点类型
	 */
	@SuppressWarnings("unchecked")
	public static <E> Queue<E> getInstance() {
		return (Queue<E>) INSTANCE;
	}

	@Override
	public boolean add(final Object e) {
		return true;
	}

	@Override
	public boolean offer(final Object e) {
		return true;
	}

	@Override
	public Object poll() {
		return null;
	}

	@Override
	public Object peek() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public Iterator<Object> iterator() {
		return Collections.emptyIterator();
	}
}
