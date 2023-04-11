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

package org.dromara.hutool.core.bean;

import org.dromara.hutool.core.annotation.AnnotationUtil;
import org.dromara.hutool.core.annotation.PropIgnore;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.func.LambdaUtil;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.reflect.ReflectUtil;
import org.dromara.hutool.core.reflect.TypeUtil;

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
	final Field field;
	/**
	 * Getter方法
	 */
	protected Method getter;
	/**
	 * Setter方法
	 */
	protected Method setter;

	/**
	 * 构造<br>
	 * Getter和Setter方法设置为默认可访问
	 *
	 * @param field  字段
	 * @param getter get方法
	 * @param setter set方法
	 */
	public PropDesc(final Field field, final Method getter, final Method setter) {
		this.field = field;
		this.getter = ReflectUtil.setAccessible(getter);
		this.setter = ReflectUtil.setAccessible(setter);
	}

	/**
	 * 获取字段名，如果存在Alias注解，读取注解的值作为名称
	 *
	 * @return 字段名
	 */
	public String getFieldName() {
		return FieldUtil.getFieldName(this.field);
	}

	/**
	 * 获取字段名称
	 *
	 * @return 字段名
	 * @since 5.1.6
	 */
	public String getRawFieldName() {
		return null == this.field ? null : this.field.getName();
	}

	/**
	 * 获取字段
	 *
	 * @return 字段
	 */
	public Field getField() {
		return this.field;
	}

	/**
	 * 获得字段类型<br>
	 * 先获取字段的类型，如果字段不存在，则获取Getter方法的返回类型，否则获取Setter的第一个参数类型
	 *
	 * @return 字段类型
	 */
	public Type getFieldType() {
		if (null != this.field) {
			return TypeUtil.getType(this.field);
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
		if (null != this.field) {
			return TypeUtil.getClass(this.field);
		}
		return findPropClass(getter, setter);
	}

	/**
	 * 获取Getter方法，可能为{@code null}
	 *
	 * @return Getter方法
	 */
	public Method getGetter() {
		return this.getter;
	}

	/**
	 * 获取Setter方法，可能为{@code null}
	 *
	 * @return {@link Method}Setter 方法对象
	 */
	public Method getSetter() {
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
		// 检查是否有getter方法或是否为public修饰
		if (null == this.getter && ! ModifierUtil.isPublic(this.field)) {
			return false;
		}

		// 检查transient关键字和@Transient注解
		if (checkTransient && isTransientForGet()) {
			return false;
		}

		// 检查@PropIgnore注解
		return ! isIgnoreGet();
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
			//return MethodUtil.invoke(bean, this.getter);
			return LambdaUtil.buildGetter(this.getter).apply(bean);
		} else if (ModifierUtil.isPublic(this.field)) {
			return FieldUtil.getFieldValue(bean, this.field);
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
			if (! ignoreError) {
				throw new BeanException(e, "Get value of [{}] error!", getFieldName());
			}
		}

		if (null != result && null != targetType) {
			// 尝试将结果转换为目标类型，如果转换失败，返回null，即跳过此属性值。
			// 来自：issues#I41WKP@Gitee，当忽略错误情况下，目标类型转换失败应返回null
			// 如果返回原值，在集合注入时会成功，但是集合取值时会报类型转换错误
			return Convert.convertWithCheck(targetType, result, null, ignoreError);
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
		// 检查是否有getter方法或是否为public修饰
		if (null == this.setter && ! ModifierUtil.isPublic(this.field)) {
			return false;
		}

		// 检查transient关键字和@Transient注解
		if (checkTransient && isTransientForSet()) {
			return false;
		}

		// 检查@PropIgnore注解
		return ! isIgnoreSet();
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
			//MethodUtil.invoke(bean, this.setter, value);
			LambdaUtil.buildSetter(this.setter).accept(bean, value);
		} else if (ModifierUtil.isPublic(this.field)) {
			FieldUtil.setFieldValue(bean, this.field, value);
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
		if (! override && null != getValue(bean)) {
			return this;
		}

		// 当类型不匹配的时候，执行默认转换
		if (null != value) {
			final Class<?> propClass = getFieldClass();
			if (! propClass.isInstance(value)) {
				value = Convert.convertWithCheck(propClass, value, null, ignoreError);
			}
		}

		// 属性赋值
		if (null != value || ! ignoreNull) {
			try {
				this.setValue(bean, value);
			} catch (final Exception e) {
				if (! ignoreError) {
					throw new BeanException(e, "Set value of [{}] error!", getFieldName());
				}
				// 忽略注入失败
			}
		}

		return this;
	}

	//------------------------------------------------------------------------------------ Private method start

	/**
	 * 通过Getter和Setter方法中找到属性类型
	 *
	 * @param getter Getter方法
	 * @param setter Setter方法
	 * @return {@link Type}
	 */
	private Type findPropType(final Method getter, final Method setter) {
		Type type = null;
		if (null != getter) {
			type = TypeUtil.getReturnType(getter);
		}
		if (null == type && null != setter) {
			type = TypeUtil.getParamType(setter, 0);
		}
		return type;
	}

	/**
	 * 通过Getter和Setter方法中找到属性类型
	 *
	 * @param getter Getter方法
	 * @param setter Setter方法
	 * @return {@link Type}
	 */
	private Class<?> findPropClass(final Method getter, final Method setter) {
		Class<?> type = null;
		if (null != getter) {
			type = TypeUtil.getReturnClass(getter);
		}
		if (null == type && null != setter) {
			type = TypeUtil.getFirstParamClass(setter);
		}
		return type;
	}

	/**
	 * 检查字段是否被忽略写，通过{@link PropIgnore} 注解完成，规则为：
	 * <pre>
	 *     1. 在字段上有{@link PropIgnore} 注解
	 *     2. 在setXXX方法上有{@link PropIgnore} 注解
	 * </pre>
	 *
	 * @return 是否忽略写
	 * @since 5.4.2
	 */
	private boolean isIgnoreSet() {
		return AnnotationUtil.hasAnnotation(this.field, PropIgnore.class)
				|| AnnotationUtil.hasAnnotation(this.setter, PropIgnore.class);
	}

	/**
	 * 检查字段是否被忽略读，通过{@link PropIgnore} 注解完成，规则为：
	 * <pre>
	 *     1. 在字段上有{@link PropIgnore} 注解
	 *     2. 在getXXX方法上有{@link PropIgnore} 注解
	 * </pre>
	 *
	 * @return 是否忽略读
	 * @since 5.4.2
	 */
	private boolean isIgnoreGet() {
		return AnnotationUtil.hasAnnotation(this.field, PropIgnore.class)
				|| AnnotationUtil.hasAnnotation(this.getter, PropIgnore.class);
	}

	/**
	 * 字段和Getter方法是否为Transient关键字修饰的
	 *
	 * @return 是否为Transient关键字修饰的
	 * @since 5.3.11
	 */
	private boolean isTransientForGet() {
		boolean isTransient = ModifierUtil.hasModifier(this.field, ModifierUtil.ModifierType.TRANSIENT);

		// 检查Getter方法
		if (! isTransient && null != this.getter) {
			isTransient = ModifierUtil.hasModifier(this.getter, ModifierUtil.ModifierType.TRANSIENT);

			// 检查注解
			if (! isTransient) {
				isTransient = AnnotationUtil.hasAnnotation(this.getter, Transient.class);
			}
		}

		return isTransient;
	}

	/**
	 * 字段和Getter方法是否为Transient关键字修饰的
	 *
	 * @return 是否为Transient关键字修饰的
	 * @since 5.3.11
	 */
	private boolean isTransientForSet() {
		boolean isTransient = ModifierUtil.hasModifier(this.field, ModifierUtil.ModifierType.TRANSIENT);

		// 检查Getter方法
		if (! isTransient && null != this.setter) {
			isTransient = ModifierUtil.hasModifier(this.setter, ModifierUtil.ModifierType.TRANSIENT);

			// 检查注解
			if (! isTransient) {
				isTransient = AnnotationUtil.hasAnnotation(this.setter, Transient.class);
			}
		}

		return isTransient;
	}
	//------------------------------------------------------------------------------------ Private method end
}
