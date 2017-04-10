package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * char类型数组转换器
 * @author Looly
 *
 */
public class CharArrayConverter extends AbstractConverter<char[]>{
	
	@Override
	protected char[] convertInternal(Object value) {
		final Character[] result = ConverterRegistry.getInstance().convert(Character[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
