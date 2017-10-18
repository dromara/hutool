package com.xiaoleilu.hutool.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

import com.xiaoleilu.hutool.exceptions.UtilException;

/**
 * 针对 {@link Type} 的工具类封装
 * 
 * @author Looly
 * @since 3.0.8
 */
public class TypeUtil {
	
	/**
	 * 获得Type对应的原始类
	 * @param type {@link Type}
	 * @return 原始类，如果无法获取原始类，返回{@code null}
	 */
	public static Class<?> getRowType(Type type) {
		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) type).getRawType();
		}
		return null;
	}
	
	/**
	 * 获取字段对应的Type类型<br>
	 * 方法优先获取GenericType，获取不到则获取Type
	 * 
	 * @param field 字段
	 * @return {@link Type}，可能为{@code null}
	 */
	public static Type getType(Field field){
		if(null == field){
			return null;
		}
		Type type = field.getGenericType();
		if(null == type){
			type = field.getType();
		}
		return type;
	}
	
	/**
	 * 获取方法的参数类型列表<br>
	 * 优先获取方法的GenericParameterTypes，如果获取不到，则获取ParameterTypes
	 * 
	 * @param method 方法
	 * @param index 第几个参数的索引，从0开始计数
	 * @return {@link Type}，可能为{@code null}
	 */
	public static Type getParamType(Method method, int index){
		Type[] types = getParamTypes(method);
		if(null != types && types.length > index){
			return types[index];
		}
		return null;
	}
	
	/**
	 * 获取方法的参数类型列表<br>
	 * 优先获取方法的GenericParameterTypes，如果获取不到，则获取ParameterTypes
	 * 
	 * @param method 方法
	 * @return {@link Type}列表，可能为{@code null}
	 * @see Method#getGenericParameterTypes()
	 * @see Method#getParameterTypes()
	 */
	public static Type[] getParamTypes(Method method){
		if(null == method){
			return null;
		}
		
		Type[] types = method.getGenericParameterTypes();
		if(ArrayUtil.isEmpty(types)){
			types = method.getParameterTypes();
		}
		return types;
	}
	
	/**
	 * 获取方法的参数类型列表<br>
	 * 优先获取方法的GenericReturnType，如果获取不到，则获取ReturnType
	 * 
	 * @param method 方法
	 * @return {@link Type}，可能为{@code null}
	 * @see Method#getGenericReturnType()
	 * @see Method#getReturnType()
	 */
	public static Type getReturnType(Method method){
		if(null == method){
			return null;
		}
		
		Type type = method.getGenericReturnType();
		if(null == type){
			type = method.getReturnType();
		}
		return type;
	}

	/**
	 * 获得给定类的第一个泛型参数
	 * 
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @return {@link Type}，可能为{@code null}
	 */
	public static Type getTypeArgument(Class<?> clazz) {
		return getTypeArgument(clazz, 0);
	}

	/**
	 * 获得给定类的泛型参数
	 * 
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @param index 泛型类型的索引号，既第几个泛型类型
	 * @return {@link Type}
	 */
	public static Type getTypeArgument(Class<?> clazz, int index) {
		Type type = clazz;
		if (false == (type instanceof ParameterizedType)) {
			type = clazz.getGenericSuperclass();
		}
		return getTypeArgument(type, index);
	}
	
	/**
	 * 获得给定类的第一个泛型参数
	 * 
	 * @param type 被检查的类型，必须是已经确定泛型类型的类型
	 * @return {@link Type}，可能为{@code null}
	 */
	public static Type getTypeArgument(Type type) {
		return getTypeArgument(type, 0);
	}

	/**
	 * 获得给定类的泛型参数
	 * 
	 * @param type 被检查的类型，必须是已经确定泛型类型的类
	 * @param index 泛型类型的索引号，既第几个泛型类型
	 * @return {@link Type}
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
	 */
	public static Type[] getTypeArguments(Type type) {
		if (type instanceof ParameterizedType) {
			final ParameterizedType genericSuperclass = (ParameterizedType) type;
			return genericSuperclass.getActualTypeArguments();
		}
		return null;
	}
	
	/**
	 * 获取元素类型<br>
	 * 通过{@link Iterator}的next方法返回值获取其泛型类型从而确定元素类型
	 * 
	 * @param clazz {@link Iterator}
	 * @return 泛型元素类型
	 */
	public static Type getElementType(Class<?> clazz){
		if(null == clazz){
			return null;
		}
		
		Method nextMethod = null;
		try {
			if(Iterator.class.isAssignableFrom(clazz)){
				nextMethod = clazz.getMethod("next");
			}
		} catch (NoSuchMethodException e) {
			return null;
		} catch (SecurityException e) {
			throw new UtilException(e);
		}
		
		return getReturnType(nextMethod);
	}
}
