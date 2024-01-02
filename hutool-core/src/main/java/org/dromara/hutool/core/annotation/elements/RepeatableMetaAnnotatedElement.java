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

import org.dromara.hutool.core.annotation.AnnotationMapping;
import org.dromara.hutool.core.annotation.AnnotationUtil;
import org.dromara.hutool.core.annotation.RepeatableAnnotationCollector;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.CharSequenceUtil;
import org.dromara.hutool.core.array.ArrayUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * <p>支持可重复注解的增强{@link AnnotatedElement}，
 * 功能与{@link MetaAnnotatedElement}类似，但是存在下述差异：
 * <ul>
 *     <li>
 *         限制以同一根注解延伸出的树结构上——而不是{@link AnnotatedElement}上——每种类型注解只能保留一个，
 *         即当{@link AnnotatedElement}存在多个根注解有相同的元注解时，这些元注解会都会被扫描到；
 *     </li>
 *     <li>
 *         支持扫描{@link AnnotatedElement}可重复注解，即当当前实例指定的{@link RepeatableAnnotationCollector}
 *         支持从{@link AnnotatedElement}上直接声明的注解中获得可重复注解时，
 *         则将会自动将其展开直到不为容器注解为止。<br>
 *         eg：<br>
 *         A上存在注解<em>X</em>，该注解是一个容器注解，内部可重复注解<em>Y</em>，
 *         包含解析后，得到注解<em>X</em>与可重复注解<em>Y</em>,<br>
 *         同理，若存在<em>X</em>、<em>Y</em>、<em>X</em>的嵌套关系，则解析后获得全部三者；
 *     </li>
 * </ul>
 * 由于上述机制，当通过实例的{@link #getAnnotation(Class)}或{@link #getDeclaredAnnotation(Class)}
 * 方法获得指定类型注解时，若该类型注解存在多个，仅能尽可能获得最先被扫描到的那一个。
 *
 * @author huangchengxing
 * @since 6.0.0
 * @see RepeatableAnnotationCollector
 * @param <T> AnnotationMapping类型
 */
public class RepeatableMetaAnnotatedElement<T extends AnnotationMapping<Annotation>> implements AnnotatedElement, Iterable<T> {

	/**
	 * 包装的{@link AnnotatedElement}对象
	 */
	private final AnnotatedElement element;

	/**
	 * 创建{@link AnnotationMapping}的工厂方法
	 */
	private final BiFunction<T, Annotation, T> mappingFactory;

	/**
	 * 解析得到的根注解与元注解的聚合体
	 */
	private final List<Aggregation> aggregations;

	/**
	 * 可重复注解收集器
	 */
	private final RepeatableAnnotationCollector repeatableCollector;

	/**
	 * 获取{@link AnnotatedElement}上的注解结构，该方法会针对相同的{@link AnnotatedElement}缓存映射对象
	 *
	 * @param element        被注解元素
	 * @param mappingFactory 创建{@link AnnotationMapping}的工厂方法，返回值为{@code null}时将忽略该注解
	 * @param <A>            {@link AnnotationMapping}类型
	 * @return {@link AnnotatedElement}上的注解结构
	 */
	public static <A extends AnnotationMapping<Annotation>> RepeatableMetaAnnotatedElement<A> create(
		final AnnotatedElement element, final BiFunction<A, Annotation, A> mappingFactory) {
		return create(RepeatableAnnotationCollector.standard(), element, mappingFactory);
	}

	/**
	 * 获取{@link AnnotatedElement}上的注解结构，该方法会针对相同的{@link AnnotatedElement}缓存映射对象
	 *
	 * @param collector      可重复注解收集器
	 * @param element        被注解元素
	 * @param mappingFactory 创建{@link AnnotationMapping}的工厂方法，返回值为{@code null}时将忽略该注解
	 * @param <A>            {@link AnnotationMapping}类型
	 * @return {@link AnnotatedElement}上的注解结构
	 */
	public static <A extends AnnotationMapping<Annotation>> RepeatableMetaAnnotatedElement<A> create(
		final RepeatableAnnotationCollector collector,
		final AnnotatedElement element,
		final BiFunction<A, Annotation, A> mappingFactory) {
		return new RepeatableMetaAnnotatedElement<>(collector, element, mappingFactory);
	}

	/**
	 * 创建一个支持可重复注解的增强{@link AnnotatedElement}
	 *
	 * @param element        包装的{@link AnnotatedElement}对象
	 * @param mappingFactory 创建{@link AnnotationMapping}的工厂方法
	 */
	RepeatableMetaAnnotatedElement(
		final RepeatableAnnotationCollector repeatableCollector, final AnnotatedElement element, final BiFunction<T, Annotation, T> mappingFactory) {
		this.element = Objects.requireNonNull(element);
		this.mappingFactory = Objects.requireNonNull(mappingFactory);
		this.repeatableCollector = repeatableCollector;
		this.aggregations = Collections.unmodifiableList(initAggregations(element));
	}

	/**
	 * 指定注解是否在{@link #element}上直接声明的注解、直接声明的注解包含的可重复注解，
	 * 以及他们的元注解中存在
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	@Override
	public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {
		return aggregations.stream()
			.anyMatch(aggregation -> aggregation.getMappings().containsKey(annotationType));
	}

	/**
	 * 从{@link #element}上直接声明的注解、直接声明的注解包含的可重复注解，以及它们的元注解中获得指定类型的注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解
	 */
	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
		return aggregations.stream()
			.map(Aggregation::getMappings)
			.map(annotations -> annotations.get(annotationType))
			.filter(Objects::nonNull)
			.findFirst()
			.map(T::getResolvedAnnotation)
			.map(annotationType::cast)
			.orElse(null);
	}

	/**
	 * 获取{@link #element}上直接声明的注解、直接声明的注解包含的可重复注解，以及它们的元注解
	 *
	 * @return 注解
	 */
	@Override
	public Annotation[] getAnnotations() {
		return aggregations.stream()
			.map(aggregation -> aggregation.getMappings().values())
			.flatMap(Collection::stream)
			.map(T::getResolvedAnnotation)
			.toArray(Annotation[]::new);
	}

	/**
	 * 从{@link #element}上直接声明的注解、直接声明的注解包含的可重复注解，以及它们的元注解中获得指定类型的注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解
	 */
	@Override
	public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
		return aggregations.stream()
			.map(aggregation -> aggregation.getMappings().get(annotationType))
			.filter(Objects::nonNull)
			.map(T::getResolvedAnnotation)
			.map(annotationType::cast)
			.toArray(size -> ArrayUtil.newArray(annotationType, size));
	}

	/**
	 * 获取由{@link #element}直接声明的注解，不包含被直接声明的容器注解包括的可重复注解
	 *
	 * @return 注解
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return aggregations.stream()
			.filter(Aggregation::isDirect)
			.map(Aggregation::getRoot)
			.map(T::getResolvedAnnotation)
			.toArray(Annotation[]::new);
	}

	/**
	 * 获取由{@link #element}直接声明的注解，不包含被直接声明的容器注解包括的可重复注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解
	 */
	@Override
	public <A extends Annotation> A getDeclaredAnnotation(final Class<A> annotationType) {
		return aggregations.stream()
			.filter(Aggregation::isDirect)
			.map(Aggregation::getRoot)
			.filter(annotation -> Objects.equals(annotationType, annotation.annotationType()))
			.findFirst()
			.map(T::getResolvedAnnotation)
			.map(annotationType::cast)
			.orElse(null);
	}

	/**
	 * 获取由{@link #element}直接声明的注解，不包含被直接声明的容器注解包括的可重复注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解
	 */
	@Override
	public <A extends Annotation> A[] getDeclaredAnnotationsByType(final Class<A> annotationType) {
		return aggregations.stream()
			.filter(Aggregation::isDirect)
			.map(Aggregation::getRoot)
			.filter(annotation -> Objects.equals(annotationType, annotation.annotationType()))
			.map(T::getResolvedAnnotation)
			.map(annotationType::cast)
			.toArray(size -> ArrayUtil.newArray(annotationType, size));
	}

	/**
	 * 注解对象
	 *
	 * @return 被包装的原始元素
	 */
	public AnnotatedElement getElement() {
		return element;
	}

	/**
	 * 比较两个实例是否相等
	 *
	 * @param o 对象
	 * @return 是否
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final RepeatableMetaAnnotatedElement<?> that = (RepeatableMetaAnnotatedElement<?>)o;
		return element.equals(that.element) && mappingFactory.equals(that.mappingFactory) && repeatableCollector.equals(that.repeatableCollector);
	}

	/**
	 * 获取实例的哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(element, mappingFactory, repeatableCollector);
	}

	/**
	 * 获取迭代器
	 *
	 * @return 迭代器
	 */
	@Override
	public Iterator<T> iterator() {
		return aggregations.stream()
			.map(Aggregation::getMappings)
			.map(Map::values)
			.flatMap(Collection::stream)
			.iterator();
	}

	/**
	 * 初始化
	 */
	private List<Aggregation> initAggregations(final AnnotatedElement element) {
		// TODO 若有可能，一并支持处理元注解中的可重复注解
		final List<Aggregation> result = new ArrayList<>();
		for (final Annotation declaredAnnotation : AnnotationUtil.getDeclaredAnnotations(element)) {
			final List<Aggregation> repeatableAnnotations = collectRepeatable(declaredAnnotation);
			if (CollUtil.isNotEmpty(repeatableAnnotations)) {
				result.addAll(repeatableAnnotations);
			}
		}
		return result;
	}

	/**
	 * 若当前注解是可重复注解的容器，则将其平铺后把可重复注解加入{@link #aggregations}
	 */
	private List<Aggregation> collectRepeatable(final Annotation annotation) {
		return repeatableCollector.getAllRepeatableAnnotations(annotation)
			.stream()
			.map(a -> new Aggregation(a, Objects.equals(a, annotation)))
			.collect(Collectors.toList());
	}

	/**
	 * 由根注解与其元注解构成的注解聚合
	 */
	class Aggregation {

		/**
		 * 根注解
		 */
		private final T root;

		/**
		 * 注解
		 */
		private volatile Map<Class<? extends Annotation>, T> mappings;

		/**
		 * 是否是由{@link #element}直接声明的注解
		 */
		private final boolean isDirect;

		/**
		 * 创建一个合并聚合
		 */
		public Aggregation(final Annotation root, final boolean isDirect) {
			this.root = mappingFactory.apply(null, root);
			this.isDirect = isDirect;
		}

		/**
		 * 获得收集到的注解
		 */
		private Map<Class<? extends Annotation>, T> getMappings() {
			if (Objects.isNull(mappings)) {
				synchronized (this) {
					if (Objects.isNull(mappings)) {
						mappings = Collections.unmodifiableMap(initMetaAnnotations());
					}
				}
			}
			return mappings;
		}

		/**
		 * 获得注解及其元注解
		 */
		private Map<Class<? extends Annotation>, T> initMetaAnnotations() {
			final Map<Class<? extends Annotation>, T> collectedMappings = new LinkedHashMap<>();
			final Deque<T> deque = new LinkedList<>();
			deque.add(root);
			while (!deque.isEmpty()) {
				final T source = deque.removeFirst();
				if (!isNeedMapping(collectedMappings, source)) {
					continue;
				}
				collectedMappings.put(source.annotationType(), source);
				for (final Annotation annotation : AnnotationUtil.getDeclaredAnnotations(source.annotationType())) {
					if (collectedMappings.containsKey(annotation.annotationType())) {
						continue;
					}
					final T mapping = mappingFactory.apply(source, annotation);
					if (Objects.nonNull(mapping) && isNeedMapping(collectedMappings, mapping)) {
						deque.addLast(mapping);
					}
				}
			}
			return collectedMappings;
		}

		/**
		 * 该注解是否需要映射 <br>
		 * 默认情况下，已经处理过、或在{@link java.lang}包下的注解不会被处理
		 */
		private boolean isNeedMapping(final Map<Class<? extends Annotation>, T> mappings, final Annotation annotation) {
			return !CharSequenceUtil.startWith(annotation.annotationType().getName(), "java.lang.")
				&& !mappings.containsKey(annotation.annotationType());
		}

		/**
		 * 根注解是否由{@link #element}直接声明
		 *
		 * @return 是否
		 */
		public boolean isDirect() {
			return isDirect;
		}

		/**
		 * 获取根注解
		 *
		 * @return 根注解
		 */
		public T getRoot() {
			return root;
		}
	}

}
