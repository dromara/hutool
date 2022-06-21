package cn.hutool.core.date.format;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 默认日期基本信息类，包括：
 * <ul>
 *     <li>{@link #getPattern()}返回{@code null}</li>
 *     <li>{@link #getTimeZone()} ()}返回{@link TimeZone#getDefault()}</li>
 *     <li>{@link #getLocale()} ()} ()}返回{@link Locale#getDefault()}</li>
 * </ul>
 *
 * @author looly
 */
public class DefaultDateBasic implements DateBasic, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public String getPattern() {
		return null;
	}

	@Override
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}
}
