package cn.hutool.core.annotation.scanner;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 扫描注解类上存在的注解，支持处理枚举实例或枚举类型
 * 需要注意，当待解析是枚举类时，有可能与{@link TypeAnnotationScanner}冲突
 *
 * @author huangchengxing
 * @see TypeAnnotationScanner
 */
public class MetaAnnotationScanner implements AnnotationScanner {

	/**
	 * 获取当前注解的元注解后，是否继续递归扫描的元注解的元注解
	 */
	private final boolean includeSupperMetaAnnotation;

	/**
	 * 构造
	 *
	 * @param includeSupperMetaAnnotation 获取当前注解的元注解后，是否继续递归扫描的元注解的元注解
	 */
	public MetaAnnotationScanner(boolean includeSupperMetaAnnotation) {
		this.includeSupperMetaAnnotation = includeSupperMetaAnnotation;
	}

	/**
	 * 构造一个元注解扫描器，默认在扫描当前注解上的元注解后，并继续递归扫描元注解
	 */
	public MetaAnnotationScanner() {
		this(true);
	}

	@Override
	public boolean support(AnnotatedElement annotatedElement) {
		return (annotatedElement instanceof Class && ClassUtil.isAssignable(Annotation.class, (Class<?>) annotatedElement));
	}

	/**
	 * 按广度优先扫描指定注解上的元注解，对扫描到的注解与层级索引进行操作
	 *
	 * @param consumer 当前层级索引与操作
	 * @param source   源注解
	 * @param filter   过滤器
	 * @author huangchengxing
	 */
	public void scan(BiConsumer<Integer, Annotation> consumer, Class<? extends Annotation> source, Predicate<Annotation> filter) {
		filter = ObjectUtil.defaultIfNull(filter, t -> true);
		final Deque<List<Class<? extends Annotation>>> deque = CollUtil.newLinkedList(CollUtil.newArrayList(source));
		int distance = 0;
		do {
			final List<Class<? extends Annotation>> annotationTypes = deque.removeFirst();
			for (final Class<? extends Annotation> type : annotationTypes) {
				final List<Annotation> metaAnnotations = Stream.of(type.getAnnotations())
						.filter(a -> !AnnotationUtil.isJdkMetaAnnotation(a.annotationType()))
						.filter(filter)
						.collect(Collectors.toList());
				for (final Annotation metaAnnotation : metaAnnotations) {
					consumer.accept(distance, metaAnnotation);
				}
				deque.addLast(CollStreamUtil.toList(metaAnnotations, Annotation::annotationType));
			}
			distance++;
		} while (includeSupperMetaAnnotation && !deque.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Annotation> getAnnotations(AnnotatedElement annotatedElement) {
		final List<Annotation> annotations = new ArrayList<>();
		scan(
				(index, annotation) -> annotations.add(annotation),
				(Class<? extends Annotation>) annotatedElement,
				annotation -> ObjectUtil.notEqual(annotation, annotatedElement)
		);
		return annotations;
	}

}
