package cn.hutool.core.date;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期间隔
 * 
 * @author Looly
 *
 */
public class DateBetween {

	/** 开始日期 */
	private Date begin;
	/** 结束日期 */
	private Date end;

	/**
	 * 创建<br>
	 * 在前的日期做为起始时间，在后的做为结束时间，间隔只保留绝对值正数
	 * 
	 * @param begin 起始时间
	 * @param end 结束时间
	 * @return {@link DateBetween}
	 * @since 3.2.3
	 */
	public static DateBetween create(Date begin, Date end) {
		return new DateBetween(begin, end);
	}

	/**
	 * 创建<br>
	 * 在前的日期做为起始时间，在后的做为结束时间，间隔只保留绝对值正数
	 * 
	 * @param begin 起始时间
	 * @param end 结束时间
	 * @param isAbs 日期间隔是否只保留绝对值正数
	 * @return {@link DateBetween}
	 * @since 3.2.3
	 */
	public static DateBetween create(Date begin, Date end, boolean isAbs) {
		return new DateBetween(begin, end, isAbs);
	}

	/**
	 * 构造<br>
	 * 在前的日期做为起始时间，在后的做为结束时间，间隔只保留绝对值正数
	 * 
	 * @param begin 起始时间
	 * @param end 结束时间
	 */
	public DateBetween(Date begin, Date end) {
		this(begin, end, true);
	}

	/**
	 * 构造<br>
	 * 在前的日期做为起始时间，在后的做为结束时间
	 * 
	 * @param begin 起始时间
	 * @param end 结束时间
	 * @param isAbs 日期间隔是否只保留绝对值正数
	 * @since 3.1.1
	 */
	public DateBetween(Date begin, Date end, boolean isAbs) {
		if (isAbs && begin.after(end)) {
			// 间隔只为正数的情况下，如果开始日期晚于结束日期，置换之
			this.begin = end;
			this.end = begin;
		} else {
			this.begin = begin;
			this.end = end;
		}
	}

	/**
	 * 判断两个日期相差的时长<br>
	 * 返回 给定单位的时长差
	 * 
	 * @param unit 相差的单位：相差 天{@link DateUnit#DAY}、小时{@link DateUnit#HOUR} 等
	 * @return 时长差
	 */
	public long between(DateUnit unit) {
		long diff = end.getTime() - begin.getTime();
		return diff / unit.getMillis();
	}

	/**
	 * 计算两个日期相差月数<br>
	 * 在非重置情况下，如果起始日期的天小于结束日期的天，月数要少算1（不足1个月）
	 * 
	 * @param isReset 是否重置时间为起始时间（重置天时分秒）
	 * @return 相差月数
	 * @since 3.0.8
	 */
	public long betweenMonth(boolean isReset) {
		final Calendar beginCal = DateUtil.calendar(begin);
		final Calendar endCal = DateUtil.calendar(end);

		final int betweenYear = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
		final int betweenMonthOfYear = endCal.get(Calendar.MONTH) - beginCal.get(Calendar.MONTH);

		int result = betweenYear * 12 + betweenMonthOfYear;
		if (false == isReset) {
			endCal.set(Calendar.YEAR, beginCal.get(Calendar.YEAR));
			endCal.set(Calendar.MONTH, beginCal.get(Calendar.MONTH));
			long between = endCal.getTimeInMillis() - beginCal.getTimeInMillis();
			if (between < 0) {
				return result - 1;
			}
		}
		return result;
	}

	/**
	 * 计算两个日期相差年数<br>
	 * 在非重置情况下，如果起始日期的月小于结束日期的月，年数要少算1（不足1年）
	 * 
	 * @param isReset 是否重置时间为起始时间（重置月天时分秒）
	 * @return 相差年数
	 * @since 3.0.8
	 */
	public long betweenYear(boolean isReset) {
		final Calendar beginCal = DateUtil.calendar(begin);
		final Calendar endCal = DateUtil.calendar(end);

		int result = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
		if (false == isReset) {
			endCal.set(Calendar.YEAR, beginCal.get(Calendar.YEAR));
			long between = endCal.getTimeInMillis() - beginCal.getTimeInMillis();
			if (between < 0) {
				return result - 1;
			}
		}
		return result;
	}

	/**
	 * 格式化输出时间差<br>
	 * 
	 * @param level 级别
	 * @return 字符串
	 */
	public String toString(BetweenFormater.Level level) {
		return DateUtil.formatBetween(between(DateUnit.MS), level);
	}

	@Override
	public String toString() {
		return toString(BetweenFormater.Level.MILLSECOND);
	}
}
