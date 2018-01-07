package cn.hutool.core.convert.impl;

import java.nio.charset.Charset;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.util.CharsetUtil;

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
