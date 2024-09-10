/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.convert.Converter;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;

import java.lang.reflect.Field;

/**
 * 字段调用器<br>
 * 通过反射读取或赋值字段<br>
 * 读取字段值：
 * <pre>{@code
 *   FieldInvoker.of(Field).invoke(obj);
 * }</pre>
 *
 * 赋值字段值：
 * <pre>{@code
 *   FieldInvoker.of(Field).invoke(obj, value);
 * }</pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class FieldInvoker implements Invoker {

	/**
	 * 创建字段调用器
	 *
	 * @param field 字段
	 * @return {@code FieldInvoker}
	 */
	public static FieldInvoker of(final Field field) {
		return new FieldInvoker(field);
	}

	private final Field field;
	private Converter converter;

	/**
	 * 构造
	 *
	 * @param field 字段
	 */
	public FieldInvoker(final Field field) {
		this.field = Assert.notNull(field);;
	}

	/**
	 * 设置字段值转换器
	 *
	 * @param converter 转换器，{@code null}表示不转换
	 * @return this
	 */
	public FieldInvoker setConverter(final Converter converter) {
		this.converter = converter;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T invoke(final Object target, final Object... args) {
		if(ArrayUtil.isEmpty(args)){
			// 默认取值
			return (T) invokeGet(target);
		} else if(args.length == 1){
			invokeSet(target, args[0]);
			return null;
		}

		throw new HutoolException("Field [{}] cannot be set with [{}] args", field.getName(), args.length);
	}

	/**
	 * 获取字段值
	 *
	 * @param obj   对象，static字段则此字段为null
	 * @return 字段值
	 * @throws HutoolException 包装IllegalAccessException异常
	 */
	public Object invokeGet(Object obj) throws HutoolException {
		if (null == field) {
			return null;
		}
		if (obj instanceof Class) {
			// 静态字段获取时对象为null
			obj = null;
		}

		ReflectUtil.setAccessible(field);
		final Object result;
		try {
			result = field.get(obj);
		} catch (final IllegalAccessException e) {
			throw new HutoolException(e, "IllegalAccess for {}.{}", field.getDeclaringClass(), field.getName());
		}
		return result;
	}

	/**
	 * 设置字段值，传入的字段值必须和字段类型一致，否则抛出异常
	 *
	 * @param obj   对象，如果是static字段，此参数为null
	 * @param value 值，值类型必须与字段类型匹配
	 * @throws HutoolException UtilException 包装IllegalAccessException异常
	 */
	public void invokeSet(final Object obj, final Object value) throws HutoolException {
		ReflectUtil.setAccessible(field);
		try {
			field.set(obj instanceof Class ? null : obj, convertValue(value));
		} catch (final IllegalAccessException e) {
			throw new HutoolException(e, "IllegalAccess for [{}.{}]", null == obj ? field.getDeclaringClass() : obj, field.getName());
		}
	}

	@Override
	public Class<?> getType() {
		return field.getType();
	}

	/**
	 * 转换值类型
	 *
	 * @param value 值
	 * @return 转换后的值
	 */
	private Object convertValue(final Object value){
		if(null == converter){
			return value;
		}

		// 值类型检查和转换
		final Class<?> fieldType = field.getType();
		if (null != value) {
			if (!fieldType.isAssignableFrom(value.getClass())) {
				//对于类型不同的字段，尝试转换，转换失败则使用原对象类型
				final Object targetValue = converter.convert(fieldType, value);
				if (null != targetValue) {
					return targetValue;
				}
			}
		} else {
			// 获取null对应默认值，防止原始类型造成空指针问题
			return ClassUtil.getDefaultValue(fieldType);
		}

		return value;
	}
}
