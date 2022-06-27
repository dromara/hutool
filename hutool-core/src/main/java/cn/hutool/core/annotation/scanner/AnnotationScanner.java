package cn.hutool.core.annotation.scanner;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 注解扫描器，用于从支持的可注解元素上获取所需注解
 *
 * @author huangchengxing
 * @see TypeAnnotationScanner
 * @see MethodAnnotationScanner
 * @see FieldAnnotationScanner
 * @see MetaAnnotationScanner
 */
public interface AnnotationScanner {

	/**
	 * 是否支持扫描该可注解元素
	 *
	 * @param annotatedElement 可注解元素
	 * @return 是否支持扫描该可注解元素
	 */
	default boolean support(AnnotatedElement annotatedElement) {
		return false;
	}

	/**
	 * 获取可注解元素上的全部注解。调用该方法前，需要确保调用{@link #support(AnnotatedElement)}返回为true
	 *
	 * @param annotatedElement 可注解元素
	 * @return 元素上的注解
	 */
	List<Annotation> getAnnotations(AnnotatedElement annotatedElement);

	/**
	 * 若{@link #support(AnnotatedElement)}返回{@code true}，
	 * 则调用并返回{@link #getAnnotations(AnnotatedElement)}结果，
	 * 否则返回{@link Collections#emptyList()}
	 *
	 * @param annotatedElement 元素
	 * @return 元素上的注解
	 */
	default List<Annotation> getIfSupport(AnnotatedElement annotatedElement) {
		return support(annotatedElement) ? getAnnotations(annotatedElement) : Collections.emptyList();
	}

	/**
	 * 给定一组扫描器，使用第一个支持处理该类型元素的扫描器获取元素上可能存在的注解
	 *
	 * @param annotatedElement 可注解元素
	 * @param scanners 注解扫描器
	 * @return 注解
	 */
	static List<Annotation> scanByAnySupported(AnnotatedElement annotatedElement, AnnotationScanner... scanners) {
		if (ObjectUtil.isNull(annotatedElement) && ArrayUtil.isNotEmpty(scanners)) {
			return Collections.emptyList();
		}
		return Stream.of(scanners)
			.filter(scanner -> scanner.support(annotatedElement))
			.findFirst()
			.map(scanner -> scanner.getAnnotations(annotatedElement))
			.orElseGet(Collections::emptyList);
	}

	/**
	 * 根据指定的扫描器，扫描元素上可能存在的注解
	 *
	 * @param annotatedElement 可注解元素
	 * @param scanners 注解扫描器
	 * @return 注解
	 */
	static List<Annotation> scanByAllScanner(AnnotatedElement annotatedElement, AnnotationScanner... scanners) {
		if (ObjectUtil.isNull(annotatedElement) && ArrayUtil.isNotEmpty(scanners)) {
			return Collections.emptyList();
		}
		return Stream.of(scanners)
			.map(scanner -> scanner.getIfSupport(annotatedElement))
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
	}

}
