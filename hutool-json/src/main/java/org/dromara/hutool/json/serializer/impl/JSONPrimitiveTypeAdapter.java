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

import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONPrimitive;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;

/**
 * {@link JSONPrimitive}相关类型适配器，用于处理数字类型的序列化和反序列化
 *
 * @author Looly
 * @since 6.0.0
 */
public class JSONPrimitiveTypeAdapter implements MatcherJSONSerializer<Object>, MatcherJSONDeserializer<Object> {

	/**
	 * 单例
	 */
	public static final JSONPrimitiveTypeAdapter INSTANCE = new JSONPrimitiveTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return JSONPrimitive.isTypeForJSONPrimitive(bean);
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return json instanceof JSONPrimitive && JSONPrimitive.isTypeForJSONPrimitive(TypeUtil.getClass(deserializeType));
	}

	@Override
	public JSON serialize(Object bean, final JSONContext context) {
		if(bean instanceof Character){
			// 字符按照字符串存储
			bean = bean.toString();
		}

		return context.getOrCreatePrimitive(bean);
	}

	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		final Object value = json.asJSONPrimitive().getValue();


		if (null != value && TypeUtil.getClass(deserializeType).isAssignableFrom(value.getClass())) {
			return value;
		}

		return ConvertUtil.convertWithCheck(deserializeType, value, null,
			ObjUtil.apply(json.config(), JSONConfig::isIgnoreError));
	}
}
