package cn.hutool.cron;

import java.util.TimeZone;

/**
 * 定时任务配置类
 *
 * @author looly
 * @since 5.4.7
 */
public class CronConfig {

	/**
	 * 时区
	 */
	protected TimeZone timezone = TimeZone.getDefault();
	/**
	 * 是否支持秒匹配
	 */
	protected boolean matchSecond;

	public CronConfig(){
	}

	/**
	 * 设置时区
	 *
	 * @param timezone 时区
	 * @return this
	 */
	public CronConfig setTimeZone(TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}

	/**
	 * 获得时区，默认为 {@link TimeZone#getDefault()}
	 *
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return this.timezone;
	}

	/**
	 * 是否支持秒匹配
	 *
	 * @return <code>true</code>使用，<code>false</code>不使用
	 */
	public boolean isMatchSecond() {
		return this.matchSecond;
	}

	/**
	 * 设置是否支持秒匹配，默认不使用
	 *
	 * @param isMatchSecond <code>true</code>支持，<code>false</code>不支持
	 * @return this
	 */
	public CronConfig setMatchSecond(boolean isMatchSecond) {
		this.matchSecond = isMatchSecond;
		return this;
	}
}
