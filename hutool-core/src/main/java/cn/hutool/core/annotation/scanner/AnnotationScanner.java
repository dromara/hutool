package cn.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>注解扫描器，用于从支持的可注解元素上获取所需注解
 *
 * <p>默认提供了以下扫描方式：
 * <ul>
 *     <li>{@link #NOTHING}：什么都不做，什么注解都不扫描；</li>
 *     <li>{@link #DIRECTLY}：扫描元素本身直接声明的注解，包括父类带有{@link Inherited}、被传递到元素上的注解；</li>
 *     <li>
 *         {@link #DIRECTLY_AND_META_ANNOTATION}：扫描元素本身直接声明的注解，包括父类带有{@link Inherited}、被传递到元素上的注解，
 *         以及这些注解的元注解；
 *     </li>
 *     <li>{@link #SUPERCLASS}：扫描元素本身以及父类的层级结构中声明的注解；</li>
 *     <li>{@link #SUPERCLASS_AND_META_ANNOTATION}：扫描元素本身以及父类的层级结构中声明的注解，以及这些注解的元注解；</li>
 *     <li>{@link #INTERFACE}：扫描元素本身以及父接口的层级结构中声明的注解；</li>
 *     <li>{@link #INTERFACE_AND_META_ANNOTATION}：扫描元素本身以及父接口的层级结构中声明的注解，以及这些注解的元注解；</li>
 *     <li>{@link #TYPE_HIERARCHY}：扫描元素本身以及父类、父接口的层级结构中声明的注解；</li>
 *     <li>{@link #TYPE_HIERARCHY_AND_META_ANNOTATION}：扫描元素本身以及父接口、父接口的层级结构中声明的注解，以及这些注解的元注解；</li>
 * </ul>
 *
 * @author huangchengxing
 * @see TypeAnnotationScanner
 * @see MethodAnnotationScanner
 * @see FieldAnnotationScanner
 * @see MetaAnnotationScanner
 * @see ElementAnnotationScanner
 * @see GenericAnnotationScanner
 */
public interface AnnotationScanner {

	// ============================ 预置的扫描器实例 ============================

	/**
	 * 不扫描任何注解
	 */
	AnnotationScanner NOTHING = new EmptyAnnotationScanner();

	/**
	 * 扫描元素本身直接声明的注解，包括父类带有{@link Inherited}、被传递到元素上的注解的扫描器
	 */
	AnnotationScanner DIRECTLY = new GenericAnnotationScanner(false, false, false);

	/**
	 * 扫描元素本身直接声明的注解，包括父类带有{@link Inherited}、被传递到元素上的注解，以及这些注解的元注解的扫描器
	 */
	AnnotationScanner DIRECTLY_AND_META_ANNOTATION = new GenericAnnotationScanner(true, false, false);

	/**
	 * 扫描元素本身以及父类的层级结构中声明的注解的扫描器
	 */
	AnnotationScanner SUPERCLASS = new GenericAnnotationScanner(false, true, false);

	/**
	 * 扫描元素本身以及父类的层级结构中声明的注解，以及这些注解的元注解的扫描器
	 */
	AnnotationScanner SUPERCLASS_AND_META_ANNOTATION = new GenericAnnotationScanner(true, true, false);

	/**
	 * 扫描元素本身以及父接口的层级结构中声明的注解的扫描器
	 */
	AnnotationScanner INTERFACE = new GenericAnnotationScanner(false, false, true);

	/**
	 * 扫描元素本身以及父接口的层级结构中声明的注解，以及这些注解的元注解的扫描器
	 */
	AnnotationScanner INTERFACE_AND_META_ANNOTATION = new GenericAnnotationScanner(true, false, true);

	/**
	 * 扫描元素本身以及父类、父接口的层级结构中声明的注解的扫描器
	 */
	AnnotationScanner TYPE_HIERARCHY = new GenericAnnotationScanner(false, true, true);

	/**
	 * 扫描元素本身以及父接口、父接口的层级结构中声明的注解，以及这些注解的元注解的扫描器
	 */
	AnnotationScanner TYPE_HIERARCHY_AND_META_ANNOTATION = new GenericAnnotationScanner(true, true, true);

	// ============================ 静态方法 ============================

	/**
	 * 给定一组扫描器，使用第一个支持处理该类型元素的扫描器获取元素上可能存在的注解
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param scanners     注解扫描器
	 * @return 注解
	 */
	static List<Annotation> scanByAnySupported(AnnotatedElement annotatedEle, AnnotationScanner... scanners) {
		if (ObjectUtil.isNull(annotatedEle) && ArrayUtil.isNotEmpty(scanners)) {
			return Collections.emptyList();
		}
		return Stream.of(scanners)
			.filter(scanner -> scanner.support(annotatedEle))
			.findFirst()
			.map(scanner -> scanner.getAnnotations(annotatedEle))
			.orElseGet(Collections::emptyList);
	}

	/**
	 * 根据指定的扫描器，扫描元素上可能存在的注解
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param scanners     注解扫描器
	 * @return 注解
	 */
	static List<Annotation> scanByAllSupported(AnnotatedElement annotatedEle, AnnotationScanner... scanners) {
		if (ObjectUtil.isNull(annotatedEle) && ArrayUtil.isNotEmpty(scanners)) {
			return Collections.emptyList();
		}
		return Stream.of(scanners)
			.map(scanner -> scanner.getAnnotationsIfSupport(annotatedEle))
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

	// ============================ 抽象方法 ============================

	/**
	 * 判断是否支持扫描该注解元素
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 是否支持扫描该注解元素
	 */
	default boolean support(AnnotatedElement annotatedEle) {
		return false;
	}

	/**
	 * 获取注解元素上的全部注解。调用该方法前，需要确保调用{@link #support(AnnotatedElement)}返回为true
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 注解
	 */
	default List<Annotation> getAnnotations(AnnotatedElement annotatedEle) {
		final List<Annotation> annotations = new ArrayList<>();
		scan((index, annotation) -> annotations.add(annotation), annotatedEle, null);
		return annotations;
	}

	/**
	 * 若{@link #support(AnnotatedElement)}返回{@code true}，
	 * 则调用并返回{@link #getAnnotations(AnnotatedElement)}结果，
	 * 否则返回{@link Collections#emptyList()}
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 注解
	 */
	default List<Annotation> getAnnotationsIfSupport(AnnotatedElement annotatedEle) {
		return support(annotatedEle) ? getAnnotations(annotatedEle) : Collections.emptyList();
	}

	/**
	 * 扫描注解元素的层级结构（若存在），然后对获取到的注解和注解对应的层级索引进行处理。
	 * 调用该方法前，需要确保调用{@link #support(AnnotatedElement)}返回为true
	 *
	 * @param consumer     对获取到的注解和注解对应的层级索引的处理
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param filter       注解过滤器，无法通过过滤器的注解不会被处理。该参数允许为空。
	 */
	default void scan(BiConsumer<Integer, Annotation> consumer, AnnotatedElement annotatedEle, Predicate<Annotation> filter) {
		filter = ObjectUtil.defaultIfNull(filter, (a)->annotation -> true);
		for (final Annotation annotation : annotatedEle.getAnnotations()) {
			if (AnnotationUtil.isNotJdkMateAnnotation(annotation.annotationType()) && filter.test(annotation)) {
				consumer.accept(0, annotation);
			}
		}
	}

	/**
	 * 若{@link #support(AnnotatedElement)}返回{@code true}，则调用{@link #scan(BiConsumer, AnnotatedElement, Predicate)}
	 *
	 * @param consumer     对获取到的注解和注解对应的层级索引的处理
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param filter       注解过滤器，无法通过过滤器的注解不会被处理。该参数允许为空。
	 */
	default void scanIfSupport(BiConsumer<Integer, Annotation> consumer, AnnotatedElement annotatedEle, Predicate<Annotation> filter) {
		if (support(annotatedEle)) {
			scan(consumer, annotatedEle, filter);
		}
	}

}
