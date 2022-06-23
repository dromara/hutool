package cn.hutool.json.serialize;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.date.format.GlobalCustomFormat;
import cn.hutool.core.text.StrUtil;
import cn.hutool.json.InternalJSONUtil;
import cn.hutool.json.JSONConfig;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 自定义的日期表示形式<br>
 * 支持包括：{@link Date}、{@link Calendar}、{@link TemporalAccessor}
 *
 * @author looly
 * @since 6.0.0
 */
public class DateJSONString implements JSONString {

	final Object dateObj;
	final JSONConfig jsonConfig;

	public DateJSONString(final Object dateObj, final JSONConfig jsonConfig) {
		this.dateObj = dateObj;
		this.jsonConfig = jsonConfig;
	}

	@Override
	public String toJSONString() {
		return formatDate(this.jsonConfig.getDateFormat());
	}

	/**
	 * 按照给定格式格式化日期，格式为空时返回时间戳字符串
	 *
	 * @param format 格式
	 * @return 日期字符串
	 */
	private String formatDate(final String format) {
		if (StrUtil.isNotBlank(format)) {
			final String dateStr;
			if (dateObj instanceof TemporalAccessor) {
				dateStr = TemporalAccessorUtil.format((TemporalAccessor) dateObj, format);
			} else {
				dateStr = DateUtil.format(Convert.toDate(dateObj), format);
			}

			if (GlobalCustomFormat.FORMAT_SECONDS.equals(format)
					|| GlobalCustomFormat.FORMAT_MILLISECONDS.equals(format)) {
				// Hutool自定义的秒和毫秒表示，默认不包装双引号
				return dateStr;
			}
			//用户定义了日期格式
			return InternalJSONUtil.quote(dateStr);
		}

		//默认使用时间戳
		final long timeMillis;
		if (dateObj instanceof TemporalAccessor) {
			timeMillis = TemporalAccessorUtil.toEpochMilli((TemporalAccessor) dateObj);
		} else if (dateObj instanceof Date) {
			timeMillis = ((Date) dateObj).getTime();
		} else if (dateObj instanceof Calendar) {
			timeMillis = ((Calendar) dateObj).getTimeInMillis();
		} else {
			throw new UnsupportedOperationException("Unsupported Date type: " + dateObj.getClass());
		}
		return String.valueOf(timeMillis);
	}
}
