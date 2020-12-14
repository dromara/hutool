package cn.hutool.core.date.format;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Tuple;

/**
 * 日期格式化器缓存<br>
 * Thanks to Apache Commons Lang 3.5
 * 
 * @since 2.16.2
 */
abstract class FormatCache<F extends Format> {

	/**
	 * No date or no time. Used in same parameters as DateFormat.SHORT or DateFormat.LONG
	 */
	static final int NONE = -1;

	private final ConcurrentMap<Tuple, F> cInstanceCache = new ConcurrentHashMap<>(7);

	private static final ConcurrentMap<Tuple, String> C_DATE_TIME_INSTANCE_CACHE = new ConcurrentHashMap<>(7);

	/**
	 * 使用默认的pattern、timezone和locale获得缓存中的实例
	 * @return a date/time formatter
	 */
	public F getInstance() {
		return getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, null, null);
	}

	/**
	 * 使用 pattern, time zone and locale 获得对应的 格式化器
	 * 
	 * @param pattern 非空日期格式，使用与 {@link java.text.SimpleDateFormat}相同格式
	 * @param timeZone 时区，默认当前时区
	 * @param locale 地区，默认使用当前地区
	 * @return 格式化器
	 * @throws IllegalArgumentException pattern 无效或<code>null</code>
	 */
	public F getInstance(final String pattern, TimeZone timeZone, Locale locale) {
		Assert.notBlank(pattern, "pattern must not be blank") ;
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
		if (locale == null) {
			locale = Locale.getDefault();
		}
		final Tuple key = new Tuple(pattern, timeZone, locale);
		F format = cInstanceCache.get(key);
		if (format == null) {
			format = createInstance(pattern, timeZone, locale);
			final F previousValue = cInstanceCache.putIfAbsent(key, format);
			if (previousValue != null) {
				// another thread snuck in and did the same work
				// we should return the instance that is in ConcurrentMap
				format = previousValue;
			}
		}
		return format;
	}

	/**
	 * 创建格式化器
	 * 
	 * @param pattern 非空日期格式，使用与 {@link java.text.SimpleDateFormat}相同格式
	 * @param timeZone 时区，默认当前时区
	 * @param locale 地区，默认使用当前地区
	 * @return 格式化器
	 * @throws IllegalArgumentException pattern 无效或<code>null</code>
	 */
	abstract protected F createInstance(String pattern, TimeZone timeZone, Locale locale);

	/**
	 * <p>
	 * Gets a date/time formatter instance using the specified style, time zone and locale.
	 * </p>
	 * 
	 * @param dateStyle date style: FULL, LONG, MEDIUM, or SHORT, null indicates no date in format
	 * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT, null indicates no time in format
	 * @param timeZone optional time zone, overrides time zone of formatted date, null means use default Locale
	 * @param locale optional locale, overrides system locale
	 * @return a localized standard date/time formatter
	 * @throws IllegalArgumentException if the Locale has no date/time pattern defined
	 */
	// This must remain private, see LANG-884
	private F getDateTimeInstance(final Integer dateStyle, final Integer timeStyle, final TimeZone timeZone, Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		final String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
		return getInstance(pattern, timeZone, locale);
	}

	/**
	 * <p>
	 * Gets a date/time formatter instance using the specified style, time zone and locale.
	 * </p>
	 * 
	 * @param dateStyle date style: FULL, LONG, MEDIUM, or SHORT
	 * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT
	 * @param timeZone optional time zone, overrides time zone of formatted date, null means use default Locale
	 * @param locale optional locale, overrides system locale
	 * @return a localized standard date/time formatter
	 * @throws IllegalArgumentException if the Locale has no date/time pattern defined
	 */
	// package protected, for access from FastDateFormat; do not make public or protected
	F getDateTimeInstance(final int dateStyle, final int timeStyle, final TimeZone timeZone, final Locale locale) {
		return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
	}

	/**
	 * <p>
	 * Gets a date formatter instance using the specified style, time zone and locale.
	 * </p>
	 * 
	 * @param dateStyle date style: FULL, LONG, MEDIUM, or SHORT
	 * @param timeZone optional time zone, overrides time zone of formatted date, null means use default Locale
	 * @param locale optional locale, overrides system locale
	 * @return a localized standard date/time formatter
	 * @throws IllegalArgumentException if the Locale has no date/time pattern defined
	 */
	// package protected, for access from FastDateFormat; do not make public or protected
	F getDateInstance(final int dateStyle, final TimeZone timeZone, final Locale locale) {
		return getDateTimeInstance(dateStyle, null, timeZone, locale);
	}

	/**
	 * <p>
	 * Gets a time formatter instance using the specified style, time zone and locale.
	 * </p>
	 * 
	 * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT
	 * @param timeZone optional time zone, overrides time zone of formatted date, null means use default Locale
	 * @param locale optional locale, overrides system locale
	 * @return a localized standard date/time formatter
	 * @throws IllegalArgumentException if the Locale has no date/time pattern defined
	 */
	// package protected, for access from FastDateFormat; do not make public or protected
	F getTimeInstance(final int timeStyle, final TimeZone timeZone, final Locale locale) {
		return getDateTimeInstance(null, timeStyle, timeZone, locale);
	}

	/**
	 * <p>
	 * Gets a date/time format for the specified styles and locale.
	 * </p>
	 * 
	 * @param dateStyle date style: FULL, LONG, MEDIUM, or SHORT, null indicates no date in format
	 * @param timeStyle time style: FULL, LONG, MEDIUM, or SHORT, null indicates no time in format
	 * @param locale The non-null locale of the desired format
	 * @return a localized standard date/time format
	 * @throws IllegalArgumentException if the Locale has no date/time pattern defined
	 */
	// package protected, for access from test code; do not make public or protected
	static String getPatternForStyle(final Integer dateStyle, final Integer timeStyle, final Locale locale) {
		final Tuple key = new Tuple(dateStyle, timeStyle, locale);

		String pattern = C_DATE_TIME_INSTANCE_CACHE.get(key);
		if (pattern == null) {
			try {
				DateFormat formatter;
				if (dateStyle == null) {
					formatter = DateFormat.getTimeInstance(timeStyle, locale);
				} else if (timeStyle == null) {
					formatter = DateFormat.getDateInstance(dateStyle, locale);
				} else {
					formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
				}
				pattern = ((SimpleDateFormat) formatter).toPattern();
				final String previous = C_DATE_TIME_INSTANCE_CACHE.putIfAbsent(key, pattern);
				if (previous != null) {
					// even though it doesn't matter if another thread put the pattern
					// it's still good practice to return the String instance that is
					// actually in the ConcurrentMap
					pattern = previous;
				}
			} catch (final ClassCastException ex) {
				throw new IllegalArgumentException("No date time pattern for locale: " + locale);
			}
		}
		return pattern;
	}
}
