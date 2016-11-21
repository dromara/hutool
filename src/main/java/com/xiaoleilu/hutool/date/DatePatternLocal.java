package com.xiaoleilu.hutool.date;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 线程安全的时间格式化器
 * @author Looly
 *
 */
public class DatePatternLocal {
	/** 标准日期（不含时间）格式化器 */
	protected static ThreadLocal<SimpleDateFormat> NORM_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN);
		};
	};
	/** 标准时间格式化器 */
	protected static ThreadLocal<SimpleDateFormat> NORM_TIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(DatePattern.NORM_TIME_PATTERN);
		};
	};
	/** 标准日期时间格式化器 */
	protected static ThreadLocal<SimpleDateFormat> NORM_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
		};
	};
	/** HTTP日期时间格式化器 */
	protected static ThreadLocal<SimpleDateFormat> HTTP_DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>(){
		synchronized protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(DatePattern.HTTP_DATETIME_PATTERN, Locale.US);
		};
	};
}
