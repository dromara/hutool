package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * long 类型数组转换器
 * @author Looly
 *
 */
public class LongArrayConverter extends AbstractConverter<long[]>{
	
	@Override
	protected long[] convertInternal(Object value) {
		final Long[] result = ConverterRegistry.getInstance().convert(Long[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
