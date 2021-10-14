package cn.hutool.core.date;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * {@link ZoneId}和{@link TimeZone}相关封装
 *
 * @author looly
 * @since 5.7.15
 */
public class ZoneUtil {

	/**
	 * {@link ZoneId}转换为{@link TimeZone}，{@code null}则返回系统默认值
	 *
	 * @param zoneId {@link ZoneId}，{@code null}则返回系统默认值
	 * @return {@link TimeZone}
	 */
	public static TimeZone toTimeZone(ZoneId zoneId) {
		if (null == zoneId) {
			return TimeZone.getDefault();
		}

		return TimeZone.getTimeZone(zoneId);
	}

	/**
	 * {@link TimeZone}转换为{@link ZoneId}，{@code null}则返回系统默认值
	 *
	 * @param timeZone {@link TimeZone}，{@code null}则返回系统默认值
	 * @return {@link ZoneId}
	 */
	public static ZoneId toZoneId(TimeZone timeZone) {
		if (null == timeZone) {
			return ZoneId.systemDefault();
		}

		return timeZone.toZoneId();
	}
}
