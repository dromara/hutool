package com.xiaoleilu.hutool.convert;

import java.text.MessageFormat;

import com.xiaoleilu.hutool.util.CollectionUtil;

/**
 * 抽象转换器，提供通用的转换逻辑，同时通过
 * @author Looly
 *
 */
public abstract class AbstractConverter implements Converter{
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T convert(Object value, T defaultValue) {
		final Class<?> targetType = getTargetType();
		
		if(targetType.isPrimitive()){
			//原始类型直接调用内部转换，内部转换永远不会返回null
			return (T) convertInternal(value);
		}
		
		if(null == value){
			return defaultValue;
		}
		if(null == defaultValue || targetType.isInstance(defaultValue)){
			if(targetType.isInstance(value)){
				//已经是目标类型，不需要转换
				return (T) targetType.cast(value);
			}
			final Object convertInternal = convertInternal(value);
			return (T) ((null == convertInternal) ? defaultValue : convertInternal);
		}else{
			throw new IllegalArgumentException(MessageFormat.format("Default value [{0}] is not the instance of [{1}]]", defaultValue, targetType));
		}
	}
	
	/**
	 * 内部转换器，被 {@link AbstractConverter#convert(Object, Object)} 调用，实现基本转换逻辑
	 * @param value 值
	 * @return 转换后的类型
	 */
	protected abstract Object convertInternal(Object value);
	
	/**
	 * 值转为String
	 * @param value 值
	 * @return String
	 */
	protected String convertToStr(Object value) {
		if (null == value) {
			return null;
		}
		if (value instanceof String) {
			return (String) value;
		}else if (CollectionUtil.isArray(value)) {
			return CollectionUtil.toString(value);
		}
		return value.toString();
	}
}
