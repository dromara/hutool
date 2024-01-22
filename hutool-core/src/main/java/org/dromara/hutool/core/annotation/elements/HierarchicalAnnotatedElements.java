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
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.method.MethodUtil;
import org.dromara.hutool.core.text.CharSequenceUtil;
import org.dromara.hutool.core.array.ArrayUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * <p>表示一组处于在层级结构中具有关联关系的{@link AnnotatedElement}，创建实例时，
 * 将扫描指定{@link AnnotatedElement}的层级结构中的所有{@link AnnotatedElement},
 * 并将其包装为{@link MetaAnnotatedElement}。 <br>
 * eg: <br>
 * 若存在元素<em>A</em>有对应父类与父接口<em>B</em>，<em>C</em>，
 * 则根据<em>A</em>生成的{@code HierarchicalAnnotatedElements}实例将同时包含<em>A</em>，<em>B</em>，<em>C</em>,
 * 该实例同时支持对这三个实例上直接声明的注解，以及这些注解的元注解进行访问。
 *
 * <p><strong>注解搜索范围</strong>
 * <p>在当前实例中，针对带有和不带<em>declared</em>关键字的方法定义如下：
 * <ul>
 *     <li>当方法带有<em>declared</em>关键字时，查找范围仅限被保存的所有{@link AnnotatedElement}上直接声明的注解；</li>
 *     <li>
 *         当方法不带<em>declared</em>关键字时，查找范围包括：
 *         <ol>
 *             <li>被保存的所有{@link AnnotatedElement}上直接声明的注解，及这些注解的元注解；</li>
 *             <li>若是类，则包括其所有父类和所有父接口上声明的注解和元注解；</li>
 *             <li>
 *                 若是方法，且不是静态/私有/被{@code final}修饰的方法时，
 *                 则额外获取包括其声明类的所有父类和所有父接口中，与该方法具有相同方法签名的方法上的注解和元注解；
 *             </li>
 *         </ol>
 *     </li>
 * </ul>
 *
 * <p><strong>扫描顺序</strong>
 * <p>当{@link AnnotatedElement}具有层级结构式，会按照广度优先扫描其本身(元素是{@link Class})、
 * 或其声明类(元素是{@link Method})的层级结构。<br>
 * 在该过程中，总是先扫描父类，再扫描父接口，
 * 若存在多个父接口，则其扫描顺序遵循从{@link Class#getInterfaces()}获得该接口的顺序。
 *
 * @author huangchengxing
 * @since 6.0.0
 */
public class HierarchicalAnnotatedElements implements AnnotatedElement, Iterable<AnnotatedElement> {

	/**
	 * 创建{@link AnnotatedElement}的工厂方法，当返回{@code null}时将忽略该元素
	 */
	protected final BiFunction<Set<AnnotatedElement>, AnnotatedElement, AnnotatedElement> elementFactory;

	/**
	 * 层级中的全部{@link AnnotatedElement}对象，默认为懒加载，需要通过{@link #getElementMappings()}触发初始化 <br>
	 * 该集合中的元素按照其与被包装的{@link AnnotatedElement}的距离和被按广度优先扫描的顺序排序
	 */
	private volatile Set<AnnotatedElement> elementMappings;

	/**
	 * 被包装的{@link AnnotatedElement}对象
	 */
	protected final AnnotatedElement source;

	/**
	 * 创建一个分层注解元素
	 *
	 * @param element 被包装的元素，若元素已是{@code HierarchicalAnnotatedElements}，则返回其本身
	 * @return {@code HierarchicalAnnotatedElements}实例，
	 * 当{@code element}也是一个{@code HierarchicalAnnotatedElements}时，返回{@code element}本身
	 */
	public static HierarchicalAnnotatedElements of(final AnnotatedElement element) {
		return of(element, (es, e) -> e);
	}

	/**
	 * 创建一个分层注解元素
	 *
	 * @param element        被包装的元素，若元素已是{@code HierarchicalAnnotatedElements}，则返回其本身
	 * @param elementFactory 创建{@link AnnotatedElement}的工厂方法，当返回{@code null}时将忽略该元素
	 * @return {@code HierarchicalAnnotatedElements}实例，
	 * 当{@code element}也是一个{@code HierarchicalAnnotatedElements}时，返回{@code element}本身
	 */
	public static HierarchicalAnnotatedElements of(
		final AnnotatedElement element,
		final BiFunction<Set<AnnotatedElement>, AnnotatedElement, AnnotatedElement> elementFactory) {
		return element instanceof HierarchicalAnnotatedElements ?
			(HierarchicalAnnotatedElements)element : new HierarchicalAnnotatedElements(element, elementFactory);
	}

	/**
	 * 构造
	 *
	 * @param element        被包装的元素
	 * @param elementFactory 创建{@link AnnotatedElement}的工厂方法，当返回{@code null}时将忽略该元素
	 */
	HierarchicalAnnotatedElements(
		final AnnotatedElement element,
		final BiFunction<Set<AnnotatedElement>, AnnotatedElement, AnnotatedElement> elementFactory) {
		this.source = Objects.requireNonNull(element);
		// 懒加载
		this.elementMappings = null;
		this.elementFactory = Objects.requireNonNull(elementFactory);
	}

	/**
	 * 注解是否在层级结构中所有{@link AnnotatedElement}上的注解和元注解中存在
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	@Override
	public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {
		return getElementMappings().stream()
			.anyMatch(element -> element.isAnnotationPresent(annotationType));
	}

	/**
	 * 从层级结构中所有{@link AnnotatedElement}上的注解和元注解中获取指定类型的注解
	 *
	 * @return 注解对象
	 */
	@Override
	public Annotation[] getAnnotations() {
		return getElementMappings().stream()
			.map(AnnotatedElement::getAnnotations)
			.filter(ArrayUtil::isNotEmpty)
			.flatMap(Stream::of)
			.toArray(Annotation[]::new);
	}

	/**
	 * 从层级结构中所有{@link AnnotatedElement}上的注解和元注解中获取指定类型的注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解对象
	 */
	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
		return getElementMappings().stream()
			.map(e -> e.getAnnotation(annotationType))
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(null);
	}

	/**
	 * 从层级结构中所有{@link AnnotatedElement}上的注解和元注解中获取指定类型的注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解对象
	 */
	public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
		return getElementMappings().stream()
			.map(e -> e.getAnnotationsByType(annotationType))
			.filter(ArrayUtil::isNotEmpty)
			.flatMap(Stream::of)
			.toArray(size -> ArrayUtil.newArray(annotationType, size));
	}

	/**
	 * 获取层级结构中所有{@link AnnotatedElement}上直接声明的注解
	 *
	 * @return 注解对象
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return getElementMappings().stream()
			.map(AnnotationUtil::getDeclaredAnnotations)
			.filter(ArrayUtil::isNotEmpty)
			.flatMap(Stream::of)
			.toArray(Annotation[]::new);
	}

	/**
	 * 获取层级结构中所有{@link AnnotatedElement}上直接声明的指定类型注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解对象
	 */
	@Override
	public <A extends Annotation> A getDeclaredAnnotation(final Class<A> annotationType) {
		return getElementMappings().stream()
			.map(element -> element.getDeclaredAnnotation(annotationType))
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(null);
	}

	/**
	 * 获取层级结构中所有{@link AnnotatedElement}上直接声明的指定类型注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解对象
	 */
	@Override
	public <A extends Annotation> A[] getDeclaredAnnotationsByType(final Class<A> annotationType) {
		return getElementMappings().stream()
			.map(element -> element.getDeclaredAnnotationsByType(annotationType))
			.filter(ArrayUtil::isNotEmpty)
			.flatMap(Stream::of)
			.toArray(size -> ArrayUtil.newArray(annotationType, size));
	}

	/**
	 * 获取注解元素映射集合的迭代器
	 *
	 * @return 迭代器
	 */
	@Override
	public Iterator<AnnotatedElement> iterator() {
		return getElementMappings().iterator();
	}

	/**
	 * 获取被包装的原始{@link AnnotatedElement}对象
	 *
	 * @return 注解对象
	 */
	public AnnotatedElement getElement() {
		return source;
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
		final HierarchicalAnnotatedElements that = (HierarchicalAnnotatedElements)o;
		return elementFactory.equals(that.elementFactory) && source.equals(that.source);
	}

	/**
	 * 获取实例的哈希值
	 *
	 * @return 哈希值
	 */
	@Override
	public int hashCode() {
		return Objects.hash(elementFactory, source);
	}

	// ========================= protected =========================

	/**
	 * 获取当前元素及层级结构中的关联元素的映射对象，结果只读
	 *
	 * @return 元素映射对象
	 */
	public final Set<AnnotatedElement> getElementMappings() {
		initElementMappingsIfNecessary();
		return this.elementMappings;
	}

	/**
	 * 检验方法的签名是否与原始方法匹配
	 *
	 * @param source 原始的方法
	 * @param target 比较的方法
	 * @return 是否
	 */
	protected boolean isMatchMethod(final Method source, final Method target) {
		return CharSequenceUtil.equals(source.getName(), target.getName())
			// 不可为桥接方法或者合成方法
			&& !target.isBridge() && !target.isSynthetic()
			// 返回值需可通过原始方法的返回值转换得到
			&& ClassUtil.isAssignable(target.getReturnType(), source.getReturnType())
			// 参数数量必须一致，且类型也必须严格一致，但不检验泛型
			&& Arrays.equals(source.getParameterTypes(), target.getParameterTypes());
	}

	// ========================= private =========================

	/**
	 * 将元素转为{@link MetaAnnotatedElement}后添加至{@code mappings}
	 */
	private void collectElement(final Set<AnnotatedElement> elements, final AnnotatedElement element) {
		final AnnotatedElement target = elementFactory.apply(elements, element);
		if (Objects.nonNull(target)) {
			elements.add(target);
		}
	}

	/**
	 * 遍历层级结构，获取层级结构中所有关联的{@link AnnotatedElement}，并添加到{@link #elementMappings}
	 */
	private void initElementMappingsIfNecessary() {
		// 双重检查保证初始化过程线程安全
		if (Objects.isNull(elementMappings)) {
			synchronized (this) {
				if (Objects.isNull(elementMappings)) {
					final Set<AnnotatedElement> mappings = initElementMappings();
					elementMappings = SetUtil.view(mappings);
				}
			}
		}
	}

	/**
	 * 遍历层级结构，获取层级结构中所有关联的{@link AnnotatedElement}，并添加到{@link #elementMappings}
	 */
	private Set<AnnotatedElement> initElementMappings() {
		final Set<AnnotatedElement> mappings = new LinkedHashSet<>();
		// 原始元素是类
		if (source instanceof Class) {
			scanHierarchy(mappings, (Class<?>)source, false, source);
		}
		// 原始元素是方法
		else if (source instanceof Method) {
			final Method methodSource = (Method)source;
			// 静态、私有与被final关键字修饰方法无法被子类重写，因此不可能具有层级结构
			if (Modifier.isPrivate(methodSource.getModifiers())
				|| Modifier.isFinal(methodSource.getModifiers())
				|| Modifier.isStatic(methodSource.getModifiers())) {
				collectElement(mappings, methodSource);
			} else {
				scanHierarchy(mappings, methodSource.getDeclaringClass(), true, methodSource);
			}
		}
		return mappings;
	}

	/**
	 * 按广度优先，遍历{@code type}的父类以及父接口，并从类上/类中指定方法上获得所需的注解
	 */
	private void scanHierarchy(
			final Set<AnnotatedElement> mappings, Class<?> type, final boolean isMethod, final AnnotatedElement source) {
		final Method methodSource = isMethod ? (Method)source : null;
		final Deque<Class<?>> deque = new LinkedList<>();
		deque.addLast(type);
		final Set<Class<?>> accessed = new HashSet<>();
		while (!deque.isEmpty()) {
			type = deque.removeFirst();
			// 已访问过的类不再处理
			if (!isNeedMapping(type, accessed)) {
				continue;
			}
			// 收集元素
			if (!isMethod) {
				collectElement(mappings, type);
			} else {
				Stream.of(MethodUtil.getDeclaredMethods(type))
					.filter(method -> isMatchMethod(methodSource, method))
					.forEach(method -> collectElement(mappings, method));
			}
			// 获取父类与父接口
			accessed.add(type);
			deque.addLast(type.getSuperclass());
			CollUtil.addAll(deque, type.getInterfaces());
		}
	}

	/**
	 * 是否需要处理该类，不符合任意一点则不处理：
	 * <ul>
	 *     <li>该类不为{@code null}；</li>
	 *     <li>该类不为在{@code accessedTypes}中不存在；</li>
	 *     <li>该类不为{@link Object}；</li>
	 * </ul>
	 */
	private boolean isNeedMapping(final Class<?> type, final Set<Class<?>> accessedTypes) {
		return Objects.nonNull(type)
			&& !accessedTypes.contains(type)
			&& !Objects.equals(type, Object.class);
	}

}
