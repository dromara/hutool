package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;

/**
 * 无泛型检查的枚举转换器
 * 
 * @author Looly
 * @since 4.0.2
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EnumConverter extends AbstractConverter<Object> {

	private Class enumClass;
	
	/**
	 * 构造
	 * 
	 * @param enumClass 转换成的目标Enum类
	 */
	public EnumConverter(Class enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	protected Object convertInternal(Object value) {
		return Enum.valueOf(enumClass, convertToStr(value));
	}

	@Override
	public Class getTargetType() {
		return this.enumClass;
	}
}
