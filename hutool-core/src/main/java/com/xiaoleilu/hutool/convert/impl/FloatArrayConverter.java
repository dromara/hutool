package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * float 类型数组转换器
 * @author Looly
 *
 */
public class FloatArrayConverter extends AbstractConverter<float[]>{
	
	@Override
	protected float[] convertInternal(Object value) {
		final Float[] result = ConverterRegistry.getInstance().convert(Float[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
