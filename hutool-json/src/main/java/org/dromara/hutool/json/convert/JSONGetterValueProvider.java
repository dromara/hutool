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

package org.dromara.hutool.json.convert;

import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.json.JSONGetter;

import java.lang.reflect.Type;

/**
 * JSONGetter的ValueProvider
 *
 * @param <K> 键类型
 * @author looly
 */
public class JSONGetterValueProvider<K> implements ValueProvider<K> {

	private final JSONGetter<K> jsonGetter;

	/**
	 * 构造
	 *
	 * @param jsonGetter {@link JSONGetter}
	 */
	public JSONGetterValueProvider(final JSONGetter<K> jsonGetter) {
		this.jsonGetter = jsonGetter;
	}

	@Override
	public Object value(final K key, final Type valueType) {
		return jsonGetter.get(key, valueType);
	}

	@Override
	public boolean containsKey(final K key) {
		return !jsonGetter.isNull(key);
	}
}
