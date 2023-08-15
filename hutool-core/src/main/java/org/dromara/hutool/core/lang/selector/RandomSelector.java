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

package org.dromara.hutool.core.lang.selector;

import org.dromara.hutool.core.util.RandomUtil;

import java.util.ArrayList;

/**
 * 随机选择器
 *
 * @param <T> 元素类型
 * @author Looly
 * @since 6.0.0
 */
public class RandomSelector<T> extends ArrayList<T> implements Selector<T> {
	private static final long serialVersionUID = 1L;

	// region ----- Constructors

	/**
	 * 构造
	 */
	public RandomSelector() {
		super();
	}

	/**
	 * 构造
	 *
	 * @param objList 对象列表
	 */
	public RandomSelector(final Iterable<T> objList) {
		this();
		for (final T obj : objList) {
			add(obj);
		}
	}
	// endregion

	@Override
	public T select() {
		return get(RandomUtil.randomInt(0, size()));
	}
}
