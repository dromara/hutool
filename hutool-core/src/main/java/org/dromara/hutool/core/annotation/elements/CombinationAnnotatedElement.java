/*
 * Copyright (c) 2023-2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.annotation.elements;

import org.dromara.hutool.core.annotation.AnnotationUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.map.TableMap;

import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 组合注解 对JDK的原生注解机制做一个增强，支持类似Spring的组合注解。<br>
 * 核心实现使用了递归获取指定元素上的注解以及注解的注解，以实现复合注解的获取。
 *
 * @author Succy, Looly
 * @since 4.0.9
 **/

public class CombinationAnnotatedElement implements AnnotatedElement, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建CombinationAnnotationElement
	 *
	 * @param element   需要解析注解的元素：可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param predicate 过滤器，{@link Predicate#test(Object)}返回{@code true}保留，否则不保留
	 * @return CombinationAnnotationElement
	 * @since 5.8.0
	 */
	public static CombinationAnnotatedElement of(final AnnotatedElement element, final Predicate<Annotation> predicate) {
		return new CombinationAnnotatedElement(element, predicate);
	}

	/**
	 * 元注解
	 */
	private static final Set<Class<? extends Annotation>> META_ANNOTATIONS = SetUtil.of(
		Target.class, //
		Retention.class, //
		Inherited.class, //
		Documented.class, //
		SuppressWarnings.class, //
		Override.class, //
		Deprecated.class//
	);

	/**
	 * 注解类型与注解对象对应表
	 */
	private Map<Class<? extends Annotation>, Annotation> annotationMap;
	/**
	 * 直接注解类型与注解对象对应表
	 */
	private Map<Class<? extends Annotation>, Annotation> declaredAnnotationMap;
	/**
	 * 过滤器
	 */
	private final Predicate<Annotation> predicate;

	/**
	 * 构造
	 *
	 * @param element 需要解析注解的元素：可以是Class、Method、Field、Constructor、ReflectPermission
	 */
	public CombinationAnnotatedElement(final AnnotatedElement element) {
		this(element, null);
	}

	/**
	 * 构造
	 *
	 * @param element   需要解析注解的元素：可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param predicate 过滤器，{@link Predicate#test(Object)}返回{@code true}保留，否则不保留
	 * @since 5.8.0
	 */
	public CombinationAnnotatedElement(final AnnotatedElement element, final Predicate<Annotation> predicate) {
		this.predicate = predicate;
		init(element);
	}

	@Override
	public boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
		return annotationMap.containsKey(annotationClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
		final Annotation annotation = annotationMap.get(annotationClass);
		return (annotation == null) ? null : (T) annotation;
	}

	@Override
	public Annotation[] getAnnotations() {
		final Collection<Annotation> annotations = this.annotationMap.values();
		return annotations.toArray(new Annotation[0]);
	}

	@Override
	public Annotation[] getDeclaredAnnotations() {
		final Collection<Annotation> annotations = this.declaredAnnotationMap.values();
		return annotations.toArray(new Annotation[0]);
	}

	/**
	 * 初始化
	 *
	 * @param element 元素
	 */
	private void init(final AnnotatedElement element) {
		final Annotation[] declaredAnnotations = AnnotationUtil.getDeclaredAnnotations(element);
		this.declaredAnnotationMap = new TableMap<>();
		parseDeclared(declaredAnnotations);

		final Annotation[] annotations = element.getAnnotations();
		if (Arrays.equals(declaredAnnotations, annotations)) {
			this.annotationMap = this.declaredAnnotationMap;
		} else {
			this.annotationMap = new TableMap<>();
			parse(annotations);
		}
	}

	/**
	 * 进行递归解析注解，直到全部都是元注解为止
	 *
	 * @param annotations Class, Method, Field等
	 */
	private void parseDeclared(final Annotation[] annotations) {
		Class<? extends Annotation> annotationType;
		// 直接注解
		for (final Annotation annotation : annotations) {
			annotationType = annotation.annotationType();
			if (!META_ANNOTATIONS.contains(annotationType)
				// issue#I5FQGW@Gitee：跳过元注解和已经处理过的注解，防止递归调用
				&& !declaredAnnotationMap.containsKey(annotationType)) {
				if (test(annotation)) {
					declaredAnnotationMap.put(annotationType, annotation);
				}
				// 测试不通过的注解，不影响继续递归
				parseDeclared(AnnotationUtil.getDeclaredAnnotations(annotationType));
			}
		}
	}

	/**
	 * 进行递归解析注解，直到全部都是元注解为止
	 *
	 * @param annotations Class, Method, Field等
	 */
	private void parse(final Annotation[] annotations) {
		Class<? extends Annotation> annotationType;
		for (final Annotation annotation : annotations) {
			annotationType = annotation.annotationType();
			if (!META_ANNOTATIONS.contains(annotationType)
				// issue#I5FQGW@Gitee：跳过元注解和已经处理过的注解，防止递归调用
				&& !annotationMap.containsKey(annotationType)) {
				if (test(annotation)) {
					annotationMap.put(annotationType, annotation);
				}
				// 测试不通过的注解，不影响继续递归
				parse(annotationType.getAnnotations());
			}
		}
	}

	/**
	 * 检查给定的注解是否符合过滤条件
	 *
	 * @param annotation 注解对象
	 * @return 是否符合条件
	 */
	private boolean test(final Annotation annotation) {
		return null == this.predicate || this.predicate.test(annotation);
	}
}
