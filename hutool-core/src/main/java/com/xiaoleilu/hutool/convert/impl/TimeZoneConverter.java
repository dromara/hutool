package com.xiaoleilu.hutool.convert.impl;

import java.util.TimeZone;

import com.xiaoleilu.hutool.convert.AbstractConverter;

/**
 * TimeZone转换器
 * @author Looly
 *
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone>{

	@Override
	protected TimeZone convertInternal(Object value) {
		return TimeZone.getTimeZone(convertToStr(value));
	}

}
