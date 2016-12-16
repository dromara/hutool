package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * short 类型数组转换器
 * @author Looly
 *
 */
public class ShortArrayConverter extends AbstractConverter<short[]>{
	
	@Override
	protected short[] convertInternal(Object value) {
		final Short[] result = Convert.convert(Short[].class, value);
		return CollectionUtil.unWrap(result);
	}

}
