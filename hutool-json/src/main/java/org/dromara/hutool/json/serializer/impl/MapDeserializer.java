/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.serializer.impl;

import org.dromara.hutool.core.convert.CompositeConverter;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map反序列化器，用于将JSON对象转换为Map对象。
 *
 * @author looly
 * @since 6.0.0
 */
public class MapDeserializer implements MatcherJSONDeserializer<Map<?, ?>> {

	/**
	 * 单例
	 */
	public static final MapDeserializer INSTANCE = new MapDeserializer();

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		if(json instanceof JSONObject){
			final Class<?> rawType = TypeUtil.getClass(deserializeType);
			return Map.class.isAssignableFrom(rawType);
		}
		return false;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public Map<?, ?> deserialize(final JSON json, final Type deserializeType) {
		final Map map = MapUtil.createMap(TypeUtil.getClass(deserializeType), LinkedHashMap::new);
		final Type keyType = TypeUtil.getTypeArgument(deserializeType, 0);
		final Type valueType = TypeUtil.getTypeArgument(deserializeType, 1);

		for (final Map.Entry<String, JSON> entry : (JSONObject) json) {
			map.put(
				// key类型为String转目标类型，使用标准转换器
				CompositeConverter.getInstance().convert(keyType, entry.getKey()),
				entry.getValue().toBean(valueType)
			);
		}
		return map;
	}
}
