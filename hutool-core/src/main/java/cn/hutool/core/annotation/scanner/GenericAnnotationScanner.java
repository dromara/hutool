package cn.hutool.core.annotation.scanner;

import cn.hutool.core.map.multi.ListValueMap;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * <p>通用注解扫描器，支持按不同的层级结构扫描{@link AnnotatedElement}上的注解。
 *
 * <p>当{@link AnnotatedElement}类型不同时，“层级结构”指向的对象将有所区别：
 * <ul>
 *     <li>
 *         当元素为{@link Method}时，此处层级结构指声明方法的类的层级结构，
 *         扫描器将从层级结构中寻找与该方法签名相同的方法，并对其进行扫描；
 *     </li>
 *     <li>
 *         当元素为{@link Class}时，此处层级结构即指类本身与其父类、父接口共同构成的层级结构，
 *         扫描器将扫描层级结构中类、接口声明的注解；
 *     </li>
 *     <li>当元素不为{@link Method}或{@link Class}时，则其层级结构仅有其本身一层；</li>
 * </ul>
 * 此外，扫描器支持在获取到层级结构中的注解对象后，再对注解对象的元注解进行扫描。
 *
 * @author huangchengxing
 * @see TypeAnnotationScanner
 * @see MethodAnnotationScanner
 * @see MetaAnnotationScanner
 * @see ElementAnnotationScanner
 */
public class GenericAnnotationScanner implements AnnotationScanner {

	/**
	 * 类型扫描器
	 */
	private final AnnotationScanner typeScanner;

	/**
	 * 方法扫描器
	 */
	private final AnnotationScanner methodScanner;

	/**
	 * 元注解扫描器
	 */
	private final AnnotationScanner metaScanner;

	/**
	 * 普通元素扫描器
	 */
	private final AnnotationScanner elementScanner;

	/**
	 * 通用注解扫描器支持扫描所有类型的{@link AnnotatedElement}
	 *
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @return 是否支持扫描该注解元素
	 */
	@Override
	public boolean support(AnnotatedElement annotatedEle) {
		return true;
	}

	/**
	 * 构造一个通用注解扫描器
	 *
	 * @param enableScanMetaAnnotation  是否扫描注解上的元注解
	 * @param enableScanSupperClass     是否扫描父类
	 * @param enableScanSupperInterface 是否扫描父接口
	 */
	public GenericAnnotationScanner(
			boolean enableScanMetaAnnotation,
			boolean enableScanSupperClass,
			boolean enableScanSupperInterface) {

		this.metaScanner = enableScanMetaAnnotation ? new MetaAnnotationScanner() : new EmptyAnnotationScanner();
		this.typeScanner = new TypeAnnotationScanner(
				enableScanSupperClass, enableScanSupperInterface, a -> true, Collections.emptySet()
		);
		this.methodScanner = new MethodAnnotationScanner(
				enableScanSupperClass, enableScanSupperInterface, a -> true, Collections.emptySet()
		);
		this.elementScanner = new ElementAnnotationScanner();
	}

	/**
	 * 扫描注解元素的层级结构（若存在），然后对获取到的注解和注解对应的层级索引进行处理
	 *
	 * @param consumer     对获取到的注解和注解对应的层级索引的处理
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param filter       注解过滤器，无法通过过滤器的注解不会被处理。该参数允许为空。
	 */
	@Override
	public void scan(BiConsumer<Integer, Annotation> consumer, AnnotatedElement annotatedEle, Predicate<Annotation> filter) {
		filter = ObjectUtil.defaultIfNull(filter, a -> t -> true);
		if (ObjectUtil.isNull(annotatedEle)) {
			return;
		}
		// 注解元素是类
		if (annotatedEle instanceof Class) {
			scanElements(typeScanner, consumer, annotatedEle, filter);
		}
		// 注解元素是方法
		else if (annotatedEle instanceof Method) {
			scanElements(methodScanner, consumer, annotatedEle, filter);
		}
		// 注解元素是其他类型
		else {
			scanElements(elementScanner, consumer, annotatedEle, filter);
		}
	}

	/**
	 * 扫描注解类的层级结构（若存在），然后对获取到的注解和注解对应的层级索引进行处理
	 *
	 * @param scanner      使用的扫描器
	 * @param consumer     对获取到的注解和注解对应的层级索引的处理
	 * @param annotatedEle {@link AnnotatedElement}，可以是Class、Method、Field、Constructor、ReflectPermission
	 * @param filter       注解过滤器，无法通过过滤器的注解不会被处理。该参数允许为空。
	 */
	private void scanElements(
			AnnotationScanner scanner,
			BiConsumer<Integer, Annotation> consumer,
			AnnotatedElement annotatedEle,
			Predicate<Annotation> filter) {
		// 扫描类上注解
		final ListValueMap<Integer, Annotation> classAnnotations = new ListValueMap<>(new LinkedHashMap<>());
		scanner.scan((index, annotation) -> {
			if (filter.test(annotation)) {
				classAnnotations.putValue(index, annotation);
			}
		}, annotatedEle, filter);

		// 扫描元注解
		classAnnotations.forEach((index, annotations) ->
				annotations.forEach(annotation -> {
					consumer.accept(index, annotation);
					metaScanner.scan(consumer, annotation.annotationType(), filter);
				})
		);
	}

}
