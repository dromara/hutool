package cn.hutool.core.annotation;

import java.util.Collection;

/**
 * 合成注解属性选择器。用于中合成注解中从指定类型的注解里获取到对应的属性值
 *
 * @author huangchengxing
 */
@FunctionalInterface
public interface SynthesizedAnnotationAttributeProcessor<A extends SynthesizedAnnotation<?>> {

	/**
	 * 从一批被合成注解中，获取指定名称与类型的属性值
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @param synthesizedAnnotations 被合成的注解
	 * @param <T> 属性类型
	 * @return 属性值
	 */
	<T> T getAttributeValue(String attributeName, Class<T> attributeType, Collection<A> synthesizedAnnotations);

}
