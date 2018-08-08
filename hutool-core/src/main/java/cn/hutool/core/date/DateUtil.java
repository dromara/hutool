package cn.hutool.core.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.format.DateParser;
import cn.hutool.core.date.format.DatePrinter;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;

/**
 * 时间工具类
 * 
 * @author xiaoleilu
 */
public class DateUtil {

	/**
	 * 转换为{@link DateTime}对象
	 * 
	 * @return 当前时间
	 */
	public static DateTime date() {
		return new DateTime();
	}

	/**
	 * {@link Date}类型时间转为{@link DateTime}
	 * 
	 * @param date Long类型Date（Unix时间戳）
	 * @return 时间对象
	 * @since 3.0.7
	 */
	public static DateTime date(Date date) {
		if (date instanceof DateTime) {
			return (DateTime) date;
		}
		return new DateTime(date);
	}

	/**
	 * Long类型时间转为{@link DateTime}<br>
	 * 同时支持10位秒级别时间戳和13位毫秒级别时间戳
	 * 
	 * @param date Long类型Date（Unix时间戳）
	 * @return 时间对象
	 */
	public static DateTime date(long date) {
		return new DateTime(date);
	}

	/**
	 * {@link Calendar}类型时间转为{@link DateTime}
	 * 
	 * @param calendar {@link Calendar}
	 * @return 时间对象
	 */
	public static DateTime date(Calendar calendar) {
		return new DateTime(calendar);
	}

	/**
	 * 转换为Calendar对象
	 * 
	 * @param date 日期对象
	 * @return Calendar对象
	 */
	public static Calendar calendar(Date date) {
		return calendar(date.getTime());
	}

	/**
	 * 转换为Calendar对象
	 * 
	 * @param millis 时间戳
	 * @return Calendar对象
	 */
	public static Calendar calendar(long millis) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal;
	}

	/**
	 * 当前时间，格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return 当前时间的标准形式字符串
	 */
	public static String now() {
		return formatDateTime(new DateTime());
	}

	/**
	 * 当前时间long
	 * 
	 * @param isNano 是否为高精度时间
	 * @return 时间
	 */
	public static long current(boolean isNano) {
		return isNano ? System.nanoTime() : System.currentTimeMillis();
	}

	/**
	 * 当前时间秒数
	 * 
	 * @return 当前时间秒数
	 * @since 4.0.0
	 */
	public static long currentSeconds() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 当前日期，格式 yyyy-MM-dd
	 * 
	 * @return 当前日期的标准形式字符串
	 */
	public static String today() {
		return formatDate(new DateTime());
	}

	// -------------------------------------------------------------- Part of Date start
	/**
	 * 获得年的部分
	 * 
	 * @param date 日期
	 * @return 年的部分
	 */
	public static int year(Date date) {
		return DateTime.of(date).year();
	}

	/**
	 * 获得指定日期所属季度
	 * 
	 * @param date 日期
	 * @return 第几个季度
	 * @deprecated 请使用{@link #quarter(Date)}
	 */
	@Deprecated
	public static int season(Date date) {
		return quarter(date);
	}

	/**
	 * 获得指定日期所属季度，从1开始计数
	 * 
	 * @param date 日期
	 * @return 第几个季度
	 * @since 4.1.0
	 */
	public static int quarter(Date date) {
		return DateTime.of(date).quarter();
	}

	/**
	 * 获得指定日期所属季度
	 * 
	 * @param date 日期
	 * @return 第几个季度枚举
	 * @since 4.1.0
	 */
	public static Quarter quarterEnum(Date date) {
		return DateTime.of(date).quarterEnum();
	}

	/**
	 * 获得月份，从0开始计数
	 * 
	 * @param date 日期
	 * @return 月份，从0开始计数
	 */
	public static int month(Date date) {
		return DateTime.of(date).month();
	}

	/**
	 * 获得月份
	 * 
	 * @param date 日期
	 * @return {@link Month}
	 */
	public static Month monthEnum(Date date) {
		return DateTime.of(date).monthEnum();
	}

	/**
	 * 获得指定日期是所在年份的第几周<br>
	 * 
	 * @param date 日期
	 * @return 周
	 */
	public static int weekOfYear(Date date) {
		return DateTime.of(date).weekOfYear();
	}

	/**
	 * 获得指定日期是所在月份的第几周<br>
	 * 
	 * @param date 日期
	 * @return 周
	 */
	public static int weekOfMonth(Date date) {
		return DateTime.of(date).weekOfMonth();
	}

	/**
	 * 获得指定日期是这个日期所在月份的第几天<br>
	 * 
	 * @param date 日期
	 * @return 天
	 */
	public static int dayOfMonth(Date date) {
		return DateTime.of(date).dayOfMonth();
	}

	/**
	 * 获得指定日期是星期几，1表示周日，2表示周一
	 * 
	 * @param date 日期
	 * @return 天
	 */
	public static int dayOfWeek(Date date) {
		return DateTime.of(date).dayOfWeek();
	}

	/**
	 * 获得指定日期是星期几
	 * 
	 * @param date 日期
	 * @return {@link Week}
	 */
	public static Week dayOfWeekEnum(Date date) {
		return DateTime.of(date).dayOfWeekEnum();
	}

	/**
	 * 获得指定日期的小时数部分<br>
	 * 
	 * @param date 日期
	 * @param is24HourClock 是否24小时制
	 * @return 小时数
	 */
	public static int hour(Date date, boolean is24HourClock) {
		return DateTime.of(date).hour(is24HourClock);
	}

	/**
	 * 获得指定日期的分钟数部分<br>
	 * 例如：10:04:15.250 =》 4
	 * 
	 * @param date 日期
	 * @return 分钟数
	 */
	public static int minute(Date date) {
		return DateTime.of(date).minute();
	}

	/**
	 * 获得指定日期的秒数部分<br>
	 * 
	 * @param date 日期
	 * @return 秒数
	 */
	public static int second(Date date) {
		return DateTime.of(date).second();
	}

	/**
	 * 获得指定日期的毫秒数部分<br>
	 * 
	 * @param date 日期
	 * @return 毫秒数
	 */
	public static int millsecond(Date date) {
		return DateTime.of(date).millsecond();
	}

	/**
	 * 是否为上午
	 * 
	 * @param date 日期
	 * @return 是否为上午
	 */
	public static boolean isAM(Date date) {
		return DateTime.of(date).isAM();
	}

	/**
	 * 是否为下午
	 * 
	 * @param date 日期
	 * @return 是否为下午
	 */
	public static boolean isPM(Date date) {
		return DateTime.of(date).isPM();
	}

	/**
	 * @return 今年
	 */
	public static int thisYear() {
		return year(date());
	}

	/**
	 * @return 当前月份
	 */
	public static int thisMonth() {
		return month(date());
	}

	/**
	 * @return 当前月份 {@link Month}
	 */
	public static Month thisMonthEnum() {
		return monthEnum(date());
	}

	/**
	 * @return 当前日期所在年份的第几周
	 */
	public static int thisWeekOfYear() {
		return weekOfYear(date());
	}

	/**
	 * @return 当前日期所在年份的第几周
	 */
	public static int thisWeekOfMonth() {
		return weekOfMonth(date());
	}

	/**
	 * @return 当前日期是这个日期所在月份的第几天
	 */
	public static int thisDayOfMonth() {
		return dayOfMonth(date());
	}

	/**
	 * @return 当前日期是星期几
	 */
	public static int thisDayOfWeek() {
		return dayOfWeek(date());
	}

	/**
	 * @return 当前日期是星期几 {@link Week}
	 */
	public static Week thisDayOfWeekEnum() {
		return dayOfWeekEnum(date());
	}

	/**
	 * @param is24HourClock 是否24小时制
	 * @return 当前日期的小时数部分<br>
	 */
	public static int thisHour(boolean is24HourClock) {
		return hour(date(), is24HourClock);
	}

	/**
	 * @return 当前日期的分钟数部分<br>
	 */
	public static int thisMinute() {
		return minute(date());
	}

	/**
	 * @return 当前日期的秒数部分<br>
	 */
	public static int thisSecond() {
		return second(date());
	}

	/**
	 * @return 当前日期的毫秒数部分<br>
	 */
	public static int thisMillsecond() {
		return millsecond(date());
	}
	// -------------------------------------------------------------- Part of Date end

	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * 
	 * @param date 日期
	 * @return Season ，类似于 20132
	 * @deprecated 请使用{@link #yearAndQuarter} 代替
	 */
	@Deprecated
	public static String yearAndSeason(Date date) {
		return yearAndSeason(calendar(date));
	}

	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * 
	 * @param date 日期
	 * @return Quarter ，类似于 20132
	 */
	public static String yearAndQuarter(Date date) {
		return yearAndQuarter(calendar(date));
	}

	/**
	 * 获得指定日期区间内的年份和季节<br>
	 * 
	 * @param startDate 起始日期（包含）
	 * @param endDate 结束日期（包含）
	 * @return Season列表 ，元素类似于 20132
	 * @deprecated 请使用{@link #yearAndQuarter} 代替
	 */
	@Deprecated
	public static LinkedHashSet<String> yearAndSeasons(Date startDate, Date endDate) {
		final LinkedHashSet<String> seasons = new LinkedHashSet<String>();
		if (startDate == null || endDate == null) {
			return seasons;
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while (true) {
			// 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
			if (startDate.after(endDate)) {
				startDate = endDate;
			}

			seasons.add(yearAndSeason(cal));

			if (startDate.equals(endDate)) {
				break;
			}

			cal.add(Calendar.MONTH, 3);
			startDate = cal.getTime();
		}

		return seasons;
	}

	/**
	 * 获得指定日期区间内的年份和季节<br>
	 * 
	 * @param startDate 起始日期（包含）
	 * @param endDate 结束日期（包含）
	 * @return Season列表 ，元素类似于 20132
	 */
	public static LinkedHashSet<String> yearAndQuarter(Date startDate, Date endDate) {
		final LinkedHashSet<String> quarter = new LinkedHashSet<String>();
		if (startDate == null || endDate == null) {
			return quarter;
		}

		final Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while (true) {
			// 如果开始时间超出结束时间，让结束时间为开始时间，处理完后结束循环
			if (startDate.after(endDate)) {
				startDate = endDate;
			}

			quarter.add(yearAndSeason(cal));

			if (startDate.equals(endDate)) {
				break;
			}

			cal.add(Calendar.MONTH, 3);
			startDate = cal.getTime();
		}

		return quarter;
	}

	// ------------------------------------ Format start ----------------------------------------------
	/**
	 * 根据特定格式格式化日期
	 * 
	 * @param date 被格式化的日期
	 * @param format 日期格式，常用格式见： {@link DatePattern}
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, String format) {
		if (null == date || StrUtil.isBlank(format)) {
			return null;
		}
		return format(date, FastDateFormat.getInstance(format));
	}

	/**
	 * 根据特定格式格式化日期
	 * 
	 * @param date 被格式化的日期
	 * @param format {@link DatePrinter} 或 {@link FastDateFormat}
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, DatePrinter format) {
		if (null == format || null == date) {
			return null;
		}
		return format.format(date);
	}

	/**
	 * 根据特定格式格式化日期
	 * 
	 * @param date 被格式化的日期
	 * @param format {@link SimpleDateFormat}
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, DateFormat format) {
		if (null == format || null == date) {
			return null;
		}
		return format.format(date);
	}

	/**
	 * 格式化日期时间<br>
	 * 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date 被格式化的日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime(Date date) {
		if (null == date) {
			return null;
		}
		return DatePattern.NORM_DATETIME_FORMAT.format(date);
	}

	/**
	 * 格式化日期部分（不包括时间）<br>
	 * 格式 yyyy-MM-dd
	 * 
	 * @param date 被格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		if (null == date) {
			return null;
		}
		return DatePattern.NORM_DATE_FORMAT.format(date);
	}

	/**
	 * 格式化时间<br>
	 * 格式 HH:mm:ss
	 * 
	 * @param date 被格式化的日期
	 * @return 格式化后的字符串
	 * @since 3.0.1
	 */
	public static String formatTime(Date date) {
		if (null == date) {
			return null;
		}
		return DatePattern.NORM_TIME_FORMAT.format(date);
	}

	/**
	 * 格式化为Http的标准日期格式
	 * 
	 * @param date 被格式化的日期
	 * @return HTTP标准形式日期字符串
	 */
	public static String formatHttpDate(Date date) {
		if (null == date) {
			return null;
		}
		return DatePattern.HTTP_DATETIME_FORMAT.format(date);
	}
	// ------------------------------------ Format end ----------------------------------------------

	// ------------------------------------ Parse start ----------------------------------------------

	/**
	 * 构建DateTime对象
	 * 
	 * @param dateStr Date字符串
	 * @param dateFormat 格式化器 {@link SimpleDateFormat}
	 * @return DateTime对象
	 */
	public static DateTime parse(String dateStr, DateFormat dateFormat) {
		return new DateTime(dateStr, dateFormat);
	}

	/**
	 * 构建DateTime对象
	 * 
	 * @param dateStr Date字符串
	 * @param parser 格式化器,{@link FastDateFormat}
	 * @return DateTime对象
	 */
	public static DateTime parse(String dateStr, DateParser parser) {
		return new DateTime(dateStr, parser);
	}

	/**
	 * 将特定格式的日期转换为Date对象
	 * 
	 * @param dateStr 特定格式的日期
	 * @param format 格式，例如yyyy-MM-dd
	 * @return 日期对象
	 */
	public static DateTime parse(String dateStr, String format) {
		return new DateTime(dateStr, format);
	}

	/**
	 * 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString 标准形式的时间字符串
	 * @return 日期对象
	 */
	public static DateTime parseDateTime(String dateString) {
		dateString = normalize(dateString);
		return parse(dateString, DatePattern.NORM_DATETIME_FORMAT);
	}

	/**
	 * 格式yyyy-MM-dd
	 * 
	 * @param dateString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseDate(String dateString) {
		dateString = normalize(dateString);
		return parse(dateString, DatePattern.NORM_DATE_FORMAT);
	}

	/**
	 * 解析时间，格式HH:mm:ss，默认为1970-01-01
	 * 
	 * @param timeString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseTime(String timeString) {
		timeString = normalize(timeString);
		return parse(timeString, DatePattern.NORM_TIME_FORMAT);
	}

	/**
	 * 解析时间，格式HH:mm:ss，日期默认为今天
	 * 
	 * @param timeString 标准形式的日期字符串
	 * @return 日期对象
	 * @since 3.1.1
	 */
	public static DateTime parseTimeToday(String timeString) {
		timeString = StrUtil.format("{} {}", today(), timeString);
		return parse(timeString, DatePattern.NORM_DATETIME_PATTERN);
	}

	/**
	 * 将日期字符串转换为{@link DateTime}对象，格式：<br>
	 * <ol>
	 * <li>yyyy-MM-dd HH:mm:ss</li>
	 * <li>yyyy/MM/dd HH:mm:ss</li>
	 * <li>yyyy.MM.dd HH:mm:ss</li>
	 * <li>yyyy年MM月dd日 HH时mm分ss秒</li>
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy/MM/dd</li>
	 * <li>yyyy.MM.dd</li>
	 * <li>HH:mm:ss</li>
	 * <li>HH时mm分ss秒</li>
	 * <li>yyyy-MM-dd HH:mm</li>
	 * <li>yyyy-MM-dd HH:mm:ss.SSS</li>
	 * <li>yyyyMMddHHmmss</li>
	 * <li>yyyyMMddHHmmssSSS</li>
	 * <li>yyyyMMdd</li>
	 * <li>EEE, dd MMM yyyy HH:mm:ss z</li>
	 * <li>EEE MMM dd HH:mm:ss zzz yyyy</li>
	 * </ol>
	 * 
	 * @param dateStr 日期字符串
	 * @return 日期
	 */
	public static DateTime parse(String dateStr) {
		if (null == dateStr) {
			return null;
		}
		// 去掉两边空格并去掉中文日期中的“日”，以规范长度
		dateStr = dateStr.trim().replace("日", "");
		int length = dateStr.length();

		if (Validator.isNumber(dateStr)) {
			// 纯数字形式
			if (length == DatePattern.PURE_DATETIME_PATTERN.length()) {
				return parse(dateStr, DatePattern.PURE_DATETIME_FORMAT);
			} else if (length == DatePattern.PURE_DATETIME_MS_PATTERN.length()) {
				return parse(dateStr, DatePattern.PURE_DATETIME_MS_FORMAT);
			} else if (length == DatePattern.PURE_DATE_PATTERN.length()) {
				return parse(dateStr, DatePattern.PURE_DATE_FORMAT);
			} else if (length == DatePattern.PURE_TIME_PATTERN.length()) {
				return parse(dateStr, DatePattern.PURE_TIME_FORMAT);
			}
		}

		if (length == DatePattern.NORM_DATETIME_PATTERN.length() || length == DatePattern.NORM_DATETIME_PATTERN.length() + 1) {
			return parseDateTime(dateStr);
		} else if (length == DatePattern.NORM_DATE_PATTERN.length()) {
			return parseDate(dateStr);
		} else if (length == DatePattern.NORM_TIME_PATTERN.length() || length == DatePattern.NORM_TIME_PATTERN.length() + 1) {
			return parseTime(dateStr);
		} else if (length == DatePattern.NORM_DATETIME_MINUTE_PATTERN.length() || length == DatePattern.NORM_DATETIME_MINUTE_PATTERN.length() + 1) {
			return parse(normalize(dateStr), DatePattern.NORM_DATETIME_MINUTE_FORMAT);
		} else if (length >= DatePattern.NORM_DATETIME_MS_PATTERN.length() - 2) {
			return parse(normalize(dateStr), DatePattern.NORM_DATETIME_MS_FORMAT);
		}

		// 没有更多匹配的时间格式
		throw new DateException("No format fit for date String [{}] !", dateStr);
	}

	// ------------------------------------ Parse end ----------------------------------------------

	// ------------------------------------ Offset start ----------------------------------------------
	/**
	 * 获取某天的开始时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfDay(Date date) {
		return new DateTime(beginOfDay(calendar(date)));
	}

	/**
	 * 获取某天的结束时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfDay(Date date) {
		return new DateTime(endOfDay(calendar(date)));
	}

	/**
	 * 获取某天的开始时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar beginOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/**
	 * 获取某天的结束时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar endOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar;
	}

	/**
	 * 获取某周的开始时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfWeek(Date date) {
		return new DateTime(beginOfWeek(calendar(date)));
	}

	/**
	 * 获取某周的结束时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfWeek(Date date) {
		return new DateTime(endOfWeek(calendar(date)));
	}

	/**
	 * 获取某周的开始时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar beginOfWeek(Calendar calendar) {
		return beginOfWeek(calendar, true);
	}

	/**
	 * 获取某周的开始时间，周一定为一周的开始时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @param isMondayAsFirstDay 是否周一做为一周的第一天（false表示周日做为第一天）
	 * @return {@link Calendar}
	 * @since 3.1.2
	 */
	public static Calendar beginOfWeek(Calendar calendar, boolean isMondayAsFirstDay) {
		if (isMondayAsFirstDay) {
			// 设置周一为一周开始
			calendar.setFirstDayOfWeek(Week.MONDAY.getValue());
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}
		return beginOfDay(calendar);
	}

	/**
	 * 获取某周的结束时间，周日定为一周的结束
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar endOfWeek(Calendar calendar) {
		return endOfWeek(calendar, true);
	}

	/**
	 * 获取某周的结束时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @param isSundayAsLastDay 是否周日做为一周的最后一天（false表示周六做为最后一天）
	 * @return {@link Calendar}
	 * @since 3.1.2
	 */
	public static Calendar endOfWeek(Calendar calendar, boolean isSundayAsLastDay) {
		if (isSundayAsLastDay) {
			// 设置周一为一周开始
			calendar.setFirstDayOfWeek(Week.MONDAY.getValue());
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		} else {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		}
		return endOfDay(calendar);
	}

	/**
	 * 获取某月的开始时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfMonth(Date date) {
		return new DateTime(beginOfMonth(calendar(date)));
	}

	/**
	 * 获取某月的结束时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfMonth(Date date) {
		return new DateTime(endOfMonth(calendar(date)));
	}

	/**
	 * 获取某月的开始时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar beginOfMonth(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return beginOfDay(calendar);
	}

	/**
	 * 获取某月的结束时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar endOfMonth(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return endOfDay(calendar);
	}

	/**
	 * 获取某季度的开始时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfQuarter(Date date) {
		return new DateTime(beginOfQuarter(calendar(date)));
	}

	/**
	 * 获取某季度的结束时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfQuarter(Date date) {
		return new DateTime(endOfQuarter(calendar(date)));
	}

	/**
	 * 获取某季度的开始时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 * @since 4.1.0
	 */
	public static Calendar beginOfQuarter(Calendar calendar) {
		calendar.set(Calendar.MONTH, calendar.get(DateField.MONTH.getValue()) / 3 * 3);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return beginOfDay(calendar);
	}

	/**
	 * 获取某季度的结束时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 * @since 4.1.0
	 */
	public static Calendar endOfQuarter(Calendar calendar) {
		calendar.set(Calendar.MONTH, calendar.get(DateField.MONTH.getValue()) / 3 * 3 + 2);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return endOfDay(calendar);
	}

	/**
	 * 获取某年的开始时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfYear(Date date) {
		return new DateTime(beginOfYear(calendar(date)));
	}

	/**
	 * 获取某年的结束时间
	 * 
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfYear(Date date) {
		return new DateTime(endOfYear(calendar(date)));
	}

	/**
	 * 获取某年的开始时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar beginOfYear(Calendar calendar) {
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		return beginOfMonth(calendar);
	}

	/**
	 * 获取某年的结束时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return {@link Calendar}
	 */
	public static Calendar endOfYear(Calendar calendar) {
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		return endOfMonth(calendar);
	}

	// --------------------------------------------------- Offset for now
	/**
	 * 昨天
	 * 
	 * @return 昨天
	 */
	public static DateTime yesterday() {
		return offsetDay(new DateTime(), -1);
	}

	/**
	 * 明天
	 * 
	 * @return 明天
	 * @since 3.0.1
	 */
	public static DateTime tomorrow() {
		return offsetDay(new DateTime(), 1);
	}

	/**
	 * 上周
	 * 
	 * @return 上周
	 */
	public static DateTime lastWeek() {
		return offsetWeek(new DateTime(), -1);
	}

	/**
	 * 下周
	 * 
	 * @return 下周
	 * @since 3.0.1
	 */
	public static DateTime nextWeek() {
		return offsetWeek(new DateTime(), 1);
	}

	/**
	 * 上个月
	 * 
	 * @return 上个月
	 */
	public static DateTime lastMonth() {
		return offsetMonth(new DateTime(), -1);
	}

	/**
	 * 下个月
	 * 
	 * @return 下个月
	 * @since 3.0.1
	 */
	public static DateTime nextMonth() {
		return offsetMonth(new DateTime(), 1);
	}

	/**
	 * 偏移毫秒数
	 * 
	 * @param date 日期
	 * @param offset 偏移毫秒数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetMillisecond(Date date, int offset) {
		return offset(date, DateField.MILLISECOND, offset);
	}

	/**
	 * 偏移秒数
	 * 
	 * @param date 日期
	 * @param offset 偏移秒数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetSecond(Date date, int offset) {
		return offset(date, DateField.SECOND, offset);
	}

	/**
	 * 偏移分钟
	 * 
	 * @param date 日期
	 * @param offset 偏移分钟数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetMinute(Date date, int offset) {
		return offset(date, DateField.MINUTE, offset);
	}

	/**
	 * 偏移小时
	 * 
	 * @param date 日期
	 * @param offset 偏移小时数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetHour(Date date, int offset) {
		return offset(date, DateField.HOUR_OF_DAY, offset);
	}

	/**
	 * 偏移天
	 * 
	 * @param date 日期
	 * @param offset 偏移天数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetDay(Date date, int offset) {
		return offset(date, DateField.DAY_OF_YEAR, offset);
	}

	/**
	 * 偏移周
	 * 
	 * @param date 日期
	 * @param offset 偏移周数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetWeek(Date date, int offset) {
		return offset(date, DateField.WEEK_OF_YEAR, offset);
	}

	/**
	 * 偏移月
	 * 
	 * @param date 日期
	 * @param offset 偏移月数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetMonth(Date date, int offset) {
		return offset(date, DateField.MONTH, offset);
	}

	/**
	 * 获取指定日期偏移指定时间后的时间
	 * 
	 * @param date 基准日期
	 * @param dateField 偏移的粒度大小（小时、天、月等）{@link DateField}
	 * @param offset 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offset(Date date, DateField dateField, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(dateField.getValue(), offset);
		return new DateTime(cal.getTime());
	}

	/**
	 * 获取指定日期偏移指定时间后的时间
	 * 
	 * @param date 基准日期
	 * @param dateField 偏移的粒度大小（小时、天、月等）{@link DateField}
	 * @param offset 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 * @deprecated please use {@link DateUtil#offset(Date, DateField, int)}
	 */
	@Deprecated
	public static DateTime offsetDate(Date date, DateField dateField, int offset) {
		return offset(date, dateField, offset);
	}
	// ------------------------------------ Offset end ----------------------------------------------

	/**
	 * 判断两个日期相差的时长，只保留绝对值
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param unit 相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
	 * @return 日期差
	 */
	public static long between(Date beginDate, Date endDate, DateUnit unit) {
		return between(beginDate, endDate, unit, true);
	}

	/**
	 * 判断两个日期相差的时长
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param unit 相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
	 * @param isAbs 日期间隔是否只保留绝对值正数
	 * @return 日期差
	 * @since 3.3.1
	 */
	public static long between(Date beginDate, Date endDate, DateUnit unit, boolean isAbs) {
		return new DateBetween(beginDate, endDate, isAbs).between(unit);
	}

	/**
	 * 判断两个日期相差的毫秒数
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @return 日期差
	 * @since 3.0.1
	 */
	public static long betweenMs(Date beginDate, Date endDate) {
		return new DateBetween(beginDate, endDate).between(DateUnit.MS);
	}

	/**
	 * 判断两个日期相差的天数<br>
	 * 
	 * <pre>
	 * 有时候我们计算相差天数的时候需要忽略时分秒。
	 * 比如：2016-02-01 23:59:59和2016-02-02 00:00:00相差一秒
	 * 如果isReset为<code>false</code>相差天数为0。
	 * 如果isReset为<code>true</code>相差天数将被计算为1
	 * </pre>
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param isReset 是否重置时间为起始时间
	 * @return 日期差
	 * @since 3.0.1
	 */
	public static long betweenDay(Date beginDate, Date endDate, boolean isReset) {
		if (isReset) {
			beginDate = beginOfDay(beginDate);
			endDate = beginOfDay(endDate);
		}
		return between(beginDate, endDate, DateUnit.DAY);
	}

	/**
	 * 计算两个日期相差月数<br>
	 * 在非重置情况下，如果起始日期的天小于结束日期的天，月数要少算1（不足1个月）
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param isReset 是否重置时间为起始时间（重置天时分秒）
	 * @return 相差月数
	 * @since 3.0.8
	 */
	public static long betweenMonth(Date beginDate, Date endDate, boolean isReset) {
		return new DateBetween(beginDate, endDate).betweenMonth(isReset);
	}

	/**
	 * 计算两个日期相差年数<br>
	 * 在非重置情况下，如果起始日期的月小于结束日期的月，年数要少算1（不足1年）
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param isReset 是否重置时间为起始时间（重置月天时分秒）
	 * @return 相差年数
	 * @since 3.0.8
	 */
	public static long betweenYear(Date beginDate, Date endDate, boolean isReset) {
		return new DateBetween(beginDate, endDate).betweenYear(isReset);
	}

	/**
	 * 格式化日期间隔输出
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param level 级别，按照天、小时、分、秒、毫秒分为5个等级
	 * @return XX天XX小时XX分XX秒
	 */
	public static String formatBetween(Date beginDate, Date endDate, BetweenFormater.Level level) {
		return formatBetween(between(beginDate, endDate, DateUnit.MS), level);
	}

	/**
	 * 格式化日期间隔输出，精确到毫秒
	 * 
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @return XX天XX小时XX分XX秒
	 * @since 3.0.1
	 */
	public static String formatBetween(Date beginDate, Date endDate) {
		return formatBetween(between(beginDate, endDate, DateUnit.MS));
	}

	/**
	 * 格式化日期间隔输出
	 * 
	 * @param betweenMs 日期间隔
	 * @param level 级别，按照天、小时、分、秒、毫秒分为5个等级
	 * @return XX天XX小时XX分XX秒XX毫秒
	 */
	public static String formatBetween(long betweenMs, BetweenFormater.Level level) {
		return new BetweenFormater(betweenMs, level).format();
	}

	/**
	 * 格式化日期间隔输出，精确到毫秒
	 * 
	 * @param betweenMs 日期间隔
	 * @return XX天XX小时XX分XX秒XX毫秒
	 * @since 3.0.1
	 */
	public static String formatBetween(long betweenMs) {
		return new BetweenFormater(betweenMs, BetweenFormater.Level.MILLSECOND).format();
	}

	/**
	 * 当前日期是否在日期指定范围内<br>
	 * 起始日期和结束日期可以互换
	 * 
	 * @param date 被检查的日期
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @return 是否在范围内
	 * @since 3.0.8
	 */
	public static boolean isIn(Date date, Date beginDate, Date endDate) {
		if (date instanceof DateTime) {
			return ((DateTime) date).isIn(beginDate, endDate);
		} else {
			return new DateTime(date).isIn(beginDate, endDate);
		}
	}

	/**
	 * 计时，常用于记录某段代码的执行时间，单位：纳秒
	 * 
	 * @param preTime 之前记录的时间
	 * @return 时间差，纳秒
	 */
	public static long spendNt(long preTime) {
		return System.nanoTime() - preTime;
	}

	/**
	 * 计时，常用于记录某段代码的执行时间，单位：毫秒
	 * 
	 * @param preTime 之前记录的时间
	 * @return 时间差，毫秒
	 */
	public static long spendMs(long preTime) {
		return System.currentTimeMillis() - preTime;
	}

	/**
	 * 格式化成yyMMddHHmm后转换为int型
	 * 
	 * @param date 日期
	 * @return int
	 */
	public static int toIntSecond(Date date) {
		return Integer.parseInt(DateUtil.format(date, "yyMMddHHmm"));
	}

	/**
	 * 计算指定指定时间区间内的周数
	 * 
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 周数
	 */
	public static int weekCount(Date start, Date end) {
		final Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(start);
		final Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(end);

		final int startWeekofYear = startCalendar.get(Calendar.WEEK_OF_YEAR);
		final int endWeekofYear = endCalendar.get(Calendar.WEEK_OF_YEAR);

		int count = endWeekofYear - startWeekofYear + 1;

		if (Calendar.SUNDAY != startCalendar.get(Calendar.DAY_OF_WEEK)) {
			count--;
		}

		return count;
	}

	/**
	 * 计时器<br>
	 * 计算某个过程花费的时间，精确到毫秒
	 * 
	 * @return Timer
	 */
	public static TimeInterval timer() {
		return new TimeInterval();

	}

	/**
	 * 生日转为年龄，计算法定年龄
	 * 
	 * @param birthDay 生日，标准日期字符串
	 * @return 年龄
	 */
	public static int ageOfNow(String birthDay) {
		return ageOfNow(parse(birthDay));
	}

	/**
	 * 生日转为年龄，计算法定年龄
	 * 
	 * @param birthDay 生日
	 * @return 年龄
	 */
	public static int ageOfNow(Date birthDay) {
		return age(birthDay, date());
	}

	/**
	 * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
	 * 
	 * @param birthDay 生日
	 * @param dateToCompare 需要对比的日期
	 * @return 年龄
	 */
	public static int age(Date birthDay, Date dateToCompare) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateToCompare);

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException(StrUtil.format("Birthday is after date {}!", formatDate(dateToCompare)));
		}

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int age = year - cal.get(Calendar.YEAR);

		int monthBirth = cal.get(Calendar.MONTH);
		if (month == monthBirth) {
			int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
			if (dayOfMonth < dayOfMonthBirth) {
				// 如果生日在当月，但是未达到生日当天的日期，年龄减一
				age--;
			}
		} else if (month < monthBirth) {
			// 如果当前月份未达到生日的月份，年龄计算减一
			age--;
		}

		return age;
	}

	/**
	 * 是否闰年
	 * 
	 * @param year 年
	 * @return 是否闰年
	 */
	public static boolean isLeapYear(int year) {
		return new GregorianCalendar().isLeapYear(year);
	}

	/**
	 * 判定给定开始时间经过某段时间后是否过期
	 * 
	 * @param startDate 开始时间
	 * @param dateField 时间单位
	 * @param timeLength 时长
	 * @param checkedDate 被比较的时间。如果经过时长后的时间晚于被检查的时间，就表示过期
	 * @return 是否过期
	 * @since 3.1.1
	 */
	public static boolean isExpired(Date startDate, DateField dateField, int timeLength, Date checkedDate) {
		final Date endDate = offset(startDate, dateField, timeLength);
		return endDate.after(checkedDate);
	}

	/**
	 * 时间格式字符串转为秒数<br>
	 * 参考：https://github.com/iceroot
	 * 
	 * @param timeStr 字符串时分秒(HH:mm:ss)格式
	 * @return 时分秒转换后的秒数
	 * @since 3.1.2
	 */
	public static int timeToSecond(String timeStr) {
		if (StrUtil.isEmpty(timeStr)) {
			return 0;
		}

		final List<String> hms = StrUtil.splitTrim(timeStr, StrUtil.C_COLON, 3);
		int lastIndex = hms.size() - 1;

		int result = 0;
		for (int i = lastIndex; i >= 0; i--) {
			result += Integer.parseInt(hms.get(i)) * Math.pow(60, (lastIndex - i));
		}
		return result;
	}

	/**
	 * 秒数转为时间格式(HH:mm:ss)<br>
	 * 参考：https://github.com/iceroot
	 * 
	 * @param seconds 需要转换的秒数
	 * @return 转换后的字符串
	 * @since 3.1.2
	 */
	public static String secondToTime(int seconds) {
		if (seconds < 0) {
			throw new IllegalArgumentException("Seconds must be a positive number!");
		}

		int hour = seconds / 3600;
		int other = seconds % 3600;
		int minute = other / 60;
		int second = other % 60;
		final StringBuilder sb = new StringBuilder();
		if (hour < 10) {
			sb.append("0");
		}
		sb.append(hour);
		sb.append(":");
		if (minute < 10) {
			sb.append("0");
		}
		sb.append(minute);
		sb.append(":");
		if (second < 10) {
			sb.append("0");
		}
		sb.append(second);
		return sb.toString();
	}

	/**
	 * 创建日期范围生成器
	 * 
	 * @param start 起始日期时间
	 * @param end 结束日期时间
	 * @param unit 步进单位
	 * @return {@link DateRange}
	 */
	public static DateRange range(Date start, Date end, final DateField unit) {
		return new DateRange(start, end, unit);
	}

	/**
	 * 创建日期范围生成器
	 * 
	 * @param start 起始日期时间
	 * @param end 结束日期时间
	 * @param unit 步进单位
	 * @return {@link DateRange}
	 */
	public static List<DateTime> rangeToList(Date start, Date end, final DateField unit) {
		return CollUtil.newArrayList((Iterable<DateTime>) range(start, end, unit));
	}

	// ------------------------------------------------------------------------ Private method start
	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * 
	 * @param cal 日期
	 * @deprecated 请使用{@link yearAndQuarter}
	 */
	@Deprecated
	private static String yearAndSeason(Calendar cal) {
		return new StringBuilder().append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH) / 3 + 1).toString();
	}

	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * 
	 * @param cal 日期
	 */
	private static String yearAndQuarter(Calendar cal) {
		return new StringBuilder().append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH) / 3 + 1).toString();
	}

	/**
	 * 标准化日期，默认处理以空格区分的日期时间格式，空格前为日期，空格后为时间：<br>
	 * 将以下字符替换为"-"
	 * 
	 * <pre>
	 * "."
	 * "/"
	 * "年"
	 * "月"
	 * </pre>
	 * 
	 * 将以下字符去除
	 * 
	 * <pre>
	 * "日"
	 * </pre>
	 * 
	 * 将以下字符替换为":"
	 * 
	 * <pre>
	 * "时"
	 * "分"
	 * "秒"
	 * </pre>
	 * 
	 * 当末位是":"时去除之（不存在毫秒时）
	 * 
	 * @param dateStr 日期时间字符串
	 * @return 格式化后的日期字符串
	 */
	private static String normalize(String dateStr) {
		if (StrUtil.isBlank(dateStr)) {
			return dateStr;
		}

		// 日期时间分开处理
		final List<String> dateAndTime = StrUtil.splitTrim(dateStr, ' ');
		final int size = dateAndTime.size();
		if (size < 1 || size > 2) {
			// 非可被标准处理的格式
			return dateStr;
		}

		final StringBuilder builder = StrUtil.builder();

		// 日期部分（"\"、"/"、"."、"年"、"月"都替换为"-"）
		String datePart = dateAndTime.get(0).replaceAll("[\\/.年月]", "-");
		datePart = StrUtil.removeSuffix(datePart, "日");
		builder.append(datePart);

		// 时间部分
		if (size == 2) {
			builder.append(' ');
			String timePart = dateAndTime.get(1).replaceAll("[时分秒]", ":");
			timePart = StrUtil.removeSuffix(timePart, ":");
			builder.append(timePart);
		}

		return builder.toString();
	}
	// ------------------------------------------------------------------------ Private method end
}
