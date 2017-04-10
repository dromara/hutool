package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * short 类型数组转换器
 * @author Looly
 *
 */
public class ShortArrayConverter extends AbstractConverter<short[]>{
	
	@Override
	protected short[] convertInternal(Object value) {
		final Short[] result = ConverterRegistry.getInstance().convert(Short[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
