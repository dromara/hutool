package cn.hutool.core.date;

import cn.hutool.core.util.StrUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;

/**
 * {@link TemporalAccessor} 工具类封装
 *
 * @author looly
 * @since 5.3.9
 */
public class TemporalAccessorUtil extends TemporalUtil{

	/**
	 * 安全获取时间的某个属性，属性不存在返回0
	 *
	 * @param temporalAccessor 需要获取的时间对象
	 * @param field            需要获取的属性
	 * @return 时间的值，如果无法获取则默认为 0
	 */
	public static int get(TemporalAccessor temporalAccessor, TemporalField field) {
		if (temporalAccessor.isSupported(field)) {
			return temporalAccessor.get(field);
		}

		return (int)field.range().getMinimum();
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param time      {@link TemporalAccessor}
	 * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
	 * @return 格式化后的字符串
	 * @since 5.3.10
	 */
	public static String format(TemporalAccessor time, DateTimeFormatter formatter) {
		if (null == time) {
			return null;
		}

		if(null == formatter){
			formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		}

		return formatter.format(time);
	}

	/**
	 * 格式化日期时间为指定格式
	 *
	 * @param time      {@link TemporalAccessor}
	 * @param format 日期格式
	 * @return 格式化后的字符串
	 * @since 5.3.10
	 */
	public static String format(TemporalAccessor time, String format) {
		if (null == time) {
			return null;
		}

		final DateTimeFormatter formatter = StrUtil.isBlank(format)
				? null : DateTimeFormatter.ofPattern(format);

		return format(time, formatter);
	}

	/**
	 * {@link TemporalAccessor}转换为 时间戳（从1970-01-01T00:00:00Z开始的毫秒数）
	 *
	 * @param temporalAccessor Date对象
	 * @return {@link Instant}对象
	 * @since 5.4.1
	 */
	public static long toEpochMilli(TemporalAccessor temporalAccessor) {
		return toInstant(temporalAccessor).toEpochMilli();
	}

	/**
	 * {@link TemporalAccessor}转换为 {@link Instant}对象
	 *
	 * @param temporalAccessor Date对象
	 * @return {@link Instant}对象
	 * @since 5.3.10
	 */
	public static Instant toInstant(TemporalAccessor temporalAccessor) {
		if (null == temporalAccessor) {
			return null;
		}

		Instant result;
		if (temporalAccessor instanceof Instant) {
			result = (Instant) temporalAccessor;
		} else if (temporalAccessor instanceof LocalDateTime) {
			result = ((LocalDateTime) temporalAccessor).atZone(ZoneId.systemDefault()).toInstant();
		} else if (temporalAccessor instanceof ZonedDateTime) {
			result = ((ZonedDateTime) temporalAccessor).toInstant();
		} else if (temporalAccessor instanceof OffsetDateTime) {
			result = ((OffsetDateTime) temporalAccessor).toInstant();
		} else if (temporalAccessor instanceof LocalDate) {
			result = ((LocalDate) temporalAccessor).atStartOfDay(ZoneId.systemDefault()).toInstant();
		} else if (temporalAccessor instanceof LocalTime) {
			// 指定本地时间转换 为Instant，取当天日期
			result = ((LocalTime) temporalAccessor).atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
		} else if (temporalAccessor instanceof OffsetTime) {
			// 指定本地时间转换 为Instant，取当天日期
			result = ((OffsetTime) temporalAccessor).atDate(LocalDate.now()).toInstant();
		} else {
			result = Instant.from(temporalAccessor);
		}

		return result;
	}
}
