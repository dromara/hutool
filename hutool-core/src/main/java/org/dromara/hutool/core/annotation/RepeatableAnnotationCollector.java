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

package org.dromara.hutool.core.annotation;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.WeakConcurrentMap;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.CharSequenceUtil;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 可重复注解收集器，用于从一个注解获得被它包含的可重复注解
 *
 * @author huangchengxing
 */
public interface RepeatableAnnotationCollector {

	/**
	 * 空实现
	 *
	 * @return {@code RepeatableAnnotationCollector}实例
	 */
	static RepeatableAnnotationCollector none() {
		return None.INSTANCE;
	}

	/**
	 * <p>当注解中有且仅有一个名为{@code value}的属性时，
	 * 若该属性类型为注解数组，且该数组对应的注解类型被{@link Repeatable}注解，
	 * 则收集器将返回该属性中包括的可重复注解。<br>
	 * eg:
	 * <pre><code>
	 * // 容器注解
	 * {@literal @}interface Annotation {
	 * 	Item[] value() default {};
	 * }
	 * // 可重复注解
	 * {@literal @}Repeatable(Annotation.class)
	 * {@literal @}interface Item {}
	 * </code></pre>
	 * 解析任意{@code Annotation}注解对象，则可以获得{@code value}属性中的{@code Item}注解对象
	 *
	 * @return {@code RepeatableAnnotationCollector}实例
	 * @see Standard
	 */
	static RepeatableAnnotationCollector standard() {
		return Standard.INSTANCE;
	}

	/**
	 * 当解析注解属性时，将根据给定的判断条件，确定该属性中是否含有可重复注解。<br>
	 * 收集器将返回所有匹配的属性中的可重复注解。
	 *
	 * @param predicate 是否为容纳可重复注解的属性的判断条件
	 * @return {@code RepeatableAnnotationCollector}实例
	 */
	static RepeatableAnnotationCollector condition(final BiPredicate<Annotation, Method> predicate) {
		return new Condition(predicate);
	}

	/**
	 * <p>当注解中存在有属性为注解数组，且该数组对应的注解类型被{@link Repeatable}注解时，
	 * 认为该属性包含可重复注解。<br>
	 * 收集器将返回所有符合上述条件的属性中的可重复注解。<br>
	 * eg:
	 * <pre><code>
	 * {@literal @}interface Annotation {
	 * 	Item1[] items1() default {};
	 * 	Item2[] items2() default {};
	 * }
	 * </code></pre>
	 * 解析任意{@code Annotation}注解对象，
	 * 则可以获得{@code items1}属性中的{@code Item1}注解对象，
	 * 以及{@code items2}属性中的{@code Item2}注解对象，
	 *
	 * @return {@code RepeatableAnnotationCollector}实例
	 */
	static RepeatableAnnotationCollector full() {
		return Full.INSTANCE;
	}

	/**
	 * 清空单例缓存
	 */
	static void clearSingletonCaches() {
		Standard.INSTANCE.repeatableMethodCache.clear();
		Full.INSTANCE.repeatableMethodCache.clear();
	}

	/**
	 * <p>若一个注解是可重复注解的容器注解，则尝试通过其属性获得获得包含的注解对象。
	 * 若包含的注解对象也是可重复注解的容器注解，则继续解析直到获得所有非容器注解为止。<br>
	 * eg:
	 * 若存在嵌套关系{@code a -> b -> c}，
	 * 则解析注解<em>a</em>，则将得到全部<em>c</em>注解。<br>
	 * 如果注解不包含可重复注解，则返回<em>a</em>本身。
	 *
	 * @param annotation 容器注解
	 * @return 容器注解中的可重复注解，若{@code annotation}不为容器注解，则数组中仅有其本身一个对象
	 */
	List<Annotation> getFinalRepeatableAnnotations(final Annotation annotation);

	/**
	 * <p>若一个注解是可重复注解的容器注解，则尝试通过其属性获得获得包含的注解对象。
	 * 若包含的注解对象也是可重复注解的容器注解，则继续解析直到获得所有非容器注解为止。<br>
	 * eg:
	 * 若存在嵌套关系{@code a -> b -> c}，则解析注解<em>a</em>,
	 * 将获得全部<em>a</em>、<em>b</em>、<em>c</em>注解。<br>
	 * 如果注解不包含可重复注解，则返回<em>a</em>本身。
	 *
	 * @param annotation 容器注解
	 * @return 容器注解中的可重复注解，若{@code annotation}不为容器注解，则数组中仅有其本身一个对象
	 */
	List<Annotation> getAllRepeatableAnnotations(final Annotation annotation);

	/**
	 * <p>若一个注解是可重复注解的容器注解，则尝试通过其属性获得获得包含的指定类型注解对象。<br>
	 * eg:
	 * 若存在嵌套关系{@code a -> b -> c}，则：
	 * <ul>
	 *     <li>解析注解<em>a</em>：可获得<em>a</em>、<em>b</em>、<em>c</em>；</li>
	 *     <li>解析注解<em>b</em>：可获得<em>b</em>、<em>c</em>；</li>
	 *     <li>解析注解<em>c</em>：只可获得<em>c</em>；</li>
	 * </ul>
	 *
	 * @param annotation     容器注解
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 容器注解中的可重复注解
	 */
	<T extends Annotation> List<T> getRepeatableAnnotations(final Annotation annotation, final Class<T> annotationType);

	/**
	 * 空实现
	 */
	class None implements RepeatableAnnotationCollector {

		/**
		 * 默认实例
		 */
		private static final None INSTANCE = new None();

		/**
		 * 默认返回空集合
		 *
		 * @param annotation 注解
		 * @return 空集合
		 */
		@Override
		public List<Annotation> getFinalRepeatableAnnotations(final Annotation annotation) {
			return Objects.isNull(annotation) ?
				Collections.emptyList() : Collections.singletonList(annotation);
		}

		/**
		 * 默认返回空集合
		 *
		 * @param annotation 注解
		 * @return 空集合
		 */
		@Override
		public List<Annotation> getAllRepeatableAnnotations(final Annotation annotation) {
			return Objects.isNull(annotation) ?
				Collections.emptyList() : Collections.singletonList(annotation);
		}

		/**
		 * 默认返回空集合
		 *
		 * @param annotation 注解
		 * @return 空集合
		 */
		@Override
		public <T extends Annotation> List<T> getRepeatableAnnotations(final Annotation annotation, final Class<T> annotationType) {
			if (Objects.isNull(annotation)) {
				return Collections.emptyList();
			}
			return Objects.equals(annotation.annotationType(), annotationType) ?
				Collections.singletonList(annotationType.cast(annotation)) : Collections.emptyList();
		}

	}

	/**
	 * {@code RepeatableAnnotationCollector}的基本实现
	 */
	abstract class AbstractCollector implements RepeatableAnnotationCollector {

		/**
		 * <p>若一个注解是可重复注解的容器注解，则尝试通过其属性获得获得包含的注解对象。
		 * 若包含的注解对象也是可重复注解的容器注解，则继续解析直到获得所有非容器注解为止。
		 *
		 * @param annotation 容器注解
		 * @return 容器注解中的可重复注解，若{@code annotation}不为容器注解，则数组中仅有其本身一个对象
		 */
		@Override
		public final List<Annotation> getFinalRepeatableAnnotations(final Annotation annotation) {
			return find(annotation, null, false);
		}

		/**
		 * <p>若一个注解是可重复注解的容器注解，则尝试通过其属性获得获得包含的注解对象。
		 * 若包含的注解对象也是可重复注解的容器注解，则继续解析直到获得所有非容器注解为止。<br>
		 * 当{@code accumulate}为{@code true}时，返回结果为全量的注解。
		 * eg:
		 * 若存在嵌套关系{@code a -> b -> c}，则解析注解<em>a</em>,
		 * 将获得全部<em>a</em>、<em>b</em>、<em>c</em>注解。
		 * 如果注解不包含可重复注解，则返回其本身。
		 *
		 * @param annotation 容器注解
		 * @return 容器注解中的可重复注解，若{@code annotation}不为容器注解，则数组中仅有其本身一个对象
		 */
		@Override
		public List<Annotation> getAllRepeatableAnnotations(final Annotation annotation) {
			return find(annotation, null, true);
		}

		/**
		 * 若一个注解是可重复注解的容器注解，则尝试通过其属性获得获得包含的指定类型注解对象
		 *
		 * @param annotation     容器注解
		 * @param annotationType 注解类型
		 * @param <T>            注解类型
		 * @return 容器注解中的可重复注解
		 */
		@Override
		public <T extends Annotation> List<T> getRepeatableAnnotations(
			final Annotation annotation, final Class<T> annotationType) {
			final List<Annotation> annotations = find(annotation, t -> Objects.equals(t.annotationType(), annotationType), false);
			return annotations.stream()
				.map(annotationType::cast)
				.collect(Collectors.toList());
		}

		/**
		 * 递归遍历注解，将其平铺
		 */
		private List<Annotation> find(
			final Annotation annotation, final java.util.function.Predicate<Annotation> condition, final boolean accumulate) {
			if (Objects.isNull(annotation)) {
				return Collections.emptyList();
			}
			final boolean hasCondition = Objects.nonNull(condition);
			final List<Annotation> results = new ArrayList<>();
			final Deque<Annotation> deque = new LinkedList<>();
			deque.add(annotation);
			while (!deque.isEmpty()) {
				final Annotation source = deque.removeFirst();
				final List<Method> repeatableMethods = resolveRepeatableMethod(source);
				// 若是累加的，则记录每一个注解
				if (accumulate) {
					results.add(source);
				}
				final boolean isTarget = hasCondition && condition.test(source);
				if (CollUtil.isEmpty(repeatableMethods) || isTarget) {
					// 不是累加的，则仅当正在处理的注解不为可重复注解时才记录
					final boolean shouldProcess = !accumulate && (!hasCondition || isTarget);
					if (shouldProcess) {
						results.add(source);
					}
					continue;
				}
				final Annotation[] repeatableAnnotation = repeatableMethods.stream()
					.map(method -> getRepeatableAnnotationsFormAttribute(source, method))
					.filter(ArrayUtil::isNotEmpty)
					.flatMap(Stream::of)
					.toArray(Annotation[]::new);
				if (ArrayUtil.isNotEmpty(repeatableAnnotation)) {
					CollUtil.addAll(deque, repeatableAnnotation);
				}
			}
			return results;
		}

		/**
		 * 调用{@code value}方法，获得嵌套的可重复注解
		 *
		 * @param annotation 注解对象
		 * @param method     容纳可重复注解的方法
		 * @return 可重复注解
		 * @throws ClassCastException 当{@code method}调用结果无法正确转为{@link Annotation[]}类型时抛出
		 */
		protected Annotation[] getRepeatableAnnotationsFormAttribute(final Annotation annotation, final Method method) {
			return MethodUtil.invoke(annotation, method);
		}

		/**
		 * 解析获得注解中存放可重复注解的属性
		 *
		 * @param annotation 注解
		 * @return 属性
		 */
		protected abstract List<Method> resolveRepeatableMethod(final Annotation annotation);

	}

	/**
	 * 标准实现，当注解中有且仅有一个名为{@code value}的属性时，
	 * 若该属性类型为注解数组，且该数组对应的注解类型被{@link Repeatable}注解，
	 * 则收集器将返回该属性中包括的可重复注解。
	 */
	class Standard extends AbstractCollector {

		/**
		 * 默认的value属性
		 */
		private static final String VALUE = "value";

		/**
		 * 默认实例
		 */
		private static final Standard INSTANCE = new Standard();

		/**
		 * 空方法缓存
		 */
		private static final Object NONE = new Object();

		/**
		 * 可重复注解对应的方法缓存
		 */
		private final Map<Class<? extends Annotation>, Object> repeatableMethodCache = new WeakConcurrentMap<>();

		/**
		 * 构造
		 */
		Standard() {
		}

		/**
		 * 解析获得注解中存放可重复注解的属性
		 *
		 * @param annotation 注解
		 * @return 属性
		 */
		@Override
		protected List<Method> resolveRepeatableMethod(final Annotation annotation) {
			final Object cache = repeatableMethodCache.computeIfAbsent(annotation.annotationType(), this::resolveRepeatableMethodFromType);
			return (cache == NONE) ? null : Collections.singletonList((Method)cache);
		}

		/**
		 * 从缓存中获得存放可重复注解的属性
		 */
		private Object resolveRepeatableMethodFromType(final Class<? extends Annotation> annotationType) {
			final Method[] attributes = AnnotationUtil.getAnnotationAttributes(annotationType);
			if (attributes.length != 1) {
				return NONE;
			}
			return isRepeatableMethod(attributes[0]) ? attributes[0] : NONE;
		}

		/**
		 * 判断方法是否为容器注解的{@code value}方法
		 *
		 * @param attribute  注解的属性
		 * @return 该属性是否为注解存放可重复注解的方法
		 */
		protected boolean isRepeatableMethod(final Method attribute) {
			// 属性名需为“value”
			if (!CharSequenceUtil.equals(VALUE, attribute.getName())) {
				return false;
			}
			final Class<?> attributeType = attribute.getReturnType();
			// 返回值类型需为数组
			return attributeType.isArray()
				// 且数组元素需为注解
				&& attributeType.getComponentType()
				.isAnnotation()
				// 该注解类必须被@Repeatable注解，但不要求与当前属性的声明方法一致
				&& attributeType.getComponentType()
				.isAnnotationPresent(Repeatable.class);
		}

	}

	/**
	 * 自定义判断条件的实现，当解析注解属性时，将根据给定的判断条件，
	 * 确定该属性中是否含有可重复注解，收集器将返回所有匹配的属性中的可重复注解。
	 */
	class Condition extends AbstractCollector {

		/**
		 * 是否为容纳可重复注解的属性的判断条件
		 */
		private final BiPredicate<Annotation, Method> predicate;

		/**
		 * 构造
		 *
		 * @param predicate 是否为容纳可重复注解的属性的判断条件
		 */
		Condition(final BiPredicate<Annotation, Method> predicate) {
			this.predicate = Objects.requireNonNull(predicate);
		}

		/**
		 * 解析获得注解中存放可重复注解的属性
		 *
		 * @param annotation 注解
		 * @return 属性
		 */
		@Override
		protected List<Method> resolveRepeatableMethod(final Annotation annotation) {
			return Stream.of(AnnotationUtil.getAnnotationAttributes(annotation.annotationType()))
				.filter(method -> predicate.test(annotation, method))
				.collect(Collectors.toList());
		}

	}

	/**
	 * 全量实现，当注解中存在有属性为注解数组，且该数组对应的注解类型被{@link Repeatable}注解时，
	 * 认为该属性包含可重复注解。<br>
	 * 收集器将返回所有符合上述条件的属性中的可重复注解。
	 */
	class Full extends AbstractCollector {

		/**
		 * 默认实例
		 */
		private static final Full INSTANCE = new Full();

		/**
		 * 空方法缓存
		 */
		private static final Object NONE = new Object();

		/**
		 * 可重复注解对应的方法缓存
		 */
		private final Map<Class<? extends Annotation>, Object> repeatableMethodCache = new WeakConcurrentMap<>();

		/**
		 * 构造
		 */
		Full() {
		}

		/**
		 * 解析获得注解中存放可重复注解的属性
		 *
		 * @param annotation 注解
		 * @return 属性
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected List<Method> resolveRepeatableMethod(final Annotation annotation) {
			final Object cache = repeatableMethodCache.computeIfAbsent(annotation.annotationType(), this::resolveRepeatableMethodFromType);
			return (cache == NONE) ? null : (List<Method>)cache;
		}

		/**
		 * 从缓存中获得存放可重复注解的属性
		 */
		private Object resolveRepeatableMethodFromType(final Class<? extends Annotation> annotationType) {
			final List<Method> methods = Stream.of(AnnotationUtil.getAnnotationAttributes(annotationType))
				.filter(this::isRepeatableMethod)
				.collect(Collectors.toList());
			return methods.isEmpty() ? NONE : methods;
		}

		/**
		 * 判断方法是否为容器注解的{@code value}方法
		 *
		 * @param attribute  注解的属性
		 * @return 该属性是否为注解存放可重复注解的方法
		 */
		protected boolean isRepeatableMethod(final Method attribute) {
			final Class<?> attributeType = attribute.getReturnType();
			// 返回值类型需为数组
			return attributeType.isArray()
				// 且数组元素需为注解
				&& attributeType.getComponentType()
				.isAnnotation()
				// 该注解类必须被@Repeatable注解，但不要求与当前属性的声明方法一致
				&& attributeType.getComponentType()
				.isAnnotationPresent(Repeatable.class);
		}

	}

}
