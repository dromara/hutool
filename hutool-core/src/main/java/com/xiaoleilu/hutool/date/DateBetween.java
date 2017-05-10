package com.xiaoleilu.hutool.date;

import java.util.Date;

/**
 * 日期间隔
 * @author Looly
 *
 */
public class DateBetween {
	private Date start;
	private Date end;
	
	/**
	 * 构造<br>
	 * 在前的日期做为起始时间，在后的做为结束时间
	 * 
	 * @param start 起始时间
	 * @param end 结束时间
	 */
	public DateBetween(Date start, Date end) {
		if(start.before(end)){
			this.start = start;
			this.end = end;
		}else{
			this.start = end;
			this.end = start;
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
		long diff = end.getTime() - start.getTime();
		return diff / unit.getMillis();
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
