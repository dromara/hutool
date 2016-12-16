package com.xiaoleilu.hutool.convert.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.xiaoleilu.hutool.convert.AbstractConverter;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 数组转换器，包括原始类型数组
 * @author Looly
 *
 * @param <T>
 */
public class ArrayConverter<T> extends AbstractConverter<T[]>{
	
	private Class<T> targetComponentType;
	
	public ArrayConverter(Class<T> targetComponentType) {
		this.targetComponentType = targetComponentType;
	}

	@Override
	protected T[] convertInternal(Object value) {
		return value.getClass().isArray() ? convertObjectToArray(value) : convertArrayToArray(value);
	}

	
	//-------------------------------------------------------------------------------------- Private method start
	/**
	 * 数组对数组转换
	 * @param value 被转换值
	 * @return 转换后的数组
	 */
	@SuppressWarnings("unchecked")
	private T[] convertArrayToArray(Object value) {
		Class<?> valueComponentType = value.getClass().getComponentType();

		if (valueComponentType == targetComponentType) {
			return (T[]) value;
		}
		
		T[] result = null;
		if(valueComponentType.isPrimitive()){
			//原始类型
			result = convertPrimitiveArrayToArray(value, valueComponentType);
		}else{
			//非原始类型
			Object[] array = (Object[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
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
		if (value instanceof List) {
			final List<?> list = (List<?>) value;
			result = CollectionUtil.newArray(targetComponentType, list.size());
			for (int i = 0; i < list.size(); i++) {
				result[i] = Convert.convert(targetComponentType, list.get(i));
			}
		}else  if (value instanceof Collection) {
			final Collection<?> collection = (Collection<?>) value;
			result = CollectionUtil.newArray(targetComponentType, collection.size());

			int i = 0;
			for (Object element : collection) {
				result[i] = Convert.convert(targetComponentType, element);
				i++;
			}
		}else if (value instanceof Iterable) {
			final Iterable<?> iterable = (Iterable<?>) value;
			final List<T> list = new ArrayList<T>();
			for (Object element : iterable) {
				list.add(Convert.convert(targetComponentType, element));
			}

			result = CollectionUtil.newArray(targetComponentType, list.size());
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

		if (primitiveComponentType == int.class) {
			int[] array = (int[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == long.class) {
			long[] array = (long[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == float.class) {
			float[] array = (float[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == double.class) {
			double[] array = (double[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == short.class) {
			short[] array = (short[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == byte.class) {
			byte[] array = (byte[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == char.class) {
			char[] array = (char[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
			}
		}else if (primitiveComponentType == boolean.class) {
			boolean[] array = (boolean[]) value;
			result = CollectionUtil.newArray(targetComponentType, array.length);
			for (int i = 0; i < array.length; i++) {
				result[i] = Convert.convert(targetComponentType, array[i]);
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
		final T[] singleElementArray = CollectionUtil.newArray(targetComponentType, 1);
		singleElementArray[0] = Convert.convert(targetComponentType,value);
		return singleElementArray;
	}
	//-------------------------------------------------------------------------------------- Private method  end
}
