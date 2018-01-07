package cn.hutool.core.convert.impl;

import java.util.Calendar;
import java.util.Date;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 日期转换器
 * 
 * @author Looly
 *
 */
public class DateConverter extends AbstractConverter<Date> {

	private Class<? extends java.util.Date> targetType;
	/** 日期格式化 */
	private String format;

	public DateConverter(Class<? extends java.util.Date> targetType) {
		this.targetType = targetType;
	}

	public DateConverter(Class<? extends java.util.Date> targetType, String format) {
		this.targetType = targetType;
		this.format = format;
	}

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
	protected Date convertInternal(Object value) {
		long mills = 0;
		// Handle Calendar
		if (value instanceof Calendar) {
			mills = ((Calendar) value).getTimeInMillis();
		}

		// Handle Long
		if (value instanceof Long) {
			// 此处使用自动拆装箱
			mills = (Long) value;
		}

		final String valueStr = convertToStr(value);
		try {
			mills = StrUtil.isBlank(format) ? DateUtil.parse(valueStr).getTime() : DateUtil.parse(valueStr, format).getTime();
		} catch (Exception e) {
			// Ignore Exception
		}

		if (0 == mills) {
			return null;
		}

		// 返回指定类型
		else if (java.util.Date.class == targetType) {
			return new java.util.Date(mills);
		}
		if (DateTime.class == targetType) {
			return new DateTime(mills);
		} else if (java.sql.Date.class == targetType) {
			return new java.sql.Date(mills);
		} else if (java.sql.Time.class == targetType) {
			return new java.sql.Time(mills);
		} else if (java.sql.Timestamp.class == targetType) {
			return new java.sql.Timestamp(mills);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupport Date type: {}", this.targetType.getName()));
	}

}
