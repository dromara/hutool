package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.ZoneUtil;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * TimeZone转换器
 * @author Looly
 *
 */
public class TimeZoneConverter extends AbstractConverter{
	private static final long serialVersionUID = 1L;

	@Override
	protected TimeZone convertInternal(final Class<?> targetClass, final Object value) {
		if(value instanceof ZoneId){
			return ZoneUtil.toTimeZone((ZoneId) value);
		}
		return TimeZone.getTimeZone(convertToStr(value));
	}

}
