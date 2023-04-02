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

package org.dromara.hutool.annotation;

import org.dromara.hutool.reflect.MethodUtil;
import org.dromara.hutool.text.StrUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解代理<br>
 * 通过代理指定注解，可以自定义调用注解的方法逻辑，如支持{@link Alias} 注解
 *
 * @param <T> 注解类型
 * @since 5.7.23
 */
public class AnnotationProxy<T extends Annotation> implements Annotation, InvocationHandler, Serializable {
	private static final long serialVersionUID = 1L;

	private final T annotation;
	private final Class<T> type;
	private final Map<String, Object> attributes;

	/**
	 * 构造
	 *
	 * @param annotation 注解
	 */
	@SuppressWarnings("unchecked")
	public AnnotationProxy(final T annotation) {
		this.annotation = annotation;
		this.type = (Class<T>) annotation.annotationType();
		this.attributes = initAttributes();
	}


	@Override
	public Class<? extends Annotation> annotationType() {
		return type;
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

		// 注解别名
		final Alias alias = method.getAnnotation(Alias.class);
		if(null != alias){
			final String name = alias.value();
			if(StrUtil.isNotBlank(name)){
				if(false == attributes.containsKey(name)){
					throw new IllegalArgumentException(StrUtil.format("No method for alias: [{}]", name));
				}
				return attributes.get(name);
			}
		}

		final Object value = attributes.get(method.getName());
		if (value != null) {
			return value;
		}
		return method.invoke(this, args);
	}

	/**
	 * 初始化注解的属性<br>
	 * 此方法预先调用所有注解的方法，将注解方法值缓存于attributes中
	 *
	 * @return 属性（方法结果）映射
	 */
	private Map<String, Object> initAttributes() {
		final Method[] methods = MethodUtil.getMethods(this.type);
		final Map<String, Object> attributes = new HashMap<>(methods.length, 1);

		for (final Method method : methods) {
			// 跳过匿名内部类自动生成的方法
			if (method.isSynthetic()) {
				continue;
			}

			attributes.put(method.getName(), MethodUtil.invoke(this.annotation, method));
		}

		return attributes;
	}
}
