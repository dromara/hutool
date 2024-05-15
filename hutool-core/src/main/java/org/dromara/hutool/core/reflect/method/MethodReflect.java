/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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
import org.dromara.hutool.core.collection.set.UniqueKeySet;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.ModifierUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 方法反射相关操作类。
 *
 * @author Looly
 * @since 6.0.0
 */
public class MethodReflect {

	/**
	 * 获取反射对象
	 *
	 * @param clazz 类
	 * @return MethodReflect
	 */
	public static MethodReflect of(final Class<?> clazz) {
		return new MethodReflect(clazz);
	}

	private final Class<?> clazz;
	private volatile Method[] publicMethods;
	private volatile Method[] declaredMethods;
	private volatile Method[] allMethods;

	/**
	 * 构造
	 *
	 * @param clazz 类
	 */
	public MethodReflect(final Class<?> clazz) {
		this.clazz = Assert.notNull(clazz);
	}

	/**
	 * 获取当前类
	 *
	 * @return 当前类
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * 清空缓存
	 */
	synchronized public void clearCaches() {
		publicMethods = null;
		declaredMethods = null;
		allMethods = null;
	}

	// region ----- getMathod



	// endregion

	// region ----- getMathods
	/**
	 * 获取当前类及父类的所有公共方法，等同于{@link Class#getMethods()}
	 *
	 * @param predicate 方法过滤器，{@code null}表示无过滤
	 * @return 当前类及父类的所有公共方法
	 */
	public Method[] getPublicMethods(final Predicate<Method> predicate) {
		if (null == publicMethods) {
			synchronized (MethodReflect.class) {
				if (null == publicMethods) {
					publicMethods = clazz.getMethods();
				}
			}
		}
		return ArrayUtil.filter(publicMethods, predicate);
	}

	/**
	 * 获取当前类直接声明的所有方法，等同于{@link Class#getDeclaredMethods()}
	 *
	 * @param predicate 方法过滤器，{@code null}表示无过滤
	 * @return 当前类及父类的所有公共方法
	 */
	public Method[] getDeclaredMethods(final Predicate<Method> predicate) {
		if (null == declaredMethods) {
			synchronized (MethodReflect.class) {
				if (null == declaredMethods) {
					declaredMethods = clazz.getDeclaredMethods();
				}
			}
		}
		return ArrayUtil.filter(declaredMethods, predicate);
	}

	/**
	 * <p>获取当前类层级结构中的所有方法。<br>
	 * 等同于按广度优先遍历类及其所有父类与接口，并依次调用{@link Class#getDeclaredMethods()}。<br>
	 * 返回的方法排序规则如下：
	 * <ul>
	 *     <li>离{@code type}距离越近，则顺序越靠前；</li>
	 *     <li>与{@code type}距离相同，直接实现的接口方法优先于父类方法；</li>
	 *     <li>与{@code type}距离相同的接口，则顺序遵循接口在{@link Class#getInterfaces()}的顺序；</li>
	 * </ul>
	 *
	 * @param predicate 方法过滤器，{@code null}表示无过滤
	 * @return 当前类及父类的所有公共方法
	 */
	public Method[] getAllMethods(final Predicate<Method> predicate) {
		if (null == allMethods) {
			synchronized (MethodReflect.class) {
				if (null == allMethods) {
					allMethods = getMethodsDirectly(true, true);
				}
			}
		}
		return ArrayUtil.filter(allMethods, predicate);
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
	 * @param withSupers           是否包括父类或接口的方法列表
	 * @param withMethodFromObject 是否包括Object中的方法
	 * @return 方法列表
	 * @throws SecurityException 安全检查异常
	 */
	public Method[] getMethodsDirectly(final boolean withSupers, final boolean withMethodFromObject) throws SecurityException {
		final Class<?> clazz = this.clazz;

		if (clazz.isInterface()) {
			// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
			return withSupers ? clazz.getMethods() : clazz.getDeclaredMethods();
		}

		final UniqueKeySet<String, Method> result = new UniqueKeySet<>(true, MethodReflect::getUniqueKey);
		Class<?> searchType = clazz;
		while (searchType != null) {
			if (!withMethodFromObject && Object.class == searchType) {
				break;
			}
			// 本类所有方法
			result.addAllIfAbsent(Arrays.asList(searchType.getDeclaredMethods()));
			// 实现接口的所有默认方法
			result.addAllIfAbsent(getDefaultMethodsFromInterface(searchType));


			searchType = (withSupers && !searchType.isInterface()) ? searchType.getSuperclass() : null;
		}

		return result.toArray(new Method[0]);
	}
	// endregion

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
