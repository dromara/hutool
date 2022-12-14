package cn.hutool.core.date;

import java.time.LocalTime;

/**
 * 针对 {@link LocalTime} 封装的一些工具方法
 *
 * @author Looly
 * @since 6.0.0
 */
public class LocalTimeUtil {

	/**
	 * 获取最大时间，提供参数是否将毫秒归零
	 * <ul>
	 *     <li>如果{@code truncateMillisecond}为{@code false}，返回时间最大值，为：23:59:59,999</li>
	 *     <li>如果{@code truncateMillisecond}为{@code true}，返回时间最大值，为：23:59:59,000</li>
	 * </ul>
	 *
	 * @param truncateMillisecond 是否毫秒归零
	 * @return {@link LocalTime}时间最大值
	 */
	public static LocalTime max(final boolean truncateMillisecond) {
		return truncateMillisecond ? LocalTime.of(23, 59, 59) : LocalTime.MAX;
	}
}
