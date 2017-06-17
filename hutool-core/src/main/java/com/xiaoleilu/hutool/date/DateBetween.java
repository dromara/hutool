package com.xiaoleilu.hutool.date;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期间隔
 * @author Looly
 *
 */
public class DateBetween {
	private Date begin;
	private Date end;
	
	/**
	 * 构造<br>
	 * 在前的日期做为起始时间，在后的做为结束时间
	 * 
	 * @param begin 起始时间
	 * @param end 结束时间
	 */
	public DateBetween(Date begin, Date end) {
		if(begin.before(end)){
			this.begin = begin;
			this.end = end;
		}else{
			this.begin = end;
			this.end = begin;
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
		final Calendar endCal =  DateUtil.calendar(end);

		final int betweenYear = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
		final int betweenMonthOfYear = endCal.get(Calendar.MONTH) - beginCal.get(Calendar.MONTH);

		int result = betweenYear * 12 + betweenMonthOfYear;
		if (isReset && beginCal.get(Calendar.DAY_OF_MONTH) > endCal.get(Calendar.DAY_OF_MONTH)) {
			// 在非重置情况下，如果起始日期的天小于结束日期的天，月数要少算1（不足1个月）
			return result - 1;
		}
		return result;
	}

	/**
	 * 计算两个日期相差年数<br>
	 * 在非重置情况下，如果起始日期的月小于结束日期的月，年数要少算1（不足1年）
	 * 
	 * @param isReset 是否重置时间为起始时间（重置月天时分秒）
	 * @return 相差月数
	 * @since 3.0.8
	 */
	public long betweenYear(boolean isReset) {
		final Calendar beginCal = DateUtil.calendar(begin);
		final Calendar endCal = DateUtil.calendar(end);

		int result = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
		if (isReset && beginCal.get(Calendar.MONTH) > endCal.get(Calendar.MONTH)) {
			// 在非重置情况下，如果起始日期的月小于结束日期的月，年数要少算1（不足1年）
			return result - 1;
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
