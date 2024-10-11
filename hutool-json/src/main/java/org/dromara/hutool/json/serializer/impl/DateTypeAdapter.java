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

import org.dromara.hutool.core.convert.impl.DateConverter;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.format.DateFormatManager;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONPrimitive;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * 日期类型适配器，用于将日期对象转换为给定格式或时间戳
 *
 * @author looly
 * @since 6.0.0
 */
public class DateTypeAdapter implements MatcherJSONSerializer<Date>, MatcherJSONDeserializer<Date> {

	/**
	 * 单例
	 */
	public static final DateTypeAdapter INSTANCE = new DateTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof Date;
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return Date.class.isAssignableFrom(TypeUtil.getClass(deserializeType));
	}

	@Override
	public JSON serialize(final Date bean, final JSONContext context) {
		final JSONConfig config = context.config();
		final String format = ObjUtil.apply(config, JSONConfig::getDateFormat);

		final Object value;
		// 默认为时间戳
		if(null == format || DateFormatManager.FORMAT_MILLISECONDS.equals(format)){
			value = bean.getTime();
		} else if(DateFormatManager.FORMAT_SECONDS.equals(format)){
			value = Math.floorDiv(bean.getTime(), 1000L);
		} else {
			value = DateUtil.format(bean, format);
		}

		return new JSONPrimitive(value, config);
	}

	@Override
	public Date deserialize(final JSON json, final Type deserializeType) {
		final JSONConfig config = json.config();
		final String format = ObjUtil.apply(config, JSONConfig::getDateFormat);
		return (Date) new DateConverter(format).convert(deserializeType, json.asJSONPrimitive().getValue());
	}
}
