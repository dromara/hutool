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

import org.dromara.hutool.core.convert.impl.CalendarConverter;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONPrimitive;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;
import java.util.Calendar;

/**
 * 日期类型适配器，用于将日期对象转换为给定格式或时间戳
 *
 * @author looly
 * @since 6.0.0
 */
public class CalendarTypeAdapter implements MatcherJSONSerializer<Calendar>, MatcherJSONDeserializer<Calendar> {

	/**
	 * 单例
	 */
	public static final CalendarTypeAdapter INSTANCE = new CalendarTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof Calendar;
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return Calendar.class.isAssignableFrom(TypeUtil.getClass(deserializeType));
	}

	@Override
	public JSON serialize(final Calendar bean, final JSONContext context) {
		final JSONConfig config = ObjUtil.apply(context, JSONContext::config);
		final String format = ObjUtil.apply(config, JSONConfig::getDateFormat);
		return new JSONPrimitive(
			null == format
				? bean.getTimeInMillis()
				: DateUtil.format(DateUtil.date(bean), format), config);
	}

	@Override
	public Calendar deserialize(final JSON json, final Type deserializeType) {
		final JSONConfig config = json.config();
		final String format = ObjUtil.apply(config, JSONConfig::getDateFormat);
		return (Calendar) new CalendarConverter(format).convert(deserializeType, json.asJSONPrimitive().getValue());
	}
}
