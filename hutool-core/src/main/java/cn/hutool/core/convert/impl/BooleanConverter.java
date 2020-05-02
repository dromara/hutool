package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.util.BooleanUtil;

/**
 * 波尔转换器
 * @author Looly
 *
 */
public class BooleanConverter extends AbstractConverter<Boolean>{
	private static final long serialVersionUID = 1L;

	@Override
	protected Boolean convertInternal(Object value) {
		//Object不可能出现Primitive类型，故忽略
		return BooleanUtil.toBoolean(convertToStr(value));
	}

}
