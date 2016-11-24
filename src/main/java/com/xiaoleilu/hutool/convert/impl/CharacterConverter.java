package com.xiaoleilu.hutool.convert.impl;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 数字转换器
 * @author Looly
 *
 */
public class CharacterConverter extends AbstractConverter{

	@Override
	public Class<Character> getTargetType() {
		return Character.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Character convertInternal(Object value) {
		if(char.class == value.getClass()){
			return Character.valueOf((char)value);
		}else{
			final String valueStr = convertToStr(value);
			if (StrUtil.isNotBlank(valueStr)) {
				try {
					return Character.valueOf(valueStr.charAt(0));
				} catch (Exception e) {
					//Ignore Exception
				}
			}
		}
		return null;
	}

}
