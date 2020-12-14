package cn.hutool.core.date;

import cn.hutool.core.date.format.DateParser;
import cn.hutool.core.date.format.DatePrinter;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 包装java.util.Date
 *
 * @author xiaoleilu
 */
public class DateTime extends Date {
	private static final long serialVersionUID = -5395712593979185936L;

	/**
	 * 是否可变对象
	 */
	private boolean mutable = true;
	/**
	 * 一周的第一天，默认是周一， 在设置或获得 WEEK_OF_MONTH 或 WEEK_OF_YEAR 字段时，Calendar 必须确定一个月或一年的第一个星期，以此作为参考点。
	 */
	private Week firstDayOfWeek = Week.MONDAY;
	/**
	 * 时区
	 */
	private TimeZone timeZone;

	/**
	 * 转换时间戳为 DateTime
	 *
	 * @param timeMillis 时间戳，毫秒数
	 * @return DateTime
	 * @since 4.6.3
	 */
	public static DateTime of(long timeMillis) {
		return new DateTime(timeMillis);
	}

	/**
	 * 转换JDK date为 DateTime
	 *
	 * @param date JDK Date
	 * @return DateTime
	 */
	public static DateTime of(Date date) {
		if (date instanceof DateTime) {
			return (DateTime) date;
		}
		return new DateTime(date);
	}

	/**
	 * 转换 {@link Calendar} 为 DateTime
	 *
	 * @param calendar {@link Calendar}
	 * @return DateTime
	 */
	public static DateTime of(Calendar calendar) {
		return new DateTime(calendar);
	}

	/**
	 * 构造
	 *
	 * @param dateStr Date字符串
	 * @param format  格式
	 * @return this
	 * @see DatePattern
	 */
	public static DateTime of(String dateStr, String format) {
		return new DateTime(dateStr, format);
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
		this(TimeZone.getDefault());
	}

	/**
	 * 当前时间
	 *
	 * @param timeZone 时区
	 * @since 4.1.2
	 */
	public DateTime(TimeZone timeZone) {
		this(System.currentTimeMillis(), timeZone);
	}

	/**
	 * 给定日期的构造
	 *
	 * @param date 日期
	 */
	public DateTime(Date date) {
		this(
				date.getTime(),//
				(date instanceof DateTime) ? ((DateTime) date).timeZone : TimeZone.getDefault()
		);
	}

	/**
	 * 给定日期的构造
	 *
	 * @param date     日期
	 * @param timeZone 时区
	 * @since 4.1.2
	 */
	public DateTime(Date date, TimeZone timeZone) {
		this(date.getTime(), timeZone);
	}

	/**
	 * 给定日期的构造
	 *
	 * @param calendar {@link Calendar}
	 */
	public DateTime(Calendar calendar) {
		this(calendar.getTime(), calendar.getTimeZone());
		this.setFirstDayOfWeek(Week.of(calendar.getFirstDayOfWeek()));
	}

	/**
	 * 给定日期Instant的构造
	 *
	 * @param instant {@link Instant} 对象
	 * @since 5.0.0
	 */
	public DateTime(Instant instant) {
		this(instant.toEpochMilli());
	}

	/**
	 * 给定日期Instant的构造
	 *
	 * @param instant {@link Instant} 对象
	 * @param zoneId  时区ID
	 * @since 5.0.5
	 */
	public DateTime(Instant instant, ZoneId zoneId) {
		this(instant.toEpochMilli(), TimeZone.getTimeZone(ObjectUtil.defaultIfNull(zoneId, ZoneId.systemDefault())));
	}

	/**
	 * 给定日期TemporalAccessor的构造
	 *
	 * @param temporalAccessor {@link TemporalAccessor} 对象
	 * @since 5.0.0
	 */
	public DateTime(TemporalAccessor temporalAccessor) {
		this(DateUtil.toInstant(temporalAccessor));
	}

	/**
	 * 给定日期ZonedDateTime的构造
	 *
	 * @param zonedDateTime {@link ZonedDateTime} 对象
	 * @since 5.0.5
	 */
	public DateTime(ZonedDateTime zonedDateTime) {
		this(zonedDateTime.toInstant(), zonedDateTime.getZone());
	}

	/**
	 * 给定日期毫秒数的构造
	 *
	 * @param timeMillis 日期毫秒数
	 * @since 4.1.2
	 */
	public DateTime(long timeMillis) {
		this(timeMillis, TimeZone.getDefault());
	}

	/**
	 * 给定日期毫秒数的构造
	 *
	 * @param timeMillis 日期毫秒数
	 * @param timeZone   时区
	 * @since 4.1.2
	 */
	public DateTime(long timeMillis, TimeZone timeZone) {
		super(timeMillis);
		this.timeZone = ObjectUtil.defaultIfNull(timeZone, TimeZone.getDefault());
	}

	/**
	 * 构造
	 *
	 * @param dateStr Date字符串
	 * @param format  格式
	 * @see DatePattern
	 */
	public DateTime(CharSequence dateStr, String format) {
		this(dateStr, new SimpleDateFormat(format));
	}

	/**
	 * 构造
	 *
	 * @param dateStr    Date字符串
	 * @param dateFormat 格式化器 {@link SimpleDateFormat}
	 * @see DatePattern
	 */
	public DateTime(CharSequence dateStr, DateFormat dateFormat) {
		this(parse(dateStr, dateFormat), dateFormat.getTimeZone());
	}

	/**
	 * 构建DateTime对象
	 *
	 * @param dateStr   Date字符串
	 * @param formatter 格式化器,{@link DateTimeFormatter}
	 * @since 5.0.0
	 */
	public DateTime(CharSequence dateStr, DateTimeFormatter formatter) {
		this(Instant.from(formatter.parse(dateStr)), formatter.getZone());
	}

	/**
	 * 构造
	 *
	 * @param dateStr    Date字符串
	 * @param dateParser 格式化器 {@link DateParser}，可以使用 {@link FastDateFormat}
	 * @see DatePattern
	 */
	public DateTime(CharSequence dateStr, DateParser dateParser) {
		this(parse(dateStr, dateParser), dateParser.getTimeZone());
	}

	// -------------------------------------------------------------------- Constructor end

	// -------------------------------------------------------------------- offset start

	/**
	 * 调整日期和时间<br>
	 * 如果此对象为可变对象，返回自身，否则返回新对象，设置是否可变对象见{@link #setMutable(boolean)}
	 *
	 * @param datePart 调整的部分 {@link DateField}
	 * @param offset   偏移量，正数为向后偏移，负数为向前偏移
	 * @return 如果此对象为可变对象，返回自身，否则返回新对象
	 */
	public DateTime offset(DateField datePart, int offset) {
		if (DateField.ERA == datePart) {
			throw new IllegalArgumentException("ERA is not support offset!");
		}

		final Calendar cal = toCalendar();
		//noinspection MagicConstant
		cal.add(datePart.getValue(), offset);

		DateTime dt = mutable ? this : ObjectUtil.clone(this);
		return dt.setTimeInternal(cal.getTimeInMillis());
	}

	/**
	 * 调整日期和时间<br>
	 * 返回调整后的新DateTime，不影响原对象
	 *
	 * @param datePart 调整的部分 {@link DateField}
	 * @param offset   偏移量，正数为向后偏移，负数为向前偏移
	 * @return 如果此对象为可变对象，返回自身，否则返回新对象
	 * @since 3.0.9
	 */
	public DateTime offsetNew(DateField datePart, int offset) {
		final Calendar cal = toCalendar();
		//noinspection MagicConstant
		cal.add(datePart.getValue(), offset);

		DateTime dt = ObjectUtil.clone(this);
		return dt.setTimeInternal(cal.getTimeInMillis());
	}
	// -------------------------------------------------------------------- offset end

	// -------------------------------------------------------------------- Part of Date start

	/**
	 * 获得日期的某个部分<br>
	 * 例如获得年的部分，则使用 getField(DatePart.YEAR)
	 *
	 * @param field 表示日期的哪个部分的枚举 {@link DateField}
	 * @return 某个部分的值
	 */
	public int getField(DateField field) {
		return getField(field.getValue());
	}

	/**
	 * 获得日期的某个部分<br>
	 * 例如获得年的部分，则使用 getField(Calendar.YEAR)
	 *
	 * @param field 表示日期的哪个部分的int值 {@link Calendar}
	 * @return 某个部分的值
	 */
	public int getField(int field) {
		return toCalendar().get(field);
	}

	/**
	 * 设置日期的某个部分<br>
	 * 如果此对象为可变对象，返回自身，否则返回新对象，设置是否可变对象见{@link #setMutable(boolean)}
	 *
	 * @param field 表示日期的哪个部分的枚举 {@link DateField}
	 * @param value 值
	 * @return this
	 */
	public DateTime setField(DateField field, int value) {
		return setField(field.getValue(), value);
	}

	/**
	 * 设置日期的某个部分<br>
	 * 如果此对象为可变对象，返回自身，否则返回新对象，设置是否可变对象见{@link #setMutable(boolean)}
	 *
	 * @param field 表示日期的哪个部分的int值 {@link Calendar}
	 * @param value 值
	 * @return this
	 */
	public DateTime setField(int field, int value) {
		final Calendar calendar = toCalendar();
		calendar.set(field, value);

		DateTime dt = this;
		if (false == mutable) {
			dt = ObjectUtil.clone(this);
		}
		return dt.setTimeInternal(calendar.getTimeInMillis());
	}

	@Override
	public void setTime(long time) {
		if (mutable) {
			super.setTime(time);
		} else {
			throw new DateException("This is not a mutable object !");
		}
	}

	/**
	 * 获得年的部分
	 *
	 * @return 年的部分
	 */
	public int year() {
		return getField(DateField.YEAR);
	}

	/**
	 * 获得当前日期所属季度，从1开始计数<br>
	 *
	 * @return 第几个季度 {@link Quarter}
	 */
	public int quarter() {
		return month() / 3 + 1;
	}

	/**
	 * 获得当前日期所属季度<br>
	 *
	 * @return 第几个季度 {@link Quarter}
	 */
	public Quarter quarterEnum() {
		return Quarter.of(quarter());
	}

	/**
	 * 获得月份，从0开始计数
	 *
	 * @return 月份
	 */
	public int month() {
		return getField(DateField.MONTH);
	}

	/**
	 * 获取月，从1开始计数
	 *
	 * @return 月份，1表示一月
	 * @since 5.4.1
	 */
	public int monthBaseOne() {
		return month() + 1;
	}

	/**
	 * 获得月份，从1开始计数<br>
	 * 由于{@link Calendar} 中的月份按照0开始计数，导致某些需求容易误解，因此如果想用1表示一月，2表示二月则调用此方法
	 *
	 * @return 月份
	 */
	public int monthStartFromOne() {
		return month() + 1;
	}

	/**
	 * 获得月份
	 *
	 * @return {@link Month}
	 */
	public Month monthEnum() {
		return Month.of(month());
	}

	/**
	 * 获得指定日期是所在年份的第几周<br>
	 * 此方法返回值与一周的第一天有关，比如：<br>
	 * 2016年1月3日为周日，如果一周的第一天为周日，那这天是第二周（返回2）<br>
	 * 如果一周的第一天为周一，那这天是第一周（返回1）<br>
	 * 跨年的那个星期得到的结果总是1
	 *
	 * @return 周
	 * @see #setFirstDayOfWeek(Week)
	 */
	public int weekOfYear() {
		return getField(DateField.WEEK_OF_YEAR);
	}

	/**
	 * 获得指定日期是所在月份的第几周<br>
	 * 此方法返回值与一周的第一天有关，比如：<br>
	 * 2016年1月3日为周日，如果一周的第一天为周日，那这天是第二周（返回2）<br>
	 * 如果一周的第一天为周一，那这天是第一周（返回1）
	 *
	 * @return 周
	 * @see #setFirstDayOfWeek(Week)
	 */
	public int weekOfMonth() {
		return getField(DateField.WEEK_OF_MONTH);
	}

	/**
	 * 获得指定日期是这个日期所在月份的第几天，从1开始
	 *
	 * @return 天，1表示第一天
	 */
	public int dayOfMonth() {
		return getField(DateField.DAY_OF_MONTH);
	}

	/**
	 * 获得指定日期是这个日期所在年份的第几天，从1开始
	 *
	 * @return 天，1表示第一天
	 * @since 5.3.6
	 */
	public int dayOfYear() {
		return getField(DateField.DAY_OF_YEAR);
	}

	/**
	 * 获得指定日期是星期几，1表示周日，2表示周一
	 *
	 * @return 星期几
	 */
	public int dayOfWeek() {
		return getField(DateField.DAY_OF_WEEK);
	}

	/**
	 * 获得天所在的周是这个月的第几周
	 *
	 * @return 天
	 */
	public int dayOfWeekInMonth() {
		return getField(DateField.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * 获得指定日期是星期几
	 *
	 * @return {@link Week}
	 */
	public Week dayOfWeekEnum() {
		return Week.of(dayOfWeek());
	}

	/**
	 * 获得指定日期的小时数部分<br>
	 *
	 * @param is24HourClock 是否24小时制
	 * @return 小时数
	 */
	public int hour(boolean is24HourClock) {
		return getField(is24HourClock ? DateField.HOUR_OF_DAY : DateField.HOUR);
	}

	/**
	 * 获得指定日期的分钟数部分<br>
	 * 例如：10:04:15.250 =》 4
	 *
	 * @return 分钟数
	 */
	public int minute() {
		return getField(DateField.MINUTE);
	}

	/**
	 * 获得指定日期的秒数部分<br>
	 *
	 * @return 秒数
	 */
	public int second() {
		return getField(DateField.SECOND);
	}

	/**
	 * 获得指定日期的毫秒数部分<br>
	 *
	 * @return 毫秒数
	 */
	public int millisecond() {
		return getField(DateField.MILLISECOND);
	}

	/**
	 * 获得指定日期的毫秒数部分<br>
	 *
	 * @return 毫秒数
	 * @deprecated 拼写错误，请使用{@link #millisecond()}
	 */
	@Deprecated
	public int millsecond() {
		return getField(DateField.MILLISECOND);
	}

	/**
	 * 是否为上午
	 *
	 * @return 是否为上午
	 */
	public boolean isAM() {
		return Calendar.AM == getField(DateField.AM_PM);
	}

	/**
	 * 是否为下午
	 *
	 * @return 是否为下午
	 */
	public boolean isPM() {
		return Calendar.PM == getField(DateField.AM_PM);
	}

	/**
	 * 是否为周末，周末指周六或者周日
	 *
	 * @return 是否为周末，周末指周六或者周日
	 * @since 4.1.14
	 */
	public boolean isWeekend() {
		final int dayOfWeek = dayOfWeek();
		return Calendar.SATURDAY == dayOfWeek || Calendar.SUNDAY == dayOfWeek;
	}
	// -------------------------------------------------------------------- Part of Date end

	/**
	 * 是否闰年
	 *
	 * @return 是否闰年
	 * @see DateUtil#isLeapYear(int)
	 */
	public boolean isLeapYear() {
		return DateUtil.isLeapYear(year());
	}

	/**
	 * 转换为Calendar, 默认 {@link Locale}
	 *
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar() {
		return toCalendar(Locale.getDefault(Locale.Category.FORMAT));
	}

	/**
	 * 转换为Calendar
	 *
	 * @param locale 地域 {@link Locale}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(Locale locale) {
		return toCalendar(this.timeZone, locale);
	}

	/**
	 * 转换为Calendar
	 *
	 * @param zone 时区 {@link TimeZone}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(TimeZone zone) {
		return toCalendar(zone, Locale.getDefault(Locale.Category.FORMAT));
	}

	/**
	 * 转换为Calendar
	 *
	 * @param zone   时区 {@link TimeZone}
	 * @param locale 地域 {@link Locale}
	 * @return {@link Calendar}
	 */
	public Calendar toCalendar(TimeZone zone, Locale locale) {
		if (null == locale) {
			locale = Locale.getDefault(Locale.Category.FORMAT);
		}
		final Calendar cal = (null != zone) ? Calendar.getInstance(zone, locale) : Calendar.getInstance(locale);
		//noinspection MagicConstant
		cal.setFirstDayOfWeek(firstDayOfWeek.getValue());
		cal.setTime(this);
		return cal;
	}

	/**
	 * 转换为 {@link Date}<br>
	 * 考虑到很多框架（例如Hibernate）的兼容性，提供此方法返回JDK原生的Date对象
	 *
	 * @return {@link Date}
	 * @since 3.2.2
	 */
	public Date toJdkDate() {
		return new Date(this.getTime());
	}

	/**
	 * 转为{@link Timestamp}
	 *
	 * @return {@link Timestamp}
	 */
	public Timestamp toTimestamp() {
		return new Timestamp(this.getTime());
	}

	/**
	 * 转为 {@link java.sql.Date}
	 *
	 * @return {@link java.sql.Date}
	 */
	public java.sql.Date toSqlDate() {
		return new java.sql.Date(getTime());
	}

	/**
	 * 计算相差时长
	 *
	 * @param date 对比的日期
	 * @return {@link DateBetween}
	 */
	public DateBetween between(Date date) {
		return new DateBetween(this, date);
	}

	/**
	 * 计算相差时长
	 *
	 * @param date 对比的日期
	 * @param unit 单位 {@link DateUnit}
	 * @return 相差时长
	 */
	public long between(Date date, DateUnit unit) {
		return new DateBetween(this, date).between(unit);
	}

	/**
	 * 计算相差时长
	 *
	 * @param date        对比的日期
	 * @param unit        单位 {@link DateUnit}
	 * @param formatLevel 格式化级别
	 * @return 相差时长
	 */
	public String between(Date date, DateUnit unit, BetweenFormatter.Level formatLevel) {
		return new DateBetween(this, date).toString(formatLevel);
	}

	/**
	 * 当前日期是否在日期指定范围内<br>
	 * 起始日期和结束日期可以互换
	 *
	 * @param beginDate 起始日期
	 * @param endDate   结束日期
	 * @return 是否在范围内
	 * @since 3.0.8
	 */
	public boolean isIn(Date beginDate, Date endDate) {
		long beginMills = beginDate.getTime();
		long endMills = endDate.getTime();
		long thisMills = this.getTime();

		return thisMills >= Math.min(beginMills, endMills) && thisMills <= Math.max(beginMills, endMills);
	}

	/**
	 * 是否在给定日期之前
	 *
	 * @param date 日期
	 * @return 是否在给定日期之前
	 * @since 4.1.3
	 */
	public boolean isBefore(Date date) {
		if (null == date) {
			throw new NullPointerException("Date to compare is null !");
		}
		return compareTo(date) < 0;
	}

	/**
	 * 是否在给定日期之前或与给定日期相等
	 *
	 * @param date 日期
	 * @return 是否在给定日期之前或与给定日期相等
	 * @since 3.0.9
	 */
	public boolean isBeforeOrEquals(Date date) {
		if (null == date) {
			throw new NullPointerException("Date to compare is null !");
		}
		return compareTo(date) <= 0;
	}

	/**
	 * 是否在给定日期之后
	 *
	 * @param date 日期
	 * @return 是否在给定日期之后
	 * @since 4.1.3
	 */
	public boolean isAfter(Date date) {
		if (null == date) {
			throw new NullPointerException("Date to compare is null !");
		}
		return compareTo(date) > 0;
	}

	/**
	 * 是否在给定日期之后或与给定日期相等
	 *
	 * @param date 日期
	 * @return 是否在给定日期之后或与给定日期相等
	 * @since 3.0.9
	 */
	public boolean isAfterOrEquals(Date date) {
		if (null == date) {
			throw new NullPointerException("Date to compare is null !");
		}
		return compareTo(date) >= 0;
	}

	/**
	 * 对象是否可变<br>
	 * 如果为不可变对象，以下方法将返回新方法：
	 * <ul>
	 * <li>{@link DateTime#offset(DateField, int)}</li>
	 * <li>{@link DateTime#setField(DateField, int)}</li>
	 * <li>{@link DateTime#setField(int, int)}</li>
	 * </ul>
	 * 如果为不可变对象，{@link DateTime#setTime(long)}将抛出异常
	 *
	 * @return 对象是否可变
	 */
	public boolean isMutable() {
		return mutable;
	}

	/**
	 * 设置对象是否可变 如果为不可变对象，以下方法将返回新方法：
	 * <ul>
	 * <li>{@link DateTime#offset(DateField, int)}</li>
	 * <li>{@link DateTime#setField(DateField, int)}</li>
	 * <li>{@link DateTime#setField(int, int)}</li>
	 * </ul>
	 * 如果为不可变对象，{@link DateTime#setTime(long)}将抛出异常
	 *
	 * @param mutable 是否可变
	 * @return this
	 */
	public DateTime setMutable(boolean mutable) {
		this.mutable = mutable;
		return this;
	}

	/**
	 * 获得一周的第一天，默认为周一
	 *
	 * @return 一周的第一天
	 */
	public Week getFirstDayOfWeek() {
		return firstDayOfWeek;
	}

	/**
	 * 设置一周的第一天<br>
	 * JDK的Calendar中默认一周的第一天是周日，Hutool中将此默认值设置为周一<br>
	 * 设置一周的第一天主要影响{@link #weekOfMonth()}和{@link #weekOfYear()} 两个方法
	 *
	 * @param firstDayOfWeek 一周的第一天
	 * @return this
	 * @see #weekOfMonth()
	 * @see #weekOfYear()
	 */
	public DateTime setFirstDayOfWeek(Week firstDayOfWeek) {
		this.firstDayOfWeek = firstDayOfWeek;
		return this;
	}

	/**
	 * 获取时区
	 *
	 * @return 时区
	 * @since 5.0.5
	 */
	public TimeZone getTimeZone() {
		return this.timeZone;
	}

	/**
	 * 获取时区ID
	 *
	 * @return 时区ID
	 * @since 5.0.5
	 */
	public ZoneId getZoneId() {
		return this.timeZone.toZoneId();
	}

	/**
	 * 设置时区
	 *
	 * @param timeZone 时区
	 * @return this
	 * @since 4.1.2
	 */
	public DateTime setTimeZone(TimeZone timeZone) {
		this.timeZone = ObjectUtil.defaultIfNull(timeZone, TimeZone.getDefault());
		return this;
	}

	// -------------------------------------------------------------------- toString start

	/**
	 * 转为"yyyy-MM-dd HH:mm:ss" 格式字符串<br>
	 * 如果时区被设置，会转换为其时区对应的时间，否则转换为当前地点对应的时区
	 *
	 * @return "yyyy-MM-dd HH:mm:ss" 格式字符串
	 */
	@Override
	public String toString() {
		return toString(this.timeZone);
	}

	/**
	 * 转为"yyyy-MM-dd HH:mm:ss" 格式字符串<br>
	 * 时区使用当前地区的默认时区
	 *
	 * @return "yyyy-MM-dd HH:mm:ss" 格式字符串
	 * @since 4.1.14
	 */
	public String toStringDefaultTimeZone() {
		return toString(TimeZone.getDefault());
	}

	/**
	 * 转为"yyyy-MM-dd HH:mm:ss" 格式字符串<br>
	 * 如果时区不为{@code null}，会转换为其时区对应的时间，否则转换为当前时间对应的时区
	 *
	 * @param timeZone 时区
	 * @return "yyyy-MM-dd HH:mm:ss" 格式字符串
	 * @since 4.1.14
	 */
	public String toString(TimeZone timeZone) {
		if (null != timeZone) {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
			simpleDateFormat.setTimeZone(timeZone);
			return toString(simpleDateFormat);
		}
		return toString(DatePattern.NORM_DATETIME_FORMAT);
	}

	/**
	 * 转为"yyyy-MM-dd" 格式字符串
	 *
	 * @return "yyyy-MM-dd" 格式字符串
	 * @since 4.0.0
	 */
	public String toDateStr() {
		if (null != this.timeZone) {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
			simpleDateFormat.setTimeZone(this.timeZone);
			return toString(simpleDateFormat);
		}
		return toString(DatePattern.NORM_DATE_FORMAT);
	}

	/**
	 * 转为"HH:mm:ss" 格式字符串
	 *
	 * @return "HH:mm:ss" 格式字符串
	 * @since 4.1.4
	 */
	public String toTimeStr() {
		if (null != this.timeZone) {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern.NORM_TIME_PATTERN);
			simpleDateFormat.setTimeZone(this.timeZone);
			return toString(simpleDateFormat);
		}
		return toString(DatePattern.NORM_TIME_FORMAT);
	}

	/**
	 * 转为字符串
	 *
	 * @param format 日期格式，常用格式见： {@link DatePattern}
	 * @return String
	 */
	public String toString(String format) {
		if (null != this.timeZone) {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setTimeZone(this.timeZone);
			return toString(simpleDateFormat);
		}
		return toString(FastDateFormat.getInstance(format));
	}

	/**
	 * 转为字符串
	 *
	 * @param format {@link DatePrinter} 或 {@link FastDateFormat}
	 * @return String
	 */
	public String toString(DatePrinter format) {
		return format.format(this);
	}

	/**
	 * 转为字符串
	 *
	 * @param format {@link SimpleDateFormat}
	 * @return String
	 */
	public String toString(DateFormat format) {
		return format.format(this);
	}

	/**
	 * @return 输出精确到毫秒的标准日期形式
	 */
	public String toMsStr() {
		return toString(DatePattern.NORM_DATETIME_MS_FORMAT);
	}
	// -------------------------------------------------------------------- toString end

	/**
	 * 转换字符串为Date
	 *
	 * @param dateStr    日期字符串
	 * @param dateFormat {@link SimpleDateFormat}
	 * @return {@link Date}
	 */
	private static Date parse(CharSequence dateStr, DateFormat dateFormat) {
		Assert.notBlank(dateStr, "Date String must be not blank !");
		try {
			return dateFormat.parse(dateStr.toString());
		} catch (Exception e) {
			String pattern;
			if (dateFormat instanceof SimpleDateFormat) {
				pattern = ((SimpleDateFormat) dateFormat).toPattern();
			} else {
				pattern = dateFormat.toString();
			}
			throw new DateException(StrUtil.format("Parse [{}] with format [{}] error!", dateStr, pattern), e);
		}
	}

	/**
	 * 转换字符串为Date
	 *
	 * @param dateStr 日期字符串
	 * @param parser  {@link FastDateFormat}
	 * @return {@link Date}
	 */
	private static Date parse(CharSequence dateStr, DateParser parser) {
		Assert.notNull(parser, "Parser or DateFromat must be not null !");
		Assert.notBlank(dateStr, "Date String must be not blank !");
		try {
			return parser.parse(dateStr.toString());
		} catch (Exception e) {
			throw new DateException("Parse [{}] with format [{}] error!", dateStr, parser.getPattern(), e);
		}
	}

	/**
	 * 设置日期时间
	 *
	 * @param time 日期时间毫秒
	 * @return this
	 */
	private DateTime setTimeInternal(long time) {
		super.setTime(time);
		return this;
	}
}
