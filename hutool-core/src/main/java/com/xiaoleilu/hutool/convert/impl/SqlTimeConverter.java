package com.xiaoleilu.hutool.convert.impl;

import java.sql.Time;
import java.util.Calendar;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * SQL时间转换器
 * 
 * @author Looly
 *
 */
public class SqlTimeConverter extends AbstractConverter<Time> {

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
	protected Time convertInternal(Object value) {
		// Handle Calendar
		if (value instanceof Calendar) {
			return new Time(((Calendar) value).getTime().getTime());
		}

		// Handle Long
		if (value instanceof Long) {
			//此处使用自动拆装箱
			return new Time((Long)value);
		}

		final String valueStr = convertToStr(value);
		try {
			final long date = StrUtil.isBlank(format) ? DateUtil.parse(valueStr).getTime() : DateUtil.parse(valueStr, format).getTime();
			return new Time(date);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
