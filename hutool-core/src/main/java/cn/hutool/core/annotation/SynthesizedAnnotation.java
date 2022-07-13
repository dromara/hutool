package cn.hutool.core.annotation;

import cn.hutool.core.collection.CollUtil;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * <p>用于在{@link SynthesizedAnnotationAggregator}中表示一个处于合成状态的注解对象。<br>
 * 当对多个合成注解排序时，默认使用{@link #DEFAULT_CHILD_PRIORITY_COMPARATOR}进行排序，
 * 从保证合成注解按{@link #getVerticalDistance()}与{@link #getHorizontalDistance()}的返回值保持有序，
 * 从而使得距离根元素更接近的注解对象在被处理是具有更高的优先级。
 *
 * @author huangchengxing
 * @see SynthesizedAnnotationAggregator
 */
public interface SynthesizedAnnotation extends Annotation, Comparable<SynthesizedAnnotation> {

	/**
	 * {@link SynthesizedAnnotation}使用的默认的比较器，
	 * 按照按{@link #getVerticalDistance()}和{@link #getHorizontalDistance()}的返回值进行排序。<br>
	 * 一般情况下，排序越小的合成注解应当被优先处理。
	 */
	Comparator<SynthesizedAnnotation> DEFAULT_CHILD_PRIORITY_COMPARATOR = Comparator
		.comparing(SynthesizedAnnotation::getVerticalDistance)
		.thenComparing(SynthesizedAnnotation::getHorizontalDistance);

	/**
	 * 获取所属的合成注解聚合器
	 *
	 * @return 合成注解
	 */
	SynthesizedAnnotationAggregator getOwner();

	/**
	 * 获取该合成注解对应的根节点
	 *
	 * @return 合成注解对应的根节点
	 */
	Object getRoot();

	/**
	 * 获取被合成的注解对象
	 *
	 * @return 注解对象
	 */
	Annotation getAnnotation();

	/**
	 * 获取该合成注解与根对象的垂直距离。
	 * 默认情况下，该距离即为当前注解与根对象之间相隔的层级数。
	 *
	 * @return 合成注解与根对象的垂直距离
	 */
	int getVerticalDistance();

	/**
	 * 获取该合成注解与根对象的水平距离。
	 * 默认情况下，该距离即为当前注解与根对象之间相隔的已经被扫描到的注解数。
	 *
	 * @return 合成注解与根对象的水平距离
	 */
	int getHorizontalDistance();

	/**
	 * 注解是否存在该名称相同，且类型一致的属性
	 *
	 * @param attributeName 属性名
	 * @param returnType    返回值类型
	 * @return 是否存在该属性
	 */
	boolean hasAttribute(String attributeName, Class<?> returnType);

	/**
	 * 获取该注解的全部属性
	 *
	 * @return 注解属性
	 */
	Map<String, AnnotationAttribute> getAttributes();

	/**
	 * 设置该注解的全部属性
	 *
	 * @param attributes 注解属性
	 */
	default void setAttributes(Map<String, AnnotationAttribute> attributes) {
		if (CollUtil.isNotEmpty(attributes)) {
			attributes.forEach(this::setAttribute);
		}
	}

	/**
	 * 设置属性值
	 *
	 * @param attributeName 属性名称
	 * @param attribute     注解属性
	 */
	void setAttribute(String attributeName, AnnotationAttribute attribute);

	/**
	 * 替换属性值
	 *
	 * @param attributeName 属性名
	 * @param operator      替换操作
	 */
	void replaceAttribute(String attributeName, UnaryOperator<AnnotationAttribute> operator);

	/**
	 * 获取属性值
	 *
	 * @param attributeName 属性名
	 * @return 属性值
	 */
	Object getAttributeValue(String attributeName);

	/**
	 * 按{@link #getVerticalDistance()}和{@link #getHorizontalDistance()}排序
	 *
	 * @param o {@link SynthesizedAnnotation}对象
	 * @return 比较值
	 */
	@Override
	default int compareTo(SynthesizedAnnotation o) {
		return DEFAULT_CHILD_PRIORITY_COMPARATOR.compare(this, o);
	}

}
