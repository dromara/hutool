package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * float 类型数组转换器
 * @author Looly
 *
 */
public class FloatArrayConverter extends AbstractConverter<float[]>{
	
	@Override
	protected float[] convertInternal(Object value) {
		final Float[] result = Convert.convert(Float[].class, value);
		return CollectionUtil.unWrap(result);
	}

}
