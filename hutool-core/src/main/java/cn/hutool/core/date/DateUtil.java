package cn.hutool.core.date;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.format.DateParser;
import cn.hutool.core.date.format.DatePrinter;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.date.format.GlobalCustomFormat;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.PatternPool;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 时间工具类
 *
 * @author xiaoleilu
 * @see LocalDateTimeUtil java8日志工具类
 * @see DatePattern 日期常用格式工具类
 */
public class DateUtil extends CalendarUtil {

	/**
	 * java.util.Date EEE MMM zzz 缩写数组
	 */
	private final static String[] wtb = { //
			"sun", "mon", "tue", "wed", "thu", "fri", "sat", // 星期
			"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec", // 月份
			"gmt", "ut", "utc", "est", "edt", "cst", "cdt", "mst", "mdt", "pst", "pdt"// 时间标准
	};

	/**
	 * 当前时间，转换为{@link DateTime}对象
	 *
	 * @return 当前时间
	 */
	public static DateTime date() {
		return new DateTime();
	}

	/**
	 * 当前时间，转换为{@link DateTime}对象，忽略毫秒部分
	 *
	 * @return 当前时间
	 * @since 4.6.2
	 */
	public static DateTime dateSecond() {
		return beginOfSecond(date());
	}

	/**
	 * {@link Date}类型时间转为{@link DateTime}<br>
	 * 如果date本身为DateTime对象，则返回强转后的对象，否则新建一个DateTime对象
	 *
	 * @param date Long类型Date（Unix时间戳）
	 * @return 时间对象
	 * @since 3.0.7
	 */
	public static DateTime date(Date date) {
		if (date instanceof DateTime) {
			return (DateTime) date;
		}
		return dateNew(date);
	}

	/**
	 * 根据已有{@link Date} 产生新的{@link DateTime}对象
	 *
	 * @param date Date对象
	 * @return {@link DateTime}对象
	 * @since 4.3.1
	 */
	public static DateTime dateNew(Date date) {
		return new DateTime(date);
	}

	/**
	 * Long类型时间转为{@link DateTime}<br>
	 * 只支持毫秒级别时间戳，如果需要秒级别时间戳，请自行×1000
	 *
	 * @param date Long类型Date（Unix时间戳）
	 * @return 时间对象
	 */
	public static DateTime date(long date) {
		return new DateTime(date);
	}

	/**
	 * {@link Calendar}类型时间转为{@link DateTime}<br>
	 * 始终根据已有{@link Calendar} 产生新的{@link DateTime}对象
	 *
	 * @param calendar {@link Calendar}
	 * @return 时间对象
	 */
	public static DateTime date(Calendar calendar) {
		return new DateTime(calendar);
	}

	/**
	 * {@link TemporalAccessor}类型时间转为{@link DateTime}<br>
	 * 始终根据已有{@link TemporalAccessor} 产生新的{@link DateTime}对象
	 *
	 * @param temporalAccessor {@link TemporalAccessor},常用子类： {@link LocalDateTime}、 LocalDate
	 * @return 时间对象
	 * @since 5.0.0
	 */
	public static DateTime date(TemporalAccessor temporalAccessor) {
		return new DateTime(temporalAccessor);
	}

	/**
	 * 当前时间的时间戳
	 *
	 * @return 时间
	 */
	public static long current() {
		return System.currentTimeMillis();
	}

	/**
	 * 当前时间的时间戳（秒）
	 *
	 * @return 当前时间秒数
	 * @since 4.0.0
	 */
	public static long currentSeconds() {
		return System.currentTimeMillis() / 1000;
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
	 * 此方法返回值与一周的第一天有关，比如：<br>
	 * 2016年1月3日为周日，如果一周的第一天为周日，那这天是第二周（返回2）<br>
	 * 如果一周的第一天为周一，那这天是第一周（返回1）<br>
	 * 跨年的那个星期得到的结果总是1
	 *
	 * @param date 日期
	 * @return 周
	 * @see DateTime#setFirstDayOfWeek(Week)
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
	 * 获得指定日期是这个日期所在年的第几天
	 *
	 * @param date 日期
	 * @return 天
	 * @since 5.3.6
	 */
	public static int dayOfYear(Date date) {
		return DateTime.of(date).dayOfYear();
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
	 * 是否为周末（周六或周日）
	 *
	 * @param date 判定的日期{@link Date}
	 * @return 是否为周末（周六或周日）
	 * @since 5.7.6
	 */
	public static boolean isWeekend(Date date) {
		final Week week = dayOfWeekEnum(date);
		return Week.SATURDAY == week || Week.SUNDAY == week;
	}

	/**
	 * 获得指定日期的小时数部分<br>
	 *
	 * @param date          日期
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
	public static int millisecond(Date date) {
		return DateTime.of(date).millisecond();
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
	 * @return 当前日期所在月份的第几周
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
	public static int thisMillisecond() {
		return millisecond(date());
	}
	// -------------------------------------------------------------- Part of Date end

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
	 * @param endDate   结束日期（包含）
	 * @return 季度列表 ，元素类似于 20132
	 */
	public static LinkedHashSet<String> yearAndQuarter(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return new LinkedHashSet<>(0);
		}
		return yearAndQuarter(startDate.getTime(), endDate.getTime());
	}
	// ------------------------------------ Format start ----------------------------------------------

	/**
	 * 格式化日期时间<br>
	 * 格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @param localDateTime 被格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String formatLocalDateTime(LocalDateTime localDateTime) {
		return LocalDateTimeUtil.formatNormal(localDateTime);
	}

	/**
	 * 根据特定格式格式化日期
	 *
	 * @param localDateTime 被格式化的日期
	 * @param format        日期格式，常用格式见： {@link DatePattern}
	 * @return 格式化后的字符串
	 */
	public static String format(LocalDateTime localDateTime, String format) {
		return LocalDateTimeUtil.format(localDateTime, format);
	}

	/**
	 * 根据特定格式格式化日期
	 *
	 * @param date   被格式化的日期
	 * @param format 日期格式，常用格式见： {@link DatePattern} {@link DatePattern#NORM_DATETIME_PATTERN}
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, String format) {
		if (null == date || StrUtil.isBlank(format)) {
			return null;
		}

		// 检查自定义格式
		if (GlobalCustomFormat.isCustomFormat(format)) {
			return GlobalCustomFormat.format(date, format);
		}

		TimeZone timeZone = null;
		if (date instanceof DateTime) {
			timeZone = ((DateTime) date).getTimeZone();
		}
		return format(date, newSimpleFormat(format, null, timeZone));
	}

	/**
	 * 根据特定格式格式化日期
	 *
	 * @param date   被格式化的日期
	 * @param format {@link DatePrinter} 或 {@link FastDateFormat} {@link DatePattern#NORM_DATETIME_FORMAT}
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
	 * @param date   被格式化的日期
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
	 * 根据特定格式格式化日期
	 *
	 * @param date   被格式化的日期
	 * @param format {@link SimpleDateFormat} {@link DatePattern#NORM_DATETIME_FORMATTER}
	 * @return 格式化后的字符串
	 * @since 5.0.0
	 */
	public static String format(Date date, DateTimeFormatter format) {
		if (null == format || null == date) {
			return null;
		}
		// java.time.temporal.UnsupportedTemporalTypeException: Unsupported field: YearOfEra
		// 出现以上报错时，表示Instant时间戳没有时区信息，赋予默认时区
		return TemporalAccessorUtil.format(date.toInstant(), format);
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
	 * 格式化为Http的标准日期格式<br>
	 * 标准日期格式遵循RFC 1123规范，格式类似于：Fri, 31 Dec 1999 23:59:59 GMT
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

	/**
	 * 格式化为中文日期格式，如果isUppercase为false，则返回类似：2018年10月24日，否则返回二〇一八年十月二十四日
	 *
	 * @param date        被格式化的日期
	 * @param isUppercase 是否采用大写形式
	 * @param withTime    是否包含时间部分
	 * @return 中文日期字符串
	 * @since 5.3.9
	 */
	public static String formatChineseDate(Date date, boolean isUppercase, boolean withTime) {
		if (null == date) {
			return null;
		}

		if (false == isUppercase) {
			return (withTime ? DatePattern.CHINESE_DATE_TIME_FORMAT : DatePattern.CHINESE_DATE_FORMAT).format(date);
		}

		return CalendarUtil.formatChineseDate(CalendarUtil.calendar(date), withTime);
	}
	// ------------------------------------ Format end ----------------------------------------------

	// ------------------------------------ Parse start ----------------------------------------------

	/**
	 * 构建LocalDateTime对象<br>
	 * 格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @param dateStr 时间字符串（带格式）
	 * @return LocalDateTime对象
	 */
	public static LocalDateTime parseLocalDateTime(CharSequence dateStr) {
		return parseLocalDateTime(dateStr, DatePattern.NORM_DATETIME_PATTERN);
	}

	/**
	 * 构建LocalDateTime对象
	 *
	 * @param dateStr 时间字符串（带格式）
	 * @param format  使用{@link DatePattern}定义的格式
	 * @return LocalDateTime对象
	 */
	public static LocalDateTime parseLocalDateTime(CharSequence dateStr, String format) {
		return LocalDateTimeUtil.parse(dateStr, format);
	}

	/**
	 * 构建DateTime对象
	 *
	 * @param dateStr    Date字符串
	 * @param dateFormat 格式化器 {@link SimpleDateFormat}
	 * @return DateTime对象
	 */
	public static DateTime parse(CharSequence dateStr, DateFormat dateFormat) {
		return new DateTime(dateStr, dateFormat);
	}

	/**
	 * 构建DateTime对象
	 *
	 * @param dateStr Date字符串
	 * @param parser  格式化器,{@link FastDateFormat}
	 * @return DateTime对象
	 */
	public static DateTime parse(CharSequence dateStr, DateParser parser) {
		return new DateTime(dateStr, parser);
	}

	/**
	 * 构建DateTime对象
	 *
	 * @param dateStr Date字符串
	 * @param parser  格式化器,{@link FastDateFormat}
	 * @param lenient 是否宽容模式
	 * @return DateTime对象
	 * @since 5.7.14
	 */
	public static DateTime parse(CharSequence dateStr, DateParser parser, boolean lenient) {
		return new DateTime(dateStr, parser, lenient);
	}

	/**
	 * 构建DateTime对象
	 *
	 * @param dateStr   Date字符串
	 * @param formatter 格式化器,{@link DateTimeFormatter}
	 * @return DateTime对象
	 * @since 5.0.0
	 */
	public static DateTime parse(CharSequence dateStr, DateTimeFormatter formatter) {
		return new DateTime(dateStr, formatter);
	}

	/**
	 * 将特定格式的日期转换为Date对象
	 *
	 * @param dateStr 特定格式的日期
	 * @param format  格式，例如yyyy-MM-dd
	 * @return 日期对象
	 */
	public static DateTime parse(CharSequence dateStr, String format) {
		return new DateTime(dateStr, format);
	}

	/**
	 * 将特定格式的日期转换为Date对象
	 *
	 * @param dateStr 特定格式的日期
	 * @param format  格式，例如yyyy-MM-dd
	 * @param locale  区域信息
	 * @return 日期对象
	 * @since 4.5.18
	 */
	public static DateTime parse(CharSequence dateStr, String format, Locale locale) {
		if (GlobalCustomFormat.isCustomFormat(format)) {
			// 自定义格式化器忽略Locale
			return new DateTime(GlobalCustomFormat.parse(dateStr, format));
		}
		return new DateTime(dateStr, DateUtil.newSimpleFormat(format, locale, null));
	}

	/**
	 * 通过给定的日期格式解析日期时间字符串。<br>
	 * 传入的日期格式会逐个尝试，直到解析成功，返回{@link DateTime}对象，否则抛出{@link DateException}异常。
	 *
	 * @param str           日期时间字符串，非空
	 * @param parsePatterns 需要尝试的日期时间格式数组，非空, 见SimpleDateFormat
	 * @return 解析后的Date
	 * @throws IllegalArgumentException if the date string or pattern array is null
	 * @throws DateException            if none of the date patterns were suitable
	 * @since 5.3.11
	 */
	public static DateTime parse(String str, String... parsePatterns) throws DateException {
		return new DateTime(CalendarUtil.parseByPatterns(str, parsePatterns));
	}

	/**
	 * 解析日期时间字符串，格式支持：
	 *
	 * <pre>
	 * yyyy-MM-dd HH:mm:ss
	 * yyyy/MM/dd HH:mm:ss
	 * yyyy.MM.dd HH:mm:ss
	 * yyyy年MM月dd日 HH:mm:ss
	 * </pre>
	 *
	 * @param dateString 标准形式的时间字符串
	 * @return 日期对象
	 */
	public static DateTime parseDateTime(CharSequence dateString) {
		dateString = normalize(dateString);
		return parse(dateString, DatePattern.NORM_DATETIME_FORMAT);
	}

	/**
	 * 解析日期字符串，忽略时分秒，支持的格式包括：
	 * <pre>
	 * yyyy-MM-dd
	 * yyyy/MM/dd
	 * yyyy.MM.dd
	 * yyyy年MM月dd日
	 * </pre>
	 *
	 * @param dateString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseDate(CharSequence dateString) {
		dateString = normalize(dateString);
		return parse(dateString, DatePattern.NORM_DATE_FORMAT);
	}

	/**
	 * 解析时间，格式HH:mm:ss，日期部分默认为1970-01-01
	 *
	 * @param timeString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseTime(CharSequence timeString) {
		timeString = normalize(timeString);
		return parse(timeString, DatePattern.NORM_TIME_FORMAT);
	}

	/**
	 * 解析时间，格式HH:mm 或 HH:mm:ss，日期默认为今天
	 *
	 * @param timeString 标准形式的日期字符串
	 * @return 日期对象
	 * @since 3.1.1
	 */
	public static DateTime parseTimeToday(CharSequence timeString) {
		timeString = StrUtil.format("{} {}", today(), timeString);
		if (1 == StrUtil.count(timeString, ':')) {
			// 时间格式为 HH:mm
			return parse(timeString, DatePattern.NORM_DATETIME_MINUTE_PATTERN);
		} else {
			// 时间格式为 HH:mm:ss
			return parse(timeString, DatePattern.NORM_DATETIME_FORMAT);
		}
	}

	/**
	 * 解析UTC时间，格式：<br>
	 * <ol>
	 * <li>yyyy-MM-dd'T'HH:mm:ss'Z'</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss.SSS'Z'</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ssZ</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss.SSSZ</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss+0800</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss+08:00</li>
	 * </ol>
	 *
	 * @param utcString UTC时间
	 * @return 日期对象
	 * @since 4.1.14
	 */
	public static DateTime parseUTC(String utcString) {
		if (utcString == null) {
			return null;
		}
		final int length = utcString.length();
		if (StrUtil.contains(utcString, 'Z')) {
			if (length == DatePattern.UTC_PATTERN.length() - 4) {
				// 格式类似：2018-09-13T05:34:31Z，-4表示减去4个单引号的长度
				return parse(utcString, DatePattern.UTC_FORMAT);
			}

			final int patternLength = DatePattern.UTC_MS_PATTERN.length();
			// 格式类似：2018-09-13T05:34:31.999Z，-4表示减去4个单引号的长度
			// -4 ~ -6范围表示匹配毫秒1~3位的情况
			if (length <= patternLength - 4 && length >= patternLength - 6) {
				return parse(utcString, DatePattern.UTC_MS_FORMAT);
			}
		} else if (StrUtil.contains(utcString, '+')) {
			// 去除类似2019-06-01T19:45:43 +08:00加号前的空格
			utcString = utcString.replace(" +", "+");
			final String zoneOffset = StrUtil.subAfter(utcString, '+', true);
			if (StrUtil.isBlank(zoneOffset)) {
				throw new DateException("Invalid format: [{}]", utcString);
			}
			if (false == StrUtil.contains(zoneOffset, ':')) {
				// +0800转换为+08:00
				final String pre = StrUtil.subBefore(utcString, '+', true);
				utcString = pre + "+" + zoneOffset.substring(0, 2) + ":" + "00";
			}

			if (StrUtil.contains(utcString, CharUtil.DOT)) {
				// 带毫秒，格式类似：2018-09-13T05:34:31.999+08:00
				return parse(utcString, DatePattern.UTC_MS_WITH_XXX_OFFSET_FORMAT);
			} else {
				// 格式类似：2018-09-13T05:34:31+08:00
				return parse(utcString, DatePattern.UTC_WITH_XXX_OFFSET_FORMAT);
			}
		} else if(ReUtil.contains("-\\d{2}:?00", utcString)){
			// Issue#2612，类似 2022-09-14T23:59:00-08:00 或者 2022-09-14T23:59:00-0800

			// 去除类似2019-06-01T19:45:43 -08:00加号前的空格
			utcString = utcString.replace(" -", "-");
			if(':' != utcString.charAt(utcString.length() - 3)){
				utcString = utcString.substring(0, utcString.length() - 2) + ":00";
			}

			if (StrUtil.contains(utcString, CharUtil.DOT)) {
				// 带毫秒，格式类似：2018-09-13T05:34:31.999-08:00
				return new DateTime(utcString, DatePattern.UTC_MS_WITH_XXX_OFFSET_FORMAT);
			} else {
				// 格式类似：2018-09-13T05:34:31-08:00
				return new DateTime(utcString, DatePattern.UTC_WITH_XXX_OFFSET_FORMAT);
			}
		} else {
			if (length == DatePattern.UTC_SIMPLE_PATTERN.length() - 2) {
				// 格式类似：2018-09-13T05:34:31
				return parse(utcString, DatePattern.UTC_SIMPLE_FORMAT);
			} else if (length == DatePattern.UTC_SIMPLE_PATTERN.length() - 5) {
				// 格式类似：2018-09-13T05:34
				return parse(utcString + ":00", DatePattern.UTC_SIMPLE_FORMAT);
			} else if (StrUtil.contains(utcString, CharUtil.DOT)) {
				// 可能为：  2021-03-17T06:31:33.99
				return parse(utcString, DatePattern.UTC_SIMPLE_MS_FORMAT);
			}
		}
		// 没有更多匹配的时间格式
		throw new DateException("No format fit for date String [{}] !", utcString);
	}

	/**
	 * 解析CST时间，格式：<br>
	 * <ol>
	 * <li>EEE MMM dd HH:mm:ss z yyyy（例如：Wed Aug 01 00:00:00 CST 2012）</li>
	 * </ol>
	 *
	 * @param cstString UTC时间
	 * @return 日期对象
	 * @since 4.6.9
	 */
	public static DateTime parseCST(CharSequence cstString) {
		if (cstString == null) {
			return null;
		}

		return parse(cstString, DatePattern.JDK_DATETIME_FORMAT);
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
	 * <li>yyyy-MM-dd HH:mm:ss.SSSSSS</li>
	 * <li>yyyyMMddHHmmss</li>
	 * <li>yyyyMMddHHmmssSSS</li>
	 * <li>yyyyMMdd</li>
	 * <li>EEE, dd MMM yyyy HH:mm:ss z</li>
	 * <li>EEE MMM dd HH:mm:ss zzz yyyy</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss'Z'</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss.SSS'Z'</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ssZ</li>
	 * <li>yyyy-MM-dd'T'HH:mm:ss.SSSZ</li>
	 * </ol>
	 *
	 * @param dateCharSequence 日期字符串
	 * @return 日期
	 */
	public static DateTime parse(CharSequence dateCharSequence) {
		if (StrUtil.isBlank(dateCharSequence)) {
			return null;
		}
		String dateStr = dateCharSequence.toString();
		// 去掉两边空格并去掉中文日期中的“日”和“秒”，以规范长度
		dateStr = StrUtil.removeAll(dateStr.trim(), '日', '秒');
		int length = dateStr.length();

		if (NumberUtil.isNumber(dateStr)) {
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
		} else if (ReUtil.isMatch(PatternPool.TIME, dateStr)) {
			// HH:mm:ss 或者 HH:mm 时间格式匹配单独解析
			return parseTimeToday(dateStr);
		} else if (StrUtil.containsAnyIgnoreCase(dateStr, wtb)) {
			// JDK的Date对象toString默认格式，类似于：
			// Tue Jun 4 16:25:15 +0800 2019
			// Thu May 16 17:57:18 GMT+08:00 2019
			// Wed Aug 01 00:00:00 CST 2012
			return parseCST(dateStr);
		} else if (StrUtil.contains(dateStr, 'T')) {
			// UTC时间
			return parseUTC(dateStr);
		}

		//标准日期格式（包括单个数字的日期时间）
		dateStr = normalize(dateStr);
		if (ReUtil.isMatch(DatePattern.REGEX_NORM, dateStr)) {
			final int colonCount = StrUtil.count(dateStr, CharUtil.COLON);
			switch (colonCount) {
				case 0:
					// yyyy-MM-dd
					return parse(dateStr, DatePattern.NORM_DATE_FORMAT);
				case 1:
					// yyyy-MM-dd HH:mm
					return parse(dateStr, DatePattern.NORM_DATETIME_MINUTE_FORMAT);
				case 2:
					final int indexOfDot = StrUtil.indexOf(dateStr, CharUtil.DOT);
					if (indexOfDot > 0) {
						final int length1 = dateStr.length();
						// yyyy-MM-dd HH:mm:ss.SSS 或者 yyyy-MM-dd HH:mm:ss.SSSSSS
						if (length1 - indexOfDot > 4) {
							// 类似yyyy-MM-dd HH:mm:ss.SSSSSS，采取截断操作
							dateStr = StrUtil.subPre(dateStr, indexOfDot + 4);
						}
						return parse(dateStr, DatePattern.NORM_DATETIME_MS_FORMAT);
					}
					// yyyy-MM-dd HH:mm:ss
					return parse(dateStr, DatePattern.NORM_DATETIME_FORMAT);
			}
		}

		// 没有更多匹配的时间格式
		throw new DateException("No format fit for date String [{}] !", dateStr);
	}

	// ------------------------------------ Parse end ----------------------------------------------

	// ------------------------------------ Offset start ----------------------------------------------

	/**
	 * 修改日期为某个时间字段起始时间
	 *
	 * @param date      {@link Date}
	 * @param dateField 保留到的时间字段，如定义为 {@link DateField#SECOND}，表示这个字段不变，这个字段以下字段全部归0
	 * @return {@link DateTime}
	 * @since 4.5.7
	 */
	public static DateTime truncate(Date date, DateField dateField) {
		return new DateTime(truncate(calendar(date), dateField));
	}

	/**
	 * 修改日期为某个时间字段四舍五入时间
	 *
	 * @param date      {@link Date}
	 * @param dateField 时间字段
	 * @return {@link DateTime}
	 * @since 4.5.7
	 */
	public static DateTime round(Date date, DateField dateField) {
		return new DateTime(round(calendar(date), dateField));
	}

	/**
	 * 修改日期为某个时间字段结束时间
	 *
	 * @param date      {@link Date}
	 * @param dateField 保留到的时间字段，如定义为 {@link DateField#SECOND}，表示这个字段不变，这个字段以下字段全部取最大值
	 * @return {@link DateTime}
	 * @since 4.5.7
	 */
	public static DateTime ceiling(Date date, DateField dateField) {
		return new DateTime(ceiling(calendar(date), dateField));
	}

	/**
	 * 修改日期为某个时间字段结束时间<br>
	 * 可选是否归零毫秒。
	 *
	 * <p>
	 * 有时候由于毫秒部分必须为0（如MySQL数据库中），因此在此加上选项。
	 * </p>
	 *
	 * @param date                {@link Date}
	 * @param dateField           时间字段
	 * @param truncateMillisecond 是否毫秒归零
	 * @return {@link DateTime}
	 * @since 4.5.7
	 */
	public static DateTime ceiling(Date date, DateField dateField, boolean truncateMillisecond) {
		return new DateTime(ceiling(calendar(date), dateField, truncateMillisecond));
	}

	/**
	 * 获取秒级别的开始时间，即毫秒部分设置为0
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 * @since 4.6.2
	 */
	public static DateTime beginOfSecond(Date date) {
		return new DateTime(beginOfSecond(calendar(date)));
	}

	/**
	 * 获取秒级别的结束时间，即毫秒设置为999
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 * @since 4.6.2
	 */
	public static DateTime endOfSecond(Date date) {
		return new DateTime(endOfSecond(calendar(date)));
	}

	/**
	 * 获取某小时的开始时间
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfHour(Date date) {
		return new DateTime(beginOfHour(calendar(date)));
	}

	/**
	 * 获取某小时的结束时间
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfHour(Date date) {
		return new DateTime(endOfHour(calendar(date)));
	}

	/**
	 * 获取某分钟的开始时间
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfMinute(Date date) {
		return new DateTime(beginOfMinute(calendar(date)));
	}

	/**
	 * 获取某分钟的结束时间
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfMinute(Date date) {
		return new DateTime(endOfMinute(calendar(date)));
	}

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
	 * 获取某周的开始时间，周一定为一周的开始时间
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime beginOfWeek(Date date) {
		return new DateTime(beginOfWeek(calendar(date)));
	}

	/**
	 * 获取某周的开始时间
	 *
	 * @param date               日期
	 * @param isMondayAsFirstDay 是否周一做为一周的第一天（false表示周日做为第一天）
	 * @return {@link DateTime}
	 * @since 5.4.0
	 */
	public static DateTime beginOfWeek(Date date, boolean isMondayAsFirstDay) {
		return new DateTime(beginOfWeek(calendar(date), isMondayAsFirstDay));
	}

	/**
	 * 获取某周的结束时间，周日定为一周的结束
	 *
	 * @param date 日期
	 * @return {@link DateTime}
	 */
	public static DateTime endOfWeek(Date date) {
		return new DateTime(endOfWeek(calendar(date)));
	}

	/**
	 * 获取某周的结束时间
	 *
	 * @param date              日期
	 * @param isSundayAsLastDay 是否周日做为一周的最后一天（false表示周六做为最后一天）
	 * @return {@link DateTime}
	 * @since 5.4.0
	 */
	public static DateTime endOfWeek(Date date, boolean isSundayAsLastDay) {
		return new DateTime(endOfWeek(calendar(date), isSundayAsLastDay));
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
	 * @param date   日期
	 * @param offset 偏移毫秒数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetMillisecond(Date date, int offset) {
		return offset(date, DateField.MILLISECOND, offset);
	}

	/**
	 * 偏移秒数
	 *
	 * @param date   日期
	 * @param offset 偏移秒数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetSecond(Date date, int offset) {
		return offset(date, DateField.SECOND, offset);
	}

	/**
	 * 偏移分钟
	 *
	 * @param date   日期
	 * @param offset 偏移分钟数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetMinute(Date date, int offset) {
		return offset(date, DateField.MINUTE, offset);
	}

	/**
	 * 偏移小时
	 *
	 * @param date   日期
	 * @param offset 偏移小时数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetHour(Date date, int offset) {
		return offset(date, DateField.HOUR_OF_DAY, offset);
	}

	/**w
	 * 偏移天
	 *
	 * @param date   日期
	 * @param offset 偏移天数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetDay(Date date, int offset) {
		return offset(date, DateField.DAY_OF_YEAR, offset);
	}

	/**
	 * 偏移周
	 *
	 * @param date   日期
	 * @param offset 偏移周数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetWeek(Date date, int offset) {
		return offset(date, DateField.WEEK_OF_YEAR, offset);
	}

	/**
	 * 偏移月
	 *
	 * @param date   日期
	 * @param offset 偏移月数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsetMonth(Date date, int offset) {
		return offset(date, DateField.MONTH, offset);
	}

	/**
	 * 获取指定日期偏移指定时间后的时间，生成的偏移日期不影响原日期
	 *
	 * @param date      基准日期
	 * @param dateField 偏移的粒度大小（小时、天、月等）{@link DateField}
	 * @param offset    偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offset(Date date, DateField dateField, int offset) {
		return dateNew(date).offset(dateField, offset);
	}

	// ------------------------------------ Offset end ----------------------------------------------

	/**
	 * 判断两个日期相差的时长，只保留绝对值
	 *
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
	 * @param unit      相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
	 * @return 日期差
	 */
	public static long between(Date beginDate, Date endDate, DateUnit unit) {
		return between(beginDate, endDate, unit, true);
	}

	/**
	 * 判断两个日期相差的时长
	 *
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
	 * @param unit      相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
	 * @param isAbs     日期间隔是否只保留绝对值正数
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
	 * @param endDate   结束日期
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
	 * 如果isReset为{@code false}相差天数为0。
	 * 如果isReset为{@code true}相差天数将被计算为1
	 * </pre>
	 *
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
	 * @param isReset   是否重置时间为起始时间
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
	 * 计算指定时间区间内的周数
	 *
	 * @param beginDate 开始时间
	 * @param endDate   结束时间
	 * @param isReset   是否重置时间为起始时间
	 * @return 周数
	 */
	public static long betweenWeek(Date beginDate, Date endDate, boolean isReset) {
		if (isReset) {
			beginDate = beginOfDay(beginDate);
			endDate = beginOfDay(endDate);
		}
		return between(beginDate, endDate, DateUnit.WEEK);
	}

	/**
	 * 计算两个日期相差月数<br>
	 * 在非重置情况下，如果起始日期的天大于结束日期的天，月数要少算1（不足1个月）
	 *
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
	 * @param isReset   是否重置时间为起始时间（重置天时分秒）
	 * @return 相差月数
	 * @since 3.0.8
	 */
	public static long betweenMonth(Date beginDate, Date endDate, boolean isReset) {
		return new DateBetween(beginDate, endDate).betweenMonth(isReset);
	}

	/**
	 * 计算两个日期相差年数<br>
	 * 在非重置情况下，如果起始日期的月大于结束日期的月，年数要少算1（不足1年）
	 *
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
	 * @param isReset   是否重置时间为起始时间（重置月天时分秒）
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
	 * @param endDate   结束日期
	 * @param level     级别，按照天、小时、分、秒、毫秒分为5个等级
	 * @return XX天XX小时XX分XX秒
	 */
	public static String formatBetween(Date beginDate, Date endDate, BetweenFormatter.Level level) {
		return formatBetween(between(beginDate, endDate, DateUnit.MS), level);
	}

	/**
	 * 格式化日期间隔输出，精确到毫秒
	 *
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
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
	 * @param level     级别，按照天、小时、分、秒、毫秒分为5个等级
	 * @return XX天XX小时XX分XX秒XX毫秒
	 */
	public static String formatBetween(long betweenMs, BetweenFormatter.Level level) {
		return new BetweenFormatter(betweenMs, level).format();
	}

	/**
	 * 格式化日期间隔输出，精确到毫秒
	 *
	 * @param betweenMs 日期间隔
	 * @return XX天XX小时XX分XX秒XX毫秒
	 * @since 3.0.1
	 */
	public static String formatBetween(long betweenMs) {
		return new BetweenFormatter(betweenMs, BetweenFormatter.Level.MILLISECOND).format();
	}

	/**
	 * 当前日期是否在日期指定范围内<br>
	 * 起始日期和结束日期可以互换
	 *
	 * @param date      被检查的日期
	 * @param beginDate 起始日期（包含）
	 * @param endDate   结束日期（包含）
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
	 * 是否为相同时间<br>
	 * 此方法比较两个日期的时间戳是否相同
	 *
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 是否为相同时间
	 * @since 4.1.13
	 */
	public static boolean isSameTime(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}

	/**
	 * 比较两个日期是否为同一天
	 *
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 是否为同一天
	 * @since 4.1.13
	 */
	public static boolean isSameDay(final Date date1, final Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return CalendarUtil.isSameDay(calendar(date1), calendar(date2));
	}

	/**
	 * 比较两个日期是否为同一周
	 *
	 * @param date1 日期1
	 * @param date2 日期2
	 * @param isMon 是否为周一。国内第一天为星期一，国外第一天为星期日
	 * @return 是否为同一周
	 */
	public static boolean isSameWeek(final Date date1, final Date date2, boolean isMon) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return CalendarUtil.isSameWeek(calendar(date1), calendar(date2), isMon);
	}

	/**
	 * 比较两个日期是否为同一月
	 *
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 是否为同一月
	 * @since 5.4.1
	 */
	public static boolean isSameMonth(final Date date1, final Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return CalendarUtil.isSameMonth(calendar(date1), calendar(date2));
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
	 * @deprecated 2022年后结果溢出，此方法废弃
	 */
	@Deprecated
	public static int toIntSecond(Date date) {
		return Integer.parseInt(DateUtil.format(date, "yyMMddHHmm"));
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
	 * 计时器<br>
	 * 计算某个过程花费的时间，精确到毫秒
	 *
	 * @param isNano 是否使用纳秒计数，false则使用毫秒
	 * @return Timer
	 * @since 5.2.3
	 */
	public static TimeInterval timer(boolean isNano) {
		return new TimeInterval(isNano);
	}

	/**
	 * 创建秒表{@link StopWatch}，用于对代码块的执行时间计数
	 * <p>
	 * 使用方法如下：
	 *
	 * <pre>
	 * StopWatch stopWatch = DateUtil.createStopWatch();
	 *
	 * // 任务1
	 * stopWatch.start("任务一");
	 * Thread.sleep(1000);
	 * stopWatch.stop();
	 *
	 * // 任务2
	 * stopWatch.start("任务二");
	 * Thread.sleep(2000);
	 * stopWatch.stop();
	 *
	 * // 打印出耗时
	 * Console.log(stopWatch.prettyPrint());
	 *
	 * </pre>
	 *
	 * @return {@link StopWatch}
	 * @since 5.2.3
	 */
	public static StopWatch createStopWatch() {
		return new StopWatch();
	}

	/**
	 * 创建秒表{@link StopWatch}，用于对代码块的执行时间计数
	 * <p>
	 * 使用方法如下：
	 *
	 * <pre>
	 * StopWatch stopWatch = DateUtil.createStopWatch("任务名称");
	 *
	 * // 任务1
	 * stopWatch.start("任务一");
	 * Thread.sleep(1000);
	 * stopWatch.stop();
	 *
	 * // 任务2
	 * stopWatch.start("任务二");
	 * Thread.sleep(2000);
	 * stopWatch.stop();
	 *
	 * // 打印出耗时
	 * Console.log(stopWatch.prettyPrint());
	 *
	 * </pre>
	 *
	 * @param id 用于标识秒表的唯一ID
	 * @return {@link StopWatch}
	 * @since 5.2.3
	 */
	public static StopWatch createStopWatch(String id) {
		return new StopWatch(id);
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
	 * 是否闰年
	 *
	 * @param year 年
	 * @return 是否闰年
	 */
	public static boolean isLeapYear(int year) {
		return Year.isLeap(year);
	}

	/**
	 * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
	 *
	 * @param birthday      生日
	 * @param dateToCompare 需要对比的日期
	 * @return 年龄
	 */
	public static int age(Date birthday, Date dateToCompare) {
		Assert.notNull(birthday, "Birthday can not be null !");
		if (null == dateToCompare) {
			dateToCompare = date();
		}
		return age(birthday.getTime(), dateToCompare.getTime());
	}

	/**
	 * 判定给定开始时间经过某段时间后是否过期
	 *
	 * @param startDate  开始时间
	 * @param dateField  时间单位
	 * @param timeLength 实际经过时长
	 * @param endDate    被比较的时间，即有效期的截止时间。如果经过时长后的时间晚于截止时间，就表示过期
	 * @return 是否过期
	 * @since 3.1.1
	 * @deprecated 此方法存在一定的歧义，容易产生误导，废弃。
	 */
	@Deprecated
	public static boolean isExpired(Date startDate, DateField dateField, int timeLength, Date endDate) {
		final Date offsetDate = offset(startDate, dateField, timeLength);
		return offsetDate.after(endDate);
	}

	/**
	 * 判定在指定检查时间是否过期。
	 *
	 * <p>
	 * 以商品为例，startDate即生产日期，endDate即保质期的截止日期，checkDate表示在何时检查是否过期（一般为当前时间）<br>
	 * endDate和startDate的差值即为保质期（按照毫秒计），checkDate和startDate的差值即为实际经过的时长，实际时长大于保质期表示超时。
	 * </p>
	 *
	 * @param startDate 开始时间
	 * @param endDate   被比较的时间，即有效期的截止时间。如果经过时长后的时间晚于被检查的时间，就表示过期
	 * @param checkDate 检查时间，可以是当前时间，既
	 * @return 是否过期
	 * @since 5.1.1
	 * @deprecated 使用isIn方法
	 */
	@Deprecated
	public static boolean isExpired(Date startDate, Date endDate, Date checkDate) {
		return betweenMs(startDate, checkDate) > betweenMs(startDate, endDate);
	}

	/**
	 * HH:mm:ss 时间格式字符串转为秒数<br>
	 * 参考：<a href="https://github.com/iceroot">https://github.com/iceroot</a>
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
	 * 参考：<a href="https://github.com/iceroot">https://github.com/iceroot</a>
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
	 * @param start 起始日期时间（包括）
	 * @param end   结束日期时间
	 * @param unit  步进单位
	 * @return {@link DateRange}
	 */
	public static DateRange range(Date start, Date end, final DateField unit) {
		return new DateRange(start, end, unit);
	}

	/**
	 * 俩个时间区间取交集
	 *
	 * @param start 开始区间
	 * @param end   结束区间
	 * @return true 包含
	 * @author handy
	 * @since 5.7.21
	 */
	public static List<DateTime> rangeContains(DateRange start, DateRange end) {
		List<DateTime> startDateTimes = CollUtil.newArrayList((Iterable<DateTime>) start);
		List<DateTime> endDateTimes = CollUtil.newArrayList((Iterable<DateTime>) end);
		return startDateTimes.stream().filter(endDateTimes::contains).collect(Collectors.toList());
	}

	/**
	 * 俩个时间区间取差集(end - start)
	 *
	 * @param start 开始区间
	 * @param end   结束区间
	 * @return true 包含
	 * @author handy
	 * @since 5.7.21
	 */
	public static List<DateTime> rangeNotContains(DateRange start, DateRange end) {
		List<DateTime> startDateTimes = CollUtil.newArrayList((Iterable<DateTime>) start);
		List<DateTime> endDateTimes = CollUtil.newArrayList((Iterable<DateTime>) end);
		return endDateTimes.stream().filter(item -> !startDateTimes.contains(item)).collect(Collectors.toList());
	}

	/**
	 * 按日期范围遍历，执行 function
	 *
	 * @param start 起始日期时间（包括）
	 * @param end   结束日期时间
	 * @param unit  步进单位
	 * @param func  每次遍历要执行的 function
	 * @param <T>   Date经过函数处理结果类型
	 * @return 结果列表
	 * @since 5.7.21
	 */
	public static <T> List<T> rangeFunc(Date start, Date end, final DateField unit, Function<Date, T> func) {
		if (start == null || end == null || start.after(end)) {
			return Collections.emptyList();
		}
		ArrayList<T> list = new ArrayList<>();
		for (DateTime date : range(start, end, unit)) {
			list.add(func.apply(date));
		}
		return list;
	}

	/**
	 * 按日期范围遍历，执行 consumer
	 *
	 * @param start    起始日期时间（包括）
	 * @param end      结束日期时间
	 * @param unit     步进单位
	 * @param consumer 每次遍历要执行的 consumer
	 * @since 5.7.21
	 */
	public static void rangeConsume(Date start, Date end, final DateField unit, Consumer<Date> consumer) {
		if (start == null || end == null || start.after(end)) {
			return;
		}
		range(start, end, unit).forEach(consumer);
	}

	/**
	 * 根据步进单位获取起始日期时间和结束日期时间的时间区间集合
	 *
	 * @param start 起始日期时间
	 * @param end   结束日期时间
	 * @param unit  步进单位
	 * @return {@link DateRange}
	 */
	public static List<DateTime> rangeToList(Date start, Date end, DateField unit) {
		return CollUtil.newArrayList((Iterable<DateTime>) range(start, end, unit));
	}

	/**
	 * 根据步进单位和步进获取起始日期时间和结束日期时间的时间区间集合
	 *
	 * @param start 起始日期时间
	 * @param end   结束日期时间
	 * @param unit  步进单位
	 * @param step  步进
	 * @return {@link DateRange}
	 * @since 5.7.16
	 */
	public static List<DateTime> rangeToList(Date start, Date end, final DateField unit, int step) {
		return CollUtil.newArrayList((Iterable<DateTime>) new DateRange(start, end, unit, step));
	}

	/**
	 * 通过生日计算星座
	 *
	 * @param month 月，从0开始计数
	 * @param day   天
	 * @return 星座名
	 * @since 4.4.3
	 */
	public static String getZodiac(int month, int day) {
		return Zodiac.getZodiac(month, day);
	}

	/**
	 * 计算生肖，只计算1900年后出生的人
	 *
	 * @param year 农历年
	 * @return 生肖名
	 * @since 4.4.3
	 */
	public static String getChineseZodiac(int year) {
		return Zodiac.getChineseZodiac(year);
	}

	/**
	 * {@code null}安全的日期比较，{@code null}对象排在末尾
	 *
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 比较结果，如果date1 &lt; date2，返回数小于0，date1==date2返回0，date1 &gt; date2 大于0
	 * @since 4.6.2
	 */
	public static int compare(Date date1, Date date2) {
		return CompareUtil.compare(date1, date2);
	}

	/**
	 * {@code null}安全的日期比较，并只比较指定格式； {@code null}对象排在末尾, 并指定日期格式；
	 *
	 * @param date1  日期1
	 * @param date2  日期2
	 * @param format 日期格式，常用格式见： {@link DatePattern}; 允许为空； date1 date2; eg: yyyy-MM-dd
	 * @return 比较结果，如果date1 &lt; date2，返回数小于0，date1==date2返回0，date1 &gt; date2 大于0
	 * @author dazer
	 * @since 5.6.4
	 */
	public static int compare(Date date1, Date date2, String format) {
		if (format != null) {
			if (date1 != null) {
				date1 = parse(format(date1, format), format);
			}
			if (date2 != null) {
				date2 = parse(format(date2, format), format);
			}
		}
		return CompareUtil.compare(date1, date2);
	}

	/**
	 * 纳秒转毫秒
	 *
	 * @param duration 时长
	 * @return 时长毫秒
	 * @since 4.6.6
	 */
	public static long nanosToMillis(long duration) {
		return TimeUnit.NANOSECONDS.toMillis(duration);
	}

	/**
	 * 纳秒转秒，保留小数
	 *
	 * @param duration 时长
	 * @return 秒
	 * @since 4.6.6
	 */
	public static double nanosToSeconds(long duration) {
		return duration / 1_000_000_000.0;
	}

	/**
	 * Date对象转换为{@link Instant}对象
	 *
	 * @param date Date对象
	 * @return {@link Instant}对象
	 * @since 5.0.2
	 */
	public static Instant toInstant(Date date) {
		return null == date ? null : date.toInstant();
	}

	/**
	 * Date对象转换为{@link Instant}对象
	 *
	 * @param temporalAccessor Date对象
	 * @return {@link Instant}对象
	 * @since 5.0.2
	 */
	public static Instant toInstant(TemporalAccessor temporalAccessor) {
		return TemporalAccessorUtil.toInstant(temporalAccessor);
	}

	/**
	 * {@link Instant} 转换为 {@link LocalDateTime}，使用系统默认时区
	 *
	 * @param instant {@link Instant}
	 * @return {@link LocalDateTime}
	 * @see LocalDateTimeUtil#of(Instant)
	 * @since 5.0.5
	 */
	public static LocalDateTime toLocalDateTime(Instant instant) {
		return LocalDateTimeUtil.of(instant);
	}

	/**
	 * {@link Date} 转换为 {@link LocalDateTime}，使用系统默认时区
	 *
	 * @param date {@link Date}
	 * @return {@link LocalDateTime}
	 * @see LocalDateTimeUtil#of(Date)
	 * @since 5.0.5
	 */
	public static LocalDateTime toLocalDateTime(Date date) {
		return LocalDateTimeUtil.of(date);
	}

	/**
	 * {@link Date} 转换时区
	 *
	 * @param date         {@link Date}
	 * @param zoneId       {@link ZoneId}
	 * @return {@link DateTime}
	 * @since 5.8.3
	 */
	public static DateTime convertTimeZone(Date date, ZoneId zoneId) {
		return new DateTime(date, ZoneUtil.toTimeZone(zoneId));
	}

	/**
	 * {@link Date} 转换时区
	 *
	 * @param date           {@link Date}
	 * @param timeZone       {@link TimeZone}
	 * @return {@link DateTime}
	 * @since 5.8.3
	 */
	public static DateTime convertTimeZone(Date date, TimeZone timeZone) {
		return new DateTime(date, timeZone);
	}

	/**
	 * 获得指定年份的总天数
	 *
	 * @param year 年份
	 * @return 天
	 * @since 5.3.6
	 */
	public static int lengthOfYear(int year) {
		return Year.of(year).length();
	}

	/**
	 * 获得指定月份的总天数
	 *
	 * @param month      月份
	 * @param isLeapYear 是否闰年
	 * @return 天
	 * @since 5.4.2
	 */
	public static int lengthOfMonth(int month, boolean isLeapYear) {
		return java.time.Month.of(month).length(isLeapYear);
	}

	/**
	 * 创建{@link SimpleDateFormat}，注意此对象非线程安全！<br>
	 * 此对象默认为严格格式模式，即parse时如果格式不正确会报错。
	 *
	 * @param pattern 表达式
	 * @return {@link SimpleDateFormat}
	 * @since 5.5.5
	 */
	public static SimpleDateFormat newSimpleFormat(String pattern) {
		return newSimpleFormat(pattern, null, null);
	}

	/**
	 * 创建{@link SimpleDateFormat}，注意此对象非线程安全！<br>
	 * 此对象默认为严格格式模式，即parse时如果格式不正确会报错。
	 *
	 * @param pattern  表达式
	 * @param locale   {@link Locale}，{@code null}表示默认
	 * @param timeZone {@link TimeZone}，{@code null}表示默认
	 * @return {@link SimpleDateFormat}
	 * @since 5.5.5
	 */
	public static SimpleDateFormat newSimpleFormat(String pattern, Locale locale, TimeZone timeZone) {
		if (null == locale) {
			locale = Locale.getDefault(Locale.Category.FORMAT);
		}
		final SimpleDateFormat format = new SimpleDateFormat(pattern, locale);
		if (null != timeZone) {
			format.setTimeZone(timeZone);
		}
		format.setLenient(false);
		return format;
	}

	/**
	 * 获取时长单位简写
	 *
	 * @param unit 单位
	 * @return 单位简写名称
	 * @since 5.7.16
	 */
	public static String getShotName(TimeUnit unit) {
		switch (unit) {
			case NANOSECONDS:
				return "ns";
			case MICROSECONDS:
				return "μs";
			case MILLISECONDS:
				return "ms";
			case SECONDS:
				return "s";
			case MINUTES:
				return "min";
			case HOURS:
				return "h";
			default:
				return unit.name().toLowerCase();
		}
	}

	/**
	 * 检查两个时间段是否有时间重叠<br>
	 * 重叠指两个时间段是否有交集，注意此方法时间段重合时如：
	 * <ul>
	 *     <li>此方法未纠正开始时间小于结束时间</li>
	 *     <li>当realStartTime和realEndTime或startTime和endTime相等时,退化为判断区间是否包含点</li>
	 *     <li>当realStartTime和realEndTime和startTime和endTime相等时,退化为判断点与点是否相等</li>
	 * </ul>
	 * See <a href="https://www.ics.uci.edu/~alspaugh/cls/shr/allen.html">准确的区间关系参考:艾伦区间代数</a>
	 * @param realStartTime 第一个时间段的开始时间
	 * @param realEndTime   第一个时间段的结束时间
	 * @param startTime     第二个时间段的开始时间
	 * @param endTime       第二个时间段的结束时间
	 * @return true 表示时间有重合或包含或相等
	 * @since 5.7.22
	 */
	public static boolean isOverlap(Date realStartTime, Date realEndTime,
									Date startTime, Date endTime) {

		// x>b||a>y 无交集
		// 则有交集的逻辑为 !(x>b||a>y)
		// 根据德摩根公式，可化简为 x<=b && a<=y 即 realStartTime<=endTime && startTime<=realEndTime
		return realStartTime.compareTo(endTime) <=0 && startTime.compareTo(realEndTime) <= 0;
	}

	/**
	 * 是否为本月最后一天
	 * @param date {@link Date}
	 * @return 是否为本月最后一天
	 * @since 5.8.9
	 */
	public static boolean isLastDayOfMonth(Date date){
		return date(date).isLastDayOfMonth();
	}

	/**
	 * 获得本月的最后一天
	 * @param date {@link Date}
	 * @return 天
	 * @since 5.8.9
	 */
	public static int getLastDayOfMonth(Date date){
		return date(date).getLastDayOfMonth();
	}

	// ------------------------------------------------------------------------ Private method start

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
	 * <p>
	 * 将以下字符去除
	 *
	 * <pre>
	 * "日"
	 * </pre>
	 * <p>
	 * 将以下字符替换为":"
	 *
	 * <pre>
	 * "时"
	 * "分"
	 * "秒"
	 * </pre>
	 * <p>
	 * 当末位是":"时去除之（不存在毫秒时）
	 *
	 * @param dateStr 日期时间字符串
	 * @return 格式化后的日期字符串
	 */
	private static String normalize(CharSequence dateStr) {
		if (StrUtil.isBlank(dateStr)) {
			return StrUtil.str(dateStr);
		}

		// 日期时间分开处理
		final List<String> dateAndTime = StrUtil.splitTrim(dateStr, ' ');
		final int size = dateAndTime.size();
		if (size < 1 || size > 2) {
			// 非可被标准处理的格式
			return StrUtil.str(dateStr);
		}

		final StringBuilder builder = StrUtil.builder();

		// 日期部分（"\"、"/"、"."、"年"、"月"都替换为"-"）
		String datePart = dateAndTime.get(0).replaceAll("[/.年月]", "-");
		datePart = StrUtil.removeSuffix(datePart, "日");
		builder.append(datePart);

		// 时间部分
		if (size == 2) {
			builder.append(' ');
			String timePart = dateAndTime.get(1).replaceAll("[时分秒]", ":");
			timePart = StrUtil.removeSuffix(timePart, ":");
			//将ISO8601中的逗号替换为.
			timePart = timePart.replace(',', '.');
			builder.append(timePart);
		}

		return builder.toString();
	}
	// ------------------------------------------------------------------------ Private method end
}
