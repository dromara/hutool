package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * byte 类型数组转换器
 * @author Looly
 *
 */
public class ByteArrayConverter extends AbstractConverter<byte[]>{
	
	@Override
	protected byte[] convertInternal(Object value) {
		final Byte[] result = Convert.convert(Byte[].class, value);
		return ArrayUtil.unWrap(result);
	}

}
