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
import org.dromara.hutool.json.*;
import org.dromara.hutool.json.serializer.JSONDeserializer;

import java.lang.reflect.Type;

/**
 * 默认反序列化器，用于处理未匹配的JSON类型。
 *
 * @author looly
 * @since 6.0.0
 */
public class DefaultDeserializer implements JSONDeserializer<Object> {
	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		// 当目标类型不确定时，返回原JSON
		final Class<?> rawType = TypeUtil.getClass(deserializeType);
		if (null == rawType || JSON.class.isAssignableFrom(rawType)) {
			return json;
		}

		if (json instanceof JSONObject) {
			return fromJSONObject((JSONObject) json, deserializeType, rawType);
		} else if (json instanceof JSONArray) {
			return fromJSONArray((JSONArray) json, deserializeType, rawType);
		} else if (json instanceof JSONPrimitive) {
			return fromJSONPrimitive((JSONPrimitive) json, deserializeType, rawType);
		}
		throw new JSONException("Unsupported JSON type: {}", json.getClass());
	}

	private Object fromJSONObject(final JSONObject json, final Type deserializeType, final Class<?> rawType) {
		return json;
	}

	private Object fromJSONArray(final JSONArray json, final Type deserializeType, final Class<?> rawType) {
		return json;
	}

	private Object fromJSONPrimitive(final JSONPrimitive json, final Type deserializeType, final Class<?> rawType) {
		return json;
	}
}
