package cn.hutool.core.date;

import cn.hutool.core.date.format.FastDateFormat;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 日期格式化类，提供常用的日期格式化对象
 *
 * @author Looly
 */
public class DatePattern {

	/**
	 * 标准日期时间正则，每个字段支持单个数字或2个数字，包括：
	 * <pre>
	 *     yyyy-MM-dd HH:mm:ss.SSSSSS
	 *     yyyy-MM-dd HH:mm:ss.SSS
	 *     yyyy-MM-dd HH:mm:ss
	 *     yyyy-MM-dd HH:mm
	 *     yyyy-MM-dd
	 * </pre>
	 *
	 * @since 5.3.6
	 */
	public static final Pattern REGEX_NORM = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}(\\s\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?(.\\d{1,6})?");

	//-------------------------------------------------------------------------------------------------------------------------------- Normal
	/**
	 * 年月格式：yyyy-MM
	 */
	public static final String NORM_MONTH_PATTERN = "yyyy-MM";
	/**
	 * 年月格式 {@link FastDateFormat}：yyyy-MM
	 */
	public static final FastDateFormat NORM_MONTH_FORMAT = FastDateFormat.getInstance(NORM_MONTH_PATTERN);
	/**
	 * 年月格式 {@link FastDateFormat}：yyyy-MM
	 */
	public static final DateTimeFormatter NORM_MONTH_FORMATTER = createFormatter(NORM_MONTH_PATTERN);

	/**
	 * 简单年月格式：yyyyMM
	 */
	public static final String SIMPLE_MONTH_PATTERN = "yyyyMM";
	/**
	 * 简单年月格式 {@link FastDateFormat}：yyyyMM
	 */
	public static final FastDateFormat SIMPLE_MONTH_FORMAT = FastDateFormat.getInstance(SIMPLE_MONTH_PATTERN);
	/**
	 * 简单年月格式 {@link FastDateFormat}：yyyyMM
	 */
	public static final DateTimeFormatter SIMPLE_MONTH_FORMATTER = createFormatter(SIMPLE_MONTH_PATTERN);

	/**
	 * 标准日期格式：yyyy-MM-dd
	 */
	public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd
	 */
	public static final FastDateFormat NORM_DATE_FORMAT = FastDateFormat.getInstance(NORM_DATE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd
	 */
	public static final DateTimeFormatter NORM_DATE_FORMATTER = createFormatter(NORM_DATE_PATTERN);

	/**
	 * 标准时间格式：HH:mm:ss
	 */
	public static final String NORM_TIME_PATTERN = "HH:mm:ss";
	/**
	 * 标准时间格式 {@link FastDateFormat}：HH:mm:ss
	 */
	public static final FastDateFormat NORM_TIME_FORMAT = FastDateFormat.getInstance(NORM_TIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：HH:mm:ss
	 */
	public static final DateTimeFormatter NORM_TIME_FORMATTER = createFormatter(NORM_TIME_PATTERN);

	/**
	 * 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm
	 */
	public static final String NORM_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/**
	 * 标准日期时间格式，精确到分 {@link FastDateFormat}：yyyy-MM-dd HH:mm
	 */
	public static final FastDateFormat NORM_DATETIME_MINUTE_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MINUTE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd HH:mm
	 */
	public static final DateTimeFormatter NORM_DATETIME_MINUTE_FORMATTER = createFormatter(NORM_DATETIME_MINUTE_PATTERN);

	/**
	 * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
	 */
	public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 标准日期时间格式，精确到秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss
	 */
	public static final FastDateFormat NORM_DATETIME_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_PATTERN);
	/**
	 * 标准日期时间格式，精确到秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss
	 */
	public static final DateTimeFormatter NORM_DATETIME_FORMATTER = createFormatter(NORM_DATETIME_PATTERN);

	/**
	 * 标准日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String NORM_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	/**
	 * 标准日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final FastDateFormat NORM_DATETIME_MS_FORMAT = FastDateFormat.getInstance(NORM_DATETIME_MS_PATTERN);
	/**
	 * 标准日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final DateTimeFormatter NORM_DATETIME_MS_FORMATTER = createFormatter(NORM_DATETIME_MS_PATTERN);

	/**
	 * ISO8601日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final String ISO8601_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
	/**
	 * ISO8601日期时间格式，精确到毫秒 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final FastDateFormat ISO8601_FORMAT = FastDateFormat.getInstance(ISO8601_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy-MM-dd HH:mm:ss,SSS
	 */
	public static final DateTimeFormatter ISO8601_FORMATTER = createFormatter(ISO8601_PATTERN);

	/**
	 * 标准日期格式：yyyy年MM月dd日
	 */
	public static final String CHINESE_DATE_PATTERN = "yyyy年MM月dd日";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日
	 */
	public static final FastDateFormat CHINESE_DATE_FORMAT = FastDateFormat.getInstance(CHINESE_DATE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日
	 */
	public static final DateTimeFormatter CHINESE_DATE_FORMATTER = createFormatter(CHINESE_DATE_PATTERN);

	/**
	 * 标准日期格式：yyyy年MM月dd日 HH时mm分ss秒
	 */
	public static final String CHINESE_DATE_TIME_PATTERN = "yyyy年MM月dd日HH时mm分ss秒";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日HH时mm分ss秒
	 */
	public static final FastDateFormat CHINESE_DATE_TIME_FORMAT = FastDateFormat.getInstance(CHINESE_DATE_TIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyy年MM月dd日HH时mm分ss秒
	 */
	public static final DateTimeFormatter CHINESE_DATE_TIME_FORMATTER = createFormatter(CHINESE_DATE_TIME_PATTERN);

	//-------------------------------------------------------------------------------------------------------------------------------- Pure
	/**
	 * 标准日期格式：yyyyMMdd
	 */
	public static final String PURE_DATE_PATTERN = "yyyyMMdd";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMdd
	 */
	public static final FastDateFormat PURE_DATE_FORMAT = FastDateFormat.getInstance(PURE_DATE_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMdd
	 */
	public static final DateTimeFormatter PURE_DATE_FORMATTER = createFormatter(PURE_DATE_PATTERN);

	/**
	 * 标准日期格式：HHmmss
	 */
	public static final String PURE_TIME_PATTERN = "HHmmss";
	/**
	 * 标准日期格式 {@link FastDateFormat}：HHmmss
	 */
	public static final FastDateFormat PURE_TIME_FORMAT = FastDateFormat.getInstance(PURE_TIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：HHmmss
	 */
	public static final DateTimeFormatter PURE_TIME_FORMATTER = createFormatter(PURE_TIME_PATTERN);

	/**
	 * 标准日期格式：yyyyMMddHHmmss
	 */
	public static final String PURE_DATETIME_PATTERN = "yyyyMMddHHmmss";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmss
	 */
	public static final FastDateFormat PURE_DATETIME_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmss
	 */
	public static final DateTimeFormatter PURE_DATETIME_FORMATTER = createFormatter(PURE_DATETIME_PATTERN);

	/**
	 * 标准日期格式：yyyyMMddHHmmssSSS
	 */
	public static final String PURE_DATETIME_MS_PATTERN = "yyyyMMddHHmmssSSS";
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmssSSS
	 */
	public static final FastDateFormat PURE_DATETIME_MS_FORMAT = FastDateFormat.getInstance(PURE_DATETIME_MS_PATTERN);
	/**
	 * 标准日期格式 {@link FastDateFormat}：yyyyMMddHHmmssSSS
	 */
	public static final DateTimeFormatter PURE_DATETIME_MS_FORMATTER = createFormatter(PURE_DATETIME_MS_PATTERN);

	//-------------------------------------------------------------------------------------------------------------------------------- Others
	/**
	 * HTTP头中日期时间格式：EEE, dd MMM yyyy HH:mm:ss z
	 */
	public static final String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
	/**
	 * HTTP头中日期时间格式 {@link FastDateFormat}：EEE, dd MMM yyyy HH:mm:ss z
	 */
	public static final FastDateFormat HTTP_DATETIME_FORMAT = FastDateFormat.getInstance(HTTP_DATETIME_PATTERN, TimeZone.getTimeZone("GMT"), Locale.US);

	/**
	 * JDK中日期时间格式：EEE MMM dd HH:mm:ss zzz yyyy
	 */
	public static final String JDK_DATETIME_PATTERN = "EEE MMM dd HH:mm:ss zzz yyyy";
	/**
	 * JDK中日期时间格式 {@link FastDateFormat}：EEE MMM dd HH:mm:ss zzz yyyy
	 */
	public static final FastDateFormat JDK_DATETIME_FORMAT = FastDateFormat.getInstance(JDK_DATETIME_PATTERN, Locale.US);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss
	 */
	public static final String UTC_SIMPLE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss
	 */
	public static final FastDateFormat UTC_SIMPLE_FORMAT = FastDateFormat.getInstance(UTC_SIMPLE_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSS
	 */
	public static final String UTC_SIMPLE_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSS
	 */
	public static final FastDateFormat UTC_SIMPLE_MS_FORMAT = FastDateFormat.getInstance(UTC_SIMPLE_MS_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	public static final String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	public static final FastDateFormat UTC_FORMAT = FastDateFormat.getInstance(UTC_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static final String UTC_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static final FastDateFormat UTC_WITH_ZONE_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_WITH_ZONE_OFFSET_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ssXXX
	 */
	public static final String UTC_WITH_XXX_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ssXXX
	 */
	public static final FastDateFormat UTC_WITH_XXX_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_WITH_XXX_OFFSET_PATTERN);

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 */
	public static final String UTC_MS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
	 */
	public static final FastDateFormat UTC_MS_FORMAT = FastDateFormat.getInstance(UTC_MS_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static final String UTC_MS_WITH_ZONE_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static final FastDateFormat UTC_MS_WITH_ZONE_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_MS_WITH_ZONE_OFFSET_PATTERN, TimeZone.getTimeZone("UTC"));

	/**
	 * UTC时间：yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	 */
	public static final String UTC_MS_WITH_XXX_OFFSET_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	/**
	 * UTC时间{@link FastDateFormat}：yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	 */
	public static final FastDateFormat UTC_MS_WITH_XXX_OFFSET_FORMAT = FastDateFormat.getInstance(UTC_MS_WITH_XXX_OFFSET_PATTERN);

	/**
	 * 创建并为 {@link DateTimeFormatter} 赋予默认时区和位置信息，默认值为系统默认值。
	 *
	 * @param pattern 日期格式
	 * @return {@link DateTimeFormatter}
	 * @since 5.7.5
	 */
	private static DateTimeFormatter createFormatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
				.withZone(ZoneId.systemDefault());
	}
}
