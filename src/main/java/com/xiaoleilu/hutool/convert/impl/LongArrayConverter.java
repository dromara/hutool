package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * long 类型数组转换器
 * @author Looly
 *
 */
public class LongArrayConverter extends AbstractConverter<long[]>{
	
	@Override
	protected long[] convertInternal(Object value) {
		final Long[] result = Convert.convert(Long[].class, value);
		return CollectionUtil.unWrap(result);
	}

}
