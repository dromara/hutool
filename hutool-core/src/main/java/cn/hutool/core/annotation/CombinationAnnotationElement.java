package cn.hutool.core.annotation;

import cn.hutool.core.collection.CollUtil;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 组合注解 对JDK的原生注解机制做一个增强，支持类似Spring的组合注解。<br>
 * 核心实现使用了递归获取指定元素上的注解以及注解的注解，以实现复合注解的获取。
 *
 * @author Succy,Looly
 * @since 4.0.9
 **/

public class CombinationAnnotationElement implements AnnotatedElement, Serializable {
	private static final long serialVersionUID = 1L;

	/** 元注解 */
	private static final Set<Class<? extends Annotation>> META_ANNOTATIONS = CollUtil.newHashSet(Target.class, //
			Retention.class, //
			Inherited.class, //
			Documented.class, //
			SuppressWarnings.class, //
			Override.class, //
			Deprecated.class//
	);

	/** 注解类型与注解对象对应表 */
	private Map<Class<? extends Annotation>, Annotation> annotationMap;
	/** 直接注解类型与注解对象对应表 */
	private Map<Class<? extends Annotation>, Annotation> declaredAnnotationMap;

	/**
	 * 构造
	 *
	 * @param element 需要解析注解的元素：可以是Class、Method、Field、Constructor、ReflectPermission
	 */
	public CombinationAnnotationElement(AnnotatedElement element) {
		init(element);
	}

	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
		return annotationMap.containsKey(annotationClass);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
		Annotation annotation = annotationMap.get(annotationClass);
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
	private void init(AnnotatedElement element) {
		final Annotation[] declaredAnnotations = element.getDeclaredAnnotations();
		this.declaredAnnotationMap = new HashMap<>();
		parseDeclared(declaredAnnotations);

		final Annotation[] annotations = element.getAnnotations();
		if(Arrays.equals(declaredAnnotations, annotations)) {
			this.annotationMap = this.declaredAnnotationMap;
		}else {
			this.annotationMap = new HashMap<>();
			parse(annotations);
		}
	}

	/**
	 * 进行递归解析注解，直到全部都是元注解为止
	 *
	 * @param annotations Class, Method, Field等
	 */
	private void parseDeclared(Annotation[] annotations) {
		Class<? extends Annotation> annotationType;
		// 直接注解
		for (Annotation annotation : annotations) {
			annotationType = annotation.annotationType();
			if (false == META_ANNOTATIONS.contains(annotationType)) {
				declaredAnnotationMap.put(annotationType, annotation);
				parseDeclared(annotationType.getDeclaredAnnotations());
			}
		}
	}

	/**
	 * 进行递归解析注解，直到全部都是元注解为止
	 *
	 * @param annotations Class, Method, Field等
	 */
	private void parse(Annotation[] annotations) {
		Class<? extends Annotation> annotationType;
		for (Annotation annotation : annotations) {
			annotationType = annotation.annotationType();
			if (false == META_ANNOTATIONS.contains(annotationType)) {
				annotationMap.put(annotationType, annotation);
				parse(annotationType.getAnnotations());
			}
		}
	}
}