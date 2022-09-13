package cn.hutool.core.annotation;

import cn.hutool.core.stream.EasyStream;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * <p>注解元素映射，用于包装一个{@link AnnotatedElement}，然后将被包装的元素上，
 * 直接声明的注解以及这些注解的元组全部解析为{@link ResolvedAnnotationMapping}。
 * 从而用于支持对元注解的访问操作。<br>
 *
 * <p>默认情况下，总是不扫描{@link java.lang}包下的注解，
 * 并且在当前实例中，{@link Inherited}注解将不生效，
 * 即通过<em>directly</em>方法将无法获得父类上带有{@link Inherited}的注解。
 *
 * <p>当通过静态工厂方法创建时，该实例与关联的{@link ResolvedAnnotationMapping}都会针对{@link ResolvedAnnotationMapping}进行缓存，
 * 从而避免频繁的反射与代理造成不必要的性能损耗。
 *
 * @author huangchengxing
 * @see ResolvedAnnotationMapping
 * @since 6.0.0
 */
public class MetaAnnotatedElement<T extends AnnotationMapping<Annotation>> implements AnnotatedElement, Iterable<T> {

	/**
	 * 注解对象
	 */
	private final AnnotatedElement element;

	/**
	 * 创建{@link AnnotationMapping}的工厂方法，返回值为{@code null}时将忽略该注解
	 */
	private final BiFunction<T, Annotation, T> mappingFactory;

	/**
	 * 注解映射，此处为懒加载，默认为{@code null}，获取该属性必须通过{@link #getAnnotationMappings()}触发初始化
	 */
	private volatile Map<Class<? extends Annotation>, T> annotationMappings;

	/**
	 * 获取{@link AnnotatedElement}上的注解结构，该方法会针对相同的{@link AnnotatedElement}缓存映射对象
	 *
	 * @param element        被注解元素
	 * @param mappingFactory 创建{@link AnnotationMapping}的工厂方法，返回值为{@code null}时将忽略该注解
	 * @param <A>            {@link AnnotationMapping}类型
	 * @return {@link AnnotatedElement}上的注解结构
	 */
	public static <A extends AnnotationMapping<Annotation>> MetaAnnotatedElement<A> create(
		final AnnotatedElement element, final BiFunction<A, Annotation, A> mappingFactory) {
		return new MetaAnnotatedElement<>(element, mappingFactory);
	}

	/**
	 * 解析注解属性
	 *
	 * @param element        被注解元素
	 * @param mappingFactory 创建{@link AnnotationMapping}的工厂方法，返回值为{@code null}时将忽略该注解
	 */
	MetaAnnotatedElement(final AnnotatedElement element, final BiFunction<T, Annotation, T> mappingFactory) {
		this.element = Objects.requireNonNull(element);
		this.mappingFactory = Objects.requireNonNull(mappingFactory);
		// 等待懒加载
		this.annotationMappings = null;
	}

	/**
	 * 从{@link AnnotatedElement}直接声明的注解的层级结构中获得注解映射对象
	 *
	 * @param annotationType 注解类型
	 * @return 注解映射对象
	 */
	public Optional<T> getMapping(final Class<? extends Annotation> annotationType) {
		return Optional.ofNullable(annotationType)
			.map(getAnnotationMappings()::get);
	}

	/**
	 * 获取被包装的{@link AnnotatedElement}
	 *
	 * @return 被包装的{@link AnnotatedElement}
	 */
	public AnnotatedElement getElement() {
		return element;
	}

	/**
	 * 从{@link AnnotatedElement}直接声明的注解中获得注解映射对象
	 *
	 * @param annotationType 注解类型
	 * @return 注解映射对象
	 */
	public Optional<T> getDeclaredMapping(final Class<? extends Annotation> annotationType) {
		return EasyStream.of(getAnnotationMappings().values())
			.filter(T::isRoot)
			.findFirst(mapping -> ObjUtil.equals(annotationType, mapping.annotationType()));
	}

	/**
	 * 注解是否是{@link AnnotatedElement}直接声明的注解，或者在这些注解的层级结构中存在
	 *
	 * @param annotationType 注解元素
	 * @return 是否
	 */
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		return getMapping(annotationType)
			.isPresent();
	}

	/**
	 * 从{@link AnnotatedElement}直接声明的注解的层级结构中获得注解对象
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解对象
	 */
	@Override
	public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
		return getMapping(annotationType)
			.map(T::getResolvedAnnotation)
			.map(annotationType::cast)
			.orElse(null);
	}

	/**
	 * 从{@link AnnotatedElement}直接声明的注解中获得注解对象
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return 注解对象
	 */
	@Override
	public <A extends Annotation> A getDeclaredAnnotation(final Class<A> annotationType) {
		return getDeclaredMapping(annotationType)
			.map(T::getResolvedAnnotation)
			.map(annotationType::cast)
			.orElse(null);
	}

	/**
	 * 获取{@link AnnotatedElement}直接的指定类型注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return {@link AnnotatedElement}直接声明的指定类型注解
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
		final A result = getAnnotation(annotationType);
		if (Objects.nonNull(result)) {
			return (A[])new Annotation[]{ result };
		}
		return ArrayUtil.newArray(annotationType, 0);
	}

	/**
	 * 获取{@link AnnotatedElement}直接声明的指定类型注解
	 *
	 * @param annotationType 注解类型
	 * @param <A>            注解类型
	 * @return {@link AnnotatedElement}直接声明的指定类型注解
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <A extends Annotation> A[] getDeclaredAnnotationsByType(final Class<A> annotationType) {
		final A result = getDeclaredAnnotation(annotationType);
		if (Objects.nonNull(result)) {
			return (A[])new Annotation[]{ result };
		}
		return ArrayUtil.newArray(annotationType, 0);
	}

	/**
	 * 获取{@link AnnotatedElement}直接声明的注解的映射对象
	 *
	 * @return {@link AnnotatedElement}直接声明的注解的映射对象
	 */
	@Override
	public Annotation[] getDeclaredAnnotations() {
		return getAnnotationMappings().values().stream()
			.filter(T::isRoot)
			.map(T::getResolvedAnnotation)
			.toArray(Annotation[]::new);
	}

	/**
	 * 获取所有注解
	 *
	 * @return 所有注解
	 */
	@Override
	public Annotation[] getAnnotations() {
		return getAnnotationMappings().values().stream()
			.map(T::getResolvedAnnotation)
			.toArray(Annotation[]::new);
	}

	/**
	 * 获取注解映射对象集合的迭代器
	 *
	 * @return 迭代器
	 */
	@Override
	public Iterator<T> iterator() {
		return getAnnotationMappings().values().iterator();
	}

	// ========================= protected =========================

	/**
	 * 获取注解映射，若当前实例未完成初始化则先进行初始化
	 *
	 * @return 不可变的注解映射集合
	 */
	protected final Map<Class<? extends Annotation>, T> getAnnotationMappings() {
		initAnnotationMappingsIfNecessary();
		return annotationMappings;
	}

	/**
	 * 该注解是否需要映射 <br>
	 * 默认情况下，已经处理过、或在{@link java.lang}包下的注解不会被处理
	 *
	 * @param mappings   当前已处理的注解
	 * @param annotation 注解对象
	 * @return 是否
	 */
	protected boolean isNeedMapping(final Map<Class<? extends Annotation>, T> mappings, final Annotation annotation) {
		return !CharSequenceUtil.startWith(annotation.annotationType().getName(), "java.lang.")
			&& !mappings.containsKey(annotation.annotationType());
	}

	// ========================= private =========================

	/**
	 * 创建注解映射
	 */
	private T createMapping(final T source, final Annotation annotation) {
		return mappingFactory.apply(source, annotation);
	}

	/**
	 * 扫描{@link AnnotatedElement}上直接声明的注解，然后按广度优先扫描这些注解的元注解，
	 * 直到将所有类型的注解对象皆加入{@link #annotationMappings}为止
	 */
	private void initAnnotationMappingsIfNecessary() {
		// 双重检查保证初始化过程线程安全
		if (Objects.isNull(annotationMappings)) {
			synchronized (this) {
				if (Objects.isNull(annotationMappings)) {
					Map<Class<? extends Annotation>, T> mappings = new LinkedHashMap<>(8);
					initAnnotationMappings(mappings);
					this.annotationMappings = Collections.unmodifiableMap(mappings);
				}
			}
		}
	}

	/**
	 * 初始化
	 */
	private void initAnnotationMappings(final Map<Class<? extends Annotation>, T> mappings) {
		final Deque<T> deque = new LinkedList<>();
		Arrays.stream(element.getDeclaredAnnotations())
			.filter(m -> isNeedMapping(mappings, m))
			.map(annotation -> createMapping(null, annotation))
			.filter(Objects::nonNull)
			.forEach(deque::addLast);
		while (!deque.isEmpty()) {
			// 若已有该类型的注解，则不再进行扫描
			T mapping = deque.removeFirst();
			if (!isNeedMapping(mappings, mapping)) {
				continue;
			}
			// 保存该注解，并将其需要处理的元注解也加入队列
			mappings.put(mapping.annotationType(), mapping);
			Stream.of(mapping.annotationType().getDeclaredAnnotations())
				.map(annotation -> createMapping(mapping, annotation))
				.filter(Objects::nonNull)
				.filter(m -> isNeedMapping(mappings, m))
				.forEach(deque::addLast);
		}
	}

	/**
	 * 比较两个实例是否相等
	 *
	 * @param o 对象
	 * @return 是否
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MetaAnnotatedElement<?> that = (MetaAnnotatedElement<?>)o;
		return element.equals(that.element) && mappingFactory.equals(that.mappingFactory);
	}

	/**
	 * 获取实例的哈希值
	 *
	 * @return 哈希值
	 */

	@Override
	public int hashCode() {
		return Objects.hash(element, mappingFactory);
	}
}
