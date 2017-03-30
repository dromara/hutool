package com.xiaoleilu.hutool.convert.impl;

import java.sql.Timestamp;
import java.util.Calendar;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * SQL时间戳转换器
 * 
 * @author Looly
 *
 */
public class SqlTimestampConverter extends AbstractConverter<Timestamp> {

	/** 日期格式化 */
	private String format;

	/**
	 * 获取日期格式
	 * 
	 * @return 设置日期格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置日期格式
	 * 
	 * @param format 日期格式
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	protected Timestamp convertInternal(Object value) {
		// Handle Calendar
		if (value instanceof Calendar) {
			return new Timestamp(((Calendar) value).getTime().getTime());
		}

		// Handle Long
		if (value instanceof Long) {
			//此处使用自动拆装箱
			return new Timestamp((Long)value);
		}

		final String valueStr = convertToStr(value);
		try {
			final long date = StrUtil.isBlank(format) ? DateUtil.parse(valueStr).getTime() : DateUtil.parse(valueStr, format).getTime();
			return new Timestamp(date);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
