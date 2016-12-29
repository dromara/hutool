package com.xiaoleilu.hutool.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 包装java.util.Date
 * 
 * @author xiaoleilu
 *
 */
public class DateTime extends Date {
	private static final long serialVersionUID = -5395712593979185936L;

	/**
	 * 转换JDK date为 DateTime
	 * 
	 * @param date JDK Date
	 * @return DateTime
	 */
	public static DateTime of(Date date) {
		return new DateTime(date);
	}

	/**
	 * 转换JDK date为 DateTime
	 * 
	 * @param calendar JDK Date
	 * @return DateTime
	 */
	public static DateTime of(Calendar calendar) {
		return new DateTime(calendar);
	}

	/**
	 * 现在的时间
	 * 
	 * @return 现在的时间
	 */
	public static DateTime now() {
		return new DateTime();
	}

	// -------------------------------------------------------------------- Constructor start
	/**
	 * 当前时间
	 */
	public DateTime() {
		super();
	}

	/**
	 * 给定日期的构造
	 * 
	 * @param date 日期
	 */
	public DateTime(Date date) {
		this(date.getTime());
	}

	/**
	 * 给定日期的构造
	 * 
	 * @param calendar {@link Calendar}
	 */
	public DateTime(Calendar calendar) {
		this(calendar.getTime());
	}

	/**
	 * 给定日期毫秒数的构造
	 * 
	 * @param timeMillis 日期毫秒数
	 */
	public DateTime(long timeMillis) {
		super(timeMillis);
	}
	
	/**
	 * 构造
	 * @see DatePattern
	 * @param dateStr Date字符串
	 * @param format 格式
	 */
	public DateTime(String dateStr, String format){
		this(dateStr, new SimpleDateFormat(format));
	}
	
	/**
	 * 构造
	 * @see DatePatternLocal
	 * @param dateStr Date字符串
	 * @param simpleDateFormat 格式化器 {@link SimpleDateFormat}
	 */
	public DateTime(String dateStr, SimpleDateFormat simpleDateFormat) {
		this(parse(dateStr, simpleDateFormat));
	}
	// -------------------------------------------------------------------- Constructor end
	
	// -------------------------------------------------------------------- offsite start
	/**
	 * 调整日期和时间
	 * 
	 * @param datePart 调整的部分 {@link DatePart}
	 * @param offsite 偏移量，正数为向后偏移，负数为向前偏移
	 * @return 自身
	 */
	public DateTime offsite(DatePart datePart, int offsite) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(this);
		cal.add(datePart.getValue(), offsite);
		this.setTime(cal.getTimeInMillis());
		return this;
	}
	// -------------------------------------------------------------------- offsite end

	// -------------------------------------------------------------------- Part of Date start
	/**
	 * 获得年的部分
	 * 
	 * @return 年的部分
	 */
	public int year() {
		return DateUtil.year(this);
	}

	/**
	 * 获得当前日期所属季节
	 * 
	 * @return 第几个季节
	 */
	public int season() {
		return DateUtil.season(this);
	}

	/**
	 * 获得月份，从1开始计数
	 * 
	 * @return 月份
	 */
	public int month() {
		return DateUtil.month(this);
	}

	/**
	 * 获得月份
	 * 
	 * @return {@link Month}
	 */
	public Month monthEnum() {
		return DateUtil.monthEnum(this);
	}

	/**
	 * 获得指定日期是所在年份的第几周<br>
	 * 
	 * @return 周
	 */
	public int weekOfYear() {
		return DateUtil.weekOfYear(this);
	}

	/**
	 * 获得指定日期是所在月份的第几周<br>
	 * 
	 * @return 周
	 */
	public int weekOfMonth() {
		return DateUtil.weekOfMonth(this);
	}

	/**
	 * 获得指定日期是这个日期所在月份的第几天<br>
	 * 
	 * @return 天
	 */
	public int dayOfMonth() {
		return DateUtil.dayOfMonth(this);
	}

	/**
	 * 获得指定日期是星期几
	 * 
	 * @return 天
	 */
	public int dayOfWeek() {
		return DateUtil.dayOfWeek(this);
	}

	/**
	 * 获得指定日期是星期几
	 * 
	 * @return {@link Week}
	 */
	public Week dayOfWeekEnum() {
		return DateUtil.dayOfWeekEnum(this);
	}

	/**
	 * 获得指定日期的小时数部分<br>
	 * 
	 * @param is24HourClock 是否24小时制
	 * @return 小时数
	 */
	public int hour(boolean is24HourClock) {
		return DateUtil.hour(this, is24HourClock);
	}

	/**
	 * 获得指定日期的分钟数部分<br>
	 * 例如：10:04:15.250 -> 4
	 * 
	 * @return 分钟数
	 */
	public int minute() {
		return DateUtil.minute(this);
	}

	/**
	 * 获得指定日期的秒数部分<br>
	 * 
	 * @return 秒数
	 */
	public int second() {
		return DateUtil.season(this);
	}

	/**
	 * 获得指定日期的毫秒数部分<br>
	 * 
	 * @return 毫秒数
	 */
	public int millsecond() {
		return DateUtil.millsecond(this);
	}

	/**
	 * 是否为上午
	 * 
	 * @return 是否为上午
	 */
	public boolean isAM() {
		return DateUtil.isAM(this);
	}

	/**
	 * 是否为下午
	 * 
	 * @return 是否为下午
	 */
	public boolean isPM() {
		return DateUtil.isPM(this);
	}
	// -------------------------------------------------------------------- Part of Date end

	/**
	 * 是否闰年
	 * 
	 * @see DateUtil#isLeapYear(int)
	 * 
	 * @return 是否闰年
	 */
	public boolean isLeapYear() {
		return DateUtil.isLeapYear(this.year());
	}

	/**
	 * 转换为Calendar
	 * 
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar() {
		return DateUtil.calendar(this);
	}
	
	/**
	 * 计算相差时长
	 * @param date 对比的日期
	 * @return {@link DateBetween}
	 */
	public DateBetween between(Date date){
		return new DateBetween(this, date);
	}
	
	/**
	 * 计算相差时长
	 * @param date 对比的日期
	 * @param unit 单位 {@link DateUnit}
	 * @return 相差时长
	 */
	public long between(Date date, DateUnit unit){
		return new DateBetween(this, date).between(unit);
	}
	
	/**
	 * 计算相差时长
	 * @param date 对比的日期
	 * @param unit 单位 {@link DateUnit}
	 * @param formatLevel 格式化级别
	 * @return 相差时长
	 */
	public String between(Date date, DateUnit unit, BetweenFormater.Level formatLevel){
		return new DateBetween(this, date).toString(formatLevel);
	}

	// -------------------------------------------------------------------- toString start
	@Override
	public String toString() {
		return DateUtil.formatDateTime(this);
	}

	public String toString(String format) {
		return DateUtil.format(this, format);
	}

	/**
	 * @return 输出精确到毫秒的标准日期形式
	 */
	public String toMsStr() {
		return DateUtil.format(this, DateUtil.NORM_DATETIME_MS_PATTERN);
	}
	// -------------------------------------------------------------------- toString end
	
	/**
	 * 转换字符串为Date
	 * @param dateStr 日期字符串
	 * @param simpleDateFormat {@link SimpleDateFormat}
	 * @return {@link Date}
	 */
	private static Date parse(String dateStr, SimpleDateFormat simpleDateFormat){
		try {
			return simpleDateFormat.parse(dateStr);
		} catch (Exception e) {
			throw new DateException(StrUtil.format("Parse [{}] with format [{}] error!", dateStr, simpleDateFormat.toPattern()), e);
		}
	}
}
