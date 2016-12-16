package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * boolean类型数组转换器
 * @author Looly
 *
 */
public class BooleanArrayConverter extends AbstractConverter<boolean[]>{
	
	@Override
	protected boolean[] convertInternal(Object value) {
		final Boolean[] result = Convert.convert(Boolean[].class, value);
		return CollectionUtil.unWrap(result);
	}

}
