package com.xiaoleilu.hutool.convert.impl;

import java.net.URL;

import com.xiaoleilu.hutool.convert.AbstractConverter;

/**
 * 字符串转换器
 * @author Looly
 *
 */
public class URLConverter extends AbstractConverter<URL>{

	@Override
	protected URL convertInternal(Object value) {
		try {
			return new URL(convertToStr(value));
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
