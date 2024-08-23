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

package org.dromara.hutool.core.bean.copier.provider;

import org.dromara.hutool.core.bean.copier.ValueProvider;
import org.dromara.hutool.core.convert.ConvertUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Map值提供者
 *
 * @author looly
 */
@SuppressWarnings("rawtypes")
public class MapValueProvider implements ValueProvider<String> {

	private final Map map;

	/**
	 * 构造
	 *
	 * @param map map
	 */
	public MapValueProvider(final Map map) {
		this.map = map;
	}

	@Override
	public Object value(final String key, final Type valueType) {
		return ConvertUtil.convert(valueType, map.get(key));
	}

	@Override
	public boolean containsKey(final String key) {
		return map.containsKey(key);
	}
}
