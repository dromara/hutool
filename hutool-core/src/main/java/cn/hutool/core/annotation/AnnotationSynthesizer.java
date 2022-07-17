package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

/**
 * <p>注解合成器，用于处理一组给定的与{@link #getSource()}具有直接或间接联系的注解对象，
 * 并返回与原始注解对象具有不同属性的“合成”注解。
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
 * <p>使用{@link #synthesize(Class)}用于获取“合成”后的注解，
 * 该注解对象的属性可能会与原始的对象属性不同。
 *
 * @author huangchengxing
 */
public interface AnnotationSynthesizer {

	/**
	 * 获取合成注解来源最初来源
	 *
	 * @return 合成注解来源最初来源
	 */
	Object getSource();

	/**
	 * 合成注解选择器
	 *
	 * @return 注解选择器
	 */
	SynthesizedAnnotationSelector getAnnotationSelector();

	/**
	 * 获取合成注解后置处理器
	 *
	 * @return 合成注解后置处理器
	 */
	Collection<SynthesizedAnnotationPostProcessor> getAnnotationPostProcessors();

	/**
	 * 获取已合成的注解
	 *
	 * @param annotationType 注解类型
	 * @return 已合成的注解
	 */
	SynthesizedAnnotation getSynthesizedAnnotation(Class<?> annotationType);

	/**
	 * 获取全部的合成注解
	 *
	 * @return 合成注解
	 */
	Map<Class<? extends Annotation>, SynthesizedAnnotation> getAllSynthesizedAnnotation();

	/**
	 * 获取合成注解
	 *
	 * @param annotationType 注解类型
	 * @param <T>            注解类型
	 * @return 类型
	 */
	<T extends Annotation> T synthesize(Class<T> annotationType);

}
