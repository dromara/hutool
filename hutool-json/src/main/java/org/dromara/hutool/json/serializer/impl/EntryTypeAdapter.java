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
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Map.Entry序列化和反序列化器，用于将JSON对象和Map.Entry对象互转。
 *
 * @author looly
 * @since 6.0.0
 */
public class EntryTypeAdapter implements MatcherJSONSerializer<Map.Entry<?, ?>>, MatcherJSONDeserializer<Map.Entry<?, ?>> {

	/**
	 * 单例
	 */
	public static final EntryTypeAdapter INSTANCE = new EntryTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof Map.Entry;
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		if(json instanceof JSONObject){
			final Class<?> rawType = TypeUtil.getClass(deserializeType);
			return Map.Entry.class.isAssignableFrom(rawType);
		}
		return false;
	}

	@Override
	public JSON serialize(final Map.Entry<?, ?> bean, final JSONContext context) {
		return context.getOrCreateObj()
			.putValue(ConvertUtil.toStr(bean.getKey()), bean.getValue());
	}

	@Override
	public Map.Entry<?, ?> deserialize(final JSON json, final Type deserializeType) {
		final Type keyType = TypeUtil.getTypeArgument(deserializeType, 0);
		final Type valueType = TypeUtil.getTypeArgument(deserializeType, 1);

		return toEntry(deserializeType, keyType, valueType, (JSONObject) json);
	}

	/**
	 * Map转Entry
	 *
	 * @param targetType 目标的Map类型
	 * @param keyType    键类型
	 * @param valueType  值类型
	 * @param json        JSONObject
	 * @return Entry
	 */
	private Map.Entry<?, ?> toEntry(final Type targetType, final Type keyType,
									final Type valueType, final JSONObject json) {
		final String key;
		final JSON value;
		if (1 == json.size()) {
			final Map.Entry<String, JSON> entry = json.iterator().next();
			key = entry.getKey();
			value = entry.getValue();
		} else {
			// 忽略Map中其它属性
			key = json.getStr("key");
			value = json.get("value");
		}

		return (Map.Entry<?, ?>) ConstructorUtil.newInstance(TypeUtil.getClass(targetType),
			TypeUtil.isUnknown(keyType) ? key : CompositeConverter.getInstance().convert(keyType, key),
			TypeUtil.isUnknown(valueType) ? value : value.toBean(valueType)
		);
	}
}
