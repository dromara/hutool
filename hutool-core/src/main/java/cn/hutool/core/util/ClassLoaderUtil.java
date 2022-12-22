package cn.hutool.core.util;

import cn.hutool.core.convert.BasicType;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.JarClassLoader;
import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.text.CharPool;

import java.io.File;
import java.lang.reflect.Array;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link ClassLoader}工具类
 *
 * @author Looly
 * @since 3.0.9
 */
public class ClassLoaderUtil {

	/**
	 * 数组类的结尾符: "[]"
	 */
	private static final String ARRAY_SUFFIX = "[]";
	/**
	 * 内部数组类名前缀: "["
	 */
	private static final String INTERNAL_ARRAY_PREFIX = "[";
	/**
	 * 内部非原始类型类名前缀: "[L"
	 */
	private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
	/**
	 * 包名分界符: '.'
	 */
	private static final char PACKAGE_SEPARATOR = StrUtil.C_DOT;
	/**
	 * 内部类分界符: '$'
	 */
	private static final char INNER_CLASS_SEPARATOR = '$';

	/**
	 * 原始类型名和其class对应表，例如：int =》 int.class
	 */
	private static final Map<String, Class<?>> PRIMITIVE_TYPE_NAME_MAP = new SafeConcurrentHashMap<>(32);

	static {
		final List<Class<?>> primitiveTypes = new ArrayList<>(32);
		// 加入原始类型
		primitiveTypes.addAll(BasicType.PRIMITIVE_WRAPPER_MAP.keySet());
		// 加入原始类型数组类型
		primitiveTypes.add(boolean[].class);
		primitiveTypes.add(byte[].class);
		primitiveTypes.add(char[].class);
		primitiveTypes.add(double[].class);
		primitiveTypes.add(float[].class);
		primitiveTypes.add(int[].class);
		primitiveTypes.add(long[].class);
		primitiveTypes.add(short[].class);
		primitiveTypes.add(void.class);
		for (final Class<?> primitiveType : primitiveTypes) {
			PRIMITIVE_TYPE_NAME_MAP.put(primitiveType.getName(), primitiveType);
		}
	}

	/**
	 * 获取当前线程的{@link ClassLoader}
	 *
	 * @return 当前线程的class loader
	 * @see Thread#getContextClassLoader()
	 */
	public static ClassLoader getContextClassLoader() {
		if (System.getSecurityManager() == null) {
			return Thread.currentThread().getContextClassLoader();
		} else {
			// 绕开权限检查
			return AccessController.doPrivileged(
					(PrivilegedAction<ClassLoader>) () -> Thread.currentThread().getContextClassLoader());
		}
	}

	/**
	 * 获取系统{@link ClassLoader}
	 *
	 * @return 系统{@link ClassLoader}
	 * @see ClassLoader#getSystemClassLoader()
	 * @since 5.7.0
	 */
	public static ClassLoader getSystemClassLoader() {
		if (System.getSecurityManager() == null) {
			return ClassLoader.getSystemClassLoader();
		} else {
			// 绕开权限检查
			return AccessController.doPrivileged(
					(PrivilegedAction<ClassLoader>) ClassLoader::getSystemClassLoader);
		}
	}


	/**
	 * 获取{@link ClassLoader}<br>
	 * 获取顺序如下：<br>
	 *
	 * <pre>
	 * 1、获取当前线程的ContextClassLoader
	 * 2、获取当前类对应的ClassLoader
	 * 3、获取系统ClassLoader（{@link ClassLoader#getSystemClassLoader()}）
	 * </pre>
	 *
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassLoaderUtil.class.getClassLoader();
			if (null == classLoader) {
				classLoader = getSystemClassLoader();
			}
		}
		return classLoader;
	}

	// ----------------------------------------------------------------------------------- loadClass

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
	 * @param name 类名
	 * @return 类名对应的类
	 * @throws UtilException 包装{@link ClassNotFoundException}，没有类名对应的类时抛出此异常
	 */
	public static Class<?> loadClass(String name) throws UtilException {
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
	 * @param name          类名
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @return 类名对应的类
	 * @throws UtilException 包装{@link ClassNotFoundException}，没有类名对应的类时抛出此异常
	 */
	public static Class<?> loadClass(String name, boolean isInitialized) throws UtilException {
		return loadClass(name, null, isInitialized);
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
	 * @param name          类名
	 * @param classLoader   {@link ClassLoader}，{@code null} 则使用{@link #getClassLoader()}获取
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @return 类名对应的类
	 * @throws UtilException 包装{@link ClassNotFoundException}，没有类名对应的类时抛出此异常
	 */
	public static Class<?> loadClass(String name, ClassLoader classLoader, boolean isInitialized) throws UtilException {
		Assert.notNull(name, "Name must not be null");

		// 自动将包名中的"/"替换为"."
		name = name.replace(CharPool.SLASH, CharPool.DOT);
		if(null == classLoader){
			classLoader = getClassLoader();
		}

		// 加载原始类型和缓存中的类
		Class<?> clazz = loadPrimitiveClass(name);
		if (clazz == null) {
			clazz = doLoadClass(name, classLoader, isInitialized);
		}
		return clazz;
	}

	/**
	 * 加载原始类型的类。包括原始类型、原始类型数组和void
	 *
	 * @param name 原始类型名，比如 int
	 * @return 原始类型类
	 */
	public static Class<?> loadPrimitiveClass(String name) {
		Class<?> result = null;
		if (StrUtil.isNotBlank(name)) {
			name = name.trim();
			if (name.length() <= 8) {
				result = PRIMITIVE_TYPE_NAME_MAP.get(name);
			}
		}
		return result;
	}

	/**
	 * 创建新的{@link JarClassLoader}，并使用此Classloader加载目录下的class文件和jar文件
	 *
	 * @param jarOrDir jar文件或者包含jar和class文件的目录
	 * @return {@link JarClassLoader}
	 * @since 4.4.2
	 */
	public static JarClassLoader getJarClassLoader(File jarOrDir) {
		return JarClassLoader.load(jarOrDir);
	}

	/**
	 * 加载外部类
	 *
	 * @param jarOrDir jar文件或者包含jar和class文件的目录
	 * @param name     类名
	 * @return 类
	 * @since 4.4.2
	 */
	public static Class<?> loadClass(File jarOrDir, String name) {
		try {
			return getJarClassLoader(jarOrDir).loadClass(name);
		} catch (ClassNotFoundException e) {
			throw new UtilException(e);
		}
	}

	// ----------------------------------------------------------------------------------- isPresent

	/**
	 * 指定类是否被提供，使用默认ClassLoader<br>
	 * 通过调用{@link #loadClass(String, ClassLoader, boolean)}方法尝试加载指定类名的类，如果加载失败返回false<br>
	 * 加载失败的原因可能是此类不存在或其关联引用类不存在
	 *
	 * @param className 类名
	 * @return 是否被提供
	 */
	public static boolean isPresent(String className) {
		return isPresent(className, null);
	}

	/**
	 * 指定类是否被提供<br>
	 * 通过调用{@link #loadClass(String, ClassLoader, boolean)}方法尝试加载指定类名的类，如果加载失败返回false<br>
	 * 加载失败的原因可能是此类不存在或其关联引用类不存在
	 *
	 * @param className   类名
	 * @param classLoader {@link ClassLoader}
	 * @return 是否被提供
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		try {
			loadClass(className, classLoader, false);
			return true;
		} catch (Throwable ex) {
			return false;
		}
	}

	// ----------------------------------------------------------------------------------- Private method start
	/**
	 * 加载非原始类类，无缓存
	 * @param name 类名
	 * @param classLoader {@link ClassLoader}
	 * @param isInitialized 是否初始化
	 * @return 类
	 */
	private static Class<?> doLoadClass(String name, ClassLoader classLoader, boolean isInitialized){
		Class<?> clazz;
		if (name.endsWith(ARRAY_SUFFIX)) {
			// 对象数组"java.lang.String[]"风格
			final String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
			final Class<?> elementClass = loadClass(elementClassName, classLoader, isInitialized);
			clazz = Array.newInstance(elementClass, 0).getClass();
		} else if (name.startsWith(NON_PRIMITIVE_ARRAY_PREFIX) && name.endsWith(";")) {
			// "[Ljava.lang.String;" 风格
			final String elementName = name.substring(NON_PRIMITIVE_ARRAY_PREFIX.length(), name.length() - 1);
			final Class<?> elementClass = loadClass(elementName, classLoader, isInitialized);
			clazz = Array.newInstance(elementClass, 0).getClass();
		} else if (name.startsWith(INTERNAL_ARRAY_PREFIX)) {
			// "[[I" 或 "[[Ljava.lang.String;" 风格
			final String elementName = name.substring(INTERNAL_ARRAY_PREFIX.length());
			final Class<?> elementClass = loadClass(elementName, classLoader, isInitialized);
			clazz = Array.newInstance(elementClass, 0).getClass();
		} else {
			// 加载普通类
			if (null == classLoader) {
				classLoader = getClassLoader();
			}
			try {
				clazz = Class.forName(name, isInitialized, classLoader);
			} catch (ClassNotFoundException ex) {
				// 尝试获取内部类，例如java.lang.Thread.State =》java.lang.Thread$State
				clazz = tryLoadInnerClass(name, classLoader, isInitialized);
				if (null == clazz) {
					throw new UtilException(ex);
				}
			}
		}
		return clazz;
	}

	/**
	 * 尝试转换并加载内部类，例如java.lang.Thread.State =》java.lang.Thread$State
	 *
	 * @param name          类名
	 * @param classLoader   {@link ClassLoader}，{@code null} 则使用系统默认ClassLoader
	 * @param isInitialized 是否初始化类（调用static模块内容和初始化static属性）
	 * @return 类名对应的类
	 * @since 4.1.20
	 */
	private static Class<?> tryLoadInnerClass(String name, ClassLoader classLoader, boolean isInitialized) {
		// 尝试获取内部类，例如java.lang.Thread.State =》java.lang.Thread$State
		final int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
		if (lastDotIndex > 0) {// 类与内部类的分隔符不能在第一位，因此>0
			final String innerClassName = name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
			try {
				return Class.forName(innerClassName, isInitialized, classLoader);
			} catch (ClassNotFoundException ex2) {
				// 尝试获取内部类失败时，忽略之。
			}
		}
		return null;
	}
	// ----------------------------------------------------------------------------------- Private method end
}
