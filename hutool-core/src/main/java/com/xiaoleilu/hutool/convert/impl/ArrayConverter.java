package com.xiaoleilu.hutool.convert.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 数组转换器，包括原始类型数组
 * @author Looly
 *
 * @param <T> 转换的目标数组的元素类型
 */
public class ArrayConverter<T> extends AbstractConverter<T[]>{
	
	private Class<T> targetComponentType;
	
	/**
	 * 构造
	 * @param targetComponentType 目标元素类型（数组元素类型）
	 */
	public ArrayConverter(Class<T> targetComponentType) {
		this.targetComponentType = targetComponentType;
	}

	@Override
	protected T[] convertInternal(Object value) {
		return value.getClass().isArray() ? convertArrayToArray(value) : convertObjectToArray(value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class<T[]> getTargetType() {
		Class<?> targetType = super.getTargetType();
		if(null == targetType){
			if(Integer.class == this.targetComponentType){
				targetType = Integer[].class;
			}else if(Long.class == this.targetComponentType){
				targetType = Long[].class;
			}else if(Float.class == this.targetComponentType){
				targetType = Float[].class;
			}else if(Double.class == this.targetComponentType){
				targetType = Double[].class;
			}else if(Short.class == this.targetComponentType){
				targetType = Short[].class;
			}else if(Byte.class == this.targetComponentType){
				targetType = Byte[].class;
			}else if(Character.class == this.targetComponentType){
				targetType = Character[].class;
			}else if(Boolean.class == this.targetComponentType){
				targetType = Boolean[].class;
			}else if(String.class == this.targetComponentType){
				targetType = String[].class;
			}
		}
		return (Class<T[]>) targetType;
	}
	
	//-------------------------------------------------------------------------------------- Private method start
	/**
	 * 数组对数组转换
	 * @param value 被转换值
	 * @return 转换后的数组
	 */
	@SuppressWarnings("unchecked")
	private T[] convertArrayToArray(Object value) {
		final Class<?> valueComponentType = value.getClass().getComponentType();

		if (valueComponentType == targetComponentType) {
			return (T[]) value;
		}
		
		T[] result = null;
		if(valueComponentType.isPrimitive()){
			//原始类型
			result = convertPrimitiveArrayToArray(value, valueComponentType);
		}else{
			//非原始类型
			final Object[] array = (Object[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = ConverterRegistry.getInstance().convert(targetComponentType, array[i]);
			}
		}
		
		return result;
	}

	/**
	 * 非数组对数组转换
	 * @param value 被转换值
	 * @return 转换后的数组
	 */
	private T[] convertObjectToArray(Object value) {
		T[] result = null;
		final ConverterRegistry registry = ConverterRegistry.getInstance();
		if (value instanceof List) {
			final List<?> list = (List<?>) value;
			result = ArrayUtil.newArray(targetComponentType, list.size());
			for (int i = 0; i < list.size(); i++) {
				result[i] = registry.convert(targetComponentType, list.get(i));
			}
		}else  if (value instanceof Collection) {
			final Collection<?> collection = (Collection<?>) value;
			result = ArrayUtil.newArray(targetComponentType, collection.size());

			int i = 0;
			for (Object element : collection) {
				result[i] = registry.convert(targetComponentType, element);
				i++;
			}
		}else if (value instanceof Iterable) {
			final Iterable<?> iterable = (Iterable<?>) value;
			final List<T> list = new ArrayList<T>();
			for (Object element : iterable) {
				list.add(registry.convert(targetComponentType, element));
			}

			result = ArrayUtil.newArray(targetComponentType, list.size());
			result = list.toArray(result);
		}

		if (value instanceof CharSequence) {
			String[] strings = StrUtil.split(value.toString(), StrUtil.COMMA);
			return convertArrayToArray(strings);
		}

		// everything else:
		return convertToSingleElementArray(value);
	}

	/**
	 * 基本类型数组转为目标类型
	 * @param value 被转换的值
	 * @param primitiveComponentType 基本类型的类型
	 * @return 转换后的数组
	 */
	private T[] convertPrimitiveArrayToArray(Object value, Class<?> primitiveComponentType) {
		T[] result = null;
		final ConverterRegistry registry = ConverterRegistry.getInstance();

		if (primitiveComponentType == int.class) {
			int[] array = (int[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == long.class) {
			long[] array = (long[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == float.class) {
			float[] array = (float[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == double.class) {
			double[] array = (double[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == short.class) {
			short[] array = (short[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == byte.class) {
			byte[] array = (byte[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == char.class) {
			char[] array = (char[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == boolean.class) {
			boolean[] array = (boolean[]) value;
			result = ArrayUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = registry.convert(targetComponentType, array[i]);
			}
		}
		return result;
	}
	
	/**
	 * 单元素数组
	 * @param value 被转换的值
	 * @return 数组，只包含一个元素
	 */
	private T[] convertToSingleElementArray(Object value) {
		final T[] singleElementArray = ArrayUtil.newArray(targetComponentType, 1);
		singleElementArray[0] = ConverterRegistry.getInstance().convert(targetComponentType,value);
		return singleElementArray;
	}
	//-------------------------------------------------------------------------------------- Private method  end
}
