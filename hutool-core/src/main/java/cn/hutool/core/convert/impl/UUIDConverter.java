package cn.hutool.core.convert.impl;

import java.util.UUID;

import cn.hutool.core.convert.AbstractConverter;

/**
 * UUID对象转换器转换器
 * 
 * @author Looly
 * @since 4.0.10
 *
 */
public class UUIDConverter extends AbstractConverter<UUID> {

	@Override
	protected UUID convertInternal(Object value) {
		return UUID.fromString(convertToStr(value));
	}

}
