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

package org.dromara.hutool.json.serializer.impl;

import org.dromara.hutool.core.convert.impl.TemporalAccessorConverter;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.date.format.GlobalCustomFormat;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.math.NumberUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.*;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;
import java.time.*;
import java.time.temporal.TemporalAccessor;

/**
 * {@link TemporalAccessor}的JSON自定义序列化实现，支持包括：<br>
 * <ul>
 *     <li>LocalDate</li>
 *     <li>LocalDateTime</li>
 *     <li>LocalTime</li>
 * </ul>
 *
 * @author looly
 */
public class TemporalTypeAdapter implements MatcherJSONSerializer<TemporalAccessor>, MatcherJSONDeserializer<TemporalAccessor> {

	private static final String YEAR_KEY = "year";
	private static final String MONTH_KEY = "month";
	private static final String DAY_KEY = "day";
	private static final String HOUR_KEY = "hour";
	private static final String MINUTE_KEY = "minute";
	private static final String SECOND_KEY = "second";
	private static final String NANO_KEY = "nano";

	/**
	 * 单例
	 */
	public static final TemporalTypeAdapter INSTANCE = new TemporalTypeAdapter();

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		return TemporalAccessor.class.isAssignableFrom(TypeUtil.getClass(deserializeType));
	}

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		return bean instanceof TemporalAccessor;
	}

	@Override
	public JSON serialize(final TemporalAccessor bean, final JSONContext context) {
		// 如果上下文为JSONObject，转为键值对形式
		final JSON contextJson = context.getContextJson();
		if (contextJson instanceof JSONObject) {
			toJSONObject(bean, contextJson.asJSONObject());
			return contextJson;
		}

		if (bean instanceof Month) {
			return context.getOrCreatePrimitive(((Month) bean).getValue());
		} else if (bean instanceof DayOfWeek) {
			return context.getOrCreatePrimitive(((DayOfWeek) bean).getValue());
		} else if (bean instanceof MonthDay) {
			return context.getOrCreatePrimitive(((MonthDay) bean).toString());
		}

		final String format = ObjUtil.apply(context.config(), JSONConfig::getDateFormat);

		final Object value;
		// 默认为时间戳
		if (null == format || GlobalCustomFormat.FORMAT_MILLISECONDS.equals(format)) {
			value = TimeUtil.toEpochMilli(bean);
		} else if (GlobalCustomFormat.FORMAT_SECONDS.equals(format)) {
			value = Math.floorDiv(TimeUtil.toEpochMilli(bean), 1000L);
		} else {
			value = TimeUtil.format(bean, format);
		}

		return context.getOrCreatePrimitive(value);
	}

	@Override
	public TemporalAccessor deserialize(final JSON json, final Type deserializeType) {
		// JSONPrimitive
		if (json instanceof JSONPrimitive) {
			final Object value = ((JSONPrimitive) json).getValue();
			final TemporalAccessorConverter converter = new TemporalAccessorConverter(
				Opt.ofNullable(json.config()).map(JSONConfig::getDateFormat).getOrNull());
			return (TemporalAccessor) converter.convert(deserializeType, value);
		}

		final Class<?> temporalAccessorClass = TypeUtil.getClass(deserializeType);
		if (json instanceof JSONObject) {
			return fromJSONObject(temporalAccessorClass, json.asJSONObject());
		}

		if (Month.class.equals(temporalAccessorClass)) {
			return Month.of((Integer) json.asJSONPrimitive().getValue());
		} else if (DayOfWeek.class.equals(temporalAccessorClass)) {
			return DayOfWeek.of((Integer) json.asJSONPrimitive().getValue());
		} else if (MonthDay.class.equals(temporalAccessorClass)) {
			return MonthDay.parse((CharSequence) json.asJSONPrimitive().getValue());
		}

		throw new JSONException("Unsupported type from JSON {} to {}", json, deserializeType);
	}

	/**
	 * 将{@link TemporalAccessor}转换为JSONObject
	 *
	 * @param bean {@link TemporalAccessor}
	 * @param json JSONObject
	 */
	private static void toJSONObject(final TemporalAccessor bean, final JSONObject json) {
		if (bean instanceof LocalDate) {
			final LocalDate localDate = (LocalDate) bean;
			json.set(YEAR_KEY, localDate.getYear());
			json.set(MONTH_KEY, localDate.getMonthValue());
			json.set(DAY_KEY, localDate.getDayOfMonth());
		} else if (bean instanceof LocalDateTime) {
			final LocalDateTime localDateTime = (LocalDateTime) bean;
			json.set(YEAR_KEY, localDateTime.getYear());
			json.set(MONTH_KEY, localDateTime.getMonthValue());
			json.set(DAY_KEY, localDateTime.getDayOfMonth());
			json.set(HOUR_KEY, localDateTime.getHour());
			json.set(MINUTE_KEY, localDateTime.getMinute());
			json.set(SECOND_KEY, localDateTime.getSecond());
			json.set(NANO_KEY, localDateTime.getNano());
		} else if (bean instanceof LocalTime) {
			final LocalTime localTime = (LocalTime) bean;
			json.set(HOUR_KEY, localTime.getHour());
			json.set(MINUTE_KEY, localTime.getMinute());
			json.set(SECOND_KEY, localTime.getSecond());
			json.set(NANO_KEY, localTime.getNano());
		}
		throw new JSONException("Unsupported type {}.", bean.getClass().getName());
	}

	/**
	 * 从JSONObject中获取时间信息，转换为{@link TemporalAccessor}
	 *
	 * @param temporalAccessorClass 目标时间类型
	 * @param jsonObject            JSONObject
	 * @return {@link TemporalAccessor}
	 */
	private static TemporalAccessor fromJSONObject(final Class<?> temporalAccessorClass, final JSONObject jsonObject) {
		if (LocalDate.class.equals(temporalAccessorClass) || LocalDateTime.class.equals(temporalAccessorClass)) {
			// 年
			final Integer year = jsonObject.getInt(YEAR_KEY);
			Assert.notNull(year, "Field 'year' must be not null");

			// 月
			final String monthStr = jsonObject.getStr(MONTH_KEY);
			Assert.notNull(monthStr, "Field 'month' must be not null");
			Integer month = NumberUtil.parseInt(monthStr, null);
			if (null == month) {
				final Month monthEnum = Month.valueOf(monthStr);
				month = monthEnum.getValue();
			}

			// 日
			Integer day = jsonObject.getInt(DAY_KEY);
			if (null == day) {
				day = jsonObject.getInt("dayOfMonth");
				Assert.notNull(day, "Field 'day' or 'dayOfMonth' must be not null");
			}

			final LocalDate localDate = LocalDate.of(year, month, day);
			if (LocalDate.class.equals(temporalAccessorClass)) {
				return localDate;
			}

			// 时分秒毫秒
			final LocalTime localTime = LocalTime.of(
				jsonObject.getInt(HOUR_KEY, 0),
				jsonObject.getInt(MINUTE_KEY, 0),
				jsonObject.getInt(SECOND_KEY, 0),
				jsonObject.getInt(NANO_KEY, 0));

			return LocalDateTime.of(localDate, localTime);
		} else if (LocalTime.class.equals(temporalAccessorClass)) {
			return LocalTime.of(
				jsonObject.getInt(HOUR_KEY),
				jsonObject.getInt(MINUTE_KEY),
				jsonObject.getInt(SECOND_KEY),
				jsonObject.getInt(NANO_KEY));
		}

		throw new JSONException("Unsupported type from JSON {} to {}", jsonObject, temporalAccessorClass);
	}
}
