package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateException;
import org.dromara.hutool.core.date.format.DefaultDateBasic;

import java.util.Date;
import java.util.List;

/**
 * 基于注册的日期解析器，通过遍历列表，找到合适的解析器，然后解析为日期<br>
 * 默认的，可以调用{@link #INSTANCE}使用全局的解析器，亦或者通过构造自定义独立的注册解析器
 *
 * @author looly
 * @since 6.0.0
 */
public class RegisterDateParser extends DefaultDateBasic implements DateParser {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final RegisterDateParser INSTANCE = new RegisterDateParser();

	private final List<PredicateDateParser> parserList;

	/**
	 * 构造
	 */
	public RegisterDateParser() {
		parserList = ListUtil.of(
			// 纯数字形式
			PureDateParser.INSTANCE,
			// HH:mm:ss 或者 HH:mm 时间格式匹配单独解析
			TimeParser.INSTANCE,
			// JDK的Date对象toString默认格式，类似于：
			// Tue Jun 4 16:25:15 +0800 2019
			// Thu May 16 17:57:18 GMT+08:00 2019
			// Wed Aug 01 00:00:00 CST 2012
			CSTDateParser.INSTANCE,
			// ISO8601标准时间
			// yyyy-MM-dd'T'HH:mm:ss'Z'
			// yyyy-MM-dd'T'HH:mm:ss+0800
			ISO8601DateParser.INSTANCE
		);
	}

	@Override
	public Date parse(final String source) throws DateException {
		return parserList
			.stream()
			.filter(predicateDateParser -> predicateDateParser.test(source))
			.findFirst()
			.map(predicateDateParser -> predicateDateParser.parse(source)).orElse(null);
	}

	/**
	 * 注册自定义的{@link PredicateDateParser}<br>
	 * 通过此方法，用户可以自定义日期字符串的匹配和解析，通过循环匹配，找到合适的解析器，解析之。
	 *
	 * @param dateParser {@link PredicateDateParser}
	 * @return this
	 */
	public RegisterDateParser register(final PredicateDateParser dateParser) {
		this.parserList.add(dateParser);
		return this;
	}
}
