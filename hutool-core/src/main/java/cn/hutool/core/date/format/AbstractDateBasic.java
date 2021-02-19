package cn.hutool.core.date.format;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

public abstract class AbstractDateBasic implements DateBasic, Serializable {
	private static final long serialVersionUID = 6333136319870641818L;
	
	/** The pattern */
	protected final  String pattern;
	/** The time zone. */
	protected final TimeZone timeZone;
	/** The locale. */
	protected final Locale locale;
	
	/**
	 * 构造，内部使用
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区{@link TimeZone}
	 * @param locale 非空{@link Locale} 日期地理位置
	 */
	protected AbstractDateBasic(final String pattern, final TimeZone timeZone, final Locale locale) {
		this.pattern = pattern;
		this.timeZone = timeZone;
		this.locale = locale;
	}

	// ----------------------------------------------------------------------- Accessors
	@Override
	public String getPattern() {
		return pattern;
	}

	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	// ----------------------------------------------------------------------- Basics
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof FastDatePrinter == false) {
			return false;
		}
		final AbstractDateBasic other = (AbstractDateBasic) obj;
		return pattern.equals(other.pattern) && timeZone.equals(other.timeZone) && locale.equals(other.locale);
	}

	@Override
	public int hashCode() {
		return pattern.hashCode() + 13 * (timeZone.hashCode() + 13 * locale.hashCode());
	}

	@Override
	public String toString() {
		return "FastDatePrinter[" + pattern + "," + locale + "," + timeZone.getID() + "]";
	}
}
