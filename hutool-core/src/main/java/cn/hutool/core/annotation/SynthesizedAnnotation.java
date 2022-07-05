package cn.hutool.core.annotation;

import java.lang.annotation.Annotation;

/**
 * 表示一个处于合成状态的注解对象
 *
 * @author huangchengxing
 */
public interface SynthesizedAnnotation<T> extends Annotation {

	/**
	 * 获取该合成注解对应的根节点
	 *
	 * @return 数据源
	 */
	T getRoot();

	/**
	 * 该合成注解是为根对象
	 *
	 * @return 对象
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
	 * 获取属性值
	 *
	 * @param attributeName 属性名
	 * @return 属性值
	 */
	Object getAttribute(String attributeName);

}
