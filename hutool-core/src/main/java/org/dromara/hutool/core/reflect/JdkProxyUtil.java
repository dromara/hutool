/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.lang.Assert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * JDK的{@link Proxy}相关工具类封装
 *
 * @author Looly
 * @since 6.0.0
 */
public class JdkProxyUtil {

	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	// region ----- newProxyInstance

	/**
	 * 创建动态代理对象<br>
	 * 动态代理对象的创建原理是：<br>
	 * 假设创建的代理对象名为 $Proxy0
	 * 1、根据传入的interfaces动态生成一个类，实现interfaces中的接口<br>
	 * 2、通过传入的classloder将刚生成的类加载到jvm中。即将$Proxy0类load<br>
	 * 3、调用$Proxy0的$Proxy0(InvocationHandler)构造函数 创建$Proxy0的对象，并且用interfaces参数遍历其所有接口的方法，这些实现方法的实现本质上是通过反射调用被代理对象的方法<br>
	 * 4、将$Proxy0的实例返回给客户端。 <br>
	 * 5、当调用代理类的相应方法时，相当于调用 {@link InvocationHandler#invoke(Object, java.lang.reflect.Method, Object[])} 方法
	 *
	 * @param <T>               被代理对象类型
	 * @param classloader       被代理类对应的ClassLoader
	 * @param invocationHandler {@link InvocationHandler} ，被代理类通过实现此接口提供动态代理功能
	 * @param interfaces        代理类中需要实现的被代理类的接口方法
	 * @return 代理类
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newProxyInstance(final ClassLoader classloader, final InvocationHandler invocationHandler, final Class<?>... interfaces) {
		return (T) Proxy.newProxyInstance(classloader, interfaces, invocationHandler);
	}

	/**
	 * 创建动态代理对象
	 *
	 * @param <T>               被代理对象类型
	 * @param invocationHandler {@link InvocationHandler} ，被代理类通过实现此接口提供动态代理功能
	 * @param interfaces        代理类中需要实现的被代理类的接口方法
	 * @return 代理类
	 */
	public static <T> T newProxyInstance(final InvocationHandler invocationHandler, final Class<?>... interfaces) {
		return newProxyInstance(ClassLoaderUtil.getClassLoader(), invocationHandler, interfaces);
	}
	// endregion

	// region ----- isProxy

	/**
	 * 是否为代理对象，判断JDK代理或Cglib代理
	 *
	 * @param object 被检查的对象
	 * @return 是否为代理对象
	 */
	public static boolean isProxy(final Object object) {
		Assert.notNull(object);
		return isProxyClass(object.getClass());
	}

	/**
	 * 是否为JDK代理对象
	 *
	 * @param object 被检查的对象
	 * @return 是否为JDK代理对象
	 */
	public static boolean isJdkProxy(final Object object) {
		Assert.notNull(object);
		return isJdkProxyClass(object.getClass());
	}

	/**
	 * 是否Cglib代理对象
	 *
	 * @param object 被检查的对象
	 * @return 是否Cglib代理对象
	 */
	public static boolean isCglibProxy(final Object object) {
		Assert.notNull(object);
		return isCglibProxyClass(object.getClass());
	}
	// endregion

	// region ----- isProxyClass

	/**
	 * 是否为代理类，判断JDK代理或Cglib代理
	 *
	 * @param clazz 被检查的类
	 * @return 是否为代理类
	 */
	public static boolean isProxyClass(final Class<?> clazz) {
		return isJdkProxyClass(clazz) || isCglibProxyClass(clazz);
	}

	/**
	 * 是否为JDK代理类
	 *
	 * @param clazz 被检查的类
	 * @return 是否为JDK代理类
	 */
	public static boolean isJdkProxyClass(final Class<?> clazz) {
		return Proxy.isProxyClass(Assert.notNull(clazz));
	}

	/**
	 * 是否Cglib代理对象
	 *
	 * @param clazz 被检查的对象
	 * @return 是否Cglib代理对象
	 */
	public static boolean isCglibProxyClass(final Class<?> clazz) {
		return Assert.notNull(clazz).getName().contains(CGLIB_CLASS_SEPARATOR);
	}

	// endregion
}
