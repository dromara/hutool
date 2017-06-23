package com.xiaoleilu.hutool.convert.impl;

import java.lang.reflect.Array;
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
public class ArrayConverter extends AbstractConverter<Object>{
	
	/** 目标元素类型 */
	private final Class<?> targetComponentType;
	
	/**
	 * 构造
	 * @param targetType 目标数组类型
	 */
	public ArrayConverter(Class<?> targetComponentType) {
		this.targetComponentType = targetComponentType;
	}

	@Override
	protected Object convertInternal(Object value) {
		return value.getClass().isArray() ? convertArrayToArray(value) : convertObjectToArray(value);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getTargetType() {
		return this.targetComponentType;
	}
	
	//-------------------------------------------------------------------------------------- Private method start
	/**
	 * 数组对数组转换
	 * @param value 被转换值
	 * @return 转换后的数组
	 */
	private Object convertArrayToArray(Object value) {
		final Class<?> valueComponentType = value.getClass().getComponentType();

		if (valueComponentType == targetComponentType) {
			return value;
		}
		
		final int len = ArrayUtil.length(value);
		final Object result =  Array.newInstance(targetComponentType, len);
		
		final ConverterRegistry converter = ConverterRegistry.getInstance();
		for(int i = 0; i < len; i++){
			Array.set(result, i, converter.convert(targetComponentType, Array.get(value, i)));
		}
		return result;
	}

	/**
	 * 非数组对数组转换
	 * @param value 被转换值
	 * @return 转换后的数组
	 */
	private Object convertObjectToArray(Object value) {
		final ConverterRegistry converter = ConverterRegistry.getInstance();
		Object[] result = null;
		if (value instanceof List) {
			final List<?> list = (List<?>) value;
			result = ArrayUtil.newArray(targetComponentType, list.size());
			for (int i = 0; i < list.size(); i++) {
				result[i] = converter.convert(targetComponentType, list.get(i));
			}
		}else  if (value instanceof Collection) {
			final Collection<?> collection = (Collection<?>) value;
			result = ArrayUtil.newArray(targetComponentType, collection.size());

			int i = 0;
			for (Object element : collection) {
				result[i] = converter.convert(targetComponentType, element);
				i++;
			}
		}else if (value instanceof Iterable) {
			final Iterable<?> iterable = (Iterable<?>) value;
			final List<Object> list = new ArrayList<>();
			for (Object element : iterable) {
				list.add(converter.convert(targetComponentType, element));
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
	 * 单元素数组
	 * @param value 被转换的值
	 * @return 数组，只包含一个元素
	 */
	private Object convertToSingleElementArray(Object value) {
		final Object[] singleElementArray = ArrayUtil.newArray(targetComponentType, 1);
		singleElementArray[0] = ConverterRegistry.getInstance().convert(targetComponentType,value);
		return singleElementArray;
	}
	//-------------------------------------------------------------------------------------- Private method  end
}
