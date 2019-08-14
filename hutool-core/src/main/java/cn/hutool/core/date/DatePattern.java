package cn.hutool.core.date;

import java.util.Locale;
import java.util.TimeZone;

import cn.hutool.core.date.format.FastDateFormat;

/**
 * 日期格式化类，提供常用的日期格式化对象
 * 
 * @author Looly
 *
 */
public class DatePattern {

	//-------------------------------------------------------------------------------------------------------------------------------- Normal
	/** 标准日期格式：yyyy-MM-dd */
	public final static String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/** 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd */
	public final static FastDateFormat NORM_DATE_FORMAT = FastDateFormat.getInstance(NORM_DATE_PATTERN);
	
	/** 标准时间格式：HH:mm:ss */
	public final static String NORM_TIME_PATTERN = "HH:mm:ss";
	/** 标准时间格式 {@link FastDateFormat}：HH:mm:ss */
	public final static FastDateFormat NORM_TIME_FORMAT = FastDateFormat.getInstance(NORM_TIME_PATTERN);

	/** 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm */
	public final static String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/** 标准日期时间格式，精确到分 {@link FastDateFormat}：yyyy-MM-dd HH:mm */
	public final static FastDateFormat NORM_DATETIME_MINUTE_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MINUTE_PATTERN);

	/** 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss */
	public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 标准日期时间格式，精确到秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss */
	public final static FastDateFormat NORM_DATETIME_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_PATTERN);

	/** 标准日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss.SSS */
	public final static String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	/** 标准日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss.SSS */
	public final static FastDateFormat NORM_DATETIME_MS_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MS_PATTERN);
	
	/** 标准日期格式：yyyy年MM月dd日 */
	public final static String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
	/** 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日 */
	public final static FastDateFormat CHINESE_DATE_FORMAT = FastDateFormat.getInstance(CHINESE_DATE_PATTERN);
	
	//-------------------------------------------------------------------------------------------------------------------------------- Pure
	/** 标准日期格式：yyyyMMdd */
	public final static String PURE_DATE_PATTERN = "yyyyMMdd";
	/** 标准日期格式 {@link FastDateFormat}：yyyyMMdd */
	public final static FastDateFormat PURE_DATE_FORMAT = FastDateFormat.getInstance(PURE_DATE_PATTERN);
	
	/** 标准日期格式：HHmmss */
	public final static String PURE_TIME_PATTERN = "HHmmss";
	/** 标准日期格式 {@link FastDateFormat}：HHmmss */
	public final static FastDateFormat PURE_TIME_FORMAT = FastDateFormat.getInstance(PURE_TIME_PATTERN);
	
	/** 标准日期格式：yyyyMMddHHmmss */
	public final static String PURE_DATETIME_PATTERN = "yyyyMMddHHmmss";
	/** 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmss */
	public final static FastDateFormat PURE_DATETIME_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_PATTERN);
	
	/** 标准日期格式：yyyyMMddHHmmssSSS */
	public final static String PURE_DATETIME_MS_PATTERN = "yyyyMMddHHmmssSSS";
	/** 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmssSSS */
	public final static FastDateFormat PURE_DATETIME_MS_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_MS_PATTERN);
	
	//-------------------------------------------------------------------------------------------------------------------------------- Others
	/** HTTP头中日期时间格式：EEE, dd MMM yyyy HH:mm:ss z */
	public final static String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
	/** HTTP头中日期时间格式 {@link FastDateFormat}：EEE, dd MMM yyyy HH:mm:ss z */
	public final static FastDateFormat HTTP_DATETIME_FORMAT = FastDateFormat.getInstance(HTTP_DATETIME_PATTERN, TimeZone.getTimeZone("GMT"), Locale.US);

	/** JDK中日期时间格式：EEE MMM dd HH:mm:ss zzz yyyy */
	public final static String JDK_DATETIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";
	/** JDK中日期时间格式 {@link FastDateFormat}：EEE MMM dd HH:mm:ss zzz yyyy */
	public final static FastDateFormat JDK_DATETIME_FORMAT = FastDateFormat.getInstance(JDK_DATETIME_PATTERN, Locale.US);
	
	/** UTC时间：yyyy-MM-dd'T'HH:mm:ss'Z' */
	public final static String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	/** UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss'Z' */
	public final static FastDateFormat UTC_FORMAT = FastDateFormat.getInstance(UTC_PATTERN, TimeZone.getTimeZone("UTC"));
	
	/** UTC时间：yyyy-MM-dd'T'HH:mm:ssZ */
	public final static String UTC_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	/** UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ssZ */
	public final static FastDateFormat UTC_WITH_ZONE_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_WITH_ZONE_OFFSET_PATTERN, TimeZone.getTimeZone("UTC"));
	
	/** UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSS'Z' */
	public final static String UTC_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	/** UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSS'Z' */
	public final static FastDateFormat UTC_MS_FORMAT = FastDateFormat.getInstance(UTC_MS_PATTERN, TimeZone.getTimeZone("UTC"));
	
	/** UTC时间：yyyy-MM-dd'T'HH:mm:ssZ */
	public final static String UTC_MS_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	/** UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ssZ */
	public final static FastDateFormat UTC_MS_WITH_ZONE_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_MS_WITH_ZONE_OFFSET_PATTERN, TimeZone.getTimeZone("UTC"));
}
