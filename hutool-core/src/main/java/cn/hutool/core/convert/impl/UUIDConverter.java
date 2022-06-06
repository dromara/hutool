package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;

import java.util.UUID;

/**
 * UUID对象转换器转换器
 *
 * @author Looly
 * @since 4.0.10
 *
 */
public class UUIDConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;



	@Override
	protected UUID convertInternal(final Class<?> targetClass, final Object value) {
		return UUID.fromString(convertToStr(value));
	}

}
