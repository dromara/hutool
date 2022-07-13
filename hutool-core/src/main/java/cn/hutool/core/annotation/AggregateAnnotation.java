package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;

/**
 * 表示一组被聚合在一起的注解对象
 *
 * @author huangchengxing
 */
public interface AggregateAnnotation extends Annotation {

	/**
	 * 在聚合中是否存在的指定类型注解对象
	 *
	 * @param annotationType 注解类型
	 * @return 是否
	 */
	boolean isAnnotationPresent(Class<? extends Annotation> annotationType);

	/**
	 * 获取聚合中的全部注解对象
	 *
	 * @return 注解对象
	 */
	Annotation[] getAnnotations();

}
