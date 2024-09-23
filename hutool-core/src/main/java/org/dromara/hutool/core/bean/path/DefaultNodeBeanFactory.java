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

package org.dromara.hutool.core.bean.path;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.DynaBean;
import org.dromara.hutool.core.bean.path.node.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 默认的Bean创建器
 *
 * @author looly
 * @since 6.0.0
 */
public class DefaultNodeBeanFactory implements NodeBeanFactory<Object> {

	/**
	 * 单例
	 */
	public static final DefaultNodeBeanFactory INSTANCE = new DefaultNodeBeanFactory();

	@Override
	public Object create(final Object parent, final BeanPath<Object> beanPath) {
		if (parent instanceof Map || parent instanceof List || ArrayUtil.isArray(parent)) {
			// 根据下一个节点类型，判断当前节点名称对应类型
			final Node node = beanPath.next().getNode();
			if (node instanceof NameNode) {
				return ((NameNode) node).isNumber() ? new ArrayList<>() : new HashMap<>();
			}
			return new HashMap<>();
		}

		// 普通Bean
		final Node node = beanPath.getNode();
		if (node instanceof NameNode) {
			final String name = ((NameNode) node).getName();

			final Field field = FieldUtil.getField(parent.getClass(), name);
			if (null == field) {
				throw new IllegalArgumentException("No field found for name: " + name);
			}
			return ConstructorUtil.newInstanceIfPossible(field.getType());
		}

		throw new UnsupportedOperationException("Unsupported node type: " + node.getClass());
	}

	@Override
	public Object getValue(final Object bean, final BeanPath<Object> beanPath) {
		final Node node = beanPath.getNode();
		if (null == node || node instanceof EmptyNode) {
			return null;
		} else if (node instanceof ListNode) {
			return getValueByListNode(bean, (ListNode) node);
		} else if (node instanceof NameNode) {
			return getValueByNameNode(bean, (NameNode) node);
		} else if (node instanceof RangeNode) {
			return getValueByRangeNode(bean, (RangeNode) node);
		}

		throw new UnsupportedOperationException("Unsupported node type: " + node.getClass());
	}

	@Override
	public Object setValue(final Object bean, final Object value, final BeanPath<Object> beanPath) {
		final Node node = beanPath.getNode();
		if (null == node || node instanceof EmptyNode) {
			return bean;
		} else if (node instanceof NameNode) {
			return DynaBean.of(bean).set(((NameNode) node).getName(), value).getBean();
		}

		throw new UnsupportedOperationException("Unsupported node type: " + node.getClass());
	}

	/**
	 * 获取指定名称或下标列表对应的值<br>
	 * 如果为name列表，则获取Map或Bean中对应key或字段值列表<br>
	 * 如果为数字列表，则获取对应下标值列表
	 *
	 * @param bean Bean
	 * @param node 列表节点
	 * @return 值
	 */
	@SuppressWarnings("unchecked")
	private static Object getValueByListNode(final Object bean, final ListNode node) {
		final String[] names = node.getUnWrappedNames();

		if (bean instanceof Collection) {
			return CollUtil.getAny((Collection<?>) bean, ConvertUtil.convert(int[].class, names));
		} else if (ArrayUtil.isArray(bean)) {
			return ArrayUtil.getAny(bean, ConvertUtil.convert(int[].class, names));
		} else {
			final Map<String, Object> map;
			if (bean instanceof Map) {
				// 只支持String为key的Map
				map = (Map<String, Object>) bean;
			} else {
				// 一次性使用，包装Bean避免无用转换
				map = BeanUtil.toBeanMap(bean);
			}
			return MapUtil.getAny(map, names);
		}
	}

	/**
	 * 获取指定名称的值，支持Map、Bean等
	 *
	 * @param bean Bean
	 * @param node 节点
	 * @return 值
	 */
	private static Object getValueByNameNode(final Object bean, final NameNode node) {
		final String name = node.getName();
		if ("$".equals(name)) {
			return bean;
		}
		Object value = DynaBean.of(bean).get(name);
		if (null == value && StrUtil.lowerFirst(ClassUtil.getClassName(bean, true)).equals(name)) {
			// 如果bean类名与属性名相同，则返回bean本身
			value = bean;
		}
		return value;
	}

	/**
	 * 获取指定范围的值，只支持集合和数组
	 *
	 * @param bean Bean
	 * @param node 范围节点
	 * @return 值
	 */
	private static Object getValueByRangeNode(final Object bean, final RangeNode node) {
		if (bean instanceof Collection) {
			return CollUtil.sub((Collection<?>) bean, node.getStart(), node.getEnd(), node.getStep());
		} else if (ArrayUtil.isArray(bean)) {
			return ArrayUtil.sub(bean, node.getStart(), node.getEnd(), node.getStep());
		}

		throw new UnsupportedOperationException("Can not get range value for: " + bean.getClass());
	}
}
