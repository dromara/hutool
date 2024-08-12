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
