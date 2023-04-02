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

package org.dromara.hutool.map;

import org.dromara.hutool.classloader.ClassLoaderUtil;
import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.lang.getter.TypeGetter;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.array.ArrayUtil;
import org.dromara.hutool.util.BooleanUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Map代理，提供各种getXXX方法，并提供默认值支持
 *
 * @author looly
 * @since 3.2.0
 */
public class MapProxy implements Map<Object, Object>, TypeGetter<Object>, InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	Map map;

	/**
	 * 创建代理Map<br>
	 * 此类对Map做一次包装，提供各种getXXX方法
	 *
	 * @param map 被代理的Map
	 * @return {@code MapProxy}
	 */
	public static MapProxy of(final Map<?, ?> map) {
		return (map instanceof MapProxy) ? (MapProxy) map : new MapProxy(map);
	}

	/**
	 * 构造
	 *
	 * @param map 被代理的Map
	 */
	public MapProxy(final Map<?, ?> map) {
		this.map = map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getObj(final Object key, final Object defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(final Object key) {
		return map.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object put(final Object key, final Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(final Object key) {
		return map.remove(key);
	}

	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public void putAll(final Map<?, ?> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public Set<Object> keySet() {
		return map.keySet();
	}

	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@SuppressWarnings({"unchecked", "NullableProblems"})
	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return map.entrySet();
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		if (ArrayUtil.isEmpty(parameterTypes)) {
			final Class<?> returnType = method.getReturnType();
			if (void.class != returnType) {
				// 匹配Getter
				final String methodName = method.getName();
				String fieldName = null;
				if (methodName.startsWith("get")) {
					// 匹配getXXX
					fieldName = StrUtil.removePreAndLowerFirst(methodName, 3);
				} else if (BooleanUtil.isBoolean(returnType) && methodName.startsWith("is")) {
					// 匹配isXXX
					fieldName = StrUtil.removePreAndLowerFirst(methodName, 2);
				}else if ("hashCode".equals(methodName)) {
					return this.hashCode();
				} else if ("toString".equals(methodName)) {
					return this.toString();
				}

				if (StrUtil.isNotBlank(fieldName)) {
					if (false == this.containsKey(fieldName)) {
						// 驼峰不存在转下划线尝试
						fieldName = StrUtil.toUnderlineCase(fieldName);
					}
					return Convert.convert(method.getGenericReturnType(), this.get(fieldName));
				}
			}

		} else if (1 == parameterTypes.length) {
			// 匹配Setter
			final String methodName = method.getName();
			if (methodName.startsWith("set")) {
				final String fieldName = StrUtil.removePreAndLowerFirst(methodName, 3);
				if (StrUtil.isNotBlank(fieldName)) {
					this.put(fieldName, args[0]);
					final Class<?> returnType = method.getReturnType();
					if(returnType.isInstance(proxy)){
						return proxy;
					}
				}
			} else if ("equals".equals(methodName)) {
				return this.equals(args[0]);
			}
		}

		throw new UnsupportedOperationException(method.toGenericString());
	}

	/**
	 * 将Map代理为指定接口的动态代理对象
	 *
	 * @param <T> 代理的Bean类型
	 * @param interfaceClass 接口
	 * @return 代理对象
	 * @since 4.5.2
	 */
	@SuppressWarnings("unchecked")
	public <T> T toProxyBean(final Class<T> interfaceClass) {
		return (T) Proxy.newProxyInstance(ClassLoaderUtil.getClassLoader(), new Class<?>[]{interfaceClass}, this);
	}
}
