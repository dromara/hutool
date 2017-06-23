package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 字符转换器
 * @author Looly
 *
 */
public class CharacterConverter extends AbstractConverter<Character>{

	@Override
	protected Character convertInternal(Object value) {
		if(char.class == value.getClass()){
			return Character.valueOf((char)value);
		}else{
			final String valueStr = convertToStr(value);
			if (StrUtil.isNotBlank(valueStr)) {
				return Character.valueOf(valueStr.charAt(0));
			}
		}
		return null;
	}

}
