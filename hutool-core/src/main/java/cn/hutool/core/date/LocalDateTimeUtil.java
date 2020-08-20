package cn.hutool.core.date;

import cn.hutool.core.util.ObjectUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;

/**
 * JDK8+中的{@link LocalDateTime} 工具类封装
 *
 * @author looly
 * @since 5.3.9
 */
public class LocalDateTimeUtil {

	/**
	 * 当前时间，默认时区
	 *
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}，使用默认时区
	 *
	 * @param instant {@link Instant}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Instant instant) {
		return of(instant, ZoneId.systemDefault());
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}，使用UTC时区
	 *
	 * @param instant {@link Instant}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime ofUTC(Instant instant) {
		return of(instant, ZoneId.of("UTC"));
	}

	/**
	 * {@link ZonedDateTime}转{@link LocalDateTime}
	 *
	 * @param zonedDateTime {@link ZonedDateTime}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(ZonedDateTime zonedDateTime) {
		if (null == zonedDateTime) {
			return null;
		}
		return zonedDateTime.toLocalDateTime();
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}
	 *
	 * @param instant {@link Instant}
	 * @param zoneId  时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Instant instant, ZoneId zoneId) {
		if (null == instant) {
			return null;
		}

		return LocalDateTime.ofInstant(instant, ObjectUtil.defaultIfNull(zoneId, ZoneId.systemDefault()));
	}

	/**
	 * {@link Instant}转{@link LocalDateTime}
	 *
	 * @param instant  {@link Instant}
	 * @param timeZone 时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Instant instant, TimeZone timeZone) {
		if (null == instant) {
			return null;
		}

		return of(instant, ObjectUtil.defaultIfNull(timeZone, TimeZone.getDefault()).toZoneId());
	}

	/**
	 * 毫秒转{@link LocalDateTime}，使用默认时区
	 *
	 * <p>注意：此方法使用默认时区，如果非UTC，会产生时间偏移</p>
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(long epochMilli) {
		return of(Instant.ofEpochMilli(epochMilli));
	}

	/**
	 * 毫秒转{@link LocalDateTime}，使用UTC时区
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime ofUTC(long epochMilli) {
		return ofUTC(Instant.ofEpochMilli(epochMilli));
	}

	/**
	 * 毫秒转{@link LocalDateTime}，根据时区不同，结果会产生时间偏移
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @param zoneId     时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(long epochMilli, ZoneId zoneId) {
		return of(Instant.ofEpochMilli(epochMilli), zoneId);
	}

	/**
	 * 毫秒转{@link LocalDateTime}，结果会产生时间偏移
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @param timeZone     时区
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(long epochMilli, TimeZone timeZone) {
		return of(Instant.ofEpochMilli(epochMilli), timeZone);
	}

	/**
	 * {@link Date}转{@link LocalDateTime}，使用默认时区
	 *
	 * @param date Date对象
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(Date date) {
		if (null == date) {
			return null;
		}

		if (date instanceof DateTime) {
			return of(date.toInstant(), ((DateTime) date).getZoneId());
		}
		return of(date.toInstant());
	}

	/**
	 * {@link TemporalAccessor}转{@link LocalDateTime}，使用默认时区
	 *
	 * @param temporalAccessor {@link TemporalAccessor}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime of(TemporalAccessor temporalAccessor) {
		if (null == temporalAccessor) {
			return null;
		}

		if(temporalAccessor instanceof LocalDate){
			return ((LocalDate)temporalAccessor).atStartOfDay();
		}

		return LocalDateTime.of(
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.MONTH_OF_YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.DAY_OF_MONTH),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.HOUR_OF_DAY),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.MINUTE_OF_HOUR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.SECOND_OF_MINUTE),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.NANO_OF_SECOND)
		);
	}

	/**
	 * {@link TemporalAccessor}转{@link LocalDate}，使用默认时区
	 *
	 * @param temporalAccessor {@link TemporalAccessor}
	 * @return {@link LocalDate}
	 * @since 5.3.10
	 */
	public static LocalDate ofDate(TemporalAccessor temporalAccessor) {
		if (null == temporalAccessor) {
			return null;
		}

		if(temporalAccessor instanceof LocalDateTime){
			return ((LocalDateTime)temporalAccessor).toLocalDate();
		}

		return LocalDate.of(
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.MONTH_OF_YEAR),
				TemporalAccessorUtil.get(temporalAccessor, ChronoField.DAY_OF_MONTH)
		);
	}

	/**
	 * 解析日期时间字符串为{@link LocalDateTime}，仅支持yyyy-MM-dd'T'HH:mm:ss格式，例如：2007-12-03T10:15:30
	 *
	 * @param text      日期时间字符串
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime parse(CharSequence text) {
		return parse(text, (DateTimeFormatter)null);
	}

	/**
	 * 解析日期时间字符串为{@link LocalDateTime}，格式支持日期时间、日期、时间
	 *
	 * @param text      日期时间字符串
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime parse(CharSequence text, DateTimeFormatter formatter) {
		if (null == text) {
			return null;
		}
		if (null == formatter) {
			return LocalDateTime.parse(text);
		}

		return of(formatter.parse(text));
	}

	/**
	 * 解析日期时间字符串为{@link LocalDateTime}
	 *
	 * @param text   日期时间字符串
	 * @param format 日期格式，类似于yyyy-MM-dd HH:mm:ss,SSS
	 * @return {@link LocalDateTime}
	 */
	public static LocalDateTime parse(CharSequence text, String format) {
		if (null == text) {
			return null;
		}
		return parse(text, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 解析日期时间字符串为{@link LocalDate}，仅支持yyyy-MM-dd'T'HH:mm:ss格式，例如：2007-12-03T10:15:30
	 *
	 * @param text      日期时间字符串
	 * @return {@link LocalDate}
	 * @since 5.3.10
	 */
	public static LocalDate parseDate(CharSequence text) {
		return parseDate(text, (DateTimeFormatter)null);
	}

	/**
	 * 解析日期时间字符串为{@link LocalDate}，格式支持日期
	 *
	 * @param text      日期时间字符串
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return {@link LocalDate}
	 * @since 5.3.10
	 */
	public static LocalDate parseDate(CharSequence text, DateTimeFormatter formatter) {
		if (null == text) {
			return null;
		}
		if (null == formatter) {
			return LocalDate.parse(text);
		}

		return ofDate(formatter.parse(text));
	}

	/**
	 * 解析日期字符串为{@link LocalDate}
	 *
	 * @param text   日期字符串
	 * @param format 日期格式，类似于yyyy-MM-dd
	 * @return {@link LocalDateTime}
	 */
	public static LocalDate parseDate(CharSequence text, String format) {
		if (null == text) {
			return null;
		}
		return parseDate(text, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 格式化日期时间为yyyy-MM-dd HH:mm:ss格式
	 *
	 * @param time      {@link LocalDateTime}
	 * @return 格式化后的字符串
	 * @since 5.3.11
	 */
	public static String formatNormal(LocalDateTime time) {
		return format(time, DatePattern.NORM_DATETIME_FORMATTER);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param time      {@link LocalDateTime}
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return 格式化后的字符串
	 */
	public static String format(LocalDateTime time, DateTimeFormatter formatter) {
		return TemporalAccessorUtil.format(time, formatter);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param time   {@link LocalDateTime}
	 * @param format 日期格式，类似于yyyy-MM-dd HH:mm:ss,SSS
	 * @return 格式化后的字符串
	 */
	public static String format(LocalDateTime time, String format) {
		if (null == time) {
			return null;
		}
		return format(time, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 格式化日期时间为yyyy-MM-dd格式
	 *
	 * @param date      {@link LocalDate}
	 * @return 格式化后的字符串
	 * @since 5.3.11
	 */
	public static String formatNormal(LocalDate date) {
		return format(date, DatePattern.NORM_DATE_FORMATTER);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param date      {@link LocalDate}
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return 格式化后的字符串
	 * @since 5.3.10
	 */
	public static String format(LocalDate date, DateTimeFormatter formatter) {
		return TemporalAccessorUtil.format(date, formatter);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param date   {@link LocalDate}
	 * @param format 日期格式，类似于yyyy-MM-dd
	 * @return 格式化后的字符串
	 * @since 5.3.10
	 */
	public static String format(LocalDate date, String format) {
		if (null == date) {
			return null;
		}
		return format(date, DateTimeFormatter.ofPattern(format));
	}

	/**
	 * 日期偏移,根据field不同加不同值（偏移会修改传入的对象）
	 *
	 * @param time   {@link LocalDateTime}
	 * @param number 偏移量，正数为向后偏移，负数为向前偏移
	 * @param field  偏移单位，见{@link ChronoField}，不能为null
	 * @return 偏移后的日期时间
	 */
	public static LocalDateTime offset(LocalDateTime time, long number, TemporalUnit field) {
		if (null == time) {
			return null;
		}

		return time.plus(number, field);
	}

	/**
	 * 获取两个日期的差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 返回结果为{@link Duration}对象，通过调用toXXX方法返回相差单位
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 时间差 {@link Duration}对象
	 */
	public static Duration between(LocalDateTime startTime, LocalDateTime endTime) {
		return Duration.between(startTime, endTime);
	}


	/**
	 * 修改为一天的开始时间，例如：2020-02-02 00:00:00,000
	 *
	 * @param time 日期时间
	 * @return 一天的开始时间
	 */
	public static LocalDateTime beginOfDay(LocalDateTime time) {
		return time.with(LocalTime.of(0, 0, 0, 0));
	}

	/**
	 * 修改为一天的结束时间，例如：2020-02-02 23:59:59,999
	 *
	 * @param time 日期时间
	 * @return 一天的结束时间
	 */
	public static LocalDateTime endOfDay(LocalDateTime time) {
		return time.with(LocalTime.of(23, 59, 59, 999_999_999));
	}

	/**
	 * {@link TemporalAccessor}转换为 时间戳（从1970-01-01T00:00:00Z开始的毫秒数）
	 *
	 * @param temporalAccessor Date对象
	 * @return {@link Instant}对象
	 * @since 5.4.1
	 * @see TemporalAccessorUtil#toEpochMilli(TemporalAccessor)
	 */
	public static long toEpochMilli(TemporalAccessor temporalAccessor) {
		return TemporalAccessorUtil.toEpochMilli(temporalAccessor);
	}
}
