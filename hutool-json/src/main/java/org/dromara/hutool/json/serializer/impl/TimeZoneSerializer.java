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
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONPrimitive;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;
import java.util.TimeZone;

/**
 * 时区序列化器
 *
 * @author looly
 * @since 6.0.0
 */
public class TimeZoneSerializer implements MatcherJSONSerializer<TimeZone>, MatcherJSONDeserializer<TimeZone> {

	/**
	 * 单例
	 */
	public static final TimeZoneSerializer INSTANCE = new TimeZoneSerializer();

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return TimeZone.class.isAssignableFrom(TypeUtil.getClass(deserializeType));
	}

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof TimeZone;
	}

	@Override
	public JSON serialize(final TimeZone bean, final JSONContext context) {
		return new JSONPrimitive(bean.getID());
	}

	@Override
	public TimeZone deserialize(final JSON json, final Type deserializeType) {
		return TimeZone.getTimeZone(json.toString());
	}
}
