/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.reflect.method;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.NullWrapperBean;
import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.collection.set.UniqueKeySet;
import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.exception.ExceptionUtil;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Singleton;
import org.dromara.hutool.core.map.WeakConcurrentMap;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.ModifierUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.BooleanUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * 反射中{@link Method}相关工具类，包括方法获取和方法执行<br>
 * TODO 与commons-beanutils相比，Hutool缓存某个类的所有方法，而commons缓存单个方法，须性能测试哪个更加合理。
 *
 * @author looly
 */
public class MethodUtil {
	/**
	 * 方法缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Method[]> METHODS_CACHE = new WeakConcurrentMap<>();
	/**
	 * 直接声明的方法缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Method[]> DECLARED_METHODS_CACHE = new WeakConcurrentMap<>();

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
	 * @param clazz     查找方法的类
	 * @param predicate 过滤器，{@link Predicate#test(Object)}为{@code true}保留，null表示保留全部
	 * @return 过滤后的方法数组
	 */
	public static Method[] getPublicMethods(final Class<?> clazz, final Predicate<Method> predicate) {
		if (null == clazz) {
			return null;
		}

		final Method[] methods = getPublicMethods(clazz);
		if (null == predicate) {
			return methods;
		}

		return ArrayUtil.filter(methods, predicate);
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz          查找方法的类
	 * @param excludeMethods 不包括的方法
	 * @return 过滤后的方法列表
	 */
	public static Method[] getPublicMethods(final Class<?> clazz, final Method... excludeMethods) {
		final HashSet<Method> excludeMethodSet = SetUtil.of(excludeMethods);
		return getPublicMethods(clazz, method -> !excludeMethodSet.contains(method));
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz              查找方法的类
	 * @param excludeMethodNames 不包括的方法名列表
	 * @return 过滤后的方法数组
	 */
	public static Method[] getPublicMethods(final Class<?> clazz, final String... excludeMethodNames) {
		final HashSet<String> excludeMethodNameSet = SetUtil.of(excludeMethodNames);
		return getPublicMethods(clazz, method -> !excludeMethodNameSet.contains(method.getName()));
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

		Method res = null;
		final Method[] methods = getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (final Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
					&& ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
					//排除桥接方法，pr#1965@Github
					//排除协变桥接方法，pr#1965@Github
					&& (res == null || res.getReturnType().isAssignableFrom(method.getReturnType()))) {
					res = method;
				}
			}
		}
		return res;
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

		Method res = null;
		final Method[] methods = getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (final Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
					//排除协变桥接方法，pr#1965@Github
					&& (res == null || res.getReturnType().isAssignableFrom(method.getReturnType()))) {
					res = method;
				}
			}
		}
		return res;
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
	 * 获得指定类过滤后的方法列表
	 *
	 * @param clazz     查找方法的类
	 * @param predicate 过滤器，{@link Predicate#test(Object)}为{@code true}保留，null表示全部保留。
	 * @return 过滤后的方法列表
	 * @throws SecurityException 安全异常
	 */
	public static Method[] getMethods(final Class<?> clazz, final Predicate<Method> predicate) throws SecurityException {
		if (null == clazz) {
			return null;
		}
		return ArrayUtil.filter(getMethods(clazz), predicate);
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
			(key) -> getMethodsDirectly(beanClass, true, true));
	}

	/**
	 * 获得类中所有直接声明方法，不包括其父类中的方法
	 *
	 * @param beanClass 类，非{@code null}
	 * @return 方法列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Method[] getDeclaredMethods(final Class<?> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		return DECLARED_METHODS_CACHE.computeIfAbsent(beanClass,
			key -> getMethodsDirectly(beanClass, false, Objects.equals(Object.class, beanClass)));
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
			if (!withMethodFromObject && Object.class == searchType) {
				break;
			}
			result.addAllIfAbsent(Arrays.asList(searchType.getDeclaredMethods()));
			result.addAllIfAbsent(getDefaultMethodsFromInterface(searchType));


			searchType = (withSupers && !searchType.isInterface()) ? searchType.getSuperclass() : null;
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
			!"equals".equals(method.getName())) {
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
		// 参数个数必须为1
		final int parameterCount = method.getParameterCount();
		switch (parameterCount){
			case 0:
				return isGetter(method, ignoreCase);
			case 1:
				return isSetter(method, ignoreCase);
			default:
				return false;
		}
	}

	/**
	 * 检查给定方法是否为Setter方法，规则为：<br>
	 * <ul>
	 *     <li>方法参数必须为1个</li>
	 *     <li>判断是否以“set”开头</li>
	 * </ul>
	 *
	 * @param method     方法
	 * @param ignoreCase 是否忽略方法名的大小写
	 * @return 是否为Setter方法
	 */
	public static boolean isSetter(final Method method, final boolean ignoreCase) {
		if (null == method) {
			return false;
		}

		// 参数个数必须为1
		final int parameterCount = method.getParameterCount();
		if (1 != parameterCount) {
			return false;
		}

		String name = method.getName();
		// 跳过set这类特殊方法
		if ("set".equals(name)) {
			return false;
		}

		if (ignoreCase) {
			name = name.toLowerCase();
		}
		return name.startsWith("set");
	}

	/**
	 * 检查给定方法是否为Getter方法，规则为：<br>
	 * <ul>
	 *     <li>方法参数必须为0个</li>
	 *     <li>方法名称不能是getClass</li>
	 *     <li>"is"开头返回必须为boolean或Boolean</li>
	 *     <li>是否以“get”</li>
	 * </ul>
	 *
	 * @param method     方法
	 * @param ignoreCase 是否忽略方法名的大小写
	 * @return 是否为Getter方法
	 */
	public static boolean isGetter(final Method method, final boolean ignoreCase) {
		if (null == method) {
			return false;
		}

		// 参数个数必须为0或1
		final int parameterCount = method.getParameterCount();
		if (0 != parameterCount) {
			return false;
		}

		String name = method.getName();
		// 跳过getClass、get、is这类特殊方法
		if ("getClass".equals(name) || "get".equals(name) || "is".equals(name)) {
			return false;
		}

		if (ignoreCase) {
			name = name.toLowerCase();
		}

		if (name.startsWith("is")) {
			// 判断返回值是否为Boolean
			return BooleanUtil.isBoolean(method.getReturnType());
		}
		return name.startsWith("get");
	}

	// --------------------------------------------------------------------------------------------------------- invoke

	/**
	 * 执行静态方法
	 *
	 * @param <T>    对象类型
	 * @param method 方法（对象方法或static方法都可）
	 * @param args   参数对象
	 * @return 结果
	 * @throws HutoolException 多种异常包装
	 */
	public static <T> T invokeStatic(final Method method, final Object... args) throws HutoolException {
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
	 * @throws HutoolException 一些列异常的包装
	 */
	public static <T> T invokeWithCheck(final Object obj, final Method method, final Object... args) throws HutoolException {
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
	 * @throws HutoolException 一些列异常的包装
	 * @see MethodHandleUtil#invoke(Object, Method, Object...)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(final Object obj, final Method method, final Object... args) throws HutoolException {
		try{
			return MethodHandleUtil.invoke(obj, method, args);
		} catch (final Exception e){
			// 传统反射方式执行方法
			try {
				return (T) method.invoke(ModifierUtil.isStatic(method) ? null : obj, actualArgs(method, args));
			} catch (final IllegalAccessException | InvocationTargetException ex) {
				throw new HutoolException(ex);
			}
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
	 * @throws HutoolException IllegalAccessException包装
	 * @see NullWrapperBean
	 * @since 3.1.2
	 */
	public static <T> T invoke(final Object obj, final String methodName, final Object... args) throws HutoolException {
		Assert.notNull(obj, "Object to get method must be not null!");
		Assert.notBlank(methodName, "Method name must be not blank!");

		final Method method = getMethodOfObj(obj, methodName, args);
		if (null == method) {
			throw new HutoolException("No such method: [{}] from [{}]", methodName, obj.getClass());
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
	 * @param classNameWithMethodName 类名和方法名表达式，类名与方法名用{@code .}或{@code #}连接
	 *                                   例如：org.dromara.hutool.core.text.StrUtil.isEmpty 或 org.dromara.hutool.core.text.StrUtil#isEmpty
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
	 * @param classNameWithMethodName 类名和方法名表达式，
	 *                                   例如：org.dromara.hutool.core.text.StrUtil#isEmpty或org.dromara.hutool.core.text.StrUtil.isEmpty
	 * @param isSingleton             是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args                    参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(final String classNameWithMethodName, final boolean isSingleton, final Object... args) {
		if (StrUtil.isBlank(classNameWithMethodName)) {
			throw new HutoolException("Blank classNameDotMethodName!");
		}

		int splitIndex = classNameWithMethodName.lastIndexOf('#');
		if (splitIndex <= 0) {
			splitIndex = classNameWithMethodName.lastIndexOf('.');
		}
		if (splitIndex <= 0) {
			throw new HutoolException("Invalid classNameWithMethodName [{}]!", classNameWithMethodName);
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
			final Method method = getMethod(clazz, methodName, ClassUtil.getClasses(args));
			if (null == method) {
				throw new NoSuchMethodException(StrUtil.format("No such method: [{}]", methodName));
			}
			if (ModifierUtil.isStatic(method)) {
				return invoke(null, method, args);
			} else {
				return invoke(isSingleton ? Singleton.get(clazz) : ConstructorUtil.newInstance(clazz), method, args);
			}
		} catch (final Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}
	}

	/**
	 * 检查用户传入参数：
	 * <ul>
	 *     <li>1、忽略多余的参数</li>
	 *     <li>2、参数不够补齐默认值</li>
	 *     <li>3、通过NullWrapperBean传递的参数,会直接赋值null</li>
	 *     <li>4、传入参数为null，但是目标参数类型为原始类型，做转换</li>
	 *     <li>5、传入参数类型不对应，尝试转换类型</li>
	 * </ul>
	 *
	 * @param method 方法
	 * @param args   参数
	 * @return 实际的参数数组
	 */
	public static Object[] actualArgs(final Method method, final Object[] args) {
		final Class<?>[] parameterTypes = method.getParameterTypes();
		if(1 == parameterTypes.length && parameterTypes[0].isArray()){
			// 可变长参数，不做转换
			return args;
		}
		final Object[] actualArgs = new Object[parameterTypes.length];
		if (null != args) {
			for (int i = 0; i < actualArgs.length; i++) {
				if (i >= args.length || null == args[i]) {
					// 越界或者空值
					actualArgs[i] = ClassUtil.getDefaultValue(parameterTypes[i]);
				} else if (args[i] instanceof NullWrapperBean) {
					//如果是通过NullWrapperBean传递的null参数,直接赋值null
					actualArgs[i] = null;
				} else if (!parameterTypes[i].isAssignableFrom(args[i].getClass())) {
					//对于类型不同的字段，尝试转换，转换失败则使用原对象类型
					final Object targetValue = Convert.convert(parameterTypes[i], args[i], args[i]);
					if (null != targetValue) {
						actualArgs[i] = targetValue;
					}
				} else {
					actualArgs[i] = args[i];
				}
			}
		}

		return actualArgs;
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
				if (!ModifierUtil.isAbstract(m)) {
					result.add(m);
				}
			}
		}
		return result;
	}
}
