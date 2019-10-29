package cn.hutool.core.convert.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.AbstractConverter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 数组转换器，包括原始类型数组
 * 
 * @author Looly
 */
public class ArrayConverter extends AbstractConverter<Object> {
	private static final long serialVersionUID = 1L;

	private final Class<?> targetType;
	/** 目标元素类型 */
	private final Class<?> targetComponentType;

	/**
	 * 构造
	 * 
	 * @param targetType 目标数组类型
	 */
	public ArrayConverter(Class<?> targetType) {
		if (null == targetType) {
			// 默认Object数组
			targetType = Object[].class;
		}
		
		if(targetType.isArray()) {
			this.targetType = targetType;
			this.targetComponentType = targetType.getComponentType();
		}else {
			//用户传入类为非数组时，按照数组元素类型对待
			this.targetComponentType = targetType;
			this.targetType = ArrayUtil.getArrayType(targetType);
		}
	}

	@Override
	protected Object convertInternal(Object value) {
		return value.getClass().isArray() ? convertArrayToArray(value) : convertObjectToArray(value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getTargetType() {
		return this.targetType;
	}

	// -------------------------------------------------------------------------------------- Private method start
	/**
	 * 数组对数组转换
	 * 
	 * @param array 被转换的数组值
	 * @return 转换后的数组
	 */
	private Object convertArrayToArray(Object array) {
		final Class<?> valueComponentType = ArrayUtil.getComponentType(array);

		if (valueComponentType == targetComponentType) {
			return array;
		}

		final int len = ArrayUtil.length(array);
		final Object result = Array.newInstance(targetComponentType, len);

		final ConverterRegistry converter = ConverterRegistry.getInstance();
		for (int i = 0; i < len; i++) {
			Array.set(result, i, converter.convert(targetComponentType, Array.get(array, i)));
		}
		return result;
	}

	/**
	 * 非数组对数组转换
	 * 
	 * @param value 被转换值
	 * @return 转换后的数组
	 */
	private Object convertObjectToArray(Object value) {
		if (value instanceof CharSequence) {
			if (targetComponentType == char.class || targetComponentType == Character.class) {
				return convertArrayToArray(value.toString().toCharArray());
			}

			// 单纯字符串情况下按照逗号分隔后劈开
			final String[] strings = StrUtil.split(value.toString(), StrUtil.COMMA);
			return convertArrayToArray(strings);
		}

		final ConverterRegistry converter = ConverterRegistry.getInstance();
		Object result;
		if (value instanceof List) {
			// List转数组
			final List<?> list = (List<?>) value;
			result = Array.newInstance(targetComponentType, list.size());
			for (int i = 0; i < list.size(); i++) {
				Array.set(result, i, converter.convert(targetComponentType, list.get(i)));
			}
		} else if (value instanceof Collection) {
			// 集合转数组
			final Collection<?> collection = (Collection<?>) value;
			result = Array.newInstance(targetComponentType, collection.size());

			int i = 0;
			for (Object element : collection) {
				Array.set(result, i, converter.convert(targetComponentType, element));
				i++;
			}
		} else if (value instanceof Iterable) {
			// 可循环对象转数组，可循环对象无法获取长度，因此先转为List后转为数组
			final List<?> list = IterUtil.toList((Iterable<?>) value);
			result = Array.newInstance(targetComponentType, list.size());
			for (int i = 0; i < list.size(); i++) {
				Array.set(result, i, converter.convert(targetComponentType, list.get(i)));
			}
		} else if (value instanceof Iterator) {
			// 可循环对象转数组，可循环对象无法获取长度，因此先转为List后转为数组
			final List<?> list = IterUtil.toList((Iterator<?>) value);
			result = Array.newInstance(targetComponentType, list.size());
			for (int i = 0; i < list.size(); i++) {
				Array.set(result, i, converter.convert(targetComponentType, list.get(i)));
			}
		} else {
			// everything else:
			result = convertToSingleElementArray(value);
		}

		return result;
	}

	/**
	 * 单元素数组
	 * 
	 * @param value 被转换的值
	 * @return 数组，只包含一个元素
	 */
	private Object[] convertToSingleElementArray(Object value) {
		final Object[] singleElementArray = ArrayUtil.newArray(targetComponentType, 1);
		singleElementArray[0] = ConverterRegistry.getInstance().convert(targetComponentType, value);
		return singleElementArray;
	}
	// -------------------------------------------------------------------------------------- Private method end
}
