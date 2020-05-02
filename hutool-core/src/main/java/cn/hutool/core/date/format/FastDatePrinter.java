package cn.hutool.core.date.format;

import cn.hutool.core.date.DateException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * {@link java.text.SimpleDateFormat} 的线程安全版本，用于将 {@link Date} 格式化输出<br>
 * Thanks to Apache Commons Lang 3.5
 *
 * @since 2.16.2
 * @see FastDateParser
 */
class FastDatePrinter extends AbstractDateBasic implements DatePrinter {
	private static final long serialVersionUID = -6305750172255764887L;
	
	/** 规则列表. */
	private transient Rule[] rules;
	/** 估算最大长度. */
	private transient int mMaxLengthEstimate;

	// Constructor
	// -----------------------------------------------------------------------
	/**
	 * 构造，内部使用<br>
	 * 
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区{@link TimeZone}
	 * @param locale 非空{@link Locale} 日期地理位置
	 */
	protected FastDatePrinter(final String pattern, final TimeZone timeZone, final Locale locale) {
		super(pattern, timeZone, locale);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		final List<Rule> rulesList = parsePattern();
		rules = rulesList.toArray(new Rule[0]);

		int len = 0;
		for (int i = rules.length; --i >= 0;) {
			len += rules[i].estimateLength();
		}

		mMaxLengthEstimate = len;
	}

	// Parse the pattern
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Returns a list of Rules given a pattern.
	 * </p>
	 *
	 * @return a {@code List} of Rule objects
	 * @throws IllegalArgumentException if pattern is invalid
	 */
	protected List<Rule> parsePattern() {
		final DateFormatSymbols symbols = new DateFormatSymbols(locale);
		final List<Rule> rules = new ArrayList<>();

		final String[] ERAs = symbols.getEras();
		final String[] months = symbols.getMonths();
		final String[] shortMonths = symbols.getShortMonths();
		final String[] weekdays = symbols.getWeekdays();
		final String[] shortWeekdays = symbols.getShortWeekdays();
		final String[] AmPmStrings = symbols.getAmPmStrings();

		final int length = pattern.length();
		final int[] indexRef = new int[1];

		for (int i = 0; i < length; i++) {
			indexRef[0] = i;
			final String token = parseToken(pattern, indexRef);
			i = indexRef[0];

			final int tokenLen = token.length();
			if (tokenLen == 0) {
				break;
			}

			Rule rule;
			final char c = token.charAt(0);

			switch (c) {
				case 'G': // era designator (text)
					rule = new TextField(Calendar.ERA, ERAs);
					break;
				case 'y': // year (number)
				case 'Y': // week year
					if (tokenLen == 2) {
						rule = TwoDigitYearField.INSTANCE;
					} else {
						rule = selectNumberRule(Calendar.YEAR, Math.max(tokenLen, 4));
					}
					if (c == 'Y') {
						rule = new WeekYear((NumberRule) rule);
					}
					break;
				case 'M': // month in year (text and number)
					if (tokenLen >= 4) {
						rule = new TextField(Calendar.MONTH, months);
					} else if (tokenLen == 3) {
						rule = new TextField(Calendar.MONTH, shortMonths);
					} else if (tokenLen == 2) {
						rule = TwoDigitMonthField.INSTANCE;
					} else {
						rule = UnpaddedMonthField.INSTANCE;
					}
					break;
				case 'd': // day in month (number)
					rule = selectNumberRule(Calendar.DAY_OF_MONTH, tokenLen);
					break;
				case 'h': // hour in am/pm (number, 1..12)
					rule = new TwelveHourField(selectNumberRule(Calendar.HOUR, tokenLen));
					break;
				case 'H': // hour in day (number, 0..23)
					rule = selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen);
					break;
				case 'm': // minute in hour (number)
					rule = selectNumberRule(Calendar.MINUTE, tokenLen);
					break;
				case 's': // second in minute (number)
					rule = selectNumberRule(Calendar.SECOND, tokenLen);
					break;
				case 'S': // millisecond (number)
					rule = selectNumberRule(Calendar.MILLISECOND, tokenLen);
					break;
				case 'E': // day in week (text)
					rule = new TextField(Calendar.DAY_OF_WEEK, tokenLen < 4 ? shortWeekdays : weekdays);
					break;
				case 'u': // day in week (number)
					rule = new DayInWeekField(selectNumberRule(Calendar.DAY_OF_WEEK, tokenLen));
					break;
				case 'D': // day in year (number)
					rule = selectNumberRule(Calendar.DAY_OF_YEAR, tokenLen);
					break;
				case 'F': // day of week in month (number)
					rule = selectNumberRule(Calendar.DAY_OF_WEEK_IN_MONTH, tokenLen);
					break;
				case 'w': // week in year (number)
					rule = selectNumberRule(Calendar.WEEK_OF_YEAR, tokenLen);
					break;
				case 'W': // week in month (number)
					rule = selectNumberRule(Calendar.WEEK_OF_MONTH, tokenLen);
					break;
				case 'a': // am/pm marker (text)
					rule = new TextField(Calendar.AM_PM, AmPmStrings);
					break;
				case 'k': // hour in day (1..24)
					rule = new TwentyFourHourField(selectNumberRule(Calendar.HOUR_OF_DAY, tokenLen));
					break;
				case 'K': // hour in am/pm (0..11)
					rule = selectNumberRule(Calendar.HOUR, tokenLen);
					break;
				case 'X': // ISO 8601
					rule = Iso8601_Rule.getRule(tokenLen);
					break;
				case 'z': // time zone (text)
					if (tokenLen >= 4) {
						rule = new TimeZoneNameRule(timeZone, locale, TimeZone.LONG);
					} else {
						rule = new TimeZoneNameRule(timeZone, locale, TimeZone.SHORT);
					}
					break;
				case 'Z': // time zone (value)
					if (tokenLen == 1) {
						rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
					} else if (tokenLen == 2) {
						rule = Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES;
					} else {
						rule = TimeZoneNumberRule.INSTANCE_COLON;
					}
					break;
				case '\'': // literal text
					final String sub = token.substring(1);
					if (sub.length() == 1) {
						rule = new CharacterLiteral(sub.charAt(0));
					} else {
						rule = new StringLiteral(sub);
					}
					break;
				default:
					throw new IllegalArgumentException("Illegal pattern component: " + token);
			}

			rules.add(rule);
		}

		return rules;
	}

	/**
	 * <p>
	 * Performs the parsing of tokens.
	 * </p>
	 *
	 * @param pattern the pattern
	 * @param indexRef index references
	 * @return parsed token
	 */
	protected String parseToken(final String pattern, final int[] indexRef) {
		final StringBuilder buf = new StringBuilder();

		int i = indexRef[0];
		final int length = pattern.length();

		char c = pattern.charAt(i);
		if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
			// Scan a run of the same character, which indicates a time
			// pattern.
			buf.append(c);

			while (i + 1 < length) {
				final char peek = pattern.charAt(i + 1);
				if (peek == c) {
					buf.append(c);
					i++;
				} else {
					break;
				}
			}
		} else {
			// This will identify token as text.
			buf.append('\'');

			boolean inLiteral = false;

			for (; i < length; i++) {
				c = pattern.charAt(i);

				if (c == '\'') {
					if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
						// '' is treated as escaped '
						i++;
						buf.append(c);
					} else {
						inLiteral = !inLiteral;
					}
				} else if (!inLiteral && (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')) {
					i--;
					break;
				} else {
					buf.append(c);
				}
			}
		}

		indexRef[0] = i;
		return buf.toString();
	}

	/**
	 * <p>
	 * Gets an appropriate rule for the padding required.
	 * </p>
	 *
	 * @param field the field to get a rule for
	 * @param padding the padding required
	 * @return a new rule with the correct padding
	 */
	protected NumberRule selectNumberRule(final int field, final int padding) {
		switch (padding) {
			case 1:
				return new UnpaddedNumberField(field);
			case 2:
				return new TwoDigitNumberField(field);
			default:
				return new PaddedNumberField(field, padding);
		}
	}

	// Format methods
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Formats a {@code Date}, {@code Calendar} or {@code Long} (milliseconds) object.
	 * </p>
	 * 
	 * @param obj the object to format
	 * @return The formatted value.
	 */
	String format(final Object obj) {
		if (obj instanceof Date) {
			return format((Date) obj);
		} else if (obj instanceof Calendar) {
			return format((Calendar) obj);
		} else if (obj instanceof Long) {
			return format(((Long) obj).longValue());
		} else {
			throw new IllegalArgumentException("Unknown class: " + (obj == null ? "<null>" : obj.getClass().getName()));
		}
	}

	@Override
	public String format(final long millis) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTimeInMillis(millis);
		return applyRulesToString(c);
	}

	@Override
	public String format(final Date date) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTime(date);
		return applyRulesToString(c);
	}

	@Override
	public String format(final Calendar calendar) {
		return format(calendar, new StringBuilder(mMaxLengthEstimate)).toString();
	}

	@Override
	public <B extends Appendable> B format(final long millis, final B buf) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTimeInMillis(millis);
		return applyRules(c, buf);
	}

	@Override
	public <B extends Appendable> B format(final Date date, final B buf) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTime(date);
		return applyRules(c, buf);
	}

	@Override
	public <B extends Appendable> B format(Calendar calendar, final B buf) {
		// do not pass in calendar directly, this will cause TimeZone of FastDatePrinter to be ignored
		if (!calendar.getTimeZone().equals(timeZone)) {
			calendar = (Calendar) calendar.clone();
			calendar.setTimeZone(timeZone);
		}
		return applyRules(calendar, buf);
	}
	
	/**
	 * Creates a String representation of the given Calendar by applying the rules of this printer to it.
	 * 
	 * @param c the Calender to apply the rules to.
	 * @return a String representation of the given Calendar.
	 */
	private String applyRulesToString(final Calendar c) {
		return applyRules(c, new StringBuilder(mMaxLengthEstimate)).toString();
	}

	/**
	 * <p>
	 * Performs the formatting by applying the rules to the specified calendar.
	 * </p>
	 *
	 * @param calendar the calendar to format
	 * @param buf the buffer to format into
	 * @param <B> the Appendable class type, usually StringBuilder or StringBuffer.
	 * @return the specified string buffer
	 */
	private <B extends Appendable> B applyRules(final Calendar calendar, final B buf) {
		try {
			for (final Rule rule : this.rules) {
				rule.appendTo(buf, calendar);
			}
		} catch (final IOException e) {
			throw new DateException(e);
		}
		return buf;
	}

	/**
	 *估算生成的日期字符串长度<br>
	 * 实际生成的字符串长度小于或等于此值
	 *
	 * @return 日期字符串长度
	 */
	public int getMaxLengthEstimate() {
		return mMaxLengthEstimate;
	}

	// Serializing
	// -----------------------------------------------------------------------
	/**
	 * Create the object after serialization. This implementation reinitializes the transient properties.
	 *
	 * @param in ObjectInputStream from which the object is being deserialized.
	 * @throws IOException if there is an IO issue.
	 * @throws ClassNotFoundException if a class cannot be found.
	 */
	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	/**
	 * Appends two digits to the given buffer.
	 *
	 * @param buffer the buffer to append to.
	 * @param value the value to append digits from.
	 */
	private static void appendDigits(final Appendable buffer, final int value) throws IOException {
		buffer.append((char) (value / 10 + '0'));
		buffer.append((char) (value % 10 + '0'));
	}

	private static final int MAX_DIGITS = 10; // log10(Integer.MAX_VALUE) ~= 9.3

	/**
	 * Appends all digits to the given buffer.
	 *
	 * @param buffer the buffer to append to.
	 * @param value the value to append digits from.
	 */
	private static void appendFullDigits(final Appendable buffer, int value, int minFieldWidth) throws IOException {
		// specialized paths for 1 to 4 digits -> avoid the memory allocation from the temporary work array
		// see LANG-1248
		if (value < 10000) {
			// less memory allocation path works for four digits or less

			int nDigits = 4;
			if (value < 1000) {
				--nDigits;
				if (value < 100) {
					--nDigits;
					if (value < 10) {
						--nDigits;
					}
				}
			}
			// left zero pad
			for (int i = minFieldWidth - nDigits; i > 0; --i) {
				buffer.append('0');
			}

			switch (nDigits) {
				case 4:
					buffer.append((char) (value / 1000 + '0'));
					value %= 1000;
				case 3:
					if (value >= 100) {
						buffer.append((char) (value / 100 + '0'));
						value %= 100;
					} else {
						buffer.append('0');
					}
				case 2:
					if (value >= 10) {
						buffer.append((char) (value / 10 + '0'));
						value %= 10;
					} else {
						buffer.append('0');
					}
				case 1:
					buffer.append((char) (value + '0'));
			}
		} else {
			// more memory allocation path works for any digits

			// build up decimal representation in reverse
			final char[] work = new char[MAX_DIGITS];
			int digit = 0;
			while (value != 0) {
				work[digit++] = (char) (value % 10 + '0');
				value = value / 10;
			}

			// pad with zeros
			while (digit < minFieldWidth) {
				buffer.append('0');
				--minFieldWidth;
			}

			// reverse
			while (--digit >= 0) {
				buffer.append(work[digit]);
			}
		}
	}

	// Rules
	// -----------------------------------------------------------------------
	/**
	 * 规则
	 */
	private interface Rule {
		/**
		 * Returns the estimated length of the result.
		 *
		 * @return the estimated length
		 */
		int estimateLength();

		/**
		 * Appends the value of the specified calendar to the output buffer based on the rule implementation.
		 *
		 * @param buf the output buffer
		 * @param calendar calendar to be appended
		 * @throws IOException if an I/O error occurs
		 */
		void appendTo(Appendable buf, Calendar calendar) throws IOException;
	}

	/**
	 * <p>
	 * Inner class defining a numeric rule.
	 * </p>
	 */
	private interface NumberRule extends Rule {
		/**
		 * Appends the specified value to the output buffer based on the rule implementation.
		 *
		 * @param buffer the output buffer
		 * @param value the value to be appended
		 * @throws IOException if an I/O error occurs
		 */
		void appendTo(Appendable buffer, int value) throws IOException;
	}

	/**
	 * <p>
	 * Inner class to output a constant single character.
	 * </p>
	 */
	private static class CharacterLiteral implements Rule {
		private final char mValue;

		/**
		 * Constructs a new instance of {@code CharacterLiteral} to hold the specified value.
		 *
		 * @param value the character literal
		 */
		CharacterLiteral(final char value) {
			mValue = value;
		}

		@Override
		public int estimateLength() {
			return 1;
		}

		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			buffer.append(mValue);
		}
	}

	/**
	 * <p>
	 * Inner class to output a constant string.
	 * </p>
	 */
	private static class StringLiteral implements Rule {
		private final String mValue;

		/**
		 * Constructs a new instance of {@code StringLiteral} to hold the specified value.
		 *
		 * @param value the string literal
		 */
		StringLiteral(final String value) {
			mValue = value;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return mValue.length();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			buffer.append(mValue);
		}
	}

	/**
	 * <p>
	 * Inner class to output one of a set of values.
	 * </p>
	 */
	private static class TextField implements Rule {
		private final int mField;
		private final String[] mValues;

		/**
		 * Constructs an instance of {@code TextField} with the specified field and values.
		 *
		 * @param field the field
		 * @param values the field values
		 */
		TextField(final int field, final String[] values) {
			mField = field;
			mValues = values;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			int max = 0;
			for (int i = mValues.length; --i >= 0;) {
				final int len = mValues[i].length();
				if (len > max) {
					max = len;
				}
			}
			return max;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			buffer.append(mValues[calendar.get(mField)]);
		}
	}

	/**
	 * <p>
	 * Inner class to output an unpadded number.
	 * </p>
	 */
	private static class UnpaddedNumberField implements NumberRule {
		private final int mField;

		/**
		 * Constructs an instance of {@code UnpadedNumberField} with the specified field.
		 *
		 * @param field the field
		 */
		UnpaddedNumberField(final int field) {
			mField = field;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return 4;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			appendTo(buffer, calendar.get(mField));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void appendTo(final Appendable buffer, final int value) throws IOException {
			if (value < 10) {
				buffer.append((char) (value + '0'));
			} else if (value < 100) {
				appendDigits(buffer, value);
			} else {
				appendFullDigits(buffer, value, 1);
			}
		}
	}

	/**
	 * <p>
	 * Inner class to output an unpadded month.
	 * </p>
	 */
	private static class UnpaddedMonthField implements NumberRule {
		static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

		/**
		 * Constructs an instance of {@code UnpaddedMonthField}.
		 *
		 */
		UnpaddedMonthField() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return 2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void appendTo(final Appendable buffer, final int value) throws IOException {
			if (value < 10) {
				buffer.append((char) (value + '0'));
			} else {
				appendDigits(buffer, value);
			}
		}
	}

	/**
	 * <p>
	 * Inner class to output a padded number.
	 * </p>
	 */
	private static class PaddedNumberField implements NumberRule {
		private final int mField;
		private final int mSize;

		/**
		 * Constructs an instance of {@code PaddedNumberField}.
		 *
		 * @param field the field
		 * @param size size of the output field
		 */
		PaddedNumberField(final int field, final int size) {
			if (size < 3) {
				// Should use UnpaddedNumberField or TwoDigitNumberField.
				throw new IllegalArgumentException();
			}
			mField = field;
			mSize = size;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return mSize;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			appendTo(buffer, calendar.get(mField));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void appendTo(final Appendable buffer, final int value) throws IOException {
			appendFullDigits(buffer, value, mSize);
		}
	}

	/**
	 * <p>
	 * Inner class to output a two digit number.
	 * </p>
	 */
	private static class TwoDigitNumberField implements NumberRule {
		private final int mField;

		/**
		 * Constructs an instance of {@code TwoDigitNumberField} with the specified field.
		 *
		 * @param field the field
		 */
		TwoDigitNumberField(final int field) {
			mField = field;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return 2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			appendTo(buffer, calendar.get(mField));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void appendTo(final Appendable buffer, final int value) throws IOException {
			if (value < 100) {
				appendDigits(buffer, value);
			} else {
				appendFullDigits(buffer, value, 2);
			}
		}
	}

	/**
	 * <p>
	 * Inner class to output a two digit year.
	 * </p>
	 */
	private static class TwoDigitYearField implements NumberRule {
		static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

		/**
		 * Constructs an instance of {@code TwoDigitYearField}.
		 */
		TwoDigitYearField() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return 2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			appendTo(buffer, calendar.get(Calendar.YEAR) % 100);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void appendTo(final Appendable buffer, final int value) throws IOException {
			appendDigits(buffer, value);
		}
	}

	/**
	 * <p>
	 * Inner class to output a two digit month.
	 * </p>
	 */
	private static class TwoDigitMonthField implements NumberRule {
		static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

		/**
		 * Constructs an instance of {@code TwoDigitMonthField}.
		 */
		TwoDigitMonthField() {
			super();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return 2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			appendTo(buffer, calendar.get(Calendar.MONTH) + 1);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public final void appendTo(final Appendable buffer, final int value) throws IOException {
			appendDigits(buffer, value);
		}
	}

	/**
	 * <p>
	 * Inner class to output the twelve hour field.
	 * </p>
	 */
	private static class TwelveHourField implements NumberRule {
		private final NumberRule mRule;

		/**
		 * Constructs an instance of {@code TwelveHourField} with the specified {@code NumberRule}.
		 *
		 * @param rule the rule
		 */
		TwelveHourField(final NumberRule rule) {
			mRule = rule;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return mRule.estimateLength();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			int value = calendar.get(Calendar.HOUR);
			if (value == 0) {
				value = calendar.getLeastMaximum(Calendar.HOUR) + 1;
			}
			mRule.appendTo(buffer, value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final int value) throws IOException {
			mRule.appendTo(buffer, value);
		}
	}

	/**
	 * <p>
	 * Inner class to output the twenty four hour field.
	 * </p>
	 */
	private static class TwentyFourHourField implements NumberRule {
		private final NumberRule mRule;

		/**
		 * Constructs an instance of {@code TwentyFourHourField} with the specified {@code NumberRule}.
		 *
		 * @param rule the rule
		 */
		TwentyFourHourField(final NumberRule rule) {
			mRule = rule;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return mRule.estimateLength();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			int value = calendar.get(Calendar.HOUR_OF_DAY);
			if (value == 0) {
				value = calendar.getMaximum(Calendar.HOUR_OF_DAY) + 1;
			}
			mRule.appendTo(buffer, value);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final int value) throws IOException {
			mRule.appendTo(buffer, value);
		}
	}

	/**
	 * <p>
	 * Inner class to output the numeric day in week.
	 * </p>
	 */
	private static class DayInWeekField implements NumberRule {
		private final NumberRule mRule;

		DayInWeekField(final NumberRule rule) {
			mRule = rule;
		}

		@Override
		public int estimateLength() {
			return mRule.estimateLength();
		}

		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			final int value = calendar.get(Calendar.DAY_OF_WEEK);
			mRule.appendTo(buffer, value != Calendar.SUNDAY ? value - 1 : 7);
		}

		@Override
		public void appendTo(final Appendable buffer, final int value) throws IOException {
			mRule.appendTo(buffer, value);
		}
	}

	/**
	 * <p>
	 * Inner class to output the numeric day in week.
	 * </p>
	 */
	private static class WeekYear implements NumberRule {
		private final NumberRule mRule;

		WeekYear(final NumberRule rule) {
			mRule = rule;
		}

		@Override
		public int estimateLength() {
			return mRule.estimateLength();
		}

		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			mRule.appendTo(buffer, calendar.getWeekYear());
		}

		@Override
		public void appendTo(final Appendable buffer, final int value) throws IOException {
			mRule.appendTo(buffer, value);
		}
	}

	// -----------------------------------------------------------------------

	private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<>(7);

	/**
	 * <p>
	 * Gets the time zone display name, using a cache for performance.
	 * </p>
	 *
	 * @param tz the zone to query
	 * @param daylight true if daylight savings
	 * @param style the style to use {@code TimeZone.LONG} or {@code TimeZone.SHORT}
	 * @param locale the locale to use
	 * @return the textual name of the time zone
	 */
	static String getTimeZoneDisplay(final TimeZone tz, final boolean daylight, final int style, final Locale locale) {
		final TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
		String value = cTimeZoneDisplayCache.get(key);
		if (value == null) {
			// This is a very slow call, so cache the results.
			value = tz.getDisplayName(daylight, style, locale);
			final String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
			if (prior != null) {
				value = prior;
			}
		}
		return value;
	}

	/**
	 * <p>
	 * Inner class to output a time zone name.
	 * </p>
	 */
	private static class TimeZoneNameRule implements Rule {
		private final Locale mLocale;
		private final int mStyle;
		private final String mStandard;
		private final String mDaylight;

		/**
		 * Constructs an instance of {@code TimeZoneNameRule} with the specified properties.
		 *
		 * @param timeZone the time zone
		 * @param locale the locale
		 * @param style the style
		 */
		TimeZoneNameRule(final TimeZone timeZone, final Locale locale, final int style) {
			mLocale = locale;
			mStyle = style;

			mStandard = getTimeZoneDisplay(timeZone, false, style, locale);
			mDaylight = getTimeZoneDisplay(timeZone, true, style, locale);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			// We have no access to the Calendar object that will be passed to
			// appendTo so base estimate on the TimeZone passed to the
			// constructor
			return Math.max(mStandard.length(), mDaylight.length());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			final TimeZone zone = calendar.getTimeZone();
			if (calendar.get(Calendar.DST_OFFSET) != 0) {
				buffer.append(getTimeZoneDisplay(zone, true, mStyle, mLocale));
			} else {
				buffer.append(getTimeZoneDisplay(zone, false, mStyle, mLocale));
			}
		}
	}

	/**
	 * <p>
	 * Inner class to output a time zone as a number {@code +/-HHMM} or {@code +/-HH:MM}.
	 * </p>
	 */
	private static class TimeZoneNumberRule implements Rule {
		static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true);
		static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false);

		final boolean mColon;

		/**
		 * Constructs an instance of {@code TimeZoneNumberRule} with the specified properties.
		 *
		 * @param colon add colon between HH and MM in the output if {@code true}
		 */
		TimeZoneNumberRule(final boolean colon) {
			mColon = colon;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return 5;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {

			int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);

			if (offset < 0) {
				buffer.append('-');
				offset = -offset;
			} else {
				buffer.append('+');
			}

			final int hours = offset / (60 * 60 * 1000);
			appendDigits(buffer, hours);

			if (mColon) {
				buffer.append(':');
			}

			final int minutes = offset / (60 * 1000) - 60 * hours;
			appendDigits(buffer, minutes);
		}
	}

	/**
	 * <p>
	 * Inner class to output a time zone as a number {@code +/-HHMM} or {@code +/-HH:MM}.
	 * </p>
	 */
	private static class Iso8601_Rule implements Rule {

		// Sign TwoDigitHours or Z
		static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
		// Sign TwoDigitHours Minutes or Z
		static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
		// Sign TwoDigitHours : Minutes or Z
		static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);

		/**
		 * Factory method for Iso8601_Rules.
		 *
		 * @param tokenLen a token indicating the length of the TimeZone String to be formatted.
		 * @return a Iso8601_Rule that can format TimeZone String of length {@code tokenLen}. If no such rule exists, an IllegalArgumentException will be thrown.
		 */
		static Iso8601_Rule getRule(final int tokenLen) {
			switch (tokenLen) {
				case 1:
					return Iso8601_Rule.ISO8601_HOURS;
				case 2:
					return Iso8601_Rule.ISO8601_HOURS_MINUTES;
				case 3:
					return Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES;
				default:
					throw new IllegalArgumentException("invalid number of X");
			}
		}

		final int length;

		/**
		 * Constructs an instance of {@code Iso8601_Rule} with the specified properties.
		 *
		 * @param length The number of characters in output (unless Z is output)
		 */
		Iso8601_Rule(final int length) {
			this.length = length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int estimateLength() {
			return length;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
			int offset = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET);
			if (offset == 0) {
				buffer.append("Z");
				return;
			}

			if (offset < 0) {
				buffer.append('-');
				offset = -offset;
			} else {
				buffer.append('+');
			}

			final int hours = offset / (60 * 60 * 1000);
			appendDigits(buffer, hours);

			if (length < 5) {
				return;
			}

			if (length == 6) {
				buffer.append(':');
			}

			final int minutes = offset / (60 * 1000) - 60 * hours;
			appendDigits(buffer, minutes);
		}
	}

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Inner class that acts as a compound key for time zone names.
	 * </p>
	 */
	private static class TimeZoneDisplayKey {
		private final TimeZone mTimeZone;
		private final int mStyle;
		private final Locale mLocale;

		/**
		 * Constructs an instance of {@code TimeZoneDisplayKey} with the specified properties.
		 *
		 * @param timeZone the time zone
		 * @param daylight adjust the style for daylight saving time if {@code true}
		 * @param style the timezone style
		 * @param locale the timezone locale
		 */
		TimeZoneDisplayKey(final TimeZone timeZone, final boolean daylight, final int style, final Locale locale) {
			mTimeZone = timeZone;
			if (daylight) {
				mStyle = style | 0x80000000;
			} else {
				mStyle = style;
			}
			mLocale = locale;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return (mStyle * 31 + mLocale.hashCode()) * 31 + mTimeZone.hashCode();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof TimeZoneDisplayKey) {
				final TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
				return mTimeZone.equals(other.mTimeZone) && mStyle == other.mStyle && mLocale.equals(other.mLocale);
			}
			return false;
		}
	}
}
