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
