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

import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.util.EnumUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONPrimitive;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;

/**
 * 枚举类型适配器，将枚举转换为字符串，反序列化时将字符串转为枚举对象
 *
 * @author Looly
 */
public class EnumTypeAdapter implements MatcherJSONSerializer<Object>, MatcherJSONDeserializer<Object> {

	/**
	 * 单例
	 */
	public static final EnumTypeAdapter INSTANCE = new EnumTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return EnumUtil.isEnum(bean);
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return EnumUtil.isEnum(deserializeType);
	}

	@Override
	public JSON serialize(final Object bean, final JSONContext context) {
		return new JSONPrimitive(((Enum<?>) bean).name(),
			ObjUtil.apply(context, JSONContext::config));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		return EnumUtil.fromString((Class) TypeUtil.getClass(deserializeType), (String) json.asJSONPrimitive().getValue());
	}
}
