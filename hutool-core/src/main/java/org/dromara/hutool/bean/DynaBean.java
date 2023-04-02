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

package org.dromara.hutool.bean;

import org.dromara.hutool.exceptions.CloneRuntimeException;
import org.dromara.hutool.lang.Assert;
import org.dromara.hutool.reflect.ClassUtil;
import org.dromara.hutool.reflect.ConstructorUtil;
import org.dromara.hutool.reflect.MethodUtil;

import java.io.Serializable;
import java.util.Map;

/**
 * 动态Bean，通过反射对Bean的相关方法做操作<br>
 * 支持Map和普通Bean
 *
 * @author Looly
 * @since 3.0.7
 */
public class DynaBean implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	private final Class<?> beanClass;
	private final Object bean;

	/**
	 * 创建一个DynaBean
	 *
	 * @param bean 普通Bean
	 * @return DynaBean
	 */
	public static DynaBean of(final Object bean) {
		return new DynaBean(bean);
	}

	/**
	 * 创建一个DynaBean
	 *
	 * @param beanClass Bean类
	 * @return DynaBean
	 */
	public static DynaBean of(final Class<?> beanClass) {
		return new DynaBean(beanClass);
	}


	/**
	 * 创建一个DynaBean
	 *
	 * @param beanClass Bean类
	 * @param params    构造Bean所需要的参数
	 * @return DynaBean
	 */
	public static DynaBean of(final Class<?> beanClass, final Object... params) {
		return new DynaBean(beanClass, params);
	}

	//------------------------------------------------------------------------ Constructor start

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 * @param params    构造Bean所需要的参数
	 */
	public DynaBean(final Class<?> beanClass, final Object... params) {
		this(ConstructorUtil.newInstance(beanClass, params));
	}

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 */
	public DynaBean(final Class<?> beanClass) {
		this(ConstructorUtil.newInstance(beanClass));
	}

	/**
	 * 构造
	 *
	 * @param bean 原始Bean
	 */
	public DynaBean(Object bean) {
		Assert.notNull(bean);
		if (bean instanceof DynaBean) {
			bean = ((DynaBean) bean).getBean();
		}
		this.bean = bean;
		this.beanClass = ClassUtil.getClass(bean);
	}
	//------------------------------------------------------------------------ Constructor end

	/**
	 * 获得字段对应值
	 *
	 * @param <T>       属性值类型
	 * @param fieldName 字段名
	 * @return 字段值
	 * @throws BeanException 反射获取属性值或字段值导致的异常
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String fieldName) throws BeanException {
		if (Map.class.isAssignableFrom(beanClass)) {
			return (T) ((Map<?, ?>) bean).get(fieldName);
		} else {
			final PropDesc prop = BeanUtil.getBeanDesc(beanClass).getProp(fieldName);
			if (null == prop) {
				throw new BeanException("No public field or get method for {}", fieldName);
			}
			return (T) prop.getValue(bean);
		}
	}

	/**
	 * 检查是否有指定名称的bean属性
	 *
	 * @param fieldName 字段名
	 * @return 是否有bean属性
	 * @since 5.4.2
	 */
	public boolean containsProp(final String fieldName) {
		if (Map.class.isAssignableFrom(beanClass)) {
			return ((Map<?, ?>) bean).containsKey(fieldName);
		} else{
			return null != BeanUtil.getBeanDesc(beanClass).getProp(fieldName);
		}
	}

	/**
	 * 获得字段对应值，获取异常返回{@code null}
	 *
	 * @param <T>       属性值类型
	 * @param fieldName 字段名
	 * @return 字段值
	 * @since 3.1.1
	 */
	public <T> T safeGet(final String fieldName) {
		try {
			return get(fieldName);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * 设置字段值
	 *
	 * @param fieldName 字段名
	 * @param value     字段值
	 * @throws BeanException 反射获取属性值或字段值导致的异常
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void set(final String fieldName, final Object value) throws BeanException {
		if (Map.class.isAssignableFrom(beanClass)) {
			((Map) bean).put(fieldName, value);
		} else {
			final PropDesc prop = BeanUtil.getBeanDesc(beanClass).getProp(fieldName);
			if (null == prop) {
				throw new BeanException("No public field or set method for {}", fieldName);
			}
			prop.setValue(bean, value);
		}
	}

	/**
	 * 执行原始Bean中的方法
	 *
	 * @param methodName 方法名
	 * @param params     参数
	 * @return 执行结果，可能为null
	 */
	public Object invoke(final String methodName, final Object... params) {
		return MethodUtil.invoke(this.bean, methodName, params);
	}

	/**
	 * 获得原始Bean
	 *
	 * @param <T> Bean类型
	 * @return bean
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean() {
		return (T) this.bean;
	}

	/**
	 * 获得Bean的类型
	 *
	 * @param <T> Bean类型
	 * @return Bean类型
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> getBeanClass() {
		return (Class<T>) this.beanClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bean == null) ? 0 : bean.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DynaBean other = (DynaBean) obj;
		if (bean == null) {
			return other.bean == null;
		} else return bean.equals(other.bean);
	}

	@Override
	public String toString() {
		return this.bean.toString();
	}

	@Override
	public DynaBean clone() {
		try {
			return (DynaBean) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new CloneRuntimeException(e);
		}
	}
}
