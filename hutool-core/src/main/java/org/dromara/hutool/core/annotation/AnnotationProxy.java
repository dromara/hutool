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

package org.dromara.hutool.core.annotation;

import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.StrUtil;

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

	/**
	 * 注解
	 */
	private final T annotation;
	/**
	 * 注解类型
	 */
	private final Class<T> type;
	/**
	 * 注解属性
	 */
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
				if(!attributes.containsKey(name)){
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
		// 只缓存注解定义的方法
		final Method[] methods = MethodUtil.getDeclaredMethods(this.type);
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
