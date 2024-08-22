/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.classloader;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.lang.caller.CallerUtil;
import org.dromara.hutool.core.reflect.ClassDescUtil;

import java.io.File;

/**
 * {@link ClassLoader}工具类<br>
 * 此工具类加载的类，不提供缓存，缓存应由实现的ClassLoader完成。
 *
 * @author Looly
 * @since 3.0.9
 */
public class ClassLoaderUtil {

	/**
	 * 获取{@link ClassLoader}，获取顺序如下：
	 * <ol>
	 *     <li>获取调用者的ContextClassLoader</li>
	 *     <li>获取当前线程的ContextClassLoader</li>
	 *     <li>获取ClassLoaderUtil对应的ClassLoader</li>
	 *     <li>获取系统ClassLoader（{@link ClassLoader#getSystemClassLoader()}）</li>
	 * </ol>
	 *
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = CallerUtil.getCallerCaller().getClassLoader();
		if(null == classLoader){
			classLoader = getContextClassLoader();
		}
		if (classLoader == null) {
			classLoader = ClassLoaderUtil.class.getClassLoader();
			if (null == classLoader) {
				classLoader = getSystemClassLoader();
			}
		}
		return classLoader;
	}

	/**
	 * 获取调用者的{@link ClassLoader}
	 *
	 * @return {@link ClassLoader}
	 * @since 6.0.0
	 */
	public static ClassLoader getCallerClassLoader() {
		return CallerUtil.getCallerCaller().getClassLoader();
	}

	/**
	 * 获取当前线程的{@link ClassLoader}
	 *
	 * @return 当前线程的class loader
	 * @see Thread#getContextClassLoader()
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获取系统{@link ClassLoader}
	 *
	 * @return 系统{@link ClassLoader}
	 * @see ClassLoader#getSystemClassLoader()
	 * @since 5.7.0
	 */
	public static ClassLoader getSystemClassLoader() {
		return ClassLoader.getSystemClassLoader();
	}

	/**
	 * 创建新的{@link JarClassLoader}，并使用此Classloader加载目录下的class文件和jar文件
	 *
	 * @param jarOrDir jar文件或者包含jar和class文件的目录
	 * @return {@link JarClassLoader}
	 * @since 4.4.2
	 */
	public static JarClassLoader getJarClassLoader(final File jarOrDir) {
		return JarClassLoader.load(jarOrDir);
	}

	/**
	 * 加载类，通过传入类的字符串，返回其对应的类名，使用默认ClassLoader并初始化类（调用static模块内容和初始化static属性）<br>
	 * 扩展{@link Class#forName(String, boolean, ClassLoader)}方法，支持以下几类类名的加载：
	 *
	 * <pre>
	 * 1、原始类型，例如：int
	 * 2、数组类型，例如：int[]、Long[]、String[]
	 * 3、内部类，例如：java.lang.Thread.State会被转为java.lang.Thread$State加载
	 * </pre>
	 *
	 * @param <T>  目标类的类型
	 * @param name 类名
	 * @return 类名对应的类
	 * @throws HutoolException 包装{@link ClassNotFoundException}，没有类名对应的类时抛出此异常
	 */
	public static <T> Class<T> loadClass(final String name) throws HutoolException {
		return loadClass(name, true);
	}

	/**
	 * 加载类，通过传入类的字符串，返回其对应的类名，使用默认ClassLoader<br>
	 * 扩展{@link Class#forName(String, boolean, ClassLoader)}方法，支持以下几类类名的加载：
	 *
	 * <pre>
	 * 1、原始类型，例如：int
	 * 2、数组类型，例如：int[]、Long[]、String[]
	 * 3、内部类，例如：java.lang.Thread.State会被转为java.lang.Thread$State加载
	 * </pre>
	 *
	 * @param <T>           目标类的类型
	 * @param name          类名
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @return 类名对应的类
	 * @throws HutoolException 包装{@link ClassNotFoundException}，没有类名对应的类时抛出此异常
	 */
	public static <T> Class<T> loadClass(final String name, final boolean isInitialized) throws HutoolException {
		return loadClass(name, isInitialized, null);
	}

	/**
	 * 加载类，通过传入类的字符串，返回其对应的类名<br>
	 * 此方法支持缓存，第一次被加载的类之后会读取缓存中的类<br>
	 * 加载失败的原因可能是此类不存在或其关联引用类不存在<br>
	 * 扩展{@link Class#forName(String, boolean, ClassLoader)}方法，支持以下几类类名的加载：
	 *
	 * <pre>
	 * 1、原始类型，例如：int
	 * 2、数组类型，例如：int[]、Long[]、String[]
	 * 3、内部类，例如：java.lang.Thread.State会被转为java.lang.Thread$State加载
	 * </pre>
	 *
	 * @param <T>           加载的类的类型
	 * @param name          类名
	 * @param classLoader   {@link ClassLoader}，{@code null} 则使用{@link #getClassLoader()}获取
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @return 类名对应的类
	 * @throws HutoolException 包装{@link ClassNotFoundException}，没有类名对应的类时抛出此异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(final String name, final boolean isInitialized, final ClassLoader classLoader) throws HutoolException {
		return (Class<T>) ClassDescUtil.nameToClass(name, isInitialized, classLoader);
	}

	/**
	 * 加载外部类
	 *
	 * @param jarOrDir jar文件或者包含jar和class文件的目录
	 * @param name     类名
	 * @return 类
	 * @since 4.4.2
	 */
	public static Class<?> loadClass(final File jarOrDir, final String name) {
		try {
			return getJarClassLoader(jarOrDir).loadClass(name);
		} catch (final ClassNotFoundException e) {
			throw new HutoolException(e);
		}
	}

	// ----------------------------------------------------------------------------------- isPresent

	/**
	 * 指定类是否被提供，使用默认ClassLoader<br>
	 * 通过调用{@link #loadClass(String, boolean, ClassLoader)}方法尝试加载指定类名的类，如果加载失败返回false<br>
	 * 加载失败的原因可能是此类不存在或其关联引用类不存在
	 *
	 * @param className 类名
	 * @return 是否被提供
	 */
	public static boolean isPresent(final String className) {
		return isPresent(className, null);
	}

	/**
	 * 指定类是否被提供<br>
	 * 通过调用{@link #loadClass(String, boolean, ClassLoader)}方法尝试加载指定类名的类，如果加载失败返回false<br>
	 * 加载失败的原因可能是此类不存在或其关联引用类不存在
	 *
	 * @param className   类名
	 * @param classLoader {@link ClassLoader}
	 * @return 是否被提供
	 */
	public static boolean isPresent(final String className, final ClassLoader classLoader) {
		try {
			loadClass(className, false, classLoader);
			return true;
		} catch (final Throwable ex) {
			return false;
		}
	}
}
