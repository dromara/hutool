package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;

/**
 * <p>表示基于特定规则聚合，将一组注解聚合而来的注解对象，
 * 该注解对象允许根据一定规则“合成”一些跟原始注解属性不一样合成注解。
 *
 * <p>合成注解一般被用于处理类层级结果中具有直接或间接关联的注解对象，
 * 当实例被创建时，会获取到这些注解对象，并使用{@link SynthesizedAnnotationSelector}对类型相同的注解进行过滤，
 * 并最终得到类型不重复的有效注解对象。这些有效注解将被包装为{@link SynthesizedAnnotation}，
 * 然后最终用于“合成”一个{@link SynthesizedAggregateAnnotation}。<br>
 * {@link SynthesizedAnnotationSelector}是合成注解生命周期中的第一个钩子，
 * 自定义选择器以拦截原始注解被扫描的过程。
 *
 * <p>当合成注解完成对待合成注解的扫描，并完成了必要属性的加载后，
 * 将会按顺序依次调用{@link SynthesizedAnnotationPostProcessor}，
 * 注解后置处理器允许用于对完成注解的待合成注解进行二次调整，
 * 该钩子一般用于根据{@link Link}注解对属性进行调整。<br>
 * {@link SynthesizedAnnotationPostProcessor}是合成注解生命周期中的第二个钩子，
 * 自定义后置处理器以拦截原始在转为待合成注解后的初始化过程。
 *
 * <p>合成注解允许通过{@link #synthesize(Class)}合成一个指定的注解对象，
 * 该方法返回的注解对象可能是原始的注解对象，也有可能通过动态代理的方式生成，
 * 该对象实例的属性不一定来自对象本身，而是来自于经过{@link SynthesizedAnnotationAttributeProcessor}
 * 处理后的、用于合成当前实例的全部关联注解的相关属性。<br>
 * {@link SynthesizedAnnotationAttributeProcessor}是合成注解生命周期中的第三个钩子，
 * 自定义属性处理器以拦截合成注解的取值过程。
 *
 * @author huangchengxing
 * @see AnnotationSynthesizer
 * @see SynthesizedAnnotation
 * @see SynthesizedAnnotationSelector
 * @see SynthesizedAnnotationAttributeProcessor
 * @see SynthesizedAnnotationPostProcessor
 * @see GenericSynthesizedAggregateAnnotation
 */
public interface SynthesizedAggregateAnnotation extends AggregateAnnotation, Hierarchical, AnnotationSynthesizer, AnnotationAttributeValueProvider {

	// ================== hierarchical ==================

	/**
	 * 距离{@link #getRoot()}返回值的垂直距离，
	 * 默认聚合注解即为根对象，因此返回0
	 *
	 * @return 距离{@link #getRoot()}返回值的水平距离，
	 */
	@Override
	default int getVerticalDistance() {
		return 0;
	}

	/**
	 * 距离{@link #getRoot()}返回值的水平距离，
	 * 默认聚合注解即为根对象，因此返回0
	 *
	 * @return 距离{@link #getRoot()}返回值的水平距离，
	 */
	@Override
	default int getHorizontalDistance() {
		return 0;
	}

	// ================== synthesize ==================

	/**
	 * 获取在聚合中存在的指定注解对象
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 注解对象
	 */
	<T extends Annotation> T getAnnotation(Class<T> annotationType);

	/**
	 * 获取合成注解属性处理器
	 *
	 * @return 合成注解属性处理器
	 */
	SynthesizedAnnotationAttributeProcessor getAnnotationAttributeProcessor();

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
	 * 从聚合中获取指定类型的属性值
	 *
	 * @param attributeName 属性名称
	 * @param attributeType 属性类型
	 * @return 属性值
	 */
	@Override
	Object getAttributeValue(String attributeName, Class<?> attributeType);

}
