package cn.hutool.core.date.format;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期格式化输出接口<br>
 * Thanks to Apache Commons Lang 3.5
 * @author Looly
 * @since 2.16.2
 */
public interface DatePrinter extends DateBasic {

	/**
	 * 格式化日期表示的毫秒数
	 *
	 * @param millis 日期毫秒数
	 * @return the formatted string
	 * @since 2.1
	 */
	String format(long millis);

	/**
	 * 使用 {@code GregorianCalendar} 格式化 {@code Date}
	 *
	 * @param date 日期 {@link Date}
	 * @return 格式化后的字符串
	 */
	String format(Date date);

	/**
	 * <p>
	 * Formats a {@code Calendar} object.
	 * </p>
	 * 格式化 {@link Calendar}
	 *
	 * @param calendar {@link Calendar}
	 * @return 格式化后的字符串
	 */
	String format(Calendar calendar);

	/**
	 * <p>
	 * Formats a millisecond {@code long} value into the supplied {@code Appendable}.
	 * </p>
	 *
	 * @param millis the millisecond value to format
	 * @param buf the buffer to format into
	 * @param <B> the Appendable class type, usually StringBuilder or StringBuffer.
	 * @return the specified string buffer
	 */
	<B extends Appendable> B format(long millis, B buf);

	/**
	 * <p>
	 * Formats a {@code Date} object into the supplied {@code Appendable} using a {@code GregorianCalendar}.
	 * </p>
	 *
	 * @param date the date to format
	 * @param buf the buffer to format into
	 * @param <B> the Appendable class type, usually StringBuilder or StringBuffer.
	 * @return the specified string buffer
	 */
	<B extends Appendable> B format(Date date, B buf);

	/**
	 * <p>
	 * Formats a {@code Calendar} object into the supplied {@code Appendable}.
	 * </p>
	 * The TimeZone set on the Calendar is only used to adjust the time offset. The TimeZone specified during the construction of the Parser will determine the TimeZone used in the formatted string.
	 *
	 * @param calendar the calendar to format
	 * @param buf the buffer to format into
	 * @param <B> the Appendable class type, usually StringBuilder or StringBuffer.
	 * @return the specified string buffer
	 */
	<B extends Appendable> B format(Calendar calendar, B buf);
}
