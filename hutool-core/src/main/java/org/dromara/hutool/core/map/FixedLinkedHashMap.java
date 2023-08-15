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

package org.dromara.hutool.core.map;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 固定大小的{@link LinkedHashMap} 实现<br>
 * 注意此类非线程安全，由于{@link #get(Object)}操作会修改链表的顺序结构，因此也不可以使用读写锁。
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 */
public class FixedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = -629171177321416095L;

	/**
	 * 容量，超过此容量自动删除末尾元素
	 */
	private int capacity;
	/**
	 * 移除监听
	 */
	private Consumer<java.util.Map.Entry<K, V>> removeListener;

	/**
	 * 构造
	 *
	 * @param capacity 容量，实际初始容量比容量大1
	 */
	public FixedLinkedHashMap(final int capacity) {
		super(capacity + 1, 1.0f, true);
		this.capacity = capacity;
	}

	/**
	 * 获取容量
	 *
	 * @return 容量
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * 设置容量
	 *
	 * @param capacity 容量
	 */
	public void setCapacity(final int capacity) {
		this.capacity = capacity;
	}

	/**
	 * 设置自定义移除监听
	 *
	 * @param removeListener 移除监听
	 */
	public void setRemoveListener(final Consumer<Map.Entry<K, V>> removeListener) {
		this.removeListener = removeListener;
	}

	@Override
	protected boolean removeEldestEntry(final java.util.Map.Entry<K, V> eldest) {
		//当链表元素大于容量时，移除最老（最久未被使用）的元素
		if (size() > this.capacity) {
			if (null != removeListener) {
				// 自定义监听
				removeListener.accept(eldest);
			}
			return true;
		}
		return false;
	}
}
