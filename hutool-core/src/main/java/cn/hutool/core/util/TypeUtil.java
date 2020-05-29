package cn.hutool.core.util;

import cn.hutool.core.map.TableMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;

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
			} else if (type instanceof TypeVariable) {
				return (Class<?>) ((TypeVariable<?>) type).getBounds()[0];
			} else if (type instanceof WildcardType) {
				final Type[] upperBounds = ((WildcardType) type).getUpperBounds();
				if (upperBounds.length == 1) {
					return getClass(upperBounds[0]);
				}
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
		return field.getGenericType();
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
	 * @param index 泛型类型的索引号，即第几个泛型类型
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
	 * 获得指定类型中所有泛型参数类型，例如：
	 * 
	 * <pre>
	 * class A&lt;T&gt;
	 * class B extends A&lt;String&gt;
	 * </pre>
	 * 
	 * 通过此方法，传入B.class即可得到String
	 * 
	 * @param type 指定类型
	 * @return 所有泛型参数类型
	 */
	public static Type[] getTypeArguments(Type type) {
		if (null == type) {
			return null;
		}

		final ParameterizedType parameterizedType = toParameterizedType(type);
		return (null == parameterizedType) ? null : parameterizedType.getActualTypeArguments();
	}

	/**
	 * 将{@link Type} 转换为{@link ParameterizedType}<br>
	 * {@link ParameterizedType}用于获取当前类或父类中泛型参数化后的类型<br>
	 * 一般用于获取泛型参数具体的参数类型，例如：
	 * 
	 * <pre>
	 * class A&lt;T&gt;
	 * class B extends A&lt;String&gt;
	 * </pre>
	 * 
	 * 通过此方法，传入B.class即可得到B{@link ParameterizedType}，从而获取到String
	 * 
	 * @param type {@link Type}
	 * @return {@link ParameterizedType}
	 * @since 4.5.2
	 */
	public static ParameterizedType toParameterizedType(Type type) {
		ParameterizedType result = null;
		if (type instanceof ParameterizedType) {
			result = (ParameterizedType) type;
		} else if (type instanceof Class) {
			final Class<?> clazz = (Class<?>) type;
			Type genericSuper = clazz.getGenericSuperclass();
			if(null == genericSuper || Object.class.equals(genericSuper)){
				// 如果类没有父类，而是实现一些定义好的泛型接口，则取接口的Type
				final Type[] genericInterfaces = clazz.getGenericInterfaces();
				if(ArrayUtil.isNotEmpty(genericInterfaces)){
					// 默认取第一个实现接口的泛型Type
					genericSuper = genericInterfaces[0];
				}
			}
			result = toParameterizedType(genericSuper);
		}
		return result;
	}
	
	/**
	 * 获取指定泛型变量对应的真实类型<br>
	 * 由于子类中泛型参数实现和父类（接口）中泛型定义位置是一一对应的，因此可以通过对应关系找到泛型实现类型<br>
	 * 使用此方法注意：
	 * 
	 * <pre>
	 * 1. superClass必须是clazz的父类或者clazz实现的接口
	 * 2. typeVariable必须在superClass中声明
	 * </pre>
	 * 
	 * 
	 * @param actualType 真实类型所在类，此类中记录了泛型参数对应的实际类型
	 * @param typeDefineClass 泛型变量声明所在类或接口，此类中定义了泛型类型
	 * @param typeVariables 泛型变量，需要的实际类型对应的泛型参数
	 * @return 给定泛型参数对应的实际类型，如果无对应类型，返回null
	 * @since 4.5.7
	 */
	public static Type[] getActualTypes(Type actualType, Class<?> typeDefineClass, Type... typeVariables) {
		if (false == typeDefineClass.isAssignableFrom(getClass(actualType))) {
			throw new IllegalArgumentException("Parameter [superClass] must be assignable from [clazz]");
		}

		// 泛型参数标识符列表
		final TypeVariable<?>[] typeVars = typeDefineClass.getTypeParameters();
		if(ArrayUtil.isEmpty(typeVars)) {
			return null;
		}
		// 实际类型列表
		final Type[] actualTypeArguments = TypeUtil.getTypeArguments(actualType);
		if(ArrayUtil.isEmpty(actualTypeArguments)) {
			return null;
		}
		
		int size = Math.min(actualTypeArguments.length, typeVars.length);
		final Map<TypeVariable<?>, Type> tableMap = new TableMap<>(typeVars, actualTypeArguments);
		
		// 查找方法定义所在类或接口中此泛型参数的位置
		final Type[] result = new Type[size];
		for(int i = 0; i < typeVariables.length; i++) {
			//noinspection SuspiciousMethodCalls
			result[i] = (typeVariables[i] instanceof TypeVariable) ? tableMap.get(typeVariables[i]) : typeVariables[i];
		}
		return result;
	}
	
	/**
	 * 获取指定泛型变量对应的真实类型<br>
	 * 由于子类中泛型参数实现和父类（接口）中泛型定义位置是一一对应的，因此可以通过对应关系找到泛型实现类型<br>
	 * 使用此方法注意：
	 * 
	 * <pre>
	 * 1. superClass必须是clazz的父类或者clazz实现的接口
	 * 2. typeVariable必须在superClass中声明
	 * </pre>
	 * 
	 * 
	 * @param actualType 真实类型所在类，此类中记录了泛型参数对应的实际类型
	 * @param typeDefineClass 泛型变量声明所在类或接口，此类中定义了泛型类型
	 * @param typeVariable 泛型变量，需要的实际类型对应的泛型参数
	 * @return 给定泛型参数对应的实际类型
	 * @since 4.5.2
	 */
	public static Type getActualType(Type actualType, Class<?> typeDefineClass, Type typeVariable) {
		Type[] types = getActualTypes(actualType, typeDefineClass, typeVariable);
		if(ArrayUtil.isNotEmpty(types)) {
			return types[0];
		}
		return null;
	}

	/**
	 * 是否未知类型<br>
	 * type为null或者{@link TypeVariable} 都视为未知类型
	 * 
	 * @param type Type类型
	 * @return 是否未知类型
	 * @since 4.5.2
	 */
	public static boolean isUnknow(Type type) {
		return null == type || type instanceof TypeVariable;
	}
	
	/**
	 * 指定泛型数组中是否含有泛型变量
	 * @param types 泛型数组
	 * @return 是否含有泛型变量
	 * @since 4.5.7
	 */
	public static boolean hasTypeVeriable(Type... types) {
		for (Type type : types) {
			if(type instanceof TypeVariable) {
				return true;
			}
		}
		return false;
	}
}
