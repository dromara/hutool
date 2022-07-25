package cn.hutool.core.annotation;

import cn.hutool.core.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <p>表示注解的某个属性，等同于绑定的调用对象的{@link Method}方法。<br>
 * 在{@link SynthesizedAggregateAnnotation}的解析以及取值过程中，
 * 可以通过设置{@link SynthesizedAnnotation}的注解属性，
 * 从而使得可以从一个注解对象中属性获取另一个注解对象的属性值
 *
 * <p>一般情况下，注解属性的处理会发生在{@link SynthesizedAnnotationPostProcessor}调用时
 *
 * @author huangchengxing
 * @see SynthesizedAnnotationPostProcessor
 * @see WrappedAnnotationAttribute
 * @see CacheableAnnotationAttribute
 * @see AbstractWrappedAnnotationAttribute
 * @see ForceAliasedAnnotationAttribute
 * @see AliasedAnnotationAttribute
 * @see MirroredAnnotationAttribute
 */
public interface AnnotationAttribute {

	/**
	 * 获取注解对象
	 *
	 * @return 注解对象
	 */
	Annotation getAnnotation();

	/**
	 * 获取注解属性对应的方法
	 *
	 * @return 注解属性对应的方法
	 */
	Method getAttribute();

	/**
	 * 获取声明属性的注解类
	 *
	 * @return 声明注解的注解类
	 */
	default Class<?> getAnnotationType() {
		return getAttribute().getDeclaringClass();
	}

	/**
	 * 获取属性名称
	 *
	 * @return 属性名称
	 */
	default String getAttributeName() {
		return getAttribute().getName();
	}

	/**
	 * 获取注解属性
	 *
	 * @return 注解属性
	 */
	default Object getValue() {
		return ReflectUtil.invoke(getAnnotation(), getAttribute());
	}

	/**
	 * 该注解属性的值是否等于默认值
	 *
	 * @return 该注解属性的值是否等于默认值
	 */
	boolean isValueEquivalentToDefaultValue();

	/**
	 * 获取属性类型
	 *
	 * @return 属性类型
	 */
	default Class<?> getAttributeType() {
		return getAttribute().getReturnType();
	}

	/**
	 * 获取属性上的注解
	 *
	 * @param <T> 注解类型
	 * @param annotationType 注解类型
	 * @return 注解对象
	 */
	default <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		return getAttribute().getAnnotation(annotationType);
	}

	/**
	 * 当前注解属性是否已经被{@link WrappedAnnotationAttribute}包装
	 *
	 * @return boolean
	 */
	default boolean isWrapped() {
		return false;
	}

}
