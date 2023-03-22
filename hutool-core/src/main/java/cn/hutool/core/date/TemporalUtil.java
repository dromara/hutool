package cn.hutool.core.date;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * {@link Temporal} 工具类封装
 *
 * @author looly
 * @since 5.4.5
 */
public class TemporalUtil {

	/**
	 * 获取两个日期的差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 返回结果为{@link Duration}对象，通过调用toXXX方法返回相差单位
	 *
	 * @param startTimeInclude 开始时间（包含）
	 * @param endTimeExclude   结束时间（不包含）
	 * @return 时间差 {@link Duration}对象
	 */
	public static Duration between(Temporal startTimeInclude, Temporal endTimeExclude) {
		return Duration.between(startTimeInclude, endTimeExclude);
	}

	/**
	 * 获取两个日期的差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 返回结果为时间差的long值
	 *
	 * @param startTimeInclude 开始时间（包括）
	 * @param endTimeExclude   结束时间（不包括）
	 * @param unit             时间差单位
	 * @return 时间差
	 */
	public static long between(Temporal startTimeInclude, Temporal endTimeExclude, ChronoUnit unit) {
		return unit.between(startTimeInclude, endTimeExclude);
	}

	/**
	 * 将 {@link TimeUnit} 转换为 {@link ChronoUnit}.
	 *
	 * @param unit 被转换的{@link TimeUnit}单位，如果为{@code null}返回{@code null}
	 * @return {@link ChronoUnit}
	 * @since 5.7.16
	 */
	public static ChronoUnit toChronoUnit(TimeUnit unit) throws IllegalArgumentException {
		if (null == unit) {
			return null;
		}
		switch (unit) {
			case NANOSECONDS:
				return ChronoUnit.NANOS;
			case MICROSECONDS:
				return ChronoUnit.MICROS;
			case MILLISECONDS:
				return ChronoUnit.MILLIS;
			case SECONDS:
				return ChronoUnit.SECONDS;
			case MINUTES:
				return ChronoUnit.MINUTES;
			case HOURS:
				return ChronoUnit.HOURS;
			case DAYS:
				return ChronoUnit.DAYS;
			default:
				throw new IllegalArgumentException("Unknown TimeUnit constant");
		}
	}

	/**
	 * 转换 {@link ChronoUnit} 到 {@link TimeUnit}.
	 *
	 * @param unit {@link ChronoUnit}，如果为{@code null}返回{@code null}
	 * @return {@link TimeUnit}
	 * @throws IllegalArgumentException 如果{@link TimeUnit}没有对应单位抛出
	 * @since 5.7.16
	 */
	public static TimeUnit toTimeUnit(ChronoUnit unit) throws IllegalArgumentException {
		if (null == unit) {
			return null;
		}
		switch (unit) {
			case NANOS:
				return TimeUnit.NANOSECONDS;
			case MICROS:
				return TimeUnit.MICROSECONDS;
			case MILLIS:
				return TimeUnit.MILLISECONDS;
			case SECONDS:
				return TimeUnit.SECONDS;
			case MINUTES:
				return TimeUnit.MINUTES;
			case HOURS:
				return TimeUnit.HOURS;
			case DAYS:
				return TimeUnit.DAYS;
			default:
				throw new IllegalArgumentException("ChronoUnit cannot be converted to TimeUnit: " + unit);
		}
	}

	/**
	 * 日期偏移,根据field不同加不同值（偏移会修改传入的对象）
	 *
	 * @param <T>    日期类型，如LocalDate或LocalDateTime
	 * @param time   {@link Temporal}
	 * @param number 偏移量，正数为向后偏移，负数为向前偏移
	 * @param field  偏移单位，见{@link ChronoUnit}，不能为null
	 * @return 偏移后的日期时间
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Temporal> T offset(T time, long number, TemporalUnit field) {
		if (null == time) {
			return null;
		}

		return (T) time.plus(number, field);
	}

	/**
	 * 偏移到指定的周几
	 *
	 * @param temporal   日期或者日期时间
	 * @param dayOfWeek  周几
	 * @param <T>        日期类型，如LocalDate或LocalDateTime
	 * @param isPrevious 是否向前偏移，{@code true}向前偏移，{@code false}向后偏移。
	 * @return 偏移后的日期
	 * @since 5.8.0
	 */
	@SuppressWarnings("unchecked")
	public <T extends Temporal> T offset(T temporal, DayOfWeek dayOfWeek, boolean isPrevious) {
		return (T) temporal.with(isPrevious ? TemporalAdjusters.previous(dayOfWeek) : TemporalAdjusters.next(dayOfWeek));
	}
}
