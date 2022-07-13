package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * 表示一组被聚合在一起的注解对象
 *
 * @author huangchengxing
 */
public interface AnnotationAggregator extends AnnotatedElement {

	/**
	 * 获取在聚合中存在的指定注解对象
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	@Override
	<T extends Annotation> T getAnnotation(Class<T> annotationType);

	/**
	 * 在聚合中是否存在的指定类型注解对象
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	@Override
	boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

	/**
	 * 获取聚合中的全部注解对象
	 *
	 * @return 注解对象
	 */
	@Override
	Annotation[] getAnnotations();

	/**
	 * 从聚合中获取指定类型的属性值
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @return 属性值
	 */
	Object getAttribute(String attributeName, Class<?> attributeType);

}
