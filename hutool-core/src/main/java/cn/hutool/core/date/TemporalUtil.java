package cn.hutool.core.date;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * {@link Temporal} 工具类封装
 *
 * @author looly
 * @since 5.4.5
 */
public class TemporalUtil {

	/**
	 * 获取两个日期的差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 返回结果为{@link Duration}对象，通过调用toXXX方法返回相差单位
	 *
	 * @param startTimeInclude 开始时间（包含）
	 * @param endTimeExclude   结束时间（不包含）
	 * @return 时间差 {@link Duration}对象
	 */
	public static Duration between(Temporal startTimeInclude, Temporal endTimeExclude) {
		return Duration.between(startTimeInclude, endTimeExclude);
	}

	/**
	 * 获取两个日期的差，如果结束时间早于开始时间，获取结果为负。
	 * <p>
	 * 返回结果为时间差的long值
	 *
	 * @param startTimeInclude 开始时间（包括）
	 * @param endTimeExclude   结束时间（不包括）
	 * @param unit             时间差单位
	 * @return 时间差
	 */
	public static long between(Temporal startTimeInclude, Temporal endTimeExclude, ChronoUnit unit) {
		return unit.between(startTimeInclude, endTimeExclude);
	}
}
