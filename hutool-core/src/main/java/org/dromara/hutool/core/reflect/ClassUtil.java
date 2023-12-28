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

package org.dromara.hutool.core.reflect;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.NullWrapperBean;
import org.dromara.hutool.core.classloader.ClassLoaderUtil;
import org.dromara.hutool.core.convert.BasicType;
import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.core.net.url.UrlDecoder;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.stream.EasyStream;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.dromara.hutool.core.util.CharsetUtil;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 类工具类 <br>
 *
 * @author Looly
 */
public class ClassUtil {

	/**
	 * 包名分界符: '.'
	 */
	private static final char PACKAGE_SEPARATOR = CharUtil.DOT;
	/**
	 * 内部类分界符: '$'
	 */
	private static final char INNER_CLASS_SEPARATOR = '$';

	/**
	 * {@code null}安全的获取对象类型
	 *
	 * @param <T> 对象类型
	 * @param obj 对象，如果为{@code null} 返回{@code null}
	 * @return 对象类型，提供对象如果为{@code null} 返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(final T obj) {
		return ((null == obj) ? null : (Class<T>) obj.getClass());
	}

	/**
	 * 获得外围类<br>
	 * 返回定义此类或匿名类所在的类，如果类本身是在包中定义的，返回{@code null}
	 *
	 * @param clazz 类
	 * @return 外围类
	 * @since 4.5.7
	 */
	public static Class<?> getEnclosingClass(final Class<?> clazz) {
		return null == clazz ? null : clazz.getEnclosingClass();
	}

	/**
	 * 是否为顶层类，即定义在包中的类，而非定义在类中的内部类
	 *
	 * @param clazz 类
	 * @return 是否为顶层类
	 * @since 4.5.7
	 */
	public static boolean isTopLevelClass(final Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return null == getEnclosingClass(clazz);
	}

	/**
	 * 获取类名
	 *
	 * @param obj      获取类名对象
	 * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
	 * @return 类名
	 * @since 3.0.7
	 */
	public static String getClassName(final Object obj, final boolean isSimple) {
		if (null == obj) {
			return null;
		}
		final Class<?> clazz = obj.getClass();
		return getClassName(clazz, isSimple);
	}

	/**
	 * 获取类名<br>
	 * 类名并不包含“.class”这个扩展名<br>
	 * 例如：ClassUtil这个类<br>
	 *
	 * <pre>
	 * isSimple为false: "org.dromara.hutool.core.reflect.ClassUtil"
	 * isSimple为true: "ClassUtil"
	 * </pre>
	 *
	 * @param clazz    类
	 * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
	 * @return 类名
	 * @since 3.0.7
	 */
	public static String getClassName(final Class<?> clazz, final boolean isSimple) {
		if (null == clazz) {
			return null;
		}
		return isSimple ? clazz.getSimpleName() : clazz.getName();
	}

	/**
	 * 获取完整类名的短格式如：<br>
	 * text.org.dromara.hutool.core.StrUtil -》c.h.c.u.StrUtil
	 *
	 * @param className 类名
	 * @return 短格式类名
	 * @since 4.1.9
	 */
	public static String getShortClassName(final String className) {
		final List<String> packages = SplitUtil.split(className, StrUtil.DOT);
		if (null == packages || packages.size() < 2) {
			return className;
		}

		final int size = packages.size();
		final StringBuilder result = StrUtil.builder();
		result.append(packages.get(0).charAt(0));
		for (int i = 1; i < size - 1; i++) {
			result.append(CharUtil.DOT).append(packages.get(i).charAt(0));
		}
		result.append(CharUtil.DOT).append(packages.get(size - 1));
		return result.toString();
	}

	/**
	 * 获得对象数组的类数组
	 *
	 * @param objects 对象数组，如果数组中存在{@code null}元素，则此元素被认为是Object类型
	 * @return 类数组
	 */
	public static Class<?>[] getClasses(final Object... objects) {
		final Class<?>[] classes = new Class<?>[objects.length];
		Object obj;
		for (int i = 0; i < objects.length; i++) {
			obj = objects[i];
			if (obj instanceof NullWrapperBean) {
				// 自定义null值的参数类型
				classes[i] = ((NullWrapperBean<?>) obj).getWrappedClass();
			} else if (null == obj) {
				classes[i] = Object.class;
			} else {
				classes[i] = obj.getClass();
			}
		}
		return classes;
	}

	/**
	 * 指定类是否与给定的类名相同
	 *
	 * @param clazz      类
	 * @param className  类名，可以是全类名（包含包名），也可以是简单类名（不包含包名）
	 * @param ignoreCase 是否忽略大小写
	 * @return 指定类是否与给定的类名相同
	 * @since 3.0.7
	 */
	public static boolean equals(final Class<?> clazz, final String className, final boolean ignoreCase) {
		if (null == clazz || StrUtil.isBlank(className)) {
			return false;
		}
		if (ignoreCase) {
			return className.equalsIgnoreCase(clazz.getName()) || className.equalsIgnoreCase(clazz.getSimpleName());
		} else {
			return className.equals(clazz.getName()) || className.equals(clazz.getSimpleName());
		}
	}

	// ----------------------------------------------------------------------------------------- Scan classes

	/**
	 * 扫描指定包路径下所有包含指定注解的类
	 *
	 * @param packageName     包路径
	 * @param annotationClass 注解类
	 * @return 类集合
	 * @see ClassScanner#scanPackageByAnnotation(String, Class)
	 */
	public static Set<Class<?>> scanPackageByAnnotation(final String packageName, final Class<? extends Annotation> annotationClass) {
		return ClassScanner.scanPackageByAnnotation(packageName, annotationClass);
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类
	 *
	 * @param packageName 包路径
	 * @param superClass  父类或接口
	 * @return 类集合
	 * @see ClassScanner#scanPackageBySuper(String, Class)
	 */
	public static Set<Class<?>> scanPackageBySuper(final String packageName, final Class<?> superClass) {
		return ClassScanner.scanPackageBySuper(packageName, superClass);
	}

	/**
	 * 扫描该包路径下所有class文件
	 *
	 * @return 类集合
	 * @see ClassScanner#scanPackage()
	 */
	public static Set<Class<?>> scanPackage() {
		return ClassScanner.scanPackage();
	}

	/**
	 * 扫描该包路径下所有class文件
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @return 类集合
	 * @see ClassScanner#scanPackage(String)
	 */
	public static Set<Class<?>> scanPackage(final String packageName) {
		return ClassScanner.scanPackage(packageName);
	}

	/**
	 * 扫描包路径下满足class过滤器条件的所有class文件，<br>
	 * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException<br>
	 * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改<br>
	 *
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @param classFilter class过滤器，过滤掉不需要的class
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(final String packageName, final Predicate<Class<?>> classFilter) {
		return ClassScanner.scanPackage(packageName, classFilter);
	}

	// ----------------------------------------------------------------------------------------- Classpath

	/**
	 * 获得ClassPath，不解码路径中的特殊字符（例如空格和中文）
	 *
	 * @return ClassPath集合
	 */
	public static Set<String> getClassPathResources() {
		return getClassPathResources(false);
	}

	/**
	 * 获得ClassPath
	 *
	 * @param isDecode 是否解码路径中的特殊字符（例如空格和中文）
	 * @return ClassPath集合
	 * @since 4.0.11
	 */
	public static Set<String> getClassPathResources(final boolean isDecode) {
		return getClassPaths(StrUtil.EMPTY, isDecode);
	}

	/**
	 * 获得ClassPath，不解码路径中的特殊字符（例如空格和中文）
	 *
	 * @param packageName 包名称
	 * @return ClassPath路径字符串集合
	 */
	public static Set<String> getClassPaths(final String packageName) {
		return getClassPaths(packageName, false);
	}

	/**
	 * 获得ClassPath
	 *
	 * @param packageName 包名称
	 * @param isDecode    是否解码路径中的特殊字符（例如空格和中文）
	 * @return ClassPath路径字符串集合
	 * @since 4.0.11
	 */
	public static Set<String> getClassPaths(final String packageName, final boolean isDecode) {
		final String packagePath = packageName.replace(StrUtil.DOT, StrUtil.SLASH);
		final Enumeration<URL> resources;
		try {
			resources = ClassLoaderUtil.getClassLoader().getResources(packagePath);
		} catch (final IOException e) {
			throw new HutoolException(e, "Loading classPath [{}] error!", packagePath);
		}
		final Set<String> paths = new HashSet<>();
		String path;
		while (resources.hasMoreElements()) {
			path = resources.nextElement().getPath();
			paths.add(isDecode ? UrlDecoder.decode(path, CharsetUtil.defaultCharset()) : path);
		}
		return paths;
	}

	/**
	 * 获得ClassPath，将编码后的中文路径解码为原字符<br>
	 * 这个ClassPath路径会文件路径被标准化处理
	 *
	 * @return ClassPath
	 */
	public static String getClassPath() {
		return getClassPath(false);
	}

	/**
	 * 获得ClassPath，这个ClassPath路径会文件路径被标准化处理
	 *
	 * @param isEncoded 是否编码路径中的中文
	 * @return ClassPath
	 * @since 3.2.1
	 */
	public static String getClassPath(final boolean isEncoded) {
		final URL classPathUrl = ResourceUtil.getResourceUrl(StrUtil.EMPTY);
		final String url = isEncoded ? classPathUrl.getPath() : UrlUtil.getDecodedPath(classPathUrl);
		return FileUtil.normalize(url);
	}

	/**
	 * 比较判断types1和types2两组类，如果types1中所有的类都与types2对应位置的类相同，或者是其父类或接口，则返回{@code true}
	 *
	 * @param types1 类组1
	 * @param types2 类组2
	 * @return 是否相同、父类或接口
	 */
	public static boolean isAllAssignableFrom(final Class<?>[] types1, final Class<?>[] types2) {
		if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
			return true;
		}
		if (null == types1 || null == types2) {
			// 任何一个为null不相等（之前已判断两个都为null的情况）
			return false;
		}
		if (types1.length != types2.length) {
			return false;
		}

		Class<?> type1;
		Class<?> type2;
		for (int i = 0; i < types1.length; i++) {
			type1 = types1[i];
			type2 = types2[i];
			if (isBasicType(type1) && isBasicType(type2)) {
				// 原始类型和包装类型存在不一致情况
				if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
					return false;
				}
			} else if (!type1.isAssignableFrom(type2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为包装类型
	 *
	 * @param clazz 类
	 * @return 是否为包装类型
	 */
	public static boolean isPrimitiveWrapper(final Class<?> clazz) {
		return BasicType.isPrimitiveWrapper(clazz);
	}

	/**
	 * 是否为基本类型（包括包装类和原始类）
	 *
	 * @param clazz 类
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(final Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * 是否为 简单值类型 或 简单值类型的数组<br>
	 *
	 * @param clazz 属性类
	 * @return 是否为 简单值类型 或 简单值类型的数组
	 * @see #isSimpleValueType(Class)
	 */
	public static boolean isSimpleTypeOrArray(final Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * 是否为简单值类型<br>
	 * 包括：
	 * <pre>
	 *     原始类型
	 *     枚举
	 *     String、other CharSequence
	 *     Number
	 *     Date
	 *     URI
	 *     URL
	 *     Locale
	 *     Class
	 *     jdk8时间相关类型
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 是否为简单值类型
	 */
	public static boolean isSimpleValueType(final Class<?> clazz) {
		return isBasicType(clazz)
			|| clazz.isEnum()
			|| CharSequence.class.isAssignableFrom(clazz)
			|| Number.class.isAssignableFrom(clazz)
			|| Date.class.isAssignableFrom(clazz)
			|| clazz.equals(URI.class)
			|| clazz.equals(URL.class)
			|| clazz.equals(Locale.class)
			|| clazz.equals(Class.class)
			// jdk8 date object
			|| TemporalAccessor.class.isAssignableFrom(clazz);
	}

	/**
	 * 检查目标类是否可以从原类转化<br>
	 * 转化包括：<br>
	 * 1、原类是对象，目标类型是原类型实现的接口<br>
	 * 2、目标类型是原类型的父类<br>
	 * 3、两者是原始类型或者包装类型（相互转换）
	 *
	 * @param targetType 目标类型
	 * @param sourceType 原类型
	 * @return 是否可转化
	 */
	public static boolean isAssignable(final Class<?> targetType, final Class<?> sourceType) {
		if (null == targetType || null == sourceType) {
			return false;
		}

		// 对象类型
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}

		// 基本类型
		if (targetType.isPrimitive()) {
			// 目标为原始类型
			return targetType.equals(BasicType.unWrap(sourceType));
		} else {
			// 目标为包装类型
			final Class<?> resolvedWrapper = BasicType.wrap(sourceType, true);
			return resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper);
		}
	}

	/**
	 * 给定类是否实现了序列化接口{@link Serializable}
	 *
	 * @param clazz 类
	 * @return 是否实现序列化接口{@link Serializable}
	 * @since 6.0.0
	 */
	public static boolean isSerializable(final Class<?> clazz) {
		return Serializable.class.isAssignableFrom(clazz);
	}

	/**
	 * 是否为标准的类<br>
	 * 这个类必须：
	 *
	 * <pre>
	 * 1、非接口
	 * 2、非抽象类
	 * 3、非Enum枚举
	 * 4、非数组
	 * 5、非注解
	 * 6、非原始类型（int, long等）
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 是否为标准类
	 */
	public static boolean isNormalClass(final Class<?> clazz) {
		return null != clazz
			&& !clazz.isInterface()
			&& !ModifierUtil.isAbstract(clazz)
			&& !clazz.isEnum()
			&& !clazz.isArray()
			&& !clazz.isAnnotation()
			&& !clazz.isSynthetic()
			&& !clazz.isPrimitive();
	}

	/**
	 * 判断类是否为枚举类型
	 *
	 * @param clazz 类
	 * @return 是否为枚举类型
	 * @since 3.2.0
	 */
	public static boolean isEnum(final Class<?> clazz) {
		return null != clazz && clazz.isEnum();
	}

	/**
	 * 获得给定类的第一个泛型参数
	 *
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(final Class<?> clazz) {
		return getTypeArgument(clazz, 0);
	}

	/**
	 * 获得给定类的泛型参数
	 *
	 * @param clazz 被检查的类，必须是已经确定泛型类型的类
	 * @param index 泛型类型的索引号，即第几个泛型类型
	 * @return {@link Class}
	 */
	public static Class<?> getTypeArgument(final Class<?> clazz, final int index) {
		final Type argumentType = TypeUtil.getTypeArgument(clazz, index);
		return TypeUtil.getClass(argumentType);
	}

	/**
	 * 获得给定类所在包的名称<br>
	 * 例如：<br>
	 * org.dromara.hutool.core.reflect.ClassUtil =》 org.dromara.hutool.core.reflect
	 *
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackage(final Class<?> clazz) {
		if (clazz == null) {
			return StrUtil.EMPTY;
		}
		final String className = clazz.getName();
		final int packageEndIndex = className.lastIndexOf(StrUtil.DOT);
		if (packageEndIndex == -1) {
			return StrUtil.EMPTY;
		}
		return className.substring(0, packageEndIndex);
	}

	/**
	 * 获得给定类所在包的路径<br>
	 * 例如：<br>
	 * org.dromara.hutool.core.reflect.ClassUtil =》 org/dromara/hutool/core/reflect
	 *
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackagePath(final Class<?> clazz) {
		return getPackage(clazz).replace(CharUtil.DOT, CharUtil.SLASH);
	}

	/**
	 * 获取指定类型分的默认值<br>
	 * 默认值规则为：
	 *
	 * <pre>
	 * 1、如果为原始类型，返回0
	 * 2、非原始类型返回{@code null}
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 默认值
	 * @since 3.0.8
	 */
	public static Object getDefaultValue(final Class<?> clazz) {
		// 原始类型
		if (clazz.isPrimitive()) {
			return getPrimitiveDefaultValue(clazz);
		}
		return null;
	}

	/**
	 * 获取指定原始类型分的默认值<br>
	 * 默认值规则为：
	 *
	 * <pre>
	 * 1、如果为原始类型，返回0
	 * 2、非原始类型返回{@code null}
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 默认值
	 * @since 5.8.0
	 */
	public static Object getPrimitiveDefaultValue(final Class<?> clazz) {
		if (long.class == clazz) {
			return 0L;
		} else if (int.class == clazz) {
			return 0;
		} else if (short.class == clazz) {
			return (short) 0;
		} else if (char.class == clazz) {
			return (char) 0;
		} else if (byte.class == clazz) {
			return (byte) 0;
		} else if (double.class == clazz) {
			return 0D;
		} else if (float.class == clazz) {
			return 0f;
		} else if (boolean.class == clazz) {
			return false;
		}
		return null;
	}

	/**
	 * 获得默认值列表
	 *
	 * @param classes 值类型
	 * @return 默认值列表
	 * @since 3.0.9
	 */
	public static Object[] getDefaultValues(final Class<?>... classes) {
		final Object[] values = new Object[classes.length];
		for (int i = 0; i < classes.length; i++) {
			values[i] = getDefaultValue(classes[i]);
		}
		return values;
	}

	/**
	 * 是否为JDK中定义的类或接口，判断依据：
	 *
	 * <pre>
	 * 1、以java.、javax.开头的包名
	 * 2、ClassLoader为null
	 * </pre>
	 *
	 * @param clazz 被检查的类
	 * @return 是否为JDK中定义的类或接口
	 * @since 4.6.5
	 */
	public static boolean isJdkClass(final Class<?> clazz) {
		final Package objectPackage = clazz.getPackage();
		if (null == objectPackage) {
			return false;
		}
		final String objectPackageName = objectPackage.getName();
		return objectPackageName.startsWith("java.")
			|| objectPackageName.startsWith("javax.")
			|| clazz.getClassLoader() == null;
	}

	/**
	 * 获取class类路径URL, 不管是否在jar包中都会返回文件夹的路径<br>
	 * class在jar包中返回jar所在文件夹,class不在jar中返回文件夹目录<br>
	 * jdk中的类不能使用此方法
	 *
	 * @param clazz 类
	 * @return URL
	 * @since 5.2.4
	 */
	public static URL getLocation(final Class<?> clazz) {
		if (null == clazz) {
			return null;
		}
		return clazz.getProtectionDomain().getCodeSource().getLocation();
	}

	/**
	 * 获取class类路径, 不管是否在jar包中都会返回文件夹的路径<br>
	 * class在jar包中返回jar所在文件夹,class不在jar中返回文件夹目录<br>
	 * jdk中的类不能使用此方法
	 *
	 * @param clazz 类
	 * @return class路径
	 * @since 5.2.4
	 */
	public static String getLocationPath(final Class<?> clazz) {
		final URL location = getLocation(clazz);
		if (null == location) {
			return null;
		}
		return location.getPath();
	}

	/**
	 * 加载指定名称的类，支持：
	 * <ul>
	 *     <li>替换"/"为"."</li>
	 *     <li>自动查找内部类，如java.lang.Thread.State =》java.lang.Thread$State</li>
	 * </ul>
	 *
	 * @param name          类名
	 * @param isInitialized 是否初始化
	 * @param loader        {@link ClassLoader}，{@code null}表示默认
	 * @return 指定名称对应的类，如果不存在类，返回{@code null}
	 * @see Class#forName(String, boolean, ClassLoader)
	 */
	public static Class<?> forName(String name, final boolean isInitialized, ClassLoader loader) {
		if (null == loader) {
			loader = ClassLoaderUtil.getClassLoader();
		}
		name = name.replace(CharUtil.SLASH, CharUtil.DOT);

		// 加载普通类
		try {
			return Class.forName(name, isInitialized, loader);
		} catch (final ClassNotFoundException ex) {
			// 尝试获取内部类，例如java.lang.Thread.State =》java.lang.Thread$State
			final Class<?> clazz = forNameInnerClass(name, isInitialized, loader);
			if (null == clazz) {
				throw new HutoolException(ex);
			}
			return clazz;
		}
	}

	/**
	 * 尝试转换并加载内部类，例如java.lang.Thread.State =》java.lang.Thread$State
	 *
	 * @param name          类名
	 * @param classLoader   {@link ClassLoader}，{@code null} 则使用系统默认ClassLoader
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @return 类名对应的类，未找到返回{@code null}
	 */
	private static Class<?> forNameInnerClass(String name, final boolean isInitialized, final ClassLoader classLoader) {
		// 尝试获取内部类，例如java.lang.Thread.State =》java.lang.Thread$State
		int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
		Class<?> clazz = null;
		while (lastDotIndex > 0) {// 类与内部类的分隔符不能在第一位，因此>0
			if (!Character.isUpperCase(name.charAt(lastDotIndex + 1))) {
				// 类名必须大写，非大写的类名跳过
				break;
			}
			name = name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
			try {
				clazz = Class.forName(name, isInitialized, classLoader);
				break;
			} catch (final ClassNotFoundException ignore) {
				//ignore
			}

			// 继续向前替换.为$
			lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
		}
		return clazz;
	}


	/**
	 * 获取指定类的所有父类，结果不包括指定类本身<br>
	 * 如果无父类，返回一个空的列表
	 *
	 * @param clazz 类, 可以为{@code null}
	 * @return 指定类的所有父类列表，如果无父类，返回一个空的列表
	 */
	public static List<Class<?>> getSuperClasses(final Class<?> clazz) {
		if (clazz == null) {
			return Collections.emptyList();
		}
		final List<Class<?>> superclasses = new ArrayList<>();
		traverseTypeHierarchy(clazz, t -> !t.isInterface(), superclasses::add, false);
		return superclasses;
	}

	/**
	 * 获取指定类及其父类所有的实现接口。<br>
	 * 结果顺序取决于查找顺序，当前类在前，父类的接口在后。
	 *
	 * @param cls 被查找的类
	 * @return 接口列表，若提供的查找类为{@code null}，则返回空列表
	 */
	public static List<Class<?>> getInterfaces(final Class<?> cls) {
		if (cls == null) {
			return Collections.emptyList();
		}
		final List<Class<?>> interfaces = new ArrayList<>();
		traverseTypeHierarchy(cls, t -> true, t -> {
			if (t.isInterface()) {
				interfaces.add(t);
			}
		}, false);
		return interfaces;
	}

	/**
	 * 按广度优先遍历包括{@code root}在内，其层级结构中的所有类和接口，直到{@code terminator}返回{@code false}
	 *
	 * @param root 根类
	 * @param terminator 对遍历到的每个类与接口执行的校验，若为{@code false}则立刻中断遍历
	 */
	public static void traverseTypeHierarchyWhile(
		final Class<?> root, final Predicate<Class<?>> terminator) {
		traverseTypeHierarchyWhile(root, t -> true, terminator);
	}

	/**
	 * 按广度优先遍历包括{@code root}在内，其层级结构中的所有类和接口，直到{@code terminator}返回{@code false}
	 *
	 * @param root 根类
	 * @param filter 过滤器，被过滤的类及其层级结构中的类与接口将被忽略
	 * @param terminator 对遍历到的每个类与接口执行的校验，若为{@code false}则立刻中断遍历
	 */
	public static void traverseTypeHierarchyWhile(
		final Class<?> root, final Predicate<Class<?>> filter, final Predicate<Class<?>> terminator) {
		EasyStream.iterateHierarchies(root, ClassUtil::getNextTypeHierarchies, filter)
			.takeWhile(terminator)
			.exec();
	}

	/**
	 * <p>按广度优先遍历包括{@code root}在内，其层级结构中的所有类和接口。<br>
	 * 类遍历顺序如下：
	 * <ul>
	 *     <li>离{@code type}距离越近，则顺序越靠前；</li>
	 *     <li>与{@code type}距离相同，则父类优先于接口；</li>
	 *     <li>与{@code type}距离相同的接口，则顺序遵循接口在{@link Class#getInterfaces()}的顺序；</li>
	 * </ul>
	 *
	 * @param root 根类
	 * @param filter 过滤器，被过滤的类及其层级结构中的类与接口将被忽略
	 * @param consumer 对遍历到的每个类与接口执行的操作，每个类和接口都只会被访问一次
	 * @param includeRoot 是否包括根类
	 */
	public static void traverseTypeHierarchy(
		final Class<?> root, final Predicate<Class<?>> filter, final Consumer<Class<?>> consumer, final boolean includeRoot) {
		Objects.requireNonNull(root);
		Objects.requireNonNull(filter);
		Objects.requireNonNull(consumer);
		final Function<Class<?>, Collection<Class<?>>> function = t -> {
			if (includeRoot || !root.equals(t)) {
				consumer.accept(t);
			}
			return getNextTypeHierarchies(t);
		};
		EasyStream.iterateHierarchies(root, function, filter).exec();
	}

	private static Set<Class<?>> getNextTypeHierarchies(final Class<?> t) {
		final Set<Class<?>> next = new LinkedHashSet<>();
		final Class<?> superclass = t.getSuperclass();
		if (Objects.nonNull(superclass)) {
			next.add(superclass);
		}
		next.addAll(Arrays.asList(t.getInterfaces()));
		return next;
	}
}
