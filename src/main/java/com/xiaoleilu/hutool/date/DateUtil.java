package com.xiaoleilu.hutool.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 时间工具类
 * 
 * @author xiaoleilu
 */
public class DateUtil {
	
	/** 标准日期格式 */
	public final static String NORM_DATE_PATTERN = DatePattern.NORM_DATE_PATTERN;
	/** 标准时间格式 */
	public final static String NORM_TIME_PATTERN = DatePattern.NORM_TIME_PATTERN;
	/** 标准日期时间格式，精确到分 */
	public final static String NORM_DATETIME_MINUTE_PATTERN = DatePattern.NORM_DATETIME_MINUTE_PATTERN;
	/** 标准日期时间格式，精确到秒 */
	public final static String NORM_DATETIME_PATTERN = DatePattern.NORM_DATETIME_PATTERN;
	/** 标准日期时间格式，精确到毫秒 */
	public final static String NORM_DATETIME_MS_PATTERN = DatePattern.NORM_DATETIME_MS_PATTERN;
	/** HTTP头中日期时间格式 */
	public final static String HTTP_DATETIME_PATTERN = DatePattern.HTTP_DATETIME_PATTERN;

	/**
	 * @return 当前时间
	 */
	public static DateTime date() {
		return new DateTime();
	}

	/**
	 * Long类型时间转为Date
	 * 
	 * @param date Long类型Date（Unix时间戳）
	 * @return 时间对象
	 */
	public static DateTime date(long date) {
		return new DateTime(date);
	}
	
	/**
	 * Calendar类型时间转为Date
	 * 
	 * @param calendar {@link Calendar}
	 * @return 时间对象
	 */
	public static DateTime date(Calendar calendar) {
		return new DateTime(calendar.getTime());
	}

	/**
	 * 转换为Calendar对象
	 * 
	 * @param date 日期对象
	 * @return Calendar对象
	 */
	public static Calendar calendar(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
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
	 * 当前日期，格式 yyyy-MM-dd
	 * 
	 * @return 当前日期的标准形式字符串
	 */
	public static String today() {
		return formatDate(new DateTime());
	}

	/**
	 * @return 当前月份
	 */
	public static int thisMonth() {
		return month(date());
	}

	/**
	 * @return 今年
	 */
	public static int thisYear() {
		return year(date());
	}

	/**
	 * 获得月份，从1月开始计数
	 * 
	 * @param date 日期
	 * @return 月份
	 */
	public static int month(Date date) {
		return calendar(date).get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得年
	 * 
	 * @param date 日期
	 * @return 年
	 */
	public static int year(Date date) {
		return calendar(date).get(Calendar.YEAR);
	}

	/**
	 * 获得季节
	 * 
	 * @param date 日期
	 * @return 第几个季节
	 */
	public static int season(Date date) {
		return calendar(date).get(Calendar.MONTH) / 3 + 1;
	}

	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * 
	 * @param date 日期
	 * @return Season ，类似于 20132
	 */
	public static String yearAndSeason(Date date) {
		return yearAndSeason(calendar(date));
	}

	/**
	 * 获得指定日期区间内的年份和季节<br>
	 * 
	 * @param startDate 其实日期（包含）
	 * @param endDate 结束日期（包含）
	 * @return Season列表 ，元素类似于 20132
	 */
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

	// ------------------------------------ Format start ----------------------------------------------
	/**
	 * 根据特定格式格式化日期
	 * 
	 * @param date 被格式化的日期
	 * @param format 格式
	 * @return 格式化后的字符串
	 */
	public static String format(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 格式 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date 被格式化的日期
	 * @return 格式化后的日期
	 */
	public static String formatDateTime(Date date) {
		if(null == date){
			return null;
		}
		return DatePatternLocal.NORM_DATETIME_FORMAT.get().format(date);
	}

	/**
	 * 格式 yyyy-MM-dd
	 * 
	 * @param date 被格式化的日期
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		if(null == date){
			return null;
		}
		return DatePatternLocal.NORM_DATE_FORMAT.get().format(date);
	}

	/**
	 * 格式化为Http的标准日期格式
	 * 
	 * @param date 被格式化的日期
	 * @return HTTP标准形式日期字符串
	 */
	public static String formatHttpDate(Date date) {
		if(null == date){
			return null;
		}
		return DatePatternLocal.HTTP_DATETIME_FORMAT.get().format(date);
	}
	// ------------------------------------ Format end ----------------------------------------------

	// ------------------------------------ Parse start ----------------------------------------------

	/**
	 * 构建DateTime对象
	 * 
	 * @param dateStr Date字符串
	 * @param simpleDateFormat 格式化器
	 * @return DateTime对象
	 */
	public static DateTime parse(String dateStr, SimpleDateFormat simpleDateFormat) {
		try {
			return new DateTime(simpleDateFormat.parse(dateStr));
		} catch (Exception e) {
			throw new DateException(StrUtil.format("Parse [{}] with format [{}] error!", dateStr, simpleDateFormat.toPattern()), e);
		}
	}

	/**
	 * 将特定格式的日期转换为Date对象
	 * 
	 * @param dateString 特定格式的日期
	 * @param format 格式，例如yyyy-MM-dd
	 * @return 日期对象
	 */
	public static DateTime parse(String dateString, String format) {
		return parse(dateString, new SimpleDateFormat(format));
	}

	/**
	 * 格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString 标准形式的时间字符串
	 * @return 日期对象
	 */
	public static DateTime parseDateTime(String dateString) {
		return parse(dateString, DatePatternLocal.NORM_DATETIME_FORMAT.get());
	}

	/**
	 * 格式yyyy-MM-dd
	 * 
	 * @param dateString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseDate(String dateString) {
		return parse(dateString, DatePatternLocal.NORM_DATE_FORMAT.get());
	}

	/**
	 * 格式HH:mm:ss
	 * 
	 * @param timeString 标准形式的日期字符串
	 * @return 日期对象
	 */
	public static DateTime parseTime(String timeString) {
		return parse(timeString, DatePatternLocal.NORM_TIME_FORMAT.get());
	}

	/**
	 * 格式：<br>
	 * 1、yyyy-MM-dd HH:mm:ss<br>
	 * 2、yyyy-MM-dd<br>
	 * 3、HH:mm:ss<br>
	 * 4、yyyy-MM-dd HH:mm 5、yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param dateStr 日期字符串
	 * @return 日期
	 */
	public static DateTime parse(String dateStr) {
		if (null == dateStr) {
			return null;
		}
		dateStr = dateStr.trim();
		int length = dateStr.length();
		try {
			if (length == NORM_DATETIME_PATTERN.length()) {
				return parseDateTime(dateStr);
			} else if (length == NORM_DATE_PATTERN.length()) {
				return parseDate(dateStr);
			} else if (length == NORM_TIME_PATTERN.length()) {
				return parseTime(dateStr);
			} else if (length == NORM_DATETIME_MINUTE_PATTERN.length()) {
				return parse(dateStr, NORM_DATETIME_MINUTE_PATTERN);
			} else if (length >= NORM_DATETIME_MS_PATTERN.length() - 2) {
				return parse(dateStr, NORM_DATETIME_MS_PATTERN);
			}
		} catch (Exception e) {
			throw new DateException(StrUtil.format("Parse [{}] with format normal error!", dateStr));
		}

		// 没有更多匹配的时间格式
		throw new DateException(StrUtil.format(" [{}] format is not fit for date pattern!", dateStr));
	}
	// ------------------------------------ Parse end ----------------------------------------------

	// ------------------------------------ Offset start ----------------------------------------------
	/**
	 * 获取某天的开始时间
	 * 
	 * @param date 日期
	 * @return 某天的开始时间
	 */
	public static DateTime beginOfDay(Date date) {
		return beginOfDay(calendar(date));
	}

	/**
	 * 获取某天的结束时间
	 * 
	 * @param date 日期
	 * @return 某天的结束时间
	 */
	public static DateTime endOfDay(Date date) {
		return endOfDay(calendar(date));
	}
	
	/**
	 * 获取某天的开始时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return 某天的开始时间
	 */
	public static DateTime beginOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new DateTime(calendar.getTime());
	}
	
	/**
	 * 获取某天的结束时间
	 * 
	 * @param calendar 日期 {@link Calendar}
	 * @return 某天的结束时间
	 */
	public static DateTime endOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new DateTime(calendar.getTime());
	}

	/**
	 * 昨天
	 * 
	 * @return 昨天
	 */
	public static DateTime yesterday() {
		return offsiteDay(new DateTime(), -1);
	}

	/**
	 * 上周
	 * 
	 * @return 上周
	 */
	public static DateTime lastWeek() {
		return offsiteWeek(new DateTime(), -1);
	}

	/**
	 * 上个月
	 * 
	 * @return 上个月
	 */
	public static DateTime lastMouth() {
		return offsiteMonth(new DateTime(), -1);
	}
	
	/**
	 * 偏移毫秒数
	 * 
	 * @param date 日期
	 * @param offsite 偏移毫秒数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteMillisecond(Date date, int offsite) {
		return offsiteDate(date, Calendar.MILLISECOND, offsite);
	}
	
	/**
	 * 偏移秒数
	 * 
	 * @param date 日期
	 * @param offsite 偏移秒数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteSecond(Date date, int offsite) {
		return offsiteDate(date, Calendar.SECOND, offsite);
	}
	
	/**
	 * 偏移分钟
	 * 
	 * @param date 日期
	 * @param offsite 偏移分钟数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteMinute(Date date, int offsite) {
		return offsiteDate(date, Calendar.MINUTE, offsite);
	}
	
	/**
	 * 偏移小时
	 * 
	 * @param date 日期
	 * @param offsite 偏移小时数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteHour(Date date, int offsite) {
		return offsiteDate(date, Calendar.HOUR_OF_DAY, offsite);
	}

	/**
	 * 偏移天
	 * 
	 * @param date 日期
	 * @param offsite 偏移天数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteDay(Date date, int offsite) {
		return offsiteDate(date, Calendar.DAY_OF_YEAR, offsite);
	}

	/**
	 * 偏移周
	 * 
	 * @param date 日期
	 * @param offsite 偏移周数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteWeek(Date date, int offsite) {
		return offsiteDate(date, Calendar.WEEK_OF_YEAR, offsite);
	}

	/**
	 * 偏移月
	 * 
	 * @param date 日期
	 * @param offsite 偏移月数，正数向未来偏移，负数向历史偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteMonth(Date date, int offsite) {
		return offsiteDate(date, Calendar.MONTH, offsite);
	}

	/**
	 * 获取指定日期偏移指定时间后的时间
	 * 
	 * @param date 基准日期
	 * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 偏移后的日期
	 */
	public static DateTime offsiteDate(Date date, int calendarField, int offsite) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendarField, offsite);
		return new DateTime(cal.getTime());
	}
	// ------------------------------------ Offset end ----------------------------------------------

	/**
	 * 判断两个日期相差的时长<br/>
	 * 返回 minuend - subtrahend 的差
	 * 
	 * @param subtrahend 减数日期
	 * @param minuend 被减数日期
	 * @param unit 相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
	 * @return 日期差
	 */
	public static long diff(Date subtrahend, Date minuend, DateUnit unit) {
		long diff = minuend.getTime() - subtrahend.getTime();
		return diff / unit.getMillis();
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
	 * 计算某个过程话费的时间，精确到毫秒
	 * 
	 * @return Timer
	 */
	public static TimeInterval timer() {
		return new TimeInterval();

	}
	
	/**
	 * 生日转为年龄，计算法定年龄
	 * @param birthDay 生日，标准日期字符串
	 * @return 年龄
	 * @throws Exception
	 */
	public static int ageOfNow(String birthDay) {
		return ageOfNow(parse(birthDay));
	}

	/**
	 * 生日转为年龄，计算法定年龄
	 * @param birthDay 生日
	 * @return 年龄
	 * @throws Exception
	 */
	public static int ageOfNow(Date birthDay) {
		return age(birthDay,date());
	}
	
	/**
	 * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
	 * @param birthDay 生日
	 * @param dateToCompare 需要对比的日期
	 * @return 年龄
	 * @throws Exception
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
				//如果生日在当月，但是未达到生日当天的日期，年龄减一
				age--;
			}
		} else if (month < monthBirth){
			//如果当前月份未达到生日的月份，年龄计算减一
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
	public static boolean isLeapYear(int year){
		return new GregorianCalendar().isLeapYear(year);
	}
	
	// ------------------------------------------------------------------------ Private method start
	/**
	 * 获得指定日期年份和季节<br>
	 * 格式：[20131]表示2013年第一季度
	 * 
	 * @param cal 日期
	 */
	private static String yearAndSeason(Calendar cal) {
		return new StringBuilder().append(cal.get(Calendar.YEAR)).append(cal.get(Calendar.MONTH) / 3 + 1).toString();
	}
	// ------------------------------------------------------------------------ Private method end
}
