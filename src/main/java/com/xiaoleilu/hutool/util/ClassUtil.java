package com.xiaoleilu.hutool.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.lang.BasicType;
import com.xiaoleilu.hutool.lang.Filter;
import com.xiaoleilu.hutool.lang.Singleton;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * 类工具类 1、扫描指定包下的所有类<br>
 * 参考 http://www.oschina.net/code/snippet_234657_22722
 * 
 * @author seaside_hi, xiaoleilu
 *
 */
public class ClassUtil {
	private final static Log log = StaticLog.get();

	private ClassUtil() {
		// 静态类不可实例化
	}

	/**
	 * 获得对象数组的类数组
	 * 
	 * @param objects 对象数组
	 * @return 类数组
	 */
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		for (int i = 0; i < objects.length; i++) {
			classes[i] = objects[i].getClass();
		}
		return classes;
	}

	// ----------------------------------------------------------------------------------------- Scan Package
	/**
	 * 扫面该包路径下所有class文件
	 * 
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage() {
		return scanPackage(StrUtil.EMPTY, null);
	}

	/**
	 * 扫面该包路径下所有class文件
	 * 
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName) {
		return scanPackage(packageName, null);
	}

	/**
	 * 扫描指定包路径下所有包含指定注解的类
	 * 
	 * @param packageName 包路径
	 * @param annotationClass 注解类
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
		return scanPackage(packageName, new ClassFilter(){
			@Override
			public boolean accept(Class<?> clazz) {
				return clazz.isAnnotationPresent(annotationClass);
			}
		});
	}

	/**
	 * 扫描指定包路径下所有指定类或接口的子类或实现类
	 * 
	 * @param packageName 包路径
	 * @param superClass 父类或接口
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
		return scanPackage(packageName, new ClassFilter(){
			@Override
			public boolean accept(Class<?> clazz) {
				return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
			}
		});
	}

	/**
	 * 扫面包路径下满足class过滤器条件的所有class文件，</br>
	 * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException</br>
	 * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改</br>
	 * 
	 * @param packageName 包路径 com | com. | com.abs | com.abs.
	 * @param classFilter class过滤器，过滤掉不需要的class
	 * @return 类集合
	 */
	public static Set<Class<?>> scanPackage(String packageName, ClassFilter classFilter) {
		if (StrUtil.isBlank(packageName)) {
			packageName = StrUtil.EMPTY;
		}
		log.debug("Scan classes from package [{}]...", packageName);
		packageName = getWellFormedPackageName(packageName);

		final Set<Class<?>> classes = new HashSet<Class<?>>();
		for (String classPath : getClassPaths(packageName)) {
			// bug修复，由于路径中空格和中文导致的Jar找不到
			classPath = URLUtil.decode(classPath, CharsetUtil.systemCharset());

			log.debug("Scan classpath: [{}]", classPath);
			// 填充 classes
			fillClasses(classPath, packageName, classFilter, classes);
		}

		// 如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
		if (classes.isEmpty()) {
			for (String classPath : getJavaClassPaths()) {
				// bug修复，由于路径中空格和中文导致的Jar找不到
				classPath = URLUtil.decode(classPath, CharsetUtil.systemCharset());

				log.debug("Scan java classpath: [{}]", classPath);
				// 填充 classes
				fillClasses(classPath, new File(classPath), packageName, classFilter, classes);
			}
		}
		return classes;
	}

	// ----------------------------------------------------------------------------------------- Method
	/**
	 * 获得指定类中的Public方法名<br>
	 * 去重重载的方法
	 * 
	 * @param clazz 类
	 */
	public static Set<String> getPublicMethodNames(Class<?> clazz) {
		HashSet<String> methodSet = new HashSet<String>();
		Method[] methodArray = getPublicMethods(clazz);
		for (Method method : methodArray) {
			String methodName = method.getName();
			methodSet.add(methodName);
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
		return clazz.getMethods();
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

		Method[] methods = getPublicMethods(clazz);
		List<Method> methodList;
		if (null != filter) {
			methodList = new ArrayList<>();
			Method filteredMethod;
			for (Method method : methods) {
				filteredMethod = filter.modify(method);
				if (null != filteredMethod) {
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
		Filter<Method> filter = new Filter<Method>(){
			@Override
			public Method modify(Method method) {
				return excludeMethodSet.contains(method) ? null : method;
			}
		};

		return getPublicMethods(clazz, filter);
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
		Filter<Method> filter = new Filter<Method>(){
			@Override
			public Method modify(Method method) {
				return excludeMethodNameSet.contains(method.getName()) ? null : method;
			}
		};

		return getPublicMethods(clazz, filter);
	}

	/**
	 * 查找指定Public方法
	 * 
	 * @param clazz 类
	 * @param methodName 方法名
	 * @param paramTypes 参数类型
	 * @return 方法
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method getPublicMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws NoSuchMethodException, SecurityException {
		try {
			return clazz.getMethod(methodName, paramTypes);
		} catch (NoSuchMethodException ex) {
			return getDeclaredMethod(clazz, methodName, paramTypes);
		}
	}

	/**
	 * 获得指定类中的Public方法名<br>
	 * 去重重载的方法
	 * 
	 * @param clazz 类
	 */
	public static Set<String> getDeclaredMethodNames(Class<?> clazz) {
		HashSet<String> methodSet = new HashSet<String>();
		Method[] methodArray = getDeclaredMethods(clazz);
		for (Method method : methodArray) {
			String methodName = method.getName();
			methodSet.add(methodName);
		}
		return methodSet;
	}

	/**
	 * 获得声明的所有方法，包括本类及其父类和接口的所有方法和Object类的方法
	 * 
	 * @param clazz 类
	 * @return 方法数组
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {
		Set<Method> methodSet = new HashSet<>();
		Method[] declaredMethods;
		for (; null != clazz; clazz = clazz.getSuperclass()) {
			declaredMethods = clazz.getDeclaredMethods();
			for (Method method : declaredMethods) {
				methodSet.add(method);
			}
		}
		return methodSet.toArray(new Method[methodSet.size()]);
	}

	/**
	 * 查找指定对象中的所有方法（包括非public方法），也包括父对象和Object类的方法
	 * 
	 * @param obj 被查找的对象
	 * @param methodName 方法名
	 * @param args 参数
	 * @return 方法
	 * @throws NoSuchMethodException 无此方法
	 * @throws SecurityException
	 */
	public static Method getDeclaredMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, SecurityException {
		return getDeclaredMethod(obj.getClass(), methodName, getClasses(args));
	}

	/**
	 * 查找指定类中的所有方法（包括非public方法），也包括父类和Object类的方法
	 * 
	 * @param clazz 被查找的类
	 * @param methodName 方法名
	 * @param parameterTypes 参数类型
	 * @return 方法
	 * @throws NoSuchMethodException 无此方法
	 * @throws SecurityException
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
		Method method = null;
		for (; null != clazz; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				break;
			} catch (NoSuchMethodException e) {
				// 继续向上寻找
			}
		}
		return method;
	}

	/**
	 * 是否为equals方法
	 * 
	 * @param method 方法
	 * @return 是否为equals方法
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null || !method.getName().equals("equals")) {
			return false;
		}
		Class<?>[] paramTypes = method.getParameterTypes();
		return (paramTypes.length == 1 && paramTypes[0] == Object.class);
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

	// ----------------------------------------------------------------------------------------- Classpath
	/**
	 * 获得ClassPath
	 * 
	 * @return ClassPath集合
	 */
	public static Set<String> getClassPathResources() {
		return getClassPaths(StrUtil.EMPTY);
	}

	/**
	 * 获得ClassPath
	 * 
	 * @param packageName 包名称
	 * @return ClassPath路径字符串集合
	 */
	public static Set<String> getClassPaths(String packageName) {
		String packagePath = packageName.replace(StrUtil.DOT, StrUtil.SLASH);
		Enumeration<URL> resources;
		try {
			resources = getClassLoader().getResources(packagePath);
		} catch (IOException e) {
			throw new UtilException(StrUtil.format("Loading classPath [{}] error!", packagePath), e);
		}
		Set<String> paths = new HashSet<String>();
		while (resources.hasMoreElements()) {
			paths.add(resources.nextElement().getPath());
		}
		return paths;
	}

	/**
	 * 获得ClassPath
	 * 
	 * @return ClassPath
	 */
	public static String getClassPath() {
		return getClassPathURL().getPath();
	}

	/**
	 * 获得ClassPath URL
	 * 
	 * @return ClassPath URL
	 */
	public static URL getClassPathURL() {
		return getURL(StrUtil.EMPTY);
	}

	/**
	 * 获得资源的URL
	 * 
	 * @param resource 资源（相对Classpath的路径）
	 * @return 资源URL
	 */
	public static URL getURL(String resource) {
		return ClassUtil.getClassLoader().getResource(resource);
	}

	/**
	 * @return 获得Java ClassPath路径，不包括 jre
	 */
	public static String[] getJavaClassPaths() {
		String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
		return classPaths;
	}

	/**
	 * 转换为原始类型
	 * 
	 * @param clazz 被转换为原始类型的类，必须为包装类型的类
	 * @return 基本类型类
	 */
	public static Class<?> castToPrimitive(Class<?> clazz) {
		if (null == clazz || clazz.isPrimitive()) {
			return clazz;
		}
		
		return BasicType.wrapperPrimitiveMap.get(clazz);
	}
	
	/**
	 * 原始类型转换为包装类型
	 * 
	 * @param clazz 被转换为包装类型的类，必须为原始类型的类
	 * @return 基本类型类
	 */
	public static Class<?> castToWrapper(Class<?> clazz) {
		return BasicType.primitiveWrapperMap.get(clazz);
	}

	/**
	 * @return 当前线程的class loader
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * 获得class loader<br>
	 * 若当前线程class loader不存在，取当前类的class loader
	 * 
	 * @return 类加载器
	 */
	public static ClassLoader getClassLoader() {
		ClassLoader classLoader = getContextClassLoader();
		if (classLoader == null) {
			classLoader = ClassUtil.class.getClassLoader();
		}
		return classLoader;
	}

	/**
	 * 实例化对象
	 * 
	 * @param clazz 类名
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazz) {
		try {
			return (T) Class.forName(clazz).newInstance();
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Instance class [{}] error!", clazz), e);
		}
	}

	/**
	 * 实例化对象
	 * 
	 * @param clazz 类
	 * @return 对象
	 */
	public static <T> T newInstance(Class<T> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Instance class [{}] error!", clazz), e);
		}
	}

	/**
	 * 实例化对象
	 * 
	 * @param clazz 类
	 * @return 对象
	 */
	public static <T> T newInstance(Class<T> clazz, Object... params) {
		if (CollectionUtil.isEmpty(params)) {
			return newInstance(clazz);
		}

		try {
			return clazz.getDeclaredConstructor(getClasses(params)).newInstance(params);
		} catch (Exception e) {
			throw new UtilException(StrUtil.format("Instance class [{}] error!", clazz), e);
		}
	}

	/**
	 * 加载类
	 * 
	 * @param <T>
	 * @param className 类名
	 * @param isInitialized 是否初始化
	 * @return 类
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> loadClass(String className, boolean isInitialized) {
		Class<T> clazz;
		try {
			clazz = (Class<T>) Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new UtilException(e);
		}
		return clazz;
	}

	/**
	 * 加载类并初始化
	 * 
	 * @param <T>
	 * @param className 类名
	 * @return 类
	 */
	public static <T> Class<T> loadClass(String className) {
		return loadClass(className, true);
	}

	// ---------------------------------------------------------------------------------------------------- Invoke start
	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 非单例模式，如果是非静态方法，每次创建一个新对象
	 * 
	 * @param <T>
	 * @param classNameDotMethodName 类名和方法名表达式，例如：com.xiaoleilu.hutool.StrUtil.isEmpty
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String classNameDotMethodName, Object[] args) {
		return invoke(classNameDotMethodName, false, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 
	 * @param <T>
	 * @param classNameDotMethodName 类名和方法名表达式，例如：com.xiaoleilu.hutool.StrUtil.isEmpty
	 * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String classNameDotMethodName, boolean isSingleton, Object[] args) {
		if (StrUtil.isBlank(classNameDotMethodName)) {
			throw new UtilException("Blank classNameDotMethodName!");
		}
		final int dotIndex = classNameDotMethodName.lastIndexOf('.');
		if (dotIndex <= 0) {
			throw new UtilException("Invalid classNameDotMethodName [{}]!", classNameDotMethodName);
		}

		final String className = classNameDotMethodName.substring(0, dotIndex);
		final String methodName = classNameDotMethodName.substring(dotIndex + 1);

		return invoke(className, methodName, isSingleton, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 非单例模式，如果是非静态方法，每次创建一个新对象
	 * 
	 * @param <T>
	 * @param className 类名，完整类路径
	 * @param methodName 方法名
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String className, String methodName, Object[] args) {
		return invoke(className, methodName, false, args);
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 执行非static方法时，必须满足对象有默认构造方法<br>
	 * 
	 * @param <T>
	 * @param className 类名，完整类路径
	 * @param methodName 方法名
	 * @param isSingleton 是否为单例对象，如果此参数为false，每次执行方法时创建一个新对象
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(String className, String methodName, boolean isSingleton, Object[] args) {
		Class<Object> clazz = loadClass(className);
		try {
			return invoke(isSingleton ? Singleton.get(clazz) : clazz.newInstance(), methodName, args);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 执行方法<br>
	 * 可执行Private方法，也可执行static方法<br>
	 * 
	 * @param <T>
	 * @param obj 对象
	 * @param methodName 方法名
	 * @param args 参数，必须严格对应指定方法的参数类型和数量
	 * @return 返回结果
	 */
	public static <T> T invoke(Object obj, String methodName, Object[] args) {
		try {
			final Method method = getDeclaredMethod(obj, methodName, args);
			return invoke(obj, method, args);
		} catch (Exception e) {
			throw new UtilException(e);
		}
	}

	/**
	 * 执行方法
	 * 
	 * @param obj 对象
	 * @param method 方法（对象方法或static方法都可）
	 * @param args 参数对象
	 * @return 结果
	 * @throws UtilException IllegalAccessException and IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Object obj, Method method, Object[] args) throws InvocationTargetException {
		if (false == method.isAccessible()) {
			method.setAccessible(true);
		}
		try {
			return (T) method.invoke(isStatic(method) ? null : obj, args);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new UtilException(e);
		}
	}
	// ---------------------------------------------------------------------------------------------------- Invoke end

	/**
	 * 新建代理对象<br>
	 * 动态代理类对象用于动态创建一个代理对象，可以在调用接口方法的时候动态执行相应逻辑
	 * 
	 * @param interfaceClass 被代理接口
	 * @param invocationHandler 代理执行类，此类用于实现具体的接口方法
	 * @return 被代理接口
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newProxyInstance(Class<T> interfaceClass, InvocationHandler invocationHandler) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass }, invocationHandler);
	}

	/**
	 * 是否为包装类型
	 * 
	 * @param clazz 类
	 * @return 是否为包装类型
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return BasicType.wrapperPrimitiveMap.containsKey(clazz);
	}

	/**
	 * 是否为基本类型（包括包装类和原始类）
	 * 
	 * @param clazz 类
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * 是否简单值类型或简单值类型的数组<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class及其数组
	 * 
	 * @param clazz 属性类
	 * @return 是否简单值类型或简单值类型的数组
	 */
	public static boolean isSimpleTypeOrArray(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * 是否为简单值类型<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class.
	 * 
	 * @param clazz 类
	 * @return 是否为简单值类型
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return isBasicType(clazz) || clazz.isEnum() || CharSequence.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) || clazz
				.equals(URI.class) || clazz.equals(URL.class) || clazz.equals(Locale.class) || clazz.equals(Class.class);
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
	public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
		if (null == targetType || null == sourceType) {
			return false;
		}

		// 对象类型
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}

		// 基本类型
		if (targetType.isPrimitive()) {
			// 原始类型
			Class<?> resolvedPrimitive = BasicType.wrapperPrimitiveMap.get(sourceType);
			if (resolvedPrimitive != null && targetType.equals(resolvedPrimitive)) {
				return true;
			}
		} else {
			// 包装类型
			Class<?> resolvedWrapper = BasicType.primitiveWrapperMap.get(sourceType);
			if (resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 指定类是否为Public
	 * 
	 * @param clazz 类
	 * @return 是否为public
	 */
	public static boolean isPublic(Class<?> clazz) {
		if (null == clazz) {
			throw new NullPointerException("Class to provided is null.");
		}
		return Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 指定方法是否为Public
	 * 
	 * @param method 方法
	 * @return 是否为public
	 */
	public static boolean isPublic(Method method) {
		if (null == method) {
			throw new NullPointerException("Method to provided is null.");
		}
		return isPublic(method.getDeclaringClass());
	}

	/**
	 * 指定类是否为非public
	 * 
	 * @param clazz 类
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Class<?> clazz) {
		return false == isPublic(clazz);
	}

	/**
	 * 指定方法是否为非public
	 * 
	 * @param method 方法
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Method method) {
		return false == isPublic(method);
	}

	/**
	 * 是否为静态方法
	 * 
	 * @param method 方法
	 * @return 是否为静态方法
	 */
	public static boolean isStatic(Method method) {
		return Modifier.isStatic(method.getModifiers());
	}

	/**
	 * 设置方法为可访问
	 * 
	 * @param method 方法
	 * @return 方法
	 */
	public static Method setAccessible(Method method) {
		if (null != method && ClassUtil.isNotPublic(method)) {
			method.setAccessible(true);
		}
		return method;
	}

	/**
	 * 是否为抽象类
	 * 
	 * @param clazz 类
	 * @return 是否为抽象类
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * 是否为标准的类<br>
	 * 这个类必须：<br>
	 * 1、非接口 2、非抽象类 3、非Enum枚举 4、非数组 5、非注解 6、非原始类型（int, long等）
	 * 
	 * @param clazz 类
	 * @return 是否为标准类
	 */
	public static boolean isNormalClass(Class<?> clazz) {
		return null != clazz && false == clazz.isInterface() && false == isAbstract(clazz) && false == clazz.isEnum() && false == clazz.isArray() && false == clazz.isAnnotation() && false == clazz
				.isSynthetic() && false == clazz.isPrimitive();
	}

	// --------------------------------------------------------------------------------------------------- Private method start
	/**
	 * 文件过滤器，过滤掉不需要的文件<br>
	 * 只保留Class文件、目录和Jar
	 */
	private static FileFilter fileFilter = new FileFilter(){
		@Override
		public boolean accept(File pathname) {
			return isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);
		}
	};

	/**
	 * 改变 com -> com. 避免在比较的时候把比如 completeTestSuite.class类扫描进去，如果没有"."</br>
	 * 那class里面com开头的class类也会被扫描进去,其实名称后面或前面需要一个 ".",来添加包的特征
	 * 
	 * @param packageName
	 * @return 格式化后的包名
	 */
	private static String getWellFormedPackageName(String packageName) {
		return packageName.lastIndexOf(StrUtil.DOT) != packageName.length() - 1 ? packageName + StrUtil.DOT : packageName;
	}

	/**
	 * 填充满足条件的class 填充到 classes<br>
	 * 同时会判断给定的路径是否为Jar包内的路径，如果是，则扫描此Jar包
	 * 
	 * @param path Class文件路径或者所在目录Jar包路径
	 * @param packageName 需要扫面的包名
	 * @param classFilter class过滤器
	 * @param classes List 集合
	 */
	private static void fillClasses(String path, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		// 判定给定的路径是否为Jar
		int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
		if (index != -1) {
			// Jar文件
			path = path.substring(0, index + FileUtil.JAR_FILE_EXT.length()); // 截取jar路径
			path = StrUtil.removePrefix(path, FileUtil.PATH_FILE_PRE); // 去掉文件前缀
			processJarFile(new File(path), packageName, classFilter, classes);
		} else {
			fillClasses(path, new File(path), packageName, classFilter, classes);
		}
	}

	/**
	 * 填充满足条件的class 填充到 classes
	 * 
	 * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
	 * @param file Class文件或者所在目录Jar包文件
	 * @param packageName 需要扫面的包名
	 * @param classFilter class过滤器
	 * @param classes List 集合
	 */
	private static void fillClasses(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		if (file.isDirectory()) {
			processDirectory(classPath, file, packageName, classFilter, classes);
		} else if (isClassFile(file)) {
			processClassFile(classPath, file, packageName, classFilter, classes);
		} else if (isJarFile(file)) {
			processJarFile(file, packageName, classFilter, classes);
		}
	}

	/**
	 * 处理如果为目录的情况,需要递归调用 fillClasses方法
	 * 
	 * @param directory 目录
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		for (File file : directory.listFiles(fileFilter)) {
			fillClasses(classPath, file, packageName, classFilter, classes);
		}
	}

	/**
	 * 处理为class文件的情况,填充满足条件的class 到 classes
	 * 
	 * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
	 * @param file class文件
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		if (false == classPath.endsWith(File.separator)) {
			classPath += File.separator;
		}
		String path = file.getAbsolutePath();
		if (StrUtil.isEmpty(packageName)) {
			path = StrUtil.removePrefix(path, classPath);
		}
		final String filePathWithDot = path.replace(File.separator, StrUtil.DOT);

		int subIndex = -1;
		if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
			final int endIndex = filePathWithDot.lastIndexOf(FileUtil.CLASS_EXT);

			final String className = filePathWithDot.substring(subIndex, endIndex);
			fillClass(className, packageName, classes, classFilter);
		}
	}

	/**
	 * 处理为jar文件的情况，填充满足条件的class 到 classes
	 * 
	 * @param file jar文件
	 * @param packageName 包名
	 * @param classFilter 类过滤器
	 * @param classes 类集合
	 */
	private static void processJarFile(File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
		try {
			for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
				if (isClass(entry.getName())) {
					final String className = entry.getName().replace(StrUtil.SLASH, StrUtil.DOT).replace(FileUtil.CLASS_EXT, StrUtil.EMPTY);
					fillClass(className, packageName, classes, classFilter);
				}
			}
		} catch (Exception ex) {
			log.error(ex);
		}
	}

	/**
	 * 填充class 到 classes
	 * 
	 * @param className 类名
	 * @param packageName 包名
	 * @param classes 类集合
	 * @param classFilter 类过滤器
	 */
	private static void fillClass(String className, String packageName, Set<Class<?>> classes, ClassFilter classFilter) {
		if (className.startsWith(packageName)) {
			try {
				final Class<?> clazz = Class.forName(className, false, getClassLoader());
				if (classFilter == null || classFilter.accept(clazz)) {
					classes.add(clazz);
				}
			} catch (Exception ex) {
				// Pass Load Error.
			}
		}
	}

	/**
	 * @param file 文件
	 * @return 是否为类文件
	 */
	private static boolean isClassFile(File file) {
		return isClass(file.getName());
	}

	/**
	 * @param fileName 文件名
	 * @return 是否为类文件
	 */
	private static boolean isClass(String fileName) {
		return fileName.endsWith(FileUtil.CLASS_EXT);
	}

	/**
	 * @param file 文件
	 * @return是否为Jar文件
	 */
	private static boolean isJarFile(File file) {
		return file.getName().endsWith(FileUtil.JAR_FILE_EXT);
	}
	// --------------------------------------------------------------------------------------------------- Private method end

	/**
	 * 类过滤器，用于过滤不需要加载的类<br>
	 */
	public interface ClassFilter {
		boolean accept(Class<?> clazz);
	}
}