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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TimeUtil;
import org.dromara.hutool.core.date.format.DateFormatManager;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.time.*;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * JDK8中新加入的java.time包对象解析转换器<br>
 * 支持的对象包括：
 *
 * <pre>
 * java.time.Instant
 * java.time.LocalDateTime
 * java.time.LocalDate
 * java.time.LocalTime
 * java.time.ZonedDateTime
 * java.time.OffsetDateTime
 * java.time.OffsetTime
 * </pre>
 *
 * @author looly
 * @since 5.0.0
 */
public class TemporalAccessorConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final TemporalAccessorConverter INSTANCE = new TemporalAccessorConverter();

	/**
	 * 日期格式化
	 */
	private String format;

	/**
	 * 构造
	 */
	public TemporalAccessorConverter() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param format 日期格式
	 */
	public TemporalAccessorConverter(final String format) {
		this.format = format;
	}

	/**
	 * 获取日期格式
	 *
	 * @return 设置日期格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置日期格式
	 *
	 * @param format 日期格式
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

	@Override
	protected TemporalAccessor convertInternal(final Class<?> targetClass, final Object value) {
		if (value instanceof Long) {
			return parseFromLong(targetClass, (Long) value);
		} else if (value instanceof Integer) {
			return parseFromLong(targetClass, ((Integer) value).longValue());
		} else if (value instanceof TemporalAccessor) {
			return parseFromTemporalAccessor(targetClass, (TemporalAccessor) value);
		} else if (value instanceof Date) {
			final DateTime dateTime = DateUtil.date((Date) value);
			return parseFromInstant(targetClass, dateTime.toInstant(), dateTime.getZoneId());
		} else if (value instanceof Calendar) {
			final Calendar calendar = (Calendar) value;
			return parseFromInstant(targetClass, calendar.toInstant(), calendar.getTimeZone().toZoneId());
		} else {
			return parseFromCharSequence(targetClass, convertToStr(value));
		}
	}

	/**
	 * 通过反射从字符串转java.time中的对象
	 *
	 * @param value 字符串值
	 * @return 日期对象
	 */
	private TemporalAccessor parseFromCharSequence(final Class<?> targetClass, final CharSequence value) {
		if (StrUtil.isBlank(value)) {
			return null;
		}

		if (DayOfWeek.class == targetClass) {
			return DayOfWeek.valueOf(StrUtil.toString(value));
		} else if (Month.class == targetClass) {
			return Month.valueOf(StrUtil.toString(value));
		} else if (Era.class == targetClass) {
			return IsoEra.valueOf(StrUtil.toString(value));
		} else if (MonthDay.class == targetClass) {
			return MonthDay.parse(value);
		}

		final Instant instant;
		ZoneId zoneId = null;
		if (null != this.format) {
			final DateTimeFormatter formatter = TimeUtil.ofPattern(this.format);

			// issue#I9HQQE
			final TemporalAccessor temporalAccessor = parseWithFormat(targetClass, value, formatter);
			if(null != temporalAccessor){
				return temporalAccessor;
			}

			instant = formatter.parse(value, Instant::from);
			zoneId = formatter.getZone();
		} else {
			final DateTime date = DateUtil.parse(value);
			instant = Objects.requireNonNull(date).toInstant();
			zoneId = date.getZoneId();
		}
		return parseFromInstant(targetClass, instant, zoneId);
	}

	/**
	 * 对于自定义格式的字符串，单独解析为{@link TemporalAccessor}
	 *
	 * @param targetClass 目标类型
	 * @param value       日期字符串
	 * @param formatter   格式
	 * @return {@link TemporalAccessor}
	 */
	private TemporalAccessor parseWithFormat(final Class<?> targetClass, final CharSequence value, final DateTimeFormatter formatter) {
		// issue#I9HQQE
		if (LocalDate.class == targetClass) {
			return LocalDate.parse(value, formatter);
		} else if (LocalDateTime.class == targetClass) {
			return LocalDateTime.parse(value, formatter);
		} else if (LocalTime.class == targetClass) {
			return LocalTime.parse(value, formatter);
		}
		return null;
	}

	/**
	 * 将Long型时间戳转换为java.time中的对象
	 *
	 * @param targetClass 目标类型
	 * @param time        时间戳
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromLong(final Class<?> targetClass, final Long time) {
		if (targetClass == Month.class) {
			return Month.of(Math.toIntExact(time));
		} else if (targetClass == DayOfWeek.class) {
			return DayOfWeek.of(Math.toIntExact(time));
		} else if (Era.class == targetClass) {
			return IsoEra.of(Math.toIntExact(time));
		}

		final Instant instant;
		if (DateFormatManager.FORMAT_SECONDS.equals(this.format)) {
			// https://gitee.com/dromara/hutool/issues/I6IS5B
			// Unix时间戳
			instant = Instant.ofEpochSecond(time);
		} else {
			instant = Instant.ofEpochMilli(time);
		}
		return parseFromInstant(targetClass, instant, null);
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param temporalAccessor TemporalAccessor对象
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromTemporalAccessor(final Class<?> targetClass, final TemporalAccessor temporalAccessor) {
		if (DayOfWeek.class == targetClass) {
			return DayOfWeek.from(temporalAccessor);
		} else if (Month.class == targetClass) {
			return Month.from(temporalAccessor);
		} else if (MonthDay.class == targetClass) {
			return MonthDay.from(temporalAccessor);
		}

		TemporalAccessor result = null;
		if (temporalAccessor instanceof LocalDateTime) {
			result = parseFromLocalDateTime(targetClass, (LocalDateTime) temporalAccessor);
		} else if (temporalAccessor instanceof ZonedDateTime) {
			result = parseFromZonedDateTime(targetClass, (ZonedDateTime) temporalAccessor);
		}

		if (null == result) {
			result = parseFromInstant(targetClass, DateUtil.toInstant(temporalAccessor), null);
		}

		return result;
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param targetClass   目标类
	 * @param localDateTime {@link LocalDateTime}对象
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromLocalDateTime(final Class<?> targetClass, final LocalDateTime localDateTime) {
		if (Instant.class.equals(targetClass)) {
			return DateUtil.toInstant(localDateTime);
		}
		if (LocalDate.class.equals(targetClass)) {
			return localDateTime.toLocalDate();
		}
		if (LocalTime.class.equals(targetClass)) {
			return localDateTime.toLocalTime();
		}
		if (ZonedDateTime.class.equals(targetClass)) {
			return localDateTime.atZone(ZoneId.systemDefault());
		}
		if (OffsetDateTime.class.equals(targetClass)) {
			return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
		}
		if (OffsetTime.class.equals(targetClass)) {
			return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime().toOffsetTime();
		}

		return null;
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param zonedDateTime {@link ZonedDateTime}对象
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromZonedDateTime(final Class<?> targetClass, final ZonedDateTime zonedDateTime) {
		if (Instant.class.equals(targetClass)) {
			return DateUtil.toInstant(zonedDateTime);
		}
		if (LocalDateTime.class.equals(targetClass)) {
			return zonedDateTime.toLocalDateTime();
		}
		if (LocalDate.class.equals(targetClass)) {
			return zonedDateTime.toLocalDate();
		}
		if (LocalTime.class.equals(targetClass)) {
			return zonedDateTime.toLocalTime();
		}
		if (OffsetDateTime.class.equals(targetClass)) {
			return zonedDateTime.toOffsetDateTime();
		}
		if (OffsetTime.class.equals(targetClass)) {
			return zonedDateTime.toOffsetDateTime().toOffsetTime();
		}

		return null;
	}

	/**
	 * 将TemporalAccessor型时间戳转换为java.time中的对象
	 *
	 * @param instant {@link Instant}对象
	 * @param zoneId  时区ID，null表示当前系统默认的时区
	 * @return java.time中的对象
	 */
	private TemporalAccessor parseFromInstant(final Class<?> targetClass, final Instant instant, ZoneId zoneId) {
		if (Instant.class.equals(targetClass)) {
			return instant;
		}

		zoneId = ObjUtil.defaultIfNull(zoneId, ZoneId::systemDefault);

		TemporalAccessor result = null;
		if (LocalDateTime.class.equals(targetClass)) {
			result = LocalDateTime.ofInstant(instant, zoneId);
		} else if (LocalDate.class.equals(targetClass)) {
			result = instant.atZone(zoneId).toLocalDate();
		} else if (LocalTime.class.equals(targetClass)) {
			result = instant.atZone(zoneId).toLocalTime();
		} else if (ZonedDateTime.class.equals(targetClass)) {
			result = instant.atZone(zoneId);
		} else if (OffsetDateTime.class.equals(targetClass)) {
			result = OffsetDateTime.ofInstant(instant, zoneId);
		} else if (OffsetTime.class.equals(targetClass)) {
			result = OffsetTime.ofInstant(instant, zoneId);
		}
		return result;
	}
}
