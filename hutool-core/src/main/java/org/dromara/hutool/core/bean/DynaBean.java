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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.exception.CloneException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 动态Bean，通过反射对Bean的相关方法做操作<br>
 * 支持Map和普通Bean和Collection
 *
 * @author Looly
 * @since 3.0.7
 */
public class DynaBean implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * bean类
	 */
	private final Class<?> beanClass;
	/**
	 * bean对象
	 */
	private Object bean;

	/**
	 * 创建一个DynaBean
	 *
	 * @param beanClass Bean类
	 * @param params    构造Bean所需要的参数
	 * @return DynaBean
	 */
	public static DynaBean of(final Class<?> beanClass, final Object... params) {
		return of(ConstructorUtil.newInstance(beanClass, params));
	}

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
	 * 构造
	 *
	 * @param bean 原始Bean
	 */
	public DynaBean(final Object bean) {
		Assert.notNull(bean);
		if (bean instanceof DynaBean) {
			// 已经是动态Bean，则提取对象
			this.bean = ((DynaBean) bean).getBean();
			this.beanClass = ((DynaBean) bean).getBeanClass();
		} else if (bean instanceof Class) {
			// 用户传入类，默认按照此类的默认实例对待
			this.bean = ConstructorUtil.newInstance((Class<?>) bean);
			this.beanClass = (Class<?>) bean;
		} else {
			// 普通Bean
			this.bean = bean;
			this.beanClass = ClassUtil.getClass(bean);
		}
	}

	/**
	 * 获得path表达式对应的值
	 *
	 * @param expression path表达式
	 * @param <T>        属性值类型
	 * @return 值
	 */
	public <T> T getProperty(final String expression) {
		return BeanUtil.getProperty(bean, expression);
	}

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
		} else if (bean instanceof Collection) {
			try {
				return (T) CollUtil.get((Collection<?>) bean, Integer.parseInt(fieldName));
			} catch (final NumberFormatException e) {
				// 非数字，see pr#254@Gitee
				return (T) CollUtil.map((Collection<?>) bean, (beanEle) -> DynaBean.of(beanEle).get(fieldName), false);
			}
		} else if (ArrayUtil.isArray(bean)) {
			try {
				return ArrayUtil.get(bean, Integer.parseInt(fieldName));
			} catch (final NumberFormatException e) {
				// 非数字，see pr#254@Gitee
				return (T) ArrayUtil.map(bean, Object.class, (beanEle) -> DynaBean.of(beanEle).get(fieldName));
			}
		} else {
			final PropDesc prop = BeanUtil.getBeanDesc(beanClass).getProp(fieldName);
			if (null == prop) {
				// 节点字段不存在，类似于Map无key，返回null而非报错
				return null;
				//throw new BeanException("No public field or get method for {}", fieldName);
			}
			return (T) prop.getValue(bean, false);
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
		} else if (bean instanceof Collection) {
			return CollUtil.size(bean) > Integer.parseInt(fieldName);
		} else {
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
	 * 设置属性值
	 *
	 * @param expression path表达式
	 * @param value      值
	 * @return this
	 */
	public DynaBean setProperty(final String expression, final Object value) {
		BeanUtil.setProperty(bean, expression, value);
		return this;
	}

	/**
	 * 设置字段值
	 *
	 * @param fieldName 字段名
	 * @param value     字段值
	 * @return this;
	 * @throws BeanException 反射获取属性值或字段值导致的异常
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public DynaBean set(final String fieldName, final Object value) throws BeanException {
		if (Map.class.isAssignableFrom(beanClass)) {
			((Map) bean).put(fieldName, value);
		} else if (bean instanceof List) {
			ListUtil.setOrPadding((List) bean, ConvertUtil.toInt(fieldName), value);
		} else if (ArrayUtil.isArray(bean)) {
			// issue#3008，追加产生新数组，此处返回新数组
			this.bean = ArrayUtil.setOrPadding(bean, ConvertUtil.toInt(fieldName), value);
		} else {
			final PropDesc prop = BeanUtil.getBeanDesc(beanClass).getProp(fieldName);
			if (null == prop) {
				throw new BeanException("No public field or set method for '{}'", fieldName);
			}

			prop.setValue(bean, value, false, false);
		}
		return this;
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
			throw new CloneException(e);
		}
	}
}
