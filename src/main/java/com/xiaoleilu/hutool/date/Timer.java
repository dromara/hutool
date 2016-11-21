package com.xiaoleilu.hutool.date;

/**
 * 计时器<br>
 * 计算某个过程话费的时间，精确到毫秒
 * 
 * @author Looly
 *
 */
public class Timer {
	private long time;
	private boolean isNano;

	public Timer() {
		this(false);
	}

	public Timer(boolean isNano) {
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
	public long durationRestart() {
		long now = DateUtil.current(isNano);
		long d = now - time;
		time = now;
		return d;
	}

	/**
	 * @return 从开始到当前的持续时间
	 */
	public long duration() {
		return DateUtil.current(isNano) - time;
	}
}
