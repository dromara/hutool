package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * double 类型数组转换器
 * @author Looly
 *
 */
public class DoubleArrayConverter extends AbstractConverter<double[]>{
	
	@Override
	protected double[] convertInternal(Object value) {
		final Double[] result = Convert.convert(Double[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
