package cn.hutool.core.annotation;

import cn.hutool.core.collection.CollUtil;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 用于在{@link SyntheticAnnotation}中表示一个处于合成状态的注解对象
 *
 * @author huangchengxing
 * @see SyntheticAnnotation
 */
public interface SynthesizedAnnotation extends Annotation {

	/**
	 * 获取所属的合成注解
	 *
	 * @return 合成注解
	 */
	SyntheticAnnotation getOwner();

	/**
	 * 获取该合成注解对应的根节点
	 *
	 * @return 合成注解对应的根节点
	 */
	Object getRoot();

	/**
	 * 该合成注解是为根对象
	 *
	 * @return 根对象
	 */
	default boolean isRoot() {
		return getRoot() == this;
	}

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
			attributes.forEach(this::setAttributes);
		}
	}

	/**
	 * 设置属性值
	 *
	 * @param attributeName 属性名称
	 * @param attribute     注解属性
	 */
	void setAttributes(String attributeName, AnnotationAttribute attribute);

	/**
	 * 获取属性值
	 *
	 * @param attributeName 属性名
	 * @return 属性值
	 */
	Object getAttributeValue(String attributeName);

}
