package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 表示基于特定规则聚合的一组注解
 *
 * @param <A> 合成注解类型
 * @author huangchengxing
 */
public interface SyntheticAnnotation<A extends SynthesizedAnnotation<?>> extends Annotation, AnnotatedElement {

	/**
	 * 获取合成注解选择器
	 *
	 * @return 合成注解选择器
	 */
	SynthesizedAnnotationSelector getAnnotationSelector();

	/**
	 * 获取合成注解属性处理器
	 *
	 * @return 合成注解属性处理器
	 */
	SynthesizedAnnotationAttributeProcessor<A> getAttributeProcessor();

	/**
	 * 获取已合成的注解
	 *
	 * @param annotationType 注解类型
	 * @param <T> 注解类型
	 * @return 已合成的注解
	 */
	<T extends Annotation> SynthesizedAnnotation<T> getSynthesizedAnnotation(Class<T> annotationType);

	/**
	 * 获取当前的注解类型
	 *
	 * @return 注解类型
	 */
	@Override
	default Class<? extends Annotation> annotationType() {
		return this.getClass();
	}

	/**
	 * 获取指定注解对象
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	@Override
	<T extends Annotation> T getAnnotation(Class<T> annotationType);

	/**
	 * 是否存在指定注解
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	@Override
	boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

	/**
	 * 获取全部注解
	 *
	 * @return 注解对象
	 */
	@Override
	Annotation[] getAnnotations();

	/**
	 * 获取合成注解
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 类型
	 */
	<T extends Annotation> T syntheticAnnotation(Class<T> annotationType);

	/**
	 * 获取属性值
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @return 属性值
	 */
	Object getAttribute(String attributeName, Class<?> attributeType);

	/**
	 * 基于指定根注解，构建包括其元注解在内的合成注解
	 *
	 * @param rootAnnotation 根注解
	 * @param <T>            注解类型
	 * @return 合成注解
	 */
	static <T extends Annotation> SyntheticAnnotation<SyntheticMetaAnnotation.MetaAnnotation> of(T rootAnnotation) {
		return new SyntheticMetaAnnotation<>(rootAnnotation);
	}

}
