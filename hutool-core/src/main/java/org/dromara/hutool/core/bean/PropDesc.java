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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.annotation.AnnotationUtil;
import org.dromara.hutool.core.annotation.PropIgnore;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.reflect.*;
import org.dromara.hutool.core.reflect.method.MethodInvoker;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 属性描述，包括了字段、getter、setter和相应的方法执行
 *
 * @author looly
 */
public class PropDesc {

	/**
	 * 字段
	 */
	private Invoker fieldInvoker;
	/**
	 * 字段名
	 */
	private final String fieldName;
	/**
	 * Getter方法
	 */
	protected Invoker getter;
	/**
	 * Setter方法
	 */
	protected Invoker setter;

	/**
	 * 构造<br>
	 * Getter和Setter方法设置为默认可访问
	 *
	 * @param field  字段
	 * @param getter get方法
	 * @param setter set方法
	 */
	public PropDesc(final Field field, final Method getter, final Method setter) {
		this(FieldUtil.getFieldName(field), getter, setter);
		this.fieldInvoker = null == field ? null : FieldInvoker.of(field);
	}

	/**
	 * 构造<br>
	 * Getter和Setter方法设置为默认可访问
	 *
	 * @param fieldName    字段名
	 * @param getterMethod get方法
	 * @param setterMethod set方法
	 */
	public PropDesc(final String fieldName, final Method getterMethod, final Method setterMethod) {
		this.fieldName = fieldName;
		this.getter = null == getterMethod ? null : MethodInvoker.of(getterMethod);
		this.setter = null == setterMethod ? null : MethodInvoker.of(setterMethod);
	}

	/**
	 * 获取字段名，如果存在Alias注解，读取注解的值作为名称
	 *
	 * @return 字段名
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	/**
	 * 获取字段名称
	 *
	 * @return 字段名
	 * @since 5.1.6
	 */
	public String getRawFieldName() {
		if (null == this.fieldInvoker) {
			return this.fieldName;
		}

		return this.fieldInvoker.getName();
	}

	/**
	 * 获取字段
	 *
	 * @return 字段
	 */
	public Field getField() {
		if (null != this.fieldInvoker && this.fieldInvoker instanceof FieldInvoker) {
			return ((FieldInvoker) this.fieldInvoker).getField();
		}
		return null;
	}

	/**
	 * 获得字段类型<br>
	 * 先获取字段的类型，如果字段不存在，则获取Getter方法的返回类型，否则获取Setter的第一个参数类型
	 *
	 * @return 字段类型
	 */
	public Type getFieldType() {
		if (null != this.fieldInvoker) {
			return this.fieldInvoker.getType();
		}
		return findPropType(getter, setter);
	}

	/**
	 * 获得字段类型<br>
	 * 先获取字段的类型，如果字段不存在，则获取Getter方法的返回类型，否则获取Setter的第一个参数类型
	 *
	 * @return 字段类型
	 */
	public Class<?> getFieldClass() {
		if (null != this.fieldInvoker) {
			return this.fieldInvoker.getTypeClass();
		}
		return findPropClass(getter, setter);
	}

	/**
	 * 获取Getter方法Invoker，可能为{@code null}
	 *
	 * @return Getter方法Invoker
	 */
	public Invoker getGetter() {
		return this.getter;
	}

	/**
	 * 获取Setter方法Invoker，可能为{@code null}
	 *
	 * @return {@link Method}Setter 方法Invoker
	 */
	public Invoker getSetter() {
		return this.setter;
	}

	/**
	 * 检查属性是否可读（即是否可以通过{@link #getValue(Object)}获取到值）
	 *
	 * @param checkTransient 是否检查Transient关键字或注解
	 * @return 是否可读
	 * @since 5.4.2
	 */
	public boolean isReadable(final boolean checkTransient) {
		Field field = null;
		if (this.fieldInvoker instanceof FieldInvoker) {
			field = ((FieldInvoker) this.fieldInvoker).getField();
		}
		Method getterMethod = null;
		if (this.getter instanceof MethodInvoker) {
			getterMethod = ((MethodInvoker) this.getter).getMethod();
		}

		// 检查transient关键字和@Transient注解
		if (checkTransient && isTransientForGet(field, getterMethod)) {
			return false;
		}

		// 检查@PropIgnore注解
		if (isIgnoreGet(field, getterMethod)) {
			return false;
		}

		// 检查是否有getter方法或是否为public修饰
		return null != getterMethod || ModifierUtil.isPublic(field);
	}

	/**
	 * 获取属性值<br>
	 * 首先调用字段对应的Getter方法获取值，如果Getter方法不存在，则判断字段如果为public，则直接获取字段值<br>
	 * 此方法不检查任何注解，使用前需调用 {@link #isReadable(boolean)} 检查是否可读
	 *
	 * @param bean Bean对象
	 * @return 字段值
	 * @since 4.0.5
	 */
	public Object getValue(final Object bean) {
		if (null != this.getter) {
			return this.getter.invoke(bean);
		} else if (null != this.fieldInvoker) {
			return fieldInvoker.invoke(bean);
		}

		return null;
	}

	/**
	 * 获取属性值，自动转换属性值类型<br>
	 * 首先调用字段对应的Getter方法获取值，如果Getter方法不存在，则判断字段如果为public，则直接获取字段值
	 *
	 * @param bean        Bean对象
	 * @param targetType  返回属性值需要转换的类型，null表示不转换
	 * @param ignoreError 是否忽略错误，包括转换错误和注入错误
	 * @return this
	 * @since 5.4.2
	 */
	public Object getValue(final Object bean, final Type targetType, final boolean ignoreError) {
		Object result = null;
		try {
			result = getValue(bean);
		} catch (final Exception e) {
			if (!ignoreError) {
				throw new BeanException(e, "Get value of [{}] error!", getFieldName());
			}
		}

		if (null != result && null != targetType) {
			// 尝试将结果转换为目标类型，如果转换失败，返回null，即跳过此属性值。
			// 来自：issues#I41WKP@Gitee，当忽略错误情况下，目标类型转换失败应返回null
			// 如果返回原值，在集合注入时会成功，但是集合取值时会报类型转换错误
			return ConvertUtil.convertWithCheck(targetType, result, null, ignoreError);
		}
		return result;
	}

	/**
	 * 检查属性是否可读（即是否可以通过{@link #getValue(Object)}获取到值）
	 *
	 * @param checkTransient 是否检查Transient关键字或注解
	 * @return 是否可读
	 * @since 5.4.2
	 */
	public boolean isWritable(final boolean checkTransient) {
		Field field = null;
		if (this.fieldInvoker instanceof FieldInvoker) {
			field = ((FieldInvoker) this.fieldInvoker).getField();
		}
		Method setterMethod = null;
		if (this.setter instanceof MethodInvoker) {
			setterMethod = ((MethodInvoker) this.setter).getMethod();
		}

		// 检查transient关键字和@Transient注解
		if (checkTransient && isTransientForSet(field, setterMethod)) {
			return false;
		}

		// 检查@PropIgnore注解
		if(isIgnoreSet(field, setterMethod)){
			return false;
		}

		// 检查是否有setter方法或是否为public修饰
		return null != setterMethod || ModifierUtil.isPublic(field);
	}

	/**
	 * 设置Bean的字段值<br>
	 * 首先调用字段对应的Setter方法，如果Setter方法不存在，则判断字段如果为public，则直接赋值字段值<br>
	 * 此方法不检查任何注解，使用前需调用 {@link #isWritable(boolean)} 检查是否可写
	 *
	 * @param bean  Bean对象
	 * @param value 值，必须与字段值类型匹配
	 * @return this
	 * @since 4.0.5
	 */
	public PropDesc setValue(final Object bean, final Object value) {
		if (null != this.setter) {
			this.setter.invoke(bean, value);
		} else if (null != this.fieldInvoker) {
			fieldInvoker.invoke(bean, value);
		}
		return this;
	}

	/**
	 * 设置属性值，可以自动转换字段类型为目标类型
	 *
	 * @param bean        Bean对象
	 * @param value       属性值，可以为任意类型
	 * @param ignoreNull  是否忽略{@code null}值，true表示忽略
	 * @param ignoreError 是否忽略错误，包括转换错误和注入错误
	 * @return this
	 * @since 5.4.2
	 */
	public PropDesc setValue(final Object bean, final Object value, final boolean ignoreNull, final boolean ignoreError) {
		return setValue(bean, value, ignoreNull, ignoreError, true);
	}

	/**
	 * 设置属性值，可以自动转换字段类型为目标类型
	 *
	 * @param bean        Bean对象
	 * @param value       属性值，可以为任意类型
	 * @param ignoreNull  是否忽略{@code null}值，true表示忽略
	 * @param ignoreError 是否忽略错误，包括转换错误和注入错误
	 * @param override    是否覆盖目标值，如果不覆盖，会先读取bean的值，{@code null}则写，否则忽略。如果覆盖，则不判断直接写
	 * @return this
	 * @since 5.7.17
	 */
	public PropDesc setValue(final Object bean, Object value, final boolean ignoreNull, final boolean ignoreError, final boolean override) {
		if (null == value && ignoreNull) {
			return this;
		}

		// issue#I4JQ1N@Gitee
		// 非覆盖模式下，如果目标值存在，则跳过
		if (!override && null != getValue(bean)) {
			return this;
		}

		// 当类型不匹配的时候，执行默认转换
		if (null != value) {
			final Class<?> propClass = getFieldClass();
			if (!propClass.isInstance(value)) {
				value = ConvertUtil.convertWithCheck(propClass, value, null, ignoreError);
			}
		}

		// 属性赋值
		if (null != value || !ignoreNull) {
			try {
				this.setValue(bean, value);
			} catch (final Exception e) {
				if (!ignoreError) {
					throw new BeanException(e, "Set value of [{}] error!", getFieldName());
				}
				// 忽略注入失败
			}
		}

		return this;
	}

	@Override
	public String toString() {
		return "PropDesc{" +
			"field=" + fieldInvoker +
			", fieldName=" + fieldName +
			", getter=" + getter +
			", setter=" + setter +
			'}';
	}

	// region ----- private methods

	/**
	 * 通过Getter和Setter方法中找到属性类型
	 *
	 * @param getterInvoker Getter方法Invoker
	 * @param setterInvoker Setter方法Invoker
	 * @return {@link Type}
	 */
	private Type findPropType(final Invoker getterInvoker, final Invoker setterInvoker) {
		Type type = null;
		if (null != getterInvoker) {
			type = getterInvoker.getType();
		}
		if (null == type && null != setterInvoker) {
			type = setterInvoker.getType();
		}
		return type;
	}

	/**
	 * 通过Getter和Setter方法中找到属性类型
	 *
	 * @param getterInvoker Getter方法Invoker
	 * @param setterInvoker Setter方法Invoker
	 * @return {@link Type}
	 */
	private Class<?> findPropClass(final Invoker getterInvoker, final Invoker setterInvoker) {
		Class<?> type = null;
		if (null != getterInvoker) {
			type = getterInvoker.getTypeClass();
		}
		if (null == type && null != setterInvoker) {
			type = setterInvoker.getTypeClass();
		}
		return type;
	}

	/**
	 * 检查字段是否被忽略读，通过{@link PropIgnore} 注解完成，规则为：
	 * <pre>
	 *     1. 在字段上有{@link PropIgnore} 注解
	 *     2. 在getXXX方法上有{@link PropIgnore} 注解
	 * </pre>
	 *
	 * @param field        字段，可为{@code null}
	 * @param getterMethod 读取方法，可为{@code null}
	 * @return 是否忽略读
	 */
	private static boolean isIgnoreGet(final Field field, final Method getterMethod) {
		return AnnotationUtil.hasAnnotation(field, PropIgnore.class)
			|| AnnotationUtil.hasAnnotation(getterMethod, PropIgnore.class);
	}

	/**
	 * 检查字段是否被忽略写，通过{@link PropIgnore} 注解完成，规则为：
	 * <pre>
	 *     1. 在字段上有{@link PropIgnore} 注解
	 *     2. 在setXXX方法上有{@link PropIgnore} 注解
	 * </pre>
	 *
	 * @param field        字段，可为{@code null}
	 * @param setterMethod 写方法，可为{@code null}
	 * @return 是否忽略写
	 */
	private static boolean isIgnoreSet(final Field field, final Method setterMethod) {
		return AnnotationUtil.hasAnnotation(field, PropIgnore.class)
			|| AnnotationUtil.hasAnnotation(setterMethod, PropIgnore.class);
	}

	/**
	 * 字段和Getter方法是否为Transient关键字修饰的
	 *
	 * @param field        字段，可为{@code null}
	 * @param getterMethod 读取方法，可为{@code null}
	 * @return 是否为Transient关键字修饰的
	 */
	private static boolean isTransientForGet(final Field field, final Method getterMethod) {
		boolean isTransient = ModifierUtil.hasAny(field, ModifierType.TRANSIENT);

		// 检查Getter方法
		if (!isTransient && null != getterMethod) {
			isTransient = ModifierUtil.hasAny(getterMethod, ModifierType.TRANSIENT);

			// 检查注解
			if (!isTransient) {
				isTransient = AnnotationUtil.hasAnnotation(getterMethod, Transient.class);
			}
		}

		return isTransient;
	}

	/**
	 * 字段和Getter方法是否为Transient关键字修饰的
	 *
	 * @param field        字段，可为{@code null}
	 * @param setterMethod 写方法，可为{@code null}
	 * @return 是否为Transient关键字修饰的
	 */
	private static boolean isTransientForSet(final Field field, final Method setterMethod) {
		boolean isTransient = ModifierUtil.hasAny(field, ModifierType.TRANSIENT);

		// 检查Getter方法
		if (!isTransient && null != setterMethod) {
			isTransient = ModifierUtil.hasAny(setterMethod, ModifierType.TRANSIENT);

			// 检查注解
			if (!isTransient) {
				isTransient = AnnotationUtil.hasAnnotation(setterMethod, Transient.class);
			}
		}

		return isTransient;
	}
	// endregion
}
