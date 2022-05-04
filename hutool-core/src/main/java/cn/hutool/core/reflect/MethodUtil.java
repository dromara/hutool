package cn.hutool.core.reflect;

import cn.hutool.core.bean.NullWrapperBean;
import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.UniqueKeySet;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.func.Filter;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodUtil {
	/**
	 * 方法缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Method[]> METHODS_CACHE = new WeakConcurrentMap<>();

	// --------------------------------------------------------------------------------------------------------- method

	/**
	 * 获得指定类本类及其父类中的Public方法名<br>
	 * 去重重载的方法
	 *
	 * @param clazz 类
	 * @return 方法名Set
	 */
	public static Set<String> getPublicMethodNames(final Class<?> clazz) {
		final HashSet<String> methodSet = new HashSet<>();
		final Method[] methodArray = getPublicMethods(clazz);
		if (ArrayUtil.isNotEmpty(methodArray)) {
			for (final Method method : methodArray) {
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
	public static Method[] getPublicMethods(final Class<?> clazz) {
		return null == clazz ? null : clazz.getMethods();
	}

	/**
	 * 获得指定类过滤后的Public方法列表<br>
	 *
	 * @param clazz  查找方法的类
	 * @param filter 过滤器
	 * @return 过滤后的方法数组
	 */
	public static Method[] getPublicMethods(final Class<?> clazz, final Filter<Method> filter) {
		if (null == clazz) {
			return null;
		}

		final Method[] methods = getPublicMethods(clazz);
		if(null == filter){
			return methods;
		}

		return ArrayUtil.filter(methods, filter);
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz          查找方法的类
	 * @param excludeMethods 不包括的方法
	 * @return 过滤后的方法列表
	 */
	public static Method[] getPublicMethods(final Class<?> clazz, final Method... excludeMethods) {
		final HashSet<Method> excludeMethodSet = CollUtil.newHashSet(excludeMethods);
		return getPublicMethods(clazz, method -> false == excludeMethodSet.contains(method));
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz              查找方法的类
	 * @param excludeMethodNames 不包括的方法名列表
	 * @return 过滤后的方法数组
	 */
	public static Method[] getPublicMethods(final Class<?> clazz, final String... excludeMethodNames) {
		final HashSet<String> excludeMethodNameSet = CollUtil.newHashSet(excludeMethodNames);
		return getPublicMethods(clazz, method -> false == excludeMethodNameSet.contains(method.getName()));
	}

	/**
	 * 查找指定Public方法 如果找不到对应的方法或方法不为public的则返回{@code null}
	 *
	 * @param clazz      类
	 * @param methodName 方法名
	 * @param paramTypes 参数类型
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 */
	public static Method getPublicMethod(final Class<?> clazz, final String methodName, final Class<?>... paramTypes) throws SecurityException {
		try {
			return clazz.getMethod(methodName, paramTypes);
		} catch (final NoSuchMethodException ex) {
			return null;
		}
	}

	/**
	 * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
	 *
	 * <p>
	 * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。
	 * </p>
	 *
	 * @param obj        被查找的对象，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param args       参数
	 * @return 方法
	 * @throws SecurityException 无访问权限抛出异常
	 */
	public static Method getMethodOfObj(final Object obj, final String methodName, final Object... args) throws SecurityException {
		if (null == obj || StrUtil.isBlank(methodName)) {
			return null;
		}
		return getMethod(obj.getClass(), methodName, ClassUtil.getClasses(args));
	}

	/**
	 * 忽略大小写查找指定方法，如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 3.2.0
	 */
	public static Method getMethodIgnoreCase(final Class<?> clazz, final String methodName, final Class<?>... paramTypes) throws SecurityException {
		return getMethod(clazz, true, methodName, paramTypes);
	}

	/**
	 * 查找指定方法 如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 */
	public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... paramTypes) throws SecurityException {
		return getMethod(clazz, false, methodName, paramTypes);
	}

	/**
	 * 查找指定方法 如果找不到对应的方法则返回{@code null}<br>
	 * 此方法为精准获取方法名，即方法名和参数数量和类型必须一致，否则返回{@code null}。<br>
	 * 如果查找的方法有多个同参数类型重载，查找第一个找到的方法
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param ignoreCase 是否忽略大小写
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @param paramTypes 参数类型，指定参数类型如果是方法的子类也算
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 3.2.0
	 */
	public static Method getMethod(final Class<?> clazz, final boolean ignoreCase, final String methodName, final Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		final Method[] methods = getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (final Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
						&& ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
						//排除桥接方法，pr#1965@Github
						&& false == method.isBridge()) {
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法，如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法只检查方法名是否一致，并不检查参数的一致性。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 4.3.2
	 */
	public static Method getMethodByName(final Class<?> clazz, final String methodName) throws SecurityException {
		return getMethodByName(clazz, false, methodName);
	}

	/**
	 * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法，如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法只检查方法名是否一致（忽略大小写），并不检查参数的一致性。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 4.3.2
	 */
	public static Method getMethodByNameIgnoreCase(final Class<?> clazz, final String methodName) throws SecurityException {
		return getMethodByName(clazz, true, methodName);
	}

	/**
	 * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法，如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法只检查方法名是否一致，并不检查参数的一致性。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param ignoreCase 是否忽略大小写
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 4.3.2
	 */
	public static Method getMethodByName(final Class<?> clazz, final boolean ignoreCase, final String methodName) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		final Method[] methods = getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (final Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
						// 排除桥接方法
						&& false == method.isBridge()) {
					return method;
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
	public static Set<String> getMethodNames(final Class<?> clazz) throws SecurityException {
		final HashSet<String> methodSet = new HashSet<>();
		final Method[] methods = getMethods(clazz);
		for (final Method method : methods) {
			methodSet.add(method.getName());
		}
		return methodSet;
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz  查找方法的类
	 * @param filter 过滤器
	 * @return 过滤后的方法列表
	 * @throws SecurityException 安全异常
	 */
	public static Method[] getMethods(final Class<?> clazz, final Filter<Method> filter) throws SecurityException {
		if (null == clazz) {
			return null;
		}
		return ArrayUtil.filter(getMethods(clazz), filter);
	}

	/**
	 * 获得一个类中所有方法列表，包括其父类中的方法
	 *
	 * @param beanClass 类，非{@code null}
	 * @return 方法列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Method[] getMethods(final Class<?> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		return METHODS_CACHE.computeIfAbsent(beanClass,
				() -> getMethodsDirectly(beanClass, true, true));
	}

	/**
	 * 获得一个类中所有方法列表，直接反射获取，无缓存<br>
	 * 接口获取方法和默认方法，获取的方法包括：
	 * <ul>
	 *     <li>本类中的所有方法（包括static方法）</li>
	 *     <li>父类中的所有方法（包括static方法）</li>
	 *     <li>Object中（包括static方法）</li>
	 * </ul>
	 *
	 * @param beanClass            类或接口
	 * @param withSupers           是否包括父类或接口的方法列表
	 * @param withMethodFromObject 是否包括Object中的方法
	 * @return 方法列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Method[] getMethodsDirectly(final Class<?> beanClass, final boolean withSupers, final boolean withMethodFromObject) throws SecurityException {
		Assert.notNull(beanClass);

		if (beanClass.isInterface()) {
			// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
			return withSupers ? beanClass.getMethods() : beanClass.getDeclaredMethods();
		}

		final UniqueKeySet<String, Method> result = new UniqueKeySet<>(true, MethodUtil::getUniqueKey);
		Class<?> searchType = beanClass;
		while (searchType != null) {
			if (false == withMethodFromObject && Object.class == searchType) {
				break;
			}
			result.addAllIfAbsent(Arrays.asList(searchType.getDeclaredMethods()));
			result.addAllIfAbsent(getDefaultMethodsFromInterface(searchType));


			searchType = (withSupers && false == searchType.isInterface()) ? searchType.getSuperclass() : null;
		}

		return result.toArray(new Method[0]);
	}

	/**
	 * 是否为equals方法
	 *
	 * @param method 方法
	 * @return 是否为equals方法
	 */
	public static boolean isEqualsMethod(final Method method) {
		if (method == null ||
				1 != method.getParameterCount() ||
				false == "equals".equals(method.getName())) {
			return false;
		}
		return (method.getParameterTypes()[0] == Object.class);
	}

	/**
	 * 是否为hashCode方法
	 *
	 * @param method 方法
	 * @return 是否为hashCode方法
	 */
	public static boolean isHashCodeMethod(final Method method) {
		return method != null//
				&& "hashCode".equals(method.getName())//
				&& isEmptyParam(method);
	}

	/**
	 * 是否为toString方法
	 *
	 * @param method 方法
	 * @return 是否为toString方法
	 */
	public static boolean isToStringMethod(final Method method) {
		return method != null//
				&& "toString".equals(method.getName())//
				&& isEmptyParam(method);
	}

	/**
	 * 是否为无参数方法
	 *
	 * @param method 方法
	 * @return 是否为无参数方法
	 * @since 5.1.1
	 */
	public static boolean isEmptyParam(final Method method) {
		return method.getParameterCount() == 0;
	}

	/**
	 * 检查给定方法是否为Getter或者Setter方法，规则为：<br>
	 * <ul>
	 *     <li>方法参数必须为0个或1个</li>
	 *     <li>如果是无参方法，则判断是否以“get”或“is”开头</li>
	 *     <li>如果方法参数1个，则判断是否以“set”开头</li>
	 * </ul>
	 *
	 * @param method 方法
	 * @return 是否为Getter或者Setter方法
	 * @since 5.7.20
	 */
	public static boolean isGetterOrSetterIgnoreCase(final Method method) {
		return isGetterOrSetter(method, true);
	}

	/**
	 * 检查给定方法是否为Getter或者Setter方法，规则为：<br>
	 * <ul>
	 *     <li>方法参数必须为0个或1个</li>
	 *     <li>方法名称不能是getClass</li>
	 *     <li>如果是无参方法，则判断是否以“get”或“is”开头</li>
	 *     <li>如果方法参数1个，则判断是否以“set”开头</li>
	 * </ul>
	 *
	 * @param method     方法
	 * @param ignoreCase 是否忽略方法名的大小写
	 * @return 是否为Getter或者Setter方法
	 * @since 5.7.20
	 */
	public static boolean isGetterOrSetter(final Method method, final boolean ignoreCase) {
		if (null == method) {
			return false;
		}

		// 参数个数必须为0或1
		final int parameterCount = method.getParameterCount();
		if (parameterCount > 1) {
			return false;
		}

		String name = method.getName();
		// 跳过getClass这个特殊方法
		if ("getClass".equals(name)) {
			return false;
		}
		if (ignoreCase) {
			name = name.toLowerCase();
		}
		switch (parameterCount) {
			case 0:
				return name.startsWith("get") || name.startsWith("is");
			case 1:
				return name.startsWith("set");
			default:
				return false;
		}
	}

	// --------------------------------------------------------------------------------------------------------- invoke

	/**
	 * 执行静态方法
	 *
	 * @param <T>    对象类型
	 * @param method 方法（对象方法或static方法都可）
	 * @param args   参数对象
	 * @return 结果
	 * @throws UtilException 多种异常包装
	 */
	public static <T> T invokeStatic(final Method method, final Object... args) throws UtilException {
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
	 * @param <T>    返回对象类型
	 * @param obj    对象，如果执行静态方法，此值为{@code null}
	 * @param method 方法（对象方法或static方法都可）
	 * @param args   参数对象
	 * @return 结果
	 * @throws UtilException 一些列异常的包装
	 */
	public static <T> T invokeWithCheck(final Object obj, final Method method, final Object... args) throws UtilException {
		final Class<?>[] types = method.getParameterTypes();
		if (null != args) {
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
	 * <p>
	 * 对于用户传入参数会做必要检查，包括：
	 *
	 * <pre>
	 *     1、忽略多余的参数
	 *     2、参数不够补齐默认值
	 *     3、传入参数为null，但是目标参数类型为原始类型，做转换
	 * </pre>
	 *
	 * @param <T>    返回对象类型
	 * @param obj    对象，如果执行静态方法，此值为{@code null}
	 * @param method 方法（对象方法或static方法都可）
	 * @param args   参数对象
	 * @return 结果
	 * @throws UtilException 一些列异常的包装
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(final Object obj, final Method method, final Object... args) throws UtilException {
		ReflectUtil.setAccessible(method);

		// 检查用户传入参数：
		// 1、忽略多余的参数
		// 2、参数不够补齐默认值
		// 3、通过NullWrapperBean传递的参数,会直接赋值null
		// 4、传入参数为null，但是目标参数类型为原始类型，做转换
		// 5、传入参数类型不对应，尝试转换类型
		final Class<?>[] parameterTypes = method.getParameterTypes();
		final Object[] actualArgs = new Object[parameterTypes.length];
		if (null != args) {
			for (int i = 0; i < actualArgs.length; i++) {
				if (i >= args.length || null == args[i]) {
					// 越界或者空值
					actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
				} else if (args[i] instanceof NullWrapperBean) {
					//如果是通过NullWrapperBean传递的null参数,直接赋值null
					actualArgs[i] = null;
				} else if (false == parameterTypes[i].isAssignableFrom(args[i].getClass())) {
					//对于类型不同的字段，尝试转换，转换失败则使用原对象类型
					final Object targetValue = Convert.convert(parameterTypes[i], args[i]);
					if (null != targetValue) {
						actualArgs[i] = targetValue;
					}
				} else {
					actualArgs[i] = args[i];
				}
			}
		}

		if (method.isDefault()) {
			// 当方法是default方法时，尤其对象是代理对象，需使用句柄方式执行
			// 代理对象情况下调用method.invoke会导致循环引用执行，最终栈溢出
			return MethodHandleUtil.invokeSpecial(obj, method, args);
		}

		try {
			return (T) method.invoke(ModifierUtil.isStatic(method) ? null : obj, actualArgs);
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 执行对象中指定方法
	 * 如果需要传递的参数为null,请使用NullWrapperBean来传递,不然会丢失类型信息
	 *
	 * @param <T>        返回对象类型
	 * @param obj        方法所在对象
	 * @param methodName 方法名
	 * @param args       参数列表
	 * @return 执行结果
	 * @throws UtilException IllegalAccessException包装
	 * @see NullWrapperBean
	 * @since 3.1.2
	 */
	public static <T> T invoke(final Object obj, final String methodName, final Object... args) throws UtilException {
		Assert.notNull(obj, "Object to get method must be not null!");
		Assert.notBlank(methodName, "Method name must be not blank!");

		final Method method = getMethodOfObj(obj, methodName, args);
		if (null == method) {
			throw new UtilException("No such method: [{}] from [{}]", methodName, obj.getClass());
		}
		return invoke(obj, method, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 非单例模式，如果是非静态方法，每次创建一个新对象
	 *
	 * @param <T>                     对象类型
	 * @param classNameWithMethodName 类名和方法名表达式，类名与方法名用{@code .}或{@code #}连接 例如：com.xiaoleilu.hutool.StrUtil.isEmpty 或 com.xiaoleilu.hutool.StrUtil#isEmpty
	 * @param args                    参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(final String classNameWithMethodName, final Object[] args) {
		return invoke(classNameWithMethodName, false, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 *
	 * @param <T>                     对象类型
	 * @param classNameWithMethodName 类名和方法名表达式，例如：com.xiaoleilu.hutool.StrUtil#isEmpty或com.xiaoleilu.hutool.StrUtil.isEmpty
	 * @param isSingleton             是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args                    参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(final String classNameWithMethodName, final boolean isSingleton, final Object... args) {
		if (StrUtil.isBlank(classNameWithMethodName)) {
			throw new UtilException("Blank classNameDotMethodName!");
		}

		int splitIndex = classNameWithMethodName.lastIndexOf('#');
		if (splitIndex <= 0) {
			splitIndex = classNameWithMethodName.lastIndexOf('.');
		}
		if (splitIndex <= 0) {
			throw new UtilException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
		}

		final String className = classNameWithMethodName.substring(0, splitIndex);
		final String methodName = classNameWithMethodName.substring(splitIndex + 1);

		return invoke(className, methodName, isSingleton, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 非单例模式，如果是非静态方法，每次创建一个新对象
	 *
	 * @param <T>        对象类型
	 * @param className  类名，完整类路径
	 * @param methodName 方法名
	 * @param args       参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(final String className, final String methodName, final Object[] args) {
		return invoke(className, methodName, false, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 *
	 * @param <T>         对象类型
	 * @param className   类名，完整类路径
	 * @param methodName  方法名
	 * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args        参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(final String className, final String methodName, final boolean isSingleton, final Object... args) {
		final Class<?> clazz = ClassLoaderUtil.loadClass(className);
		try {
			final Method method = MethodUtil.getMethod(clazz, methodName, ClassUtil.getClasses(args));
			if (null == method) {
				throw new NoSuchMethodException(StrUtil.format("No such method: [{}]", methodName));
			}
			if (ModifierUtil.isStatic(method)) {
				return MethodUtil.invoke(null, method, args);
			} else {
				return MethodUtil.invoke(isSingleton ? Singleton.get(clazz) : clazz.newInstance(), method, args);
			}
		} catch (final Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 获取方法的唯一键，结构为:
	 * <pre>
	 *     返回类型#方法名:参数1类型,参数2类型...
	 * </pre>
	 *
	 * @param method 方法
	 * @return 方法唯一键
	 */
	private static String getUniqueKey(final Method method) {
		final StringBuilder sb = new StringBuilder();
		sb.append(method.getReturnType().getName()).append('#');
		sb.append(method.getName());
		final Class<?>[] parameters = method.getParameterTypes();
		for (int i = 0; i < parameters.length; i++) {
			if (i == 0) {
				sb.append(':');
			} else {
				sb.append(',');
			}
			sb.append(parameters[i].getName());
		}
		return sb.toString();
	}

	/**
	 * 获取类对应接口中的非抽象方法（default方法）
	 *
	 * @param clazz 类
	 * @return 方法列表
	 */
	private static List<Method> getDefaultMethodsFromInterface(final Class<?> clazz) {
		final List<Method> result = new ArrayList<>();
		for (final Class<?> ifc : clazz.getInterfaces()) {
			for (final Method m : ifc.getMethods()) {
				if (false == ModifierUtil.isAbstract(m)) {
					result.add(m);
				}
			}
		}
		return result;
	}
}
