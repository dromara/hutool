package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 编码对象转换器
 * @author Looly
 *
 */
public class CharsetConverter extends AbstractConverter{
	private static final long serialVersionUID = 1L;

	@Override
	protected Charset convertInternal(final Class<?> targetClass, final Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
