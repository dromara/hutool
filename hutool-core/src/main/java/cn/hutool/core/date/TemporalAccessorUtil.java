package cn.hutool.core.date;

import cn.hutool.core.date.format.GlobalCustomFormat;
import cn.hutool.core.util.StrUtil;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.Era;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * {@link TemporalAccessor} 工具类封装
 *
 * @author looly
 * @since 5.3.9
 */
public class TemporalAccessorUtil extends TemporalUtil{

	/**
	 * 安全获取时间的某个属性，属性不存在返回最小值，一般为0<br>
	 * 注意请谨慎使用此方法，某些{@link TemporalAccessor#isSupported(TemporalField)}为{@code false}的方法返回最小值
	 *
	 * @param temporalAccessor 需要获取的时间对象
	 * @param field            需要获取的属性
	 * @return 时间的值，如果无法获取则获取最小值，一般为0
	 */
	public static int get(TemporalAccessor temporalAccessor, TemporalField field) {
		if (temporalAccessor.isSupported(field)) {
			return temporalAccessor.get(field);
		}

		return (int)field.range().getMinimum();
	}

	/**
	 * 格式化日期时间为指定格式<br>
	 * 如果为{@link Month}，调用{@link Month#toString()}
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

		if(time instanceof Month){
			return time.toString();
		}

		if(null == formatter){
			formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		}

		try {
			return formatter.format(time);
		} catch (UnsupportedTemporalTypeException e){
			if(time instanceof LocalDate && e.getMessage().contains("HourOfDay")){
				// 用户传入LocalDate，但是要求格式化带有时间部分，转换为LocalDateTime重试
				return formatter.format(((LocalDate) time).atStartOfDay());
			}else if(time instanceof LocalTime && e.getMessage().contains("YearOfEra")){
				// 用户传入LocalTime，但是要求格式化带有日期部分，转换为LocalDateTime重试
				return formatter.format(((LocalTime) time).atDate(LocalDate.now()));
			} else if(time instanceof Instant){
				// 时间戳没有时区信息，赋予默认时区
				return formatter.format(((Instant) time).atZone(ZoneId.systemDefault()));
			}
			throw e;
		}
	}

	/**
	 * 格式化日期时间为指定格式<br>
	 * 如果为{@link Month}，调用{@link Month#toString()}
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

		if(time instanceof DayOfWeek || time instanceof java.time.Month || time instanceof Era || time instanceof MonthDay){
			return time.toString();
		}

		// 检查自定义格式
		if(GlobalCustomFormat.isCustomFormat(format)){
			return GlobalCustomFormat.format(time, format);
		}

		final DateTimeFormatter formatter = StrUtil.isBlank(format)
				? null : DateTimeFormatter.ofPattern(format);

		return format(time, formatter);
	}

	/**
	 * {@link TemporalAccessor}转换为 时间戳（从1970-01-01T00:00:00Z开始的毫秒数）<br>
	 * 如果为{@link Month}，调用{@link Month#getValue()}
	 *
	 * @param temporalAccessor Date对象
	 * @return {@link Instant}对象
	 * @since 5.4.1
	 */
	public static long toEpochMilli(TemporalAccessor temporalAccessor) {
		if(temporalAccessor instanceof Month){
			return ((Month) temporalAccessor).getValue();
		} else if(temporalAccessor instanceof DayOfWeek){
			return ((DayOfWeek) temporalAccessor).getValue();
		} else if(temporalAccessor instanceof Era){
			return ((Era) temporalAccessor).getValue();
		}
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
			// issue#1891@Github
			// Instant.from不能完成日期转换
			//result = Instant.from(temporalAccessor);
			result = toInstant(LocalDateTimeUtil.of(temporalAccessor));
		}

		return result;
	}

	/**
	 * 当前日期是否在日期指定范围内<br>
	 * 起始日期和结束日期可以互换
	 *
	 * @param date      被检查的日期
	 * @param beginDate 起始日期（包含）
	 * @param endDate   结束日期（包含）
	 * @return 是否在范围内
	 * @since 5.8.5
	 */
	public static boolean isIn(TemporalAccessor date, TemporalAccessor beginDate, TemporalAccessor endDate) {
		return isIn(date, beginDate, endDate, true, true);
	}

	/**
	 * 当前日期是否在日期指定范围内<br>
	 * 起始日期和结束日期可以互换<br>
	 * 通过includeBegin, includeEnd参数控制日期范围区间是否为开区间，例如：传入参数：includeBegin=true, includeEnd=false，
	 * 则本方法会判断 date ∈ (beginDate, endDate] 是否成立
	 *
	 * @param date         被检查的日期
	 * @param beginDate    起始日期
	 * @param endDate      结束日期
	 * @param includeBegin 时间范围是否包含起始日期
	 * @param includeEnd   时间范围是否包含结束日期
	 * @return 是否在范围内
	 * @author FengBaoheng
	 * @since 5.8.6
	 */
	public static boolean isIn(TemporalAccessor date, TemporalAccessor beginDate, TemporalAccessor endDate,
							   boolean includeBegin, boolean includeEnd) {
		if (date == null || beginDate == null || endDate == null) {
			throw new IllegalArgumentException("参数不可为null");
		}

		final long thisMills = toEpochMilli(date);
		final long beginMills = toEpochMilli(beginDate);
		final long endMills = toEpochMilli(endDate);
		final long rangeMin = Math.min(beginMills, endMills);
		final long rangeMax = Math.max(beginMills, endMills);

		// 先判断是否满足 date ∈ (beginDate, endDate)
		boolean isIn = rangeMin < thisMills && thisMills < rangeMax;

		// 若不满足，则再判断是否在时间范围的边界上
		if (!isIn && includeBegin) {
			isIn = thisMills == rangeMin;
		}

		if (!isIn && includeEnd) {
			isIn = thisMills == rangeMax;
		}

		return isIn;
	}
}
