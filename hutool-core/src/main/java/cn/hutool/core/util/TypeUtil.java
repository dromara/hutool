package cn.hutool.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 针对 {@link Type} 的工具类封装<br>
 * 最主要功能包括：
 * 
 * <pre>
 * 1. 获取方法的参数和返回值类型（包括Type和Class）
 * 2. 获取泛型参数类型（包括对象的泛型参数或集合元素的泛型类型）
 * </pre>
 * 
 * @author Looly
 * @since 3.0.8
 */
public class TypeUtil {

	/**
	 * 获得Type对应的原始类
	 * 
	 * @param type {@link Type}
	 * @return 原始类，如果无法获取原始类，返回{@code null}
	 */
	public static Class<?> getClass(Type type) {
		if (null != type) {
			if (type instanceof Class) {
				return (Class<?>) type;
			} else if (type instanceof ParameterizedType) {
				return (Class<?>) ((ParameterizedType) type).getRawType();
			}
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
	public static Type getType(Field field) {
		if (null == field) {
			return null;
		}
		Type type = field.getGenericType();
		if (null == type) {
			type = field.getType();
		}
		return type;
	}

	/**
	 * 获得Field对应的原始类
	 * 
	 * @param field {@link Field}
	 * @return 原始类，如果无法获取原始类，返回{@code null}
	 * @since 3.1.2
	 */
	public static Class<?> getClass(Field field) {
		return null == field ? null : field.getType();
	}

	// ----------------------------------------------------------------------------------- Param Type
	/**
	 * 获取方法的第一个参数类型<br>
	 * 优先获取方法的GenericParameterTypes，如果获取不到，则获取ParameterTypes
	 * 
	 * @param method 方法
	 * @return {@link Type}，可能为{@code null}
	 * @since 3.1.2
	 */
	public static Type getFirstParamType(Method method) {
		return getParamType(method, 0);
	}

	/**
	 * 获取方法的第一个参数类
	 * 
	 * @param method 方法
	 * @return 第一个参数类型，可能为{@code null}
	 * @since 3.1.2
	 */
	public static Class<?> getFirstParamClass(Method method) {
		return getParamClass(method, 0);
	}

	/**
	 * 获取方法的参数类型<br>
	 * 优先获取方法的GenericParameterTypes，如果获取不到，则获取ParameterTypes
	 * 
	 * @param method 方法
	 * @param index 第几个参数的索引，从0开始计数
	 * @return {@link Type}，可能为{@code null}
	 */
	public static Type getParamType(Method method, int index) {
		Type[] types = getParamTypes(method);
		if (null != types && types.length > index) {
			return types[index];
		}
		return null;
	}

	/**
	 * 获取方法的参数类
	 * 
	 * @param method 方法
	 * @param index 第几个参数的索引，从0开始计数
	 * @return 参数类，可能为{@code null}
	 * @since 3.1.2
	 */
	public static Class<?> getParamClass(Method method, int index) {
		Class<?>[] classes = getParamClasses(method);
		if (null != classes && classes.length > index) {
			return classes[index];
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
	public static Type[] getParamTypes(Method method) {
		return null == method ? null : method.getGenericParameterTypes();
	}

	/**
	 * 解析方法的参数类型列表<br>
	 * 依赖jre\lib\rt.jar
	 *
	 * @param method t方法
	 * @return 参数类型类列表
	 *
	 * @see Method#getGenericParameterTypes
	 * @see Method#getParameterTypes
	 * @since 3.1.2
	 */
	public static Class<?>[] getParamClasses(Method method) {
		return null == method ? null : method.getParameterTypes();
	}

	// ----------------------------------------------------------------------------------- Return Type
	/**
	 * 获取方法的返回值类型<br>
	 * 获取方法的GenericReturnType
	 * 
	 * @param method 方法
	 * @return {@link Type}，可能为{@code null}
	 * @see Method#getGenericReturnType()
	 * @see Method#getReturnType()
	 */
	public static Type getReturnType(Method method) {
		return null == method ? null : method.getGenericReturnType();
	}

	/**
	 * 解析方法的返回类型类列表
	 *
	 * @param method 方法
	 * @return 返回值类型的类
	 * @see Method#getGenericReturnType
	 * @see Method#getReturnType
	 * @since 3.1.2
	 */
	public static Class<?> getReturnClass(Method method) {
		return null == method ? null : method.getReturnType();
	}

	// ----------------------------------------------------------------------------------- Type Argument
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
}
