package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;

/**
 * 字符串转换器
 * @author Looly
 *
 */
public class StringConverter extends AbstractConverter{

	@Override
	public Class<String> getTargetType() {
		return String.class;
	}

	@Override
	protected String convertInternal(Object value) {
		return convertToStr(value);
	}

}
