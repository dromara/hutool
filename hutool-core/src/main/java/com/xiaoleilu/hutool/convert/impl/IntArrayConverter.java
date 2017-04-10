package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * int 类型数组转换器
 * @author Looly
 *
 */
public class IntArrayConverter extends AbstractConverter<int[]>{
	
	@Override
	protected int[] convertInternal(Object value) {
		final Integer[] result = ConverterRegistry.getInstance().convert(Integer[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
