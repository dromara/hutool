/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.annotation;

import org.dromara.hutool.core.map.SafeConcurrentHashMap;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.CharSequenceUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代理注解处理器，用于为{@link AnnotationMapping}生成代理对象，当从该代理对象上获取属性值时，
 * 总是通过{@link AnnotationMapping#getResolvedAttributeValue(String, Class)}获取。
 *
 * @param <T> 注解类型
 * @author huangchengxing
 * @see AnnotationMapping
 * @since 6.0.0
 */
public final class AnnotationMappingProxy<T extends Annotation> implements InvocationHandler {

	/**
	 * 属性映射
	 */
	private final AnnotationMapping<T> mapping;

	/**
	 * 代理方法
	 */
	private final Map<String, BiFunction<Method, Object[], Object>> methods;

	/**
	 * 属性值缓存
	 */
	private final Map<String, Object> valueCache;

	/**
	 * 创建一个代理对象
	 *
	 * @param annotationType 注解类型
	 * @param mapping        注解映射对象
	 * @param <A>            注解类型
	 * @return 代理对象
	 */
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A create(final Class<? extends A> annotationType, final AnnotationMapping<A> mapping) {
		Objects.requireNonNull(annotationType);
		Objects.requireNonNull(mapping);
		final AnnotationMappingProxy<A> invocationHandler = new AnnotationMappingProxy<>(mapping);
		return (A)Proxy.newProxyInstance(
			annotationType.getClassLoader(),
			new Class[]{ annotationType, Proxied.class },
			invocationHandler
		);
	}

	/**
	 * 当前注解是否由当前代理类生成
	 *
	 * @param annotation 注解对象
	 * @return 是否
	 */
	public static boolean isProxied(final Annotation annotation) {
		return annotation instanceof Proxied;
	}

	/**
	 * 创建一个代理方法处理器
	 *
	 * @param annotation 属性映射
	 */
	private AnnotationMappingProxy(final AnnotationMapping<T> annotation) {
		final int methodCount = annotation.getAttributes().length;
		this.methods = new HashMap<>(methodCount + 5);
		this.valueCache = new SafeConcurrentHashMap<>(methodCount);
		this.mapping = annotation;
		loadMethods();
	}

	/**
	 * 调用被代理的方法
	 *
	 * @param proxy  代理对象
	 * @param method 方法
	 * @param args   参数
	 * @return 返回值
	 */
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) {
		return Optional.ofNullable(methods.get(method.getName()))
			.map(m -> m.apply(method, args))
			.orElseGet(() -> MethodUtil.invoke(this, method, args));
	}

	// ============================== 代理方法 ==============================

	/**
	 * 预加载需要代理的方法
	 */
	private void loadMethods() {
		methods.put("equals", (method, args) -> proxyEquals(args[0]));
		methods.put("toString", (method, args) -> proxyToString());
		methods.put("hashCode", (method, args) -> proxyHashCode());
		methods.put("annotationType", (method, args) -> proxyAnnotationType());
		methods.put("getMapping", (method, args) -> proxyGetMapping());
		for (final Method attribute : mapping.getAttributes()) {
			methods.put(attribute.getName(), (method, args) -> getAttributeValue(method.getName(), method.getReturnType()));
		}
	}

	/**
	 * 代理{@link Annotation#toString()}方法
	 */
	private String proxyToString() {
		final String attributes = Stream.of(mapping.getAttributes())
			.map(attribute -> CharSequenceUtil.format("{}={}", attribute.getName(), getAttributeValue(attribute.getName(), attribute.getReturnType())))
			.collect(Collectors.joining(", "));
		return CharSequenceUtil.format("@{}({})", mapping.annotationType().getName(), attributes);
	}

	/**
	 * 代理{@link Annotation#hashCode()}方法
	 */
	private int proxyHashCode() {
		return this.hashCode();
	}

	/**
	 * 代理{@link Annotation#equals(Object)}方法
	 */
	private boolean proxyEquals(final Object o) {
		return Objects.equals(mapping, o);
	}

	/**
	 * 代理{@link Annotation#annotationType()}方法
	 */
	private Class<? extends Annotation> proxyAnnotationType() {
		return mapping.annotationType();
	}

	/**
	 * 代理{@link Proxied#getMapping()}方法
	 */
	private AnnotationMapping<T> proxyGetMapping() {
		return mapping;
	}

	/**
	 * 获取属性值
	 */
	private Object getAttributeValue(final String attributeName, final Class<?> attributeType) {
		return valueCache.computeIfAbsent(attributeName, name -> mapping.getResolvedAttributeValue(attributeName, attributeType));
	}

	/**
	 * 表明注解是一个合成的注解
	 */
	interface Proxied {

		/**
		 * 获取注解映射对象
		 *
		 * @return 注解映射对象
		 */
		AnnotationMapping<Annotation> getMapping();

	}
}
