package cn.hutool.core.date.format.parser;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.format.DefaultDateBasic;

/**
 * CST日期字符串（JDK的Date对象toString默认格式）解析，支持格式类似于；
 * <pre>
 *   Tue Jun 4 16:25:15 +0800 2019
 *   Thu May 16 17:57:18 GMT+08:00 2019
 *   Wed Aug 01 00:00:00 CST 2012
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class CSTDateParser extends DefaultDateBasic implements DateParser {

	public static CSTDateParser INSTANCE = new CSTDateParser();

	@Override
	public DateTime parse(final String source) {
		return new DateTime(source, DatePattern.JDK_DATETIME_FORMAT);
	}
}
