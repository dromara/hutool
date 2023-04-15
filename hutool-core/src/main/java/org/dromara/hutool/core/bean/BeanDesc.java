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

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.MethodUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.BooleanUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bean信息描述做为BeanInfo替代方案，此对象持有JavaBean中的setters和getters等相关信息描述<br>
 * 查找Getter和Setter方法时会：
 *
 * <pre>
 * 1. 忽略字段和方法名的大小写
 * 2. Getter查找getXXX、isXXX、getIsXXX
 * 3. Setter查找setXXX、setIsXXX
 * 4. Setter忽略参数值与字段值不匹配的情况，因此有多个参数类型的重载时，会调用首次匹配的
 * </pre>
 *
 * @author looly
 * @since 3.1.2
 */
public class BeanDesc implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Bean类
	 */
	private final Class<?> beanClass;
	/**
	 * 属性Map
	 */
	private final Map<String, PropDesc> propMap = new LinkedHashMap<>();

	/**
	 * 构造
	 *
	 * @param beanClass Bean类
	 */
	public BeanDesc(final Class<?> beanClass) {
		Assert.notNull(beanClass);
		this.beanClass = beanClass;
		init();
	}

	/**
	 * 获取Bean的全类名
	 *
	 * @return Bean的类名
	 */
	public String getName() {
		return this.beanClass.getName();
	}

	/**
	 * 获取Bean的简单类名
	 *
	 * @return Bean的类名
	 */
	public String getSimpleName() {
		return this.beanClass.getSimpleName();
	}

	/**
	 * 获取字段名-字段属性Map
	 *
	 * @param ignoreCase 是否忽略大小写，true为忽略，false不忽略
	 * @return 字段名-字段属性Map
	 */
	public Map<String, PropDesc> getPropMap(final boolean ignoreCase) {
		return ignoreCase ? new CaseInsensitiveMap<>(1, this.propMap) : this.propMap;
	}

	/**
	 * 获取字段属性列表
	 *
	 * @return {@link PropDesc} 列表
	 */
	public Collection<PropDesc> getProps() {
		return this.propMap.values();
	}

	/**
	 * 获取属性，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return {@link PropDesc}
	 */
	public PropDesc getProp(final String fieldName) {
		return this.propMap.get(fieldName);
	}

	/**
	 * 获得字段名对应的字段对象，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return 字段值
	 */
	public Field getField(final String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getField();
	}

	/**
	 * 获取Getter方法，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return Getter方法
	 */
	public Method getGetter(final String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getGetter();
	}

	/**
	 * 获取Setter方法，如果不存在返回null
	 *
	 * @param fieldName 字段名
	 * @return Setter方法
	 */
	public Method getSetter(final String fieldName) {
		final PropDesc desc = this.propMap.get(fieldName);
		return null == desc ? null : desc.getSetter();
	}

	// ------------------------------------------------------------------------------------------------------ Private method start

	/**
	 * 初始化<br>
	 * 只有与属性关联的相关Getter和Setter方法才会被读取，无关的getXXX和setXXX都被忽略
	 *
	 */
	private void init() {
		final Method[] gettersAndSetters = MethodUtil.getPublicMethods(this.beanClass, MethodUtil::isGetterOrSetterIgnoreCase);
		PropDesc prop;
		for (final Field field : FieldUtil.getFields(this.beanClass)) {
			// 排除静态属性和对象子类
			if (!ModifierUtil.isStatic(field) &&
				!FieldUtil.isOuterClassField(field)) {
				prop = createProp(field, gettersAndSetters);
				// 只有不存在时才放入，防止父类属性覆盖子类属性
				this.propMap.putIfAbsent(prop.getFieldName(), prop);
			}
		}
	}

	/**
	 * 根据字段创建属性描述<br>
	 * 查找Getter和Setter方法时会：
	 *
	 * <pre>
	 * 1. 忽略字段和方法名的大小写
	 * 2. Getter查找getXXX、isXXX、getIsXXX
	 * 3. Setter查找setXXX、setIsXXX
	 * 4. Setter忽略参数值与字段值不匹配的情况，因此有多个参数类型的重载时，会调用首次匹配的
	 * </pre>
	 *
	 * @param field   字段
	 * @param methods 类中所有的方法
	 * @return {@link PropDesc}
	 * @since 4.0.2
	 */
	private PropDesc createProp(final Field field, final Method[] methods) {
		final PropDesc prop = findProp(field, methods, false);
		// 忽略大小写重新匹配一次
		if (null == prop.getter || null == prop.setter) {
			final PropDesc propIgnoreCase = findProp(field, methods, true);
			if (null == prop.getter) {
				prop.getter = propIgnoreCase.getter;
			}
			if (null == prop.setter) {
				prop.setter = propIgnoreCase.setter;
			}
		}

		return prop;
	}

	/**
	 * 查找字段对应的Getter和Setter方法
	 *
	 * @param field            字段
	 * @param gettersOrSetters 类中所有的Getter或Setter方法
	 * @param ignoreCase       是否忽略大小写匹配
	 * @return PropDesc
	 */
	private PropDesc findProp(final Field field, final Method[] gettersOrSetters, final boolean ignoreCase) {
		final String fieldName = field.getName();
		final Class<?> fieldType = field.getType();
		final boolean isBooleanField = BooleanUtil.isBoolean(fieldType);

		Method getter = null;
		Method setter = null;
		String methodName;
		for (final Method method : gettersOrSetters) {
			methodName = method.getName();
			if (method.getParameterCount() == 0) {
				// 无参数，可能为Getter方法
				if (isMatchGetter(methodName, fieldName, isBooleanField, ignoreCase)) {
					// 方法名与字段名匹配，则为Getter方法
					getter = method;
				}
			} else if (isMatchSetter(methodName, fieldName, isBooleanField, ignoreCase)) {
				// setter方法的参数类型和字段类型必须一致，或参数类型是字段类型的子类
				if(fieldType.isAssignableFrom(method.getParameterTypes()[0])){
					setter = method;
				}
			}
			if (null != getter && null != setter) {
				// 如果Getter和Setter方法都找到了，不再继续寻找
				break;
			}
		}

		return new PropDesc(field, getter, setter);
	}

	/**
	 * 方法是否为Getter方法<br>
	 * 匹配规则如下（忽略大小写）：
	 *
	 * <pre>
	 * 字段名    -》 方法名
	 * isName  -》 isName
	 * isName  -》 isIsName
	 * isName  -》 getIsName
	 * name     -》 isName
	 * name     -》 getName
	 * </pre>
	 *
	 * @param methodName     方法名
	 * @param fieldName      字段名
	 * @param isBooleanField 是否为Boolean类型字段
	 * @param ignoreCase     匹配是否忽略大小写
	 * @return 是否匹配
	 */
	private boolean isMatchGetter(String methodName, String fieldName, final boolean isBooleanField, final boolean ignoreCase) {
		final String handledFieldName;
		if (ignoreCase) {
			// 全部转为小写，忽略大小写比较
			methodName = methodName.toLowerCase();
			handledFieldName = fieldName.toLowerCase();
			fieldName = handledFieldName;
		} else {
			handledFieldName = StrUtil.upperFirst(fieldName);
		}

		// 针对Boolean类型特殊检查
		if (isBooleanField) {
			if (fieldName.startsWith("is")) {
				// 字段已经是is开头
				if (methodName.equals(fieldName) // isName -》 isName
						|| ("get" + handledFieldName).equals(methodName)// isName -》 getIsName
						|| ("is" + handledFieldName).equals(methodName)// isName -》 isIsName
				) {
					return true;
				}
			} else if (("is" + handledFieldName).equals(methodName)) {
				// 字段非is开头， name -》 isName
				return true;
			}
		}

		// 包括boolean的任何类型只有一种匹配情况：name -》 getName
		return ("get" + handledFieldName).equals(methodName);
	}

	/**
	 * 方法是否为Setter方法<br>
	 * 匹配规则如下（忽略大小写）：
	 *
	 * <pre>
	 * 字段名    -》 方法名
	 * isName  -》 setName
	 * isName  -》 setIsName
	 * name     -》 setName
	 * </pre>
	 *
	 * @param methodName     方法名
	 * @param fieldName      字段名
	 * @param isBooleanField 是否为Boolean类型字段
	 * @param ignoreCase     匹配是否忽略大小写
	 * @return 是否匹配
	 */
	private boolean isMatchSetter(String methodName, String fieldName, final boolean isBooleanField, final boolean ignoreCase) {
		final String handledFieldName;
		if (ignoreCase) {
			// 全部转为小写，忽略大小写比较
			methodName = methodName.toLowerCase();
			handledFieldName = fieldName.toLowerCase();
			fieldName = handledFieldName;
		} else {
			handledFieldName = StrUtil.upperFirst(fieldName);
		}

		// 非标准Setter方法跳过
		if (!methodName.startsWith("set")) {
			return false;
		}

		// 针对Boolean类型特殊检查
		if (isBooleanField && fieldName.startsWith("is")) {
			// 字段是is开头
			if (("set" + StrUtil.removePrefix(fieldName, "is")).equals(methodName)// isName -》 setName
					|| ("set" + handledFieldName).equals(methodName)// isName -》 setIsName
			) {
				return true;
			}
		}

		// 包括boolean的任何类型只有一种匹配情况：name -》 setName
		return ("set" + handledFieldName).equals(methodName);
	}
	// ------------------------------------------------------------------------------------------------------ Private method end
}
