package cn.hutool.core.convert.impl;

import cn.hutool.core.convert.AbstractConverter;

/**
 * 泛型枚举转换器
 * 
 * @param <E> 枚举类类型
 * @author Looly
 * @since 4.0.2
 */
public class GenericEnumConverter<E extends Enum<E>> extends AbstractConverter<E> {

	private Class<E> enumClass;
	
	/**
	 * 构造
	 * 
	 * @param enumClass 转换成的目标Enum类
	 */
	public GenericEnumConverter(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	protected E convertInternal(Object value) {
		return Enum.valueOf(enumClass, convertToStr(value));
	}

	@Override
	public Class<E> getTargetType() {
		return this.enumClass;
	}
}
