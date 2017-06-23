package com.xiaoleilu.hutool.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 针对 {@link Type} 的工具类封装
 * 
 * @author Looly
 * @since 3.0.8
 */
public class TypeUtil {

	/**
	 * 获得给定类的泛型参数
	 * 
	 * @param type 被检查的类型，必须是已经确定泛型类型的类
	 * @param index 泛型类型的索引号，既第几个泛型类型
	 * @return {@link Type}
	 * @since 3.0.8
	 */
	public static Type getTypeArgument(Type type, int index) {
		final Type[] typeArguments = getTypeArguments(type);
		if (null != typeArguments && typeArguments.length > index) {
			return typeArguments[index];
		}
		return null;
	}

	/**
	 * 获得指定类型中所有泛型参数类型
	 * 
	 * @param type 指定类型
	 * @return 所有泛型参数类型
	 * @since 3.0.8
	 */
	public static Type[] getTypeArguments(Type type) {
		if (type instanceof ParameterizedType) {
			final ParameterizedType genericSuperclass = (ParameterizedType) type;
			return genericSuperclass.getActualTypeArguments();
		}
		return null;
	}
}
