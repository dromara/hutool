package cn.hutool.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.map.MapUtil;

/**
 * 反射工具类
 * 
 * @author Looly
 * @since 3.0.9
 */
public class ReflectUtil {

	/** 构造对象缓存 */
	private static final SimpleCache<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new SimpleCache<>();
	/** 字段缓存 */
	private static final SimpleCache<Class<?>, Field[]> FIELDS_CACHE = new SimpleCache<>();
	/** 方法缓存 */
	private static final SimpleCache<Class<?>, Method[]> METHODS_CACHE = new SimpleCache<>();

	// --------------------------------------------------------------------------------------------------------- Constructor
	/**
	 * 查找类中的指定参数的构造方法，如果找到构造方法，会自动设置可访问为true
	 * 
	 * @param <T> 对象类型
	 * @param clazz 类
	 * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可，此参数可以不传
	 * @return 构造方法，如果未找到返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
		if (null == clazz) {
			return null;
		}

		final Constructor<?>[] constructors = getConstructors(clazz);
		Class<?>[] pts;
		for (Constructor<?> constructor : constructors) {
			pts = constructor.getParameterTypes();
			if (ClassUtil.isAllAssignableFrom(pts, parameterTypes)) {
				// 构造可访问
				constructor.setAccessible(true);
				return (Constructor<T>) constructor;
			}
		}
		return null;
	}

	/**
	 * 获得一个类中所有构造列表
	 * 
	 * @param <T> 构造的对象类型
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] getConstructors(Class<T> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		Constructor<?>[] constructors = CONSTRUCTORS_CACHE.get(beanClass);
		if (null != constructors) {
			return (Constructor<T>[]) constructors;
		}

		constructors = getConstructorsDirectly(beanClass);
		return (Constructor<T>[]) CONSTRUCTORS_CACHE.put(beanClass, constructors);
	}

	/**
	 * 获得一个类中所有字段列表，直接反射获取，无缓存
	 * 
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		return beanClass.getDeclaredConstructors();
	}

	// --------------------------------------------------------------------------------------------------------- Field
	/**
	 * 查找指定类中的所有字段（包括非public字段），也包括父类和Object类的字段， 字段不存在则返回<code>null</code>
	 * 
	 * @param beanClass 被查找字段的类,不能为null
	 * @param name 字段名
	 * @return 字段
	 * @throws SecurityException 安全异常
	 */
	public static Field getField(Class<?> beanClass, String name) throws SecurityException {
		final Field[] fields = getFields(beanClass);
		if (ArrayUtil.isNotEmpty(fields)) {
			for (Field field : fields) {
				if ((name.equals(field.getName()))) {
					return field;
				}
			}
		}
		return null;
	}

	/**
	 * 获得一个类中所有字段列表，包括其父类中的字段
	 * 
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Field[] getFields(Class<?> beanClass) throws SecurityException {
		Field[] allFields = FIELDS_CACHE.get(beanClass);
		if (null != allFields) {
			return allFields;
		}

		allFields = getFieldsDirectly(beanClass, true);
		return FIELDS_CACHE.put(beanClass, allFields);
	}

	/**
	 * 获得一个类中所有字段列表，直接反射获取，无缓存
	 * 
	 * @param beanClass 类
	 * @param withSuperClassFieds 是否包括父类的字段列表
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Field[] getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFieds) throws SecurityException {
		Assert.notNull(beanClass);

		Field[] allFields = null;
		Class<?> searchType = beanClass;
		Field[] declaredFields;
		while (searchType != null) {
			declaredFields = searchType.getDeclaredFields();
			if (null == allFields) {
				allFields = declaredFields;
			} else {
				allFields = ArrayUtil.append(allFields, declaredFields);
			}
			searchType = withSuperClassFieds ? searchType.getSuperclass() : null;
		}

		return allFields;
	}

	/**
	 * 获取字段值
	 * 
	 * @param obj 对象
	 * @param fieldName 字段名
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static Object getFieldValue(Object obj, String fieldName) throws UtilException {
		if (null == obj || StrUtil.isBlank(fieldName)) {
			return null;
		}
		return getFieldValue(obj, getField(obj.getClass(), fieldName));
	}

	/**
	 * 获取字段值
	 * 
	 * @param obj 对象
	 * @param field 字段
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static Object getFieldValue(Object obj, Field field) throws UtilException {
		if (null == obj || null == field) {
			return null;
		}
		field.setAccessible(true);
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			throw new UtilException(e, "IllegalAccess for {}.{}", obj.getClass(), field.getName());
		}
		return result;
	}

	/**
	 * 获取所有字段的值
	 * @param obj bean对象
	 * @return 字段值数组
	 * @since 4.1.17
	 */
	public static Object[] getFieldsValue(Object obj) {
		if (null != obj) {
			final Field[] fields = getFields(obj.getClass());
			if (null != fields) {
				final Object[] values = new Object[fields.length];
				for (int i = 0; i < fields.length; i++) {
					values[i] = getFieldValue(obj, fields[i]);
				}
				return values;
			}
		}
		return null;
	}

	/**
	 * 设置字段值
	 * 
	 * @param obj 对象
	 * @param fieldName 字段名
	 * @param value 值，值类型必须与字段类型匹配，不会自动转换对象类型
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) throws UtilException {
		Assert.notNull(obj);
		Assert.notBlank(fieldName);
		setFieldValue(obj, getField(obj.getClass(), fieldName), value);
	}

	/**
	 * 设置字段值
	 * 
	 * @param obj 对象
	 * @param field 字段
	 * @param value 值，值类型必须与字段类型匹配，不会自动转换对象类型
	 * @throws UtilException UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(Object obj, Field field, Object value) throws UtilException {
		Assert.notNull(obj);
		Assert.notNull(field);
		field.setAccessible(true);

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			throw new UtilException(e, "IllegalAccess for {}.{}", obj.getClass(), field.getName());
		}
	}

	// --------------------------------------------------------------------------------------------------------- method
	/**
	 * 获得指定类本类及其父类中的Public方法名<br>
	 * 去重重载的方法
	 * 
	 * @param clazz 类
	 * @return 方法名Set
	 */
	public static Set<String> getPublicMethodNames(Class<?> clazz) {
		final HashSet<String> methodSet = new HashSet<String>();
		final Method[] methodArray = getPublicMethods(clazz);
		if(ArrayUtil.isNotEmpty(methodArray)) {
			for (Method method : methodArray) {
				methodSet.add(method.getName());
			}
		}
		return methodSet;
	}

	/**
	 * 获得本类及其父类所有Public方法
	 * 
	 * @param clazz 查找方法的类
	 * @return 过滤后的方法列表
	 */
	public static Method[] getPublicMethods(Class<?> clazz) {
		return null == clazz ? null : clazz.getMethods();
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 * 
	 * @param clazz 查找方法的类
	 * @param filter 过滤器
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, Filter<Method> filter) {
		if (null == clazz) {
			return null;
		}

		final Method[] methods = getPublicMethods(clazz);
		List<Method> methodList;
		if (null != filter) {
			methodList = new ArrayList<>();
			for (Method method : methods) {
				if (filter.accept(method)) {
					methodList.add(method);
				}
			}
		} else {
			methodList = CollectionUtil.newArrayList(methods);
		}
		return methodList;
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 * 
	 * @param clazz 查找方法的类
	 * @param excludeMethods 不包括的方法
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, Method... excludeMethods) {
		final HashSet<Method> excludeMethodSet = CollectionUtil.newHashSet(excludeMethods);
		return getPublicMethods(clazz, new Filter<Method>() {
			@Override
			public boolean accept(Method method) {
				return false == excludeMethodSet.contains(method);
			}
		});
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 * 
	 * @param clazz 查找方法的类
	 * @param excludeMethodNames 不包括的方法名列表
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, String... excludeMethodNames) {
		final HashSet<String> excludeMethodNameSet = CollectionUtil.newHashSet(excludeMethodNames);
		return getPublicMethods(clazz, new Filter<Method>() {
			@Override
			public boolean accept(Method method) {
				return false == excludeMethodNameSet.contains(method.getName());
			}
		});
	}

	/**
	 * 查找指定Public方法 如果找不到对应的方法或方法不为public的则返回<code>null</code>
	 * 
	 * @param clazz 类
	 * @param methodName 方法名
	 * @param paramTypes 参数类型
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 */
	public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
		try {
			return clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException ex) {
			return null;
		}
	}
	
	/**
	 * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
	 * 
	 * @param obj 被查找的对象，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param args 参数
	 * @return 方法
	 * @throws SecurityException 无访问权限抛出异常
	 */
	public static Method getMethodOfObj(Object obj, String methodName, Object... args) throws SecurityException {
		if (null == obj || StrUtil.isBlank(methodName)) {
			return null;
		}
		return getMethod(obj.getClass(), methodName, ClassUtil.getClasses(args));
	}

	/**
	 * 忽略大小写查找指定方法，如果找不到对应的方法则返回<code>null</code>
	 * 
	 * @param clazz 类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 3.2.0
	 */
	public static Method getMethodIgnoreCase(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
		return getMethod(clazz, true, methodName, paramTypes);
	}

	/**
	 * 查找指定方法 如果找不到对应的方法则返回<code>null</code>
	 * 
	 * @param clazz 类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 */
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
		return getMethod(clazz, false, methodName, paramTypes);
	}

	/**
	 * 查找指定方法 如果找不到对应的方法则返回<code>null</code>
	 * 
	 * @param clazz 类，如果为{@code null}返回{@code null}
	 * @param ignoreCase 是否忽略大小写
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 3.2.0
	 */
	public static Method getMethod(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		final Method[] methods = getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)) {
					if (ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)) {
						return method;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 获得指定类中的Public方法名<br>
	 * 去重重载的方法
	 * 
	 * @param clazz 类
	 * @return 方法名Set
	 * @throws SecurityException 安全异常
	 */
	public static Set<String> getMethodNames(Class<?> clazz) throws SecurityException {
		final HashSet<String> methodSet = new HashSet<String>();
		final Method[] methods = getMethods(clazz);
		for (Method method : methods) {
			methodSet.add(method.getName());
		}
		return methodSet;
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 * 
	 * @param clazz 查找方法的类
	 * @param filter 过滤器
	 * @return 过滤后的方法列表
	 * @throws SecurityException 安全异常
	 */
	public static Method[] getMethods(Class<?> clazz, Filter<Method> filter) throws SecurityException {
		if (null == clazz) {
			return null;
		}

		final Method[] methods = getMethods(clazz);
		if (null == filter) {
			return methods;
		}

		final List<Method> methodList = new ArrayList<>();
		for (Method method : methods) {
			if (filter.accept(method)) {
				methodList.add(method);
			}
		}
		return methodList.toArray(new Method[methodList.size()]);
	}

	/**
	 * 获得一个类中所有方法列表，包括其父类中的方法
	 * 
	 * @param beanClass 类
	 * @return 方法列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Method[] getMethods(Class<?> beanClass) throws SecurityException {
		Method[] allMethods = METHODS_CACHE.get(beanClass);
		if (null != allMethods) {
			return allMethods;
		}

		allMethods = getMethodsDirectly(beanClass, true);
		return METHODS_CACHE.put(beanClass, allMethods);
	}

	/**
	 * 获得一个类中所有方法列表，直接反射获取，无缓存
	 * 
	 * @param beanClass 类
	 * @param withSuperClassMethods 是否包括父类的方法列表
	 * @return 方法列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Method[] getMethodsDirectly(Class<?> beanClass, boolean withSuperClassMethods) throws SecurityException {
		Assert.notNull(beanClass);

		Method[] allMethods = null;
		Class<?> searchType = beanClass;
		Method[] declaredMethods;
		while (searchType != null) {
			declaredMethods = searchType.getDeclaredMethods();
			if (null == allMethods) {
				allMethods = declaredMethods;
			} else {
				allMethods = ArrayUtil.append(allMethods, declaredMethods);
			}
			searchType = withSuperClassMethods ? searchType.getSuperclass() : null;
		}

		return allMethods;
	}

	/**
	 * 是否为equals方法
	 * 
	 * @param method 方法
	 * @return 是否为equals方法
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null || false == method.getName().equals("equals")) {
			return false;
		}
		final Class<?>[] paramTypes = method.getParameterTypes();
		return (1 == paramTypes.length && paramTypes[0] == Object.class);
	}

	/**
	 * 是否为hashCode方法
	 * 
	 * @param method 方法
	 * @return 是否为hashCode方法
	 */
	public static boolean isHashCodeMethod(Method method) {
		return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
	}

	/**
	 * 是否为toString方法
	 * 
	 * @param method 方法
	 * @return 是否为toString方法
	 */
	public static boolean isToStringMethod(Method method) {
		return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
	}

	// --------------------------------------------------------------------------------------------------------- newInstance
	/**
	 * 实例化对象
	 * 
	 * @param <T> 对象类型
	 * @param clazz 类名
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazz) throws UtilException {
		try {
			return (T) Class.forName(clazz).newInstance();
		} catch (Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 实例化对象
	 * 
	 * @param <T> 对象类型
	 * @param clazz 类
	 * @param params 构造函数参数
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz, Object... params) throws UtilException {
		if (ArrayUtil.isEmpty(params)) {
			if (Map.class.isAssignableFrom(clazz)) {
				// Map
				if (LinkedHashMap.class.isAssignableFrom(clazz)) {
					return (T) MapUtil.newHashMap(true);
				} else {
					return (T) MapUtil.newHashMap();
				}
			} else if (Iterable.class.isAssignableFrom(clazz)) {
				// Iterable
				if (LinkedHashSet.class.isAssignableFrom(clazz)) {
					return (T) new LinkedHashSet<>();
				} else if (Set.class.isAssignableFrom(clazz)) {
					return (T) new HashSet<>();
				} else if (LinkedList.class.isAssignableFrom(clazz)) {
					return (T) new LinkedList<>();
				} else {
					return (T) CollUtil.newArrayList();
				}
			}

			final Constructor<T> constructor = getConstructor(clazz);
			try {
				return constructor.newInstance();
			} catch (Exception e) {
				throw new UtilException(e, "Instance class [{}] error!", clazz);
			}
		}

		final Class<?>[] paramTypes = ClassUtil.getClasses(params);
		final Constructor<T> constructor = getConstructor(clazz, paramTypes);
		if (null == constructor) {
			throw new UtilException("No Constructor matched for parameter types: [{}]", new Object[] { paramTypes });
		}
		try {
			return constructor.newInstance(params);
		} catch (Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
	 * 
	 * @param <T> 对象类型
	 * @param beanClass 被构造的类
	 * @return 构造后的对象
	 */
	public static <T> T newInstanceIfPossible(Class<T> beanClass) {
		Assert.notNull(beanClass);
		try {
			return newInstance(beanClass);
		} catch (Exception e) {
			// ignore
			// 默认构造不存在的情况下查找其它构造
		}

		final Constructor<T>[] constructors = getConstructors(beanClass);
		Class<?>[] parameterTypes;
		for (Constructor<T> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			if (0 == parameterTypes.length) {
				continue;
			}
			constructor.setAccessible(true);
			try {
				constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
			} catch (Exception e) {
				// 构造出错时继续尝试下一种构造方式
				continue;
			}
		}
		return null;
	}

	// --------------------------------------------------------------------------------------------------------- invoke
	/**
	 * 执行静态方法
	 * 
	 * @param <T> 对象类型
	 * @param method 方法（对象方法或static方法都可）
	 * @param args 参数对象
	 * @return 结果
	 * @throws UtilException 多种异常包装
	 */
	public static <T> T invokeStatic(Method method, Object... args) throws UtilException {
		return invoke(null, method, args);
	}

	/**
	 * 执行方法<br>
	 * 执行前要检查给定参数：
	 * 
	 * <pre>
	 * 1. 参数个数是否与方法参数个数一致
	 * 2. 如果某个参数为null但是方法这个位置的参数为原始类型，则赋予原始类型默认值
	 * </pre>
	 * 
	 * @param <T> 返回对象类型
	 * @param obj 对象，如果执行静态方法，此值为<code>null</code>
	 * @param method 方法（对象方法或static方法都可）
	 * @param args 参数对象
	 * @return 结果
	 * @throws UtilException 一些列异常的包装
	 */
	public static <T> T invokeWithCheck(Object obj, Method method, Object... args) throws UtilException {
		final Class<?>[] types = method.getParameterTypes();
		if (null != types && null != args) {
			Assert.isTrue(args.length == types.length, "Params length [{}] is not fit for param length [{}] of method !", args.length, types.length);
			Class<?> type;
			for (int i = 0; i < args.length; i++) {
				type = types[i];
				if (type.isPrimitive() && null == args[i]) {
					// 参数是原始类型，而传入参数为null时赋予默认值
					args[i] = ClassUtil.getDefaultValue(type);
				}
			}
		}

		return invoke(obj, method, args);
	}

	/**
	 * 执行方法
	 * 
	 * @param <T> 返回对象类型
	 * @param obj 对象，如果执行静态方法，此值为<code>null</code>
	 * @param method 方法（对象方法或static方法都可）
	 * @param args 参数对象
	 * @return 结果
	 * @throws UtilException 一些列异常的包装
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object obj, Method method, Object... args) throws UtilException {
		if (false == method.isAccessible()) {
			method.setAccessible(true);
		}

		try {
			return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, args);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 执行对象中指定方法
	 * 
	 * @param <T> 返回对象类型
	 * @param obj 方法所在对象
	 * @param methodName 方法名
	 * @param args 参数列表
	 * @return 执行结果
	 * @throws UtilException IllegalAccessException包装
	 * @since 3.1.2
	 */
	public static <T> T invoke(Object obj, String methodName, Object... args) throws UtilException {
		final Method method = getMethodOfObj(obj, methodName, args);
		if (null == method) {
			throw new UtilException(StrUtil.format("No such method: [{}]", methodName));
		}
		return invoke(obj, method, args);
	}
}
