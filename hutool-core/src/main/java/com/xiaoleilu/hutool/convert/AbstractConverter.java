package com.xiaoleilu.hutool.convert;

import java.text.MessageFormat;

import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.ClassUtil;

/**
 * 抽象转换器，提供通用的转换逻辑，同时通过
 * @author Looly
 *
 */
public abstract class AbstractConverter<T> implements Converter<T>{
	
	@Override
	@SuppressWarnings("unchecked")
	public T convert(Object value, T defaultValue) {
		Class<T> targetType = getTargetType();
		if(null == targetType && null == defaultValue){
			throw new NullPointerException("[type] and [defaultValue] are both null for Converter ["+this.getClass()+"], we can not know what type to convert !");
		}
		if(null == targetType){
			targetType = (Class<T>) defaultValue.getClass();
		}
		
		if(targetType.isPrimitive()){
			//原始类型直接调用内部转换，内部转换永远不会返回null
			return convertInternal(value);
		}
		
		if(null == value){
			return defaultValue;
		}
		if(null == defaultValue || targetType.isInstance(defaultValue)){
			if(targetType.isInstance(value)){
				//已经是目标类型，不需要转换
				return (T) targetType.cast(value);
			}
			final T convertInternal = convertInternal(value);
			return ((null == convertInternal) ? defaultValue : convertInternal);
		}else{
			throw new IllegalArgumentException(MessageFormat.format("Default value [{0}] is not the instance of [{1}]]", defaultValue, targetType));
		}
	}
	
	/**
	 * 内部转换器，被 {@link AbstractConverter#convert(Object, Object)} 调用，实现基本转换逻辑
	 * @param value 值
	 * @return 转换后的类型
	 */
	protected abstract T convertInternal(Object value);
	
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
		}else if (ArrayUtil.isArray(value)) {
			return ArrayUtil.toString(value);
		}
		return value.toString();
	}
	
	/**
	 * 获得此类实现类的泛型类型
	 * @return 此类的泛型类型
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getTargetType() {
		return (Class<T>) ClassUtil.getTypeArgument(getClass());
	}
}
