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

package org.dromara.hutool.map;

import java.util.Map;

/**
 * 自定义键的Map，默认HashMap实现
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author Looly
 * @since 4.0.7
 */
public abstract class CustomKeyMap<K, V> extends TransMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * 构造<br>
	 * 通过传入一个Map从而确定Map的类型，子类需创建一个空的Map，而非传入一个已有Map，否则值可能会被修改
	 *
	 * @param emptyMap Map 被包装的Map，必须为空Map，否则自定义key会无效
	 * @since 3.1.2
	 */
	public CustomKeyMap(final Map<K, V> emptyMap) {
		super(emptyMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected V customValue(final Object value) {
		return (V)value;
	}
}
