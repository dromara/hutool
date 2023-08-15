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

import org.dromara.hutool.core.text.StrJoiner;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.array.ArrayUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Map拼接器，可以拼接包括Map、Entry列表等。
 *
 * @author looly
 */
public class MapJoiner {

	private final StrJoiner joiner;
	private final String keyValueSeparator;

	/**
	 * 构建一个MapJoiner
	 *
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @return MapJoiner
	 */
	public static MapJoiner of(final String separator, final String keyValueSeparator) {
		return of(StrJoiner.of(separator), keyValueSeparator);
	}

	/**
	 * 构建一个MapJoiner
	 *
	 * @param joiner            entry之间的Joiner
	 * @param keyValueSeparator kv之间的连接符
	 * @return MapJoiner
	 */
	public static MapJoiner of(final StrJoiner joiner, final String keyValueSeparator) {
		return new MapJoiner(joiner, keyValueSeparator);
	}

	/**
	 * 构造
	 *
	 * @param joiner            entry之间的Joiner
	 * @param keyValueSeparator kv之间的连接符
	 */
	public MapJoiner(final StrJoiner joiner, final String keyValueSeparator) {
		this.joiner = joiner;
		this.keyValueSeparator = keyValueSeparator;
	}

	/**
	 * 追加Map
	 *
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @param map       Map
	 * @param predicate Map过滤器
	 * @return this
	 */
	public <K, V> MapJoiner append(final Map<K, V> map, final Predicate<Map.Entry<K, V>> predicate) {
		return append(map.entrySet().iterator(), predicate);
	}

	/**
	 * 追加Entry列表
	 *
	 * @param <K>       键类型
	 * @param <V>       值类型
	 * @param parts     Entry列表
	 * @param predicate Map过滤器
	 * @return this
	 */
	public <K, V> MapJoiner append(final Iterator<? extends Map.Entry<K, V>> parts, final Predicate<Map.Entry<K, V>> predicate) {
		if (null == parts) {
			return this;
		}

		Map.Entry<K, V> entry;
		while (parts.hasNext()) {
			entry = parts.next();
			if (null == predicate || predicate.test(entry)) {
				joiner.append(StrJoiner.of(this.keyValueSeparator).append(entry.getKey()).append(entry.getValue()));
			}
		}

		return this;
	}

	/**
	 * 追加其他字符串，其他字符串简单拼接
	 *
	 * @param params 字符串列表
	 * @return this
	 */
	public MapJoiner append(final String... params) {
		if(ArrayUtil.isNotEmpty(params)){
			joiner.append(StrUtil.concat(false, params));
		}
		return this;
	}

	@Override
	public String toString() {
		return joiner.toString();
	}
}
