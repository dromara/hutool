package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.date.ZoneUtil;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * {@link ZoneId}转换器
 *
 * @author Looly
 */
public class ZoneIdConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	@Override
	protected ZoneId convertInternal(final Class<?> targetClass, final Object value) {
		if (value instanceof TimeZone) {
			return ZoneUtil.toZoneId((TimeZone) value);
		}
		return ZoneId.of(convertToStr(value));
	}

}
