/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.core.lang.func;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.classloader.ClassLoaderUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrPool;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 存放lambda信息<br>
 * 此类是{@link SerializedLambda}信息的扩充和补充类，包括：
 * <ul>
 *     <li>实例化后的对象方法参数类型，一般用于方法引用</li>
 * </ul>
 *
 * @author VampireAchao
 */
public class LambdaInfo {

	private static final Type[] EMPTY_TYPE = new Type[0];
	// 实例对象的方法参数类型
	private final Type[] instantiatedMethodParameterTypes;
	// 方法或构造的参数类型
	private final Type[] parameterTypes;
	private final Type returnType;
	// 方法名或构造名称
	private final String name;
	private final Executable executable;
	private final Class<?> clazz;
	private final SerializedLambda lambda;

	/**
	 * 构造
	 *
	 * @param executable 构造对象{@link Constructor}或方法对象{@link Method}
	 * @param lambda     实现了序列化接口的lambda表达式
	 */
	public LambdaInfo(final Executable executable, final SerializedLambda lambda) {
		Assert.notNull(executable, "executable must be not null!");
		// return type
		final boolean isMethod = executable instanceof Method;
		final boolean isConstructor = executable instanceof Constructor;
		Assert.isTrue(isMethod || isConstructor, "Unsupported executable type: " + executable.getClass());
		this.returnType = isMethod ?
				((Method) executable).getGenericReturnType() : ((Constructor<?>) executable).getDeclaringClass();

		// lambda info
		this.parameterTypes = executable.getGenericParameterTypes();
		this.name = executable.getName();
		this.clazz = executable.getDeclaringClass();
		this.executable = executable;
		this.lambda = lambda;

		// types
		final String instantiatedMethodType = lambda.getInstantiatedMethodType();
		final int index = instantiatedMethodType.indexOf(";)");
		this.instantiatedMethodParameterTypes = (index > -1) ?
				getInstantiatedMethodParamTypes(instantiatedMethodType.substring(1, index + 1)) : EMPTY_TYPE;
	}

	/**
	 * 实例方法参数类型
	 *
	 * @return 实例方法参数类型
	 */
	public Type[] getInstantiatedMethodParameterTypes() {
		return instantiatedMethodParameterTypes;
	}

	/**
	 * 获得构造或方法参数类型列表
	 *
	 * @return 参数类型列表
	 */
	public Type[] getParameterTypes() {
		return parameterTypes;
	}

	/**
	 * 获取返回值类型（方法引用）
	 *
	 * @return 返回值类型
	 */
	public Type getReturnType() {
		return returnType;
	}

	/**
	 * 方法或构造名称
	 *
	 * @return 方法或构造名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 字段名称，主要用于方法名称截取，方法名称必须为getXXX、isXXX、setXXX
	 *
	 * @return getter或setter对应的字段名称
	 */
	public String getFieldName() {
		return BeanUtil.getFieldName(getName());
	}

	/**
	 * 方法或构造对象
	 *
	 * @return 方法或构造对象
	 */
	public Executable getExecutable() {
		return executable;
	}

	/**
	 * 方法或构造所在类
	 *
	 * @return 方法或构造所在类
	 */
	public Class<?> getClazz() {
		return clazz;
	}


	/**
	 * 获得Lambda表达式对象
	 *
	 * @return 获得Lambda表达式对象
	 */
	public SerializedLambda getLambda() {
		return lambda;
	}

	/**
	 * 根据lambda对象的方法签名信息，解析获得实际的参数类型
	 */
	private static Type[] getInstantiatedMethodParamTypes(final String className) {
		final String[] instantiatedTypeNames = className.split(";");
		final Type[] types = new Type[instantiatedTypeNames.length];
		for (int i = 0; i < instantiatedTypeNames.length; i++) {
			final boolean isArray = instantiatedTypeNames[i].startsWith(StrPool.BRACKET_START);
			if (isArray && !instantiatedTypeNames[i].endsWith(";")) {
				// 如果是数组，需要以 ";" 结尾才能加载
				instantiatedTypeNames[i] += ";";
			} else {
				if (instantiatedTypeNames[i].startsWith("L")) {
					// 如果以 "L" 开头，删除 L
					instantiatedTypeNames[i] = instantiatedTypeNames[i].substring(1);
				}
				if (instantiatedTypeNames[i].endsWith(";")) {
					// 如果以 ";" 结尾，删除 ";"
					instantiatedTypeNames[i] = instantiatedTypeNames[i].substring(0, instantiatedTypeNames[i].length() - 1);
				}
			}
			types[i] = ClassLoaderUtil.loadClass(instantiatedTypeNames[i]);
		}
		return types;
	}
}
