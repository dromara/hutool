package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.util.ClassUtil;

/**
 * 类转换器
 * @author Looly
 *
 */
public class ClassConverter extends AbstractConverter{
	
	@Override
	public Class<?> getTargetType() {
		return Class.class;
	}

	@Override
	protected Class<?> convertInternal(Object value) {
		String valueStr = convertToStr(value);
		try {
			return ClassUtil.getClassLoader().loadClass(valueStr);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

}
