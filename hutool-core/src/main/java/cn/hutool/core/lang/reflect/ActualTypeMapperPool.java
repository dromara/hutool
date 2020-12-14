package cn.hutool.core.lang.reflect;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.util.TypeUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * 泛型变量和泛型实际类型映射关系缓存
 *
 * @author looly
 * @since 5.4.2
 */
public class ActualTypeMapperPool {

	private static final SimpleCache<Type, Map<Type, Type>> CACHE = new SimpleCache<>();

	/**
	 * 获取泛型变量和泛型实际类型的对应关系Map
	 *
	 * @param type 被解析的包含泛型参数的类
	 * @return 泛型对应关系Map
	 */
	public static Map<Type, Type> get(Type type) {
		return CACHE.get(type, () -> createTypeMap(type));
	}

	/**
	 * 获得泛型变量对应的泛型实际类型，如果此变量没有对应的实际类型，返回null
	 *
	 * @param type        类
	 * @param typeVariable 泛型变量，例如T等
	 * @return 实际类型，可能为Class等
	 */
	public static Type getActualType(Type type, TypeVariable<?> typeVariable) {
		final Map<Type, Type> typeTypeMap = get(type);
		Type result = typeTypeMap.get(typeVariable);
		while (result instanceof TypeVariable) {
			result = typeTypeMap.get(result);
		}
		return result;
	}

	/**
	 * 获取指定泛型变量对应的真实类型<br>
	 * 由于子类中泛型参数实现和父类（接口）中泛型定义位置是一一对应的，因此可以通过对应关系找到泛型实现类型<br>
	 *
	 * @param type         真实类型所在类，此类中记录了泛型参数对应的实际类型
	 * @param typeVariables 泛型变量，需要的实际类型对应的泛型参数
	 * @return 给定泛型参数对应的实际类型，如果无对应类型，对应位置返回null
	 */
	public static Type[] getActualTypes(Type type, Type... typeVariables) {
		// 查找方法定义所在类或接口中此泛型参数的位置
		final Type[] result = new Type[typeVariables.length];
		for (int i = 0; i < typeVariables.length; i++) {
			result[i] = (typeVariables[i] instanceof TypeVariable)
					? getActualType(type, (TypeVariable<?>) typeVariables[i])
					: typeVariables[i];
		}
		return result;
	}

	/**
	 * 创建类中所有的泛型变量和泛型实际类型的对应关系Map
	 *
	 * @param type 被解析的包含泛型参数的类
	 * @return 泛型对应关系Map
	 */
	private static Map<Type, Type> createTypeMap(Type type) {
		final Map<Type, Type> typeMap = new HashMap<>();

		// 按继承层级寻找泛型变量和实际类型的对应关系
		// 在类中，对应关系分为两类：
		// 1. 父类定义变量，子类标注实际类型
		// 2. 父类定义变量，子类继承这个变量，让子类的子类去标注，以此类推
		// 此方法中我们将每一层级的对应关系全部加入到Map中，查找实际类型的时候，根据传入的泛型变量，
		// 找到对应关系，如果对应的是继承的泛型变量，则递归继续找，直到找到实际或返回null为止。
		// 如果传入的非Class，例如TypeReference，获取到泛型参数中实际的泛型对象类，继续按照类处理
		while (null != type) {
			final ParameterizedType parameterizedType = TypeUtil.toParameterizedType(type);
			if(null == parameterizedType){
				break;
			}
			final Type[] typeArguments = parameterizedType.getActualTypeArguments();
			final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			final Type[] typeParameters = rawType.getTypeParameters();

			for (int i = 0; i < typeParameters.length; i++) {
				typeMap.put(typeParameters[i], typeArguments[i]);
			}

			type = rawType;
		}

		return typeMap;
	}
}
