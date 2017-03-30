package com.xiaoleilu.hutool.convert.impl;

import java.nio.charset.Charset;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * 编码对象转换器
 * @author Looly
 *
 */
public class CharsetConverter extends AbstractConverter<Charset>{

	@Override
	protected Charset convertInternal(Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
