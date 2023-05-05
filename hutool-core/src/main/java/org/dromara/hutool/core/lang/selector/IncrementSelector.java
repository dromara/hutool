/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang.selector;

import java.util.ArrayList;

/**
 * 简单的轮询选择器
 *
 * @param <T> 元素类型
 * @author Looly
 * @since 6.0.0
 */
public class IncrementSelector<T> extends ArrayList<T> implements Selector<T> {
	private static final long serialVersionUID = 1L;

	private int position;

	// region ----- Constructors

	/**
	 * 构造
	 */
	public IncrementSelector() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param objList 对象列表
	 */
	public IncrementSelector(final Iterable<T> objList) {
		this();
		for (final T obj : objList) {
			add(obj);
		}
	}
	// endregion

	@Override
	public T select() {
		final T result = get(position);
		if(position >= size()){
			position = 0;
		}
		return result;
	}
}
