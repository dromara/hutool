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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
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
	 */
	private void init() {
		final Method[] gettersAndSetters = MethodUtil.getPublicMethods(this.beanClass, MethodUtil::isGetterOrSetterIgnoreCase);
		PropDesc prop;
		for (final Field field : FieldUtil.getFields(this.beanClass)) {
			// 排除静态属性和对象子类
			if (!ModifierUtil.isStatic(field) && !FieldUtil.isOuterClassField(field)) {
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

		// Getter: name -> getName, Setter: name -> setName
		final Method[] getterAndSetter = findGetterAndSetter(fieldName, fieldType, gettersOrSetters, ignoreCase);

		if (isBooleanField) {
			if (null == getterAndSetter[0]) {
				// isName -> isName or isIsName
				// name -> isName
				getterAndSetter[0] = findGetterForBoolean(fieldName, gettersOrSetters, ignoreCase);
			}
			if (null == getterAndSetter[1]) {
				// isName -> setName
				getterAndSetter[1] = findSetterForBoolean(fieldName, gettersOrSetters, ignoreCase);
			}
		}

		return new PropDesc(field, getterAndSetter[0], getterAndSetter[1]);
	}

	/**
	 * 查找字段对应的Getter和Setter方法<br>
	 * 此方法不区分是否为boolean字段，查找规则为：
	 * <ul>
	 *     <li>Getter要求无参数且返回值是字段类型或字段的父类</li>
	 *     <li>Getter中，如果字段为name，匹配getName</li>
	 *     <li>Setter要求一个参数且参数必须为字段类型或字段的子类</li>
	 *     <li>Setter中，如果字段为name，匹配setName</li>
	 * </ul>
	 *
	 * @param fieldName        字段名
	 * @param fieldType        字段类型
	 * @param gettersOrSetters 类中所有的Getter或Setter方法
	 * @return PropDesc
	 */
	private Method[] findGetterAndSetter(final String fieldName, final Class<?> fieldType,
										 final Method[] gettersOrSetters, final boolean ignoreCase) {
		Method getter = null;
		Method setter = null;
		String methodName;
		for (final Method method : gettersOrSetters) {
			methodName = method.getName();
			if (0 == method.getParameterCount()) {
				// 无参数，可能为Getter方法
				if (StrUtil.equals(methodName, StrUtil.genGetter(fieldName), ignoreCase) &&
					method.getReturnType().isAssignableFrom(fieldType)) {
					// getter的返回类型必须为字段类型或字段的父类
					getter = method;
				}
			} else if (StrUtil.equals(methodName, StrUtil.genSetter(fieldName), ignoreCase) &&
				fieldType.isAssignableFrom(method.getParameterTypes()[0])) {
				// setter方法的参数必须为字段类型或字段的子类
				setter = method;
			}
			if (null != getter && null != setter) {
				// 如果Getter和Setter方法都找到了，不再继续寻找
				break;
			}
		}

		return new Method[]{getter, setter};
	}

	/**
	 * 针对Boolean或boolean类型字段，查找其对应的Getter方法，规则为：
	 * <ul>
	 *     <li>方法必须无参数且返回boolean或Boolean</li>
	 *     <li>如果字段为isName, 匹配isName、isIsName方法，两个方法均存在，则按照提供的方法数组优先匹配。</li>
	 *     <li>如果字段为name, 匹配isName方法</li>
	 * </ul>
	 * <p>
	 * 需要注意的是，以下两种格式不匹配，由{@link #findGetterAndSetter(String, Class, Method[], boolean)}完成：
	 * <ul>
	 *     <li>如果字段为name, 匹配getName</li>
	 *     <li>如果字段为isName, 匹配getIsName</li>
	 * </ul>
	 *
	 * @param fieldName        字段名
	 * @param gettersOrSetters 所有方法
	 * @param ignoreCase       是否忽略大小写
	 * @return 查找到的方法，{@code null}表示未找到
	 */
	private Method findGetterForBoolean(final String fieldName, final Method[] gettersOrSetters, final boolean ignoreCase) {
		// 查找isXXX
		return ArrayUtil.get(gettersOrSetters, m -> {
			if (0 != m.getParameterCount() || false == BooleanUtil.isBoolean(m.getReturnType())) {
				// getter方法要求无参数且返回boolean或Boolean
				return false;
			}

			if (StrUtil.startWith(fieldName, "is", ignoreCase)) {
				// isName -》 isName
				if(StrUtil.equals(fieldName, m.getName(), ignoreCase)){
					return true;
				}
			}

			// name   -》 isName
			// isName -》 isIsName
			return StrUtil.equals(StrUtil.upperFirstAndAddPre(fieldName, "is"), m.getName(), ignoreCase);
		});
	}

	/**
	 * 针对Boolean或boolean类型字段，查找其对应的Setter方法，规则为：
	 * <ul>
	 *     <li>方法必须为1个boolean或Boolean参数</li>
	 *     <li>如果字段为isName，匹配setName</li>
	 * </ul>
	 * <p>
	 * 需要注意的是，以下两种格式不匹配，由{@link #findGetterAndSetter(String, Class, Method[], boolean)}完成：
	 * <ul>
	 *     <li>如果字段为name, 匹配setName</li>
	 *     <li>如果字段为isName, 匹配setIsName</li>
	 * </ul>
	 *
	 * @param fieldName        字段名
	 * @param gettersOrSetters 所有方法
	 * @param ignoreCase       是否忽略大小写
	 * @return 查找到的方法，{@code null}表示未找到
	 */
	private Method findSetterForBoolean(final String fieldName, final Method[] gettersOrSetters, final boolean ignoreCase) {
		// 查找isXXX
		return ArrayUtil.get(gettersOrSetters, m -> {
			if (1 != m.getParameterCount() || false == BooleanUtil.isBoolean(m.getParameterTypes()[0])) {
				// setter方法要求1个boolean或Boolean参数
				return false;
			}

			if (StrUtil.startWith(fieldName, "is", ignoreCase)) {
				// isName -》 setName
				return StrUtil.equals(
					"set" + StrUtil.removePrefix(fieldName, "is", ignoreCase),
					m.getName(), ignoreCase);
			}

			// 其它不匹配
			return false;
		});
	}
	// ------------------------------------------------------------------------------------------------------ Private method end
}
