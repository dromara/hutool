/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.text.CharUtil;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 抽象转换器，提供通用的转换逻辑，同时通过convertInternal实现对应类型的专属逻辑<br>
 * 转换器不会抛出转换异常，转换失败时会返回{@code null}<br>
 * 抽象转换器的默认逻辑不适用于有泛型参数的对象，如Map、Collection、Entry等。通用逻辑包括：
 * <ul>
 *     <li>value为{@code null}时返回{@code null}</li>
 *     <li>目标类型是{@code null}或者{@link java.lang.reflect.TypeVariable}时，抛出{@link ConvertException}异常</li>
 *     <li>目标类型非class时，抛出{@link IllegalArgumentException}</li>
 *     <li>目标类型为值的父类或同类，直接强转返回</li>
 * </ul>
 *
 * @author Looly
 */
public abstract class AbstractConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object convert(final Type targetType, final Object value) throws ConvertException{
		if (null == value) {
			return null;
		}
		if (TypeUtil.isUnknown(targetType)) {
			throw new ConvertException("Unsupported convert to unKnown type: {}", targetType);
		}

		final Class<?> targetClass = TypeUtil.getClass(targetType);
		if(null == targetClass){
			throw new ConvertException("Target type [{}] is not a class!", targetType);
		}

		// 尝试强转
		if (targetClass.isInstance(value)) {
			// 除Map外，已经是目标类型，不需要转换（Map类型涉及参数类型，需要单独转换）
			return CastUtil.castTo(targetClass, value);
		}
		return convertInternal(targetClass, value);
	}

	/**
	 * 内部转换器，被 {@link AbstractConverter#convert(Type, Object)} 调用，实现基本转换逻辑<br>
	 * 内部转换器转换后如果转换失败可以做如下操作，处理结果都为返回默认值：
	 *
	 * <pre>
	 * 1、返回{@code null}
	 * 2、抛出一个{@link RuntimeException}异常
	 * </pre>
	 *
	 * @param targetClass 目标类型
	 * @param value 值
	 * @return 转换后的类型
	 */
	protected abstract Object convertInternal(Class<?> targetClass, Object value);

	/**
	 * 值转为String，用于内部转换中需要使用String中转的情况<br>
	 * 转换规则为：
	 *
	 * <pre>
	 * 1、字符串类型将被强转
	 * 2、数组将被转换为逗号分隔的字符串
	 * 3、其它类型将调用默认的toString()方法
	 * </pre>
	 *
	 * @param value 值
	 * @return String
	 */
	protected String convertToStr(final Object value) {
		if (null == value) {
			return null;
		}
		if (value instanceof CharSequence) {
			return value.toString();
		} else if (ArrayUtil.isArray(value)) {
			return ArrayUtil.toString(value);
		} else if (CharUtil.isChar(value)) {
			//对于ASCII字符使用缓存加速转换，减少空间创建
			return CharUtil.toString((char) value);
		}
		return value.toString();
	}
}
