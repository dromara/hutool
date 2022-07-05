package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 表示基于特定规则聚合的一组注解对象
 *
 * <p>合成注解一般被用于处理类层级结果中具有直接或间接关联的注解对象，
 * 当实例被创建时，会获取到这些注解对象，并使用{@link SynthesizedAnnotationSelector}对类型相同的注解进行过滤，
 * 并最终得到类型不重复的有效注解对象。这些有效注解将被包装为{@link SynthesizedAnnotation}，
 * 然后最终用于“合成”一个{@link SynthesizedAnnotation}。
 *
 * <p>合成注解可以作为一个特殊的{@link Annotation}或者{@link AnnotatedElement}，
 * 当调用{@link Annotation}的方法时，应当返回当前实例本身的有效信息，
 * 而当调用{@link AnnotatedElement}的方法时，应当返回用于合成该对象的相关注解的信息。
 *
 * <p>合成注解允许通过{@link #syntheticAnnotation(Class)}合成一个指定的注解对象，
 * 该方法返回的注解对象可能是原始的注解对象，也有可能通过动态代理的方式生成，
 * 该对象实例的属性不一定来自对象本身，而是来自于经过{@link SynthesizedAnnotationAttributeProcessor}
 * 处理后的、用于合成当前实例的全部关联注解的相关属性。
 *
 * @author huangchengxing
 * @see SynthesizedAnnotation
 * @see SynthesizedAnnotationSelector
 * @see SynthesizedAnnotationAttributeProcessor
 * @see SyntheticMetaAnnotation
 */
public interface SyntheticAnnotation extends Annotation, AnnotatedElement {

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
	SynthesizedAnnotationAttributeProcessor getAttributeProcessor();

	/**
	 * 获取已合成的注解
	 *
	 * @param annotationType 注解类型
	 * @return 已合成的注解
	 */
	SynthesizedAnnotation getSynthesizedAnnotation(Class<?> annotationType);

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
	static <T extends Annotation> SyntheticAnnotation of(T rootAnnotation) {
		return new SyntheticMetaAnnotation(rootAnnotation);
	}

}
