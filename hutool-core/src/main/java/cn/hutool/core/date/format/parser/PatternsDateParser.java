package cn.hutool.core.date.format.parser;

import cn.hutool.core.date.CalendarUtil;
import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.format.DefaultDateBasic;

import java.util.Calendar;
import java.util.Locale;

/**
 * 通过给定的日期格式解析日期时间字符串。<br>
 * 传入的日期格式会逐个尝试，直到解析成功，返回{@link Calendar}对象，否则抛出{@link DateException}异常。
 *
 * @author looly
 * @since 6.0.0
 */
public class PatternsDateParser extends DefaultDateBasic implements DateParser {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建 PatternsDateParser
	 *
	 * @param parsePatterns 多个日期格式
	 * @return PatternsDateParser
	 */
	public static PatternsDateParser of(final String... parsePatterns) {
		return new PatternsDateParser(parsePatterns);
	}

	private String[] parsePatterns;
	private Locale locale;

	/**
	 * 构造
	 *
	 * @param parsePatterns 多个日期格式
	 */
	public PatternsDateParser(final String... parsePatterns) {
		this.parsePatterns = parsePatterns;
	}

	/**
	 * 设置多个日期格式
	 *
	 * @param parsePatterns 日期格式列表
	 * @return this
	 */
	public PatternsDateParser setParsePatterns(final String... parsePatterns) {
		this.parsePatterns = parsePatterns;
		return this;
	}

	/**
	 * 设置{@link Locale}
	 *
	 * @param locale {@link Locale}
	 * @return this
	 */
	public PatternsDateParser setLocale(final Locale locale) {
		this.locale = locale;
		return this;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public DateTime parse(final String source) {
		return new DateTime(CalendarUtil.parseByPatterns(source, this.locale, this.parsePatterns));
	}
}
