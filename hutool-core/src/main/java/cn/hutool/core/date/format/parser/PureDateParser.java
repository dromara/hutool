package cn.hutool.core.date.format.parser;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.format.DefaultDateBasic;

/**
 * 纯数字的日期字符串解析，支持格式包括；
 * <ul>
 *   <li>yyyyMMddHHmmss</li>
 *   <li>yyyyMMddHHmmssSSS</li>
 *   <li>yyyyMMdd</li>
 *   <li>HHmmss</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class PureDateParser extends DefaultDateBasic implements DateParser {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static PureDateParser INSTANCE = new PureDateParser();

	@Override
	public DateTime parse(final String source) throws DateException {
		final int length = source.length();
		// 纯数字形式
		if (length == DatePattern.PURE_DATETIME_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_DATETIME_FORMAT);
		} else if (length == DatePattern.PURE_DATETIME_MS_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_DATETIME_MS_FORMAT);
		} else if (length == DatePattern.PURE_DATE_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_DATE_FORMAT);
		} else if (length == DatePattern.PURE_TIME_PATTERN.length()) {
			return new DateTime(source, DatePattern.PURE_TIME_FORMAT);
		}

		throw new DateException("No pure format fit for date String [{}] !", source);
	}

}
