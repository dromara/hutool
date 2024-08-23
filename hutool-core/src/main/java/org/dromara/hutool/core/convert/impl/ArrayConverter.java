/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.codec.binary.Base64;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.convert.MatcherConverter;
import org.dromara.hutool.core.io.SerializeUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.util.ByteUtil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 数组转换器，包括原始类型数组
 *
 * @author Looly
 */
public class ArrayConverter extends AbstractConverter implements MatcherConverter {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final ArrayConverter INSTANCE = new ArrayConverter();

	/**
	 * 是否忽略元素转换错误
	 */
	private boolean ignoreElementError;

	/**
	 * 构造
	 */
	public ArrayConverter() {
		this(false);
	}

	/**
	 * 构造
	 *
	 * @param ignoreElementError 是否忽略元素转换错误
	 */
	public ArrayConverter(final boolean ignoreElementError) {
		this.ignoreElementError = ignoreElementError;
	}

	@Override
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return rawType.isArray();
	}

	@Override
	protected Object convertInternal(final Class<?> targetClass, final Object value) {
		final Class<?> targetComponentType;
		if (targetClass.isArray()) {
			targetComponentType = targetClass.getComponentType();
		} else {
			//用户传入类为非数组时，按照数组元素类型对待
			targetComponentType = targetClass;
		}

		return value.getClass().isArray() ? convertArrayToArray(targetComponentType, value)
			: convertObjectToArray(targetComponentType, value);
	}

	/**
	 * 设置是否忽略元素转换错误
	 *
	 * @param ignoreElementError 是否忽略元素转换错误
	 * @since 5.4.3
	 */
	public void setIgnoreElementError(final boolean ignoreElementError) {
		this.ignoreElementError = ignoreElementError;
	}

	// -------------------------------------------------------------------------------------- Private method start

	/**
	 * 数组对数组转换
	 *
	 * @param array 被转换的数组值
	 * @return 转换后的数组
	 */
	private Object convertArrayToArray(final Class<?> targetComponentType, final Object array) {
		final Class<?> valueComponentType = ArrayUtil.getComponentType(array);

		if (valueComponentType == targetComponentType) {
			return array;
		}

		final int len = ArrayUtil.length(array);
		final Object result = Array.newInstance(targetComponentType, len);

		for (int i = 0; i < len; i++) {
			Array.set(result, i, convertComponentType(targetComponentType, Array.get(array, i)));
		}
		return result;
	}

	/**
	 * 非数组对数组转换
	 *
	 * @param targetComponentType 目标单个节点类型
	 * @param value               被转换值
	 * @return 转换后的数组
	 */
	private Object convertObjectToArray(final Class<?> targetComponentType, Object value) {
		if (value instanceof CharSequence) {
			if (targetComponentType == char.class || targetComponentType == Character.class) {
				return convertArrayToArray(targetComponentType, value.toString().toCharArray());
			}

			//issue#2365
			// 字符串转bytes，首先判断是否为Base64，是则转换，否则按照默认getBytes方法。
			if (targetComponentType == byte.class) {
				final String str = value.toString();
				if (Base64.isTypeBase64(str)) {
					return Base64.decode(value.toString());
				}
				return str.getBytes();
			}

			// 单纯字符串情况下按照逗号分隔后劈开
			final String[] strings = SplitUtil.splitToArray(value.toString(), StrUtil.COMMA);
			return convertArrayToArray(targetComponentType, strings);
		}

		if(value instanceof Iterator){
			value = IterUtil.asIterable((Iterator<?>)value);
		}

		final Object result;
		if (value instanceof Iterable) {
			result = convertIterableToArray(targetComponentType, (Iterable<?>) value);
		} else if (value instanceof Number && byte.class == targetComponentType) {
			// 用户可能想序列化指定对象
			result = ByteUtil.toBytes((Number) value);
		} else if (value instanceof Serializable && byte.class == targetComponentType) {
			// 用户可能想序列化指定对象
			result = SerializeUtil.serialize(value);
		} else {
			// everything else:
			result = convertToSingleElementArray(targetComponentType, value);
		}

		return result;
	}

	/**
	 * 迭代器转数组
	 *
	 * @param targetComponentType 目标单个节点类型
	 * @param value               迭代器实现值
	 * @return 数组
	 */
	private Object convertIterableToArray(final Class<?> targetComponentType, final Iterable<?> value) {
		final Object result;
		if (value instanceof List) {
			// List转数组
			final List<?> list = (List<?>) value;
			final int size = list.size();
			result = Array.newInstance(targetComponentType, size);
			for (int i = 0; i < size; i++) {
				Array.set(result, i, convertComponentType(targetComponentType, list.get(i)));
			}
		} else if (value instanceof Collection) {
			// 集合转数组
			final Collection<?> collection = (Collection<?>) value;
			result = Array.newInstance(targetComponentType, collection.size());

			int i = 0;
			for (final Object element : collection) {
				Array.set(result, i, convertComponentType(targetComponentType, element));
				i++;
			}
		} else {
			// 可循环对象转数组，可循环对象无法获取长度，因此先转为List后转为数组
			final List<?> list = ListUtil.of(value);
			final int size = list.size();
			result = Array.newInstance(targetComponentType, size);
			for (int i = 0; i < size; i++) {
				Array.set(result, i, convertComponentType(targetComponentType, list.get(i)));
			}
		}
		return result;
	}

	/**
	 * 单元素数组
	 *
	 * @param value 被转换的值
	 * @return 数组，只包含一个元素
	 */
	private Object[] convertToSingleElementArray(final Class<?> targetComponentType, final Object value) {
		final Object[] singleElementArray = ArrayUtil.newArray(targetComponentType, 1);
		singleElementArray[0] = convertComponentType(targetComponentType, value);
		return singleElementArray;
	}

	/**
	 * 转换元素类型
	 *
	 * @param value 值
	 * @return 转换后的值，转换失败若{@link #ignoreElementError}为true，返回null，否则抛出异常
	 * @since 5.4.3
	 */
	private Object convertComponentType(final Class<?> targetComponentType, final Object value) {
		return ConvertUtil.convertWithCheck(targetComponentType, value, null, this.ignoreElementError);
	}
	// -------------------------------------------------------------------------------------- Private method end
}
