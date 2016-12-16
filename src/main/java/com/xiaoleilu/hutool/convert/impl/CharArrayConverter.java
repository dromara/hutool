package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * char类型数组转换器
 * @author Looly
 *
 */
public class CharArrayConverter extends AbstractConverter<char[]>{
	
	@Override
	protected char[] convertInternal(Object value) {
		final Character[] result = Convert.convert(Character[].class, value);
		return CollectionUtil.unWrap(result);
	}

}
