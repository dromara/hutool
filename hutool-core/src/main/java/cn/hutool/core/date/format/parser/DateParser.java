package cn.hutool.core.date.format.parser;

import cn.hutool.core.date.format.DateBasic;

import java.text.ParseException;
import java.util.Date;

/**
 * 日期解析接口，用于解析日期字符串为 {@link Date} 对象<br>
 * Thanks to Apache Commons Lang 3.5
 */
public interface DateParser extends DateBasic {

	/**
	 * 将日期字符串解析并转换为  {@link Date} 对象<br>
	 * 等价于 {@link java.text.DateFormat#parse(String)}
	 *
	 * @param source 被解析的日期字符串
	 * @return {@link Date}对象
	 * @throws ParseException 转换异常，被转换的字符串格式错误。
	 */
	Date parse(String source) throws ParseException;

	/**
	 * 将日期字符串解析并转换为  {@link Date} 对象<br>
	 *
	 * @param source 被解析的日期字符串
	 * @return {@link Date}对象
	 * @throws ParseException if the beginning of the specified string cannot be parsed.
	 * @see java.text.DateFormat#parseObject(String)
	 */
	default Object parseObject(final String source) throws ParseException {
		return parse(source);
	}
}
