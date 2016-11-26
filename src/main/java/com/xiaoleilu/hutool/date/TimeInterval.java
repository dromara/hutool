package com.xiaoleilu.hutool.date;

/**
 * 计时器<br>
 * 计算某个过程话费的时间，精确到毫秒
 * 
 * @author Looly
 *
 */
public class TimeInterval {
	private long time;
	private boolean isNano;

	public TimeInterval() {
		this(false);
	}

	public TimeInterval(boolean isNano) {
		this.isNano = isNano;
		start();
	}

	/**
	 * @return 开始计时并返回当前时间
	 */
	public long start() {
		time = DateUtil.current(isNano);
		return time;
	}

	/**
	 * @return 重新计时并返回从开始到当前的持续时间
	 */
	public long intervalRestart() {
		long now = DateUtil.current(isNano);
		long d = now - time;
		time = now;
		return d;
	}

	//----------------------------------------------------------- Interval
	/**
	 * 从开始到当前的间隔时间（毫秒数）
	 * @return 从开始到当前的间隔时间（毫秒数）
	 */
	public long interval() {
		return DateUtil.current(isNano) - time;
	}
	
	/**
	 * 从开始到当前的间隔秒数，取绝对值
	 * @return 从开始到当前的间隔秒数，取绝对值
	 */
	public long intervalSecond(){
		return interval() / DateUnit.SECOND.getMillis();
	}
	
	/**
	 * 从开始到当前的间隔分钟数，取绝对值
	 * @return 从开始到当前的间隔分钟数，取绝对值
	 */
	public long intervalMinute(){
		return interval() / DateUnit.MINUTE.getMillis();
	}
	
	/**
	 * 从开始到当前的间隔小时数，取绝对值
	 * @return 从开始到当前的间隔小时数，取绝对值
	 */
	public long intervalHour(){
		return interval() / DateUnit.HOUR.getMillis();
	}
	
	/**
	 * 从开始到当前的间隔天数，取绝对值
	 * @return 从开始到当前的间隔天数，取绝对值
	 */
	public long intervalDay(){
		return interval() / DateUnit.DAY.getMillis();
	}
	
	/**
	 * 从开始到当前的间隔周数，取绝对值
	 * @return 从开始到当前的间隔周数，取绝对值
	 */
	public long intervalWeek(){
		return interval() / DateUnit.WEEK.getMillis();
	}
	
}
