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

	/**
	 * @return 从开始到当前的间隔时间
	 */
	public long interval() {
		return DateUtil.current(isNano) - time;
	}
}
