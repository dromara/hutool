package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * double 类型数组转换器
 * @author Looly
 *
 */
public class DoubleArrayConverter extends AbstractConverter<double[]>{
	
	@Override
	protected double[] convertInternal(Object value) {
		final Double[] result = ConverterRegistry.getInstance().convert(Double[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
