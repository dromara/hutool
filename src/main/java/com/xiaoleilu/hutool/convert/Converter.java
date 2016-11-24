package com.xiaoleilu.hutool.convert;

/**
 * 转换器接口，实现类型转换
 * @author Looly
 *
 */
public interface Converter {
	
	/**
	 * 转换为指定类型<br>
	 * 转换为默认值的类型
	 * 
	 * @param value 原始值
	 * @param defaultValue 默认值，默认值必须与 {@link Converter#getTargetType()} 类型匹配
	 * @return 转换后的值
	 * @throws IllegalArgumentException 默认值与 {@link Converter#getTargetType()} 不匹配
	 */
	public <T> T convert(Object value, T defaultValue) throws IllegalArgumentException;
	
	/**
	 * 获得转换目标类型<br>
	 * 实现此接口的类需事先此方法，返回转换的目标类型的类
	 * 
	 * @return 转换目标类型
	 */
	public Class<?> getTargetType();
}
