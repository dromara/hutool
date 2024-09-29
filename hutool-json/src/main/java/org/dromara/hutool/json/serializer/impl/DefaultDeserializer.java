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

	/**
	 * 单例
	 */
	public static final DefaultDeserializer INSTANCE = new DefaultDeserializer();

	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		// 当目标类型不确定时，返回原JSON
		final Class<?> rawType = TypeUtil.getClass(deserializeType);
		if (null == rawType || Object.class == rawType || rawType.isAssignableFrom(json.getClass())) {
			return json;
		}

		// JSON类型之间互转
		if(json instanceof JSONPrimitive && JSON.class.isAssignableFrom(rawType)){
			final Object value = json.asJSONPrimitive().getValue();
			if(value instanceof CharSequence){
				// JSON字符串转JSON
				return JSONUtil.parse(value, json.config());
			}
		} else if(json instanceof JSONObject && JSONArray.class == rawType){
			return JSONUtil.parseArray(json, json.config());
		}

		throw new JSONException("Unsupported type {} to {}", json.getClass(), deserializeType.getTypeName());
	}
}
