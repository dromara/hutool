package cn.hutool.core.util;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.bean.NullWrapperBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.UniqueKeySet;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.InvocationTargetRuntimeException;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.reflect.MethodHandleUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.WeakConcurrentMap;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 反射工具类
 *
 * @author Looly
 * @since 3.0.9
 */
public class ReflectUtil {

	/**
	 * 构造对象缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Constructor<?>[]> CONSTRUCTORS_CACHE = new WeakConcurrentMap<>();
	/**
	 * 字段缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Field[]> FIELDS_CACHE = new WeakConcurrentMap<>();
	/**
	 * 方法缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Method[]> METHODS_CACHE = new WeakConcurrentMap<>();

	// --------------------------------------------------------------------------------------------------------- Constructor

	/**
	 * 查找类中的指定参数的构造方法，如果找到构造方法，会自动设置可访问为true
	 *
	 * @param <T>            对象类型
	 * @param clazz          类
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
				setAccessible(constructor);
				return (Constructor<T>) constructor;
			}
		}
		return null;
	}

	/**
	 * 获得一个类中所有构造列表
	 *
	 * @param <T>       构造的对象类型
	 * @param beanClass 类，非{@code null}
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] getConstructors(Class<T> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		return (Constructor<T>[]) CONSTRUCTORS_CACHE.computeIfAbsent(beanClass, () -> getConstructorsDirectly(beanClass));
	}

	/**
	 * 获得一个类中所有构造列表，直接反射获取，无缓存
	 *
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass) throws SecurityException {
		return beanClass.getDeclaredConstructors();
	}

	// --------------------------------------------------------------------------------------------------------- Field

	/**
	 * 查找指定类中是否包含指定名称对应的字段，包括所有字段（包括非public字段），也包括父类和Object类的字段
	 *
	 * @param beanClass 被查找字段的类,不能为null
	 * @param name      字段名
	 * @return 是否包含字段
	 * @throws SecurityException 安全异常
	 * @since 4.1.21
	 */
	public static boolean hasField(Class<?> beanClass, String name) throws SecurityException {
		return null != getField(beanClass, name);
	}

	/**
	 * 获取字段名，如果存在{@link Alias}注解，读取注解的值作为名称
	 *
	 * @param field 字段
	 * @return 字段名
	 * @since 5.1.6
	 */
	public static String getFieldName(Field field) {
		if (null == field) {
			return null;
		}

		final Alias alias = field.getAnnotation(Alias.class);
		if (null != alias) {
			return alias.value();
		}

		return field.getName();
	}

	/**
	 * 查找指定类中的指定name的字段（包括非public字段），也包括父类和Object类的字段， 字段不存在则返回{@code null}
	 *
	 * @param beanClass 被查找字段的类,不能为null
	 * @param name      字段名
	 * @return 字段
	 * @throws SecurityException 安全异常
	 */
	public static Field getField(Class<?> beanClass, String name) throws SecurityException {
		final Field[] fields = getFields(beanClass);
		return ArrayUtil.firstMatch((field) -> name.equals(getFieldName(field)), fields);
	}

	/**
	 * 获取指定类中字段名和字段对应的有序Map，包括其父类中的字段<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass 类
	 * @return 字段名和字段对应的Map，有序
	 * @since 5.0.7
	 */
	public static Map<String, Field> getFieldMap(Class<?> beanClass) {
		final Field[] fields = getFields(beanClass);
		final HashMap<String, Field> map = MapUtil.newHashMap(fields.length, true);
		for (Field field : fields) {
			map.put(field.getName(), field);
		}
		return map;
	}

	/**
	 * 获得一个类中所有字段列表，包括其父类中的字段<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Field[] getFields(Class<?> beanClass) throws SecurityException {
		Assert.notNull(beanClass);
		return FIELDS_CACHE.computeIfAbsent(beanClass, () -> getFieldsDirectly(beanClass, true));
	}


	/**
	 * 获得一个类中所有满足条件的字段列表，包括其父类中的字段<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass   类
	 * @param fieldFilter field过滤器，过滤掉不需要的field
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 * @since 5.7.14
	 */
	public static Field[] getFields(Class<?> beanClass, Filter<Field> fieldFilter) throws SecurityException {
		return ArrayUtil.filter(getFields(beanClass), fieldFilter);
	}

	/**
	 * 获得一个类中所有字段列表，直接反射获取，无缓存<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass            类
	 * @param withSuperClassFields 是否包括父类的字段列表
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Field[] getFieldsDirectly(Class<?> beanClass, boolean withSuperClassFields) throws SecurityException {
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
			searchType = withSuperClassFields ? searchType.getSuperclass() : null;
		}

		return allFields;
	}

	/**
	 * 获取字段值
	 *
	 * @param obj       对象，如果static字段，此处为类
	 * @param fieldName 字段名
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static Object getFieldValue(Object obj, String fieldName) throws UtilException {
		if (null == obj || StrUtil.isBlank(fieldName)) {
			return null;
		}
		return getFieldValue(obj, getField(obj instanceof Class ? (Class<?>) obj : obj.getClass(), fieldName));
	}

	/**
	 * 获取静态字段值
	 *
	 * @param field 字段
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 * @since 5.1.0
	 */
	public static Object getStaticFieldValue(Field field) throws UtilException {
		return getFieldValue(null, field);
	}

	/**
	 * 获取字段值
	 *
	 * @param obj   对象，static字段则此字段为null
	 * @param field 字段
	 * @return 字段值
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static Object getFieldValue(Object obj, Field field) throws UtilException {
		if (null == field) {
			return null;
		}
		if (obj instanceof Class) {
			// 静态字段获取时对象为null
			obj = null;
		}

		setAccessible(field);
		Object result;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			throw new UtilException(e, "IllegalAccess for {}.{}", field.getDeclaringClass(), field.getName());
		}
		return result;
	}

	/**
	 * 获取所有字段的值
	 *
	 * @param obj bean对象，如果是static字段，此处为类class
	 * @return 字段值数组
	 * @since 4.1.17
	 */
	public static Object[] getFieldsValue(Object obj) {
		if (null != obj) {
			final Field[] fields = getFields(obj instanceof Class ? (Class<?>) obj : obj.getClass());
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
	 * 设置字段值<br>
	 * 若值类型与字段类型不一致，则会尝试通过 {@link Convert} 进行转换<br>
	 * 若字段类型是原始类型而传入的值是 null，则会将字段设置为对应原始类型的默认值（见 {@link ClassUtil#getDefaultValue(Class)}）
	 * 如果是final字段，setFieldValue，调用这可以先调用 {@link ReflectUtil#removeFinalModify(Field)}方法去除final修饰符<br>
	 *
	 * @param obj       对象,static字段则此处传Class
	 * @param fieldName 字段名
	 * @param value     值，当值类型与字段类型不匹配时，会尝试转换
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(Object obj, String fieldName, Object value) throws UtilException {
		Assert.notNull(obj);
		Assert.notBlank(fieldName);

		final Field field = getField((obj instanceof Class) ? (Class<?>) obj : obj.getClass(), fieldName);
		Assert.notNull(field, "Field [{}] is not exist in [{}]", fieldName, obj.getClass().getName());
		setFieldValue(obj, field, value);
	}

	/**
	 * 设置字段值<br>
	 * 若值类型与字段类型不一致，则会尝试通过 {@link Convert} 进行转换<br>
	 * 若字段类型是原始类型而传入的值是 null，则会将字段设置为对应原始类型的默认值（见 {@link ClassUtil#getDefaultValue(Class)}）<br>
	 * 如果是final字段，setFieldValue，调用这可以先调用 {@link ReflectUtil#removeFinalModify(Field)}方法去除final修饰符
	 *
	 * @param obj   对象，如果是static字段，此参数为null
	 * @param field 字段
	 * @param value 值，当值类型与字段类型不匹配时，会尝试转换
	 * @throws UtilException UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(Object obj, Field field, Object value) throws UtilException {
		Assert.notNull(field, "Field in [{}] not exist !", obj);

		final Class<?> fieldType = field.getType();
		if (null != value) {
			if (false == fieldType.isAssignableFrom(value.getClass())) {
				//对于类型不同的字段，尝试转换，转换失败则使用原对象类型
				final Object targetValue = Convert.convert(fieldType, value);
				if (null != targetValue) {
					value = targetValue;
				}
			}
		} else {
			// 获取null对应默认值，防止原始类型造成空指针问题
			value = ClassUtil.getDefaultValue(fieldType);
		}

		setAccessible(field);
		try {
			field.set(obj instanceof Class ? null : obj, value);
		} catch (IllegalAccessException e) {
			throw new UtilException(e, "IllegalAccess for {}.{}", obj, field.getName());
		}
	}

	/**
	 * 是否为父类引用字段<br>
	 * 当字段所在类是对象子类时（对象中定义的非static的class），会自动生成一个以"this$0"为名称的字段，指向父类对象
	 *
	 * @param field 字段
	 * @return 是否为父类引用字段
	 * @since 5.7.20
	 */
	public static boolean isOuterClassField(Field field) {
		return "this$0".equals(field.getName());
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
		final HashSet<String> methodSet = new HashSet<>();
		final Method[] methodArray = getPublicMethods(clazz);
		if (ArrayUtil.isNotEmpty(methodArray)) {
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
	 * 获得指定类过滤后的Public方法列表<br>
	 * TODO 6.x此方法更改返回Method[]
	 *
	 * @param clazz  查找方法的类
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
			methodList = CollUtil.newArrayList(methods);
		}
		return methodList;
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz          查找方法的类
	 * @param excludeMethods 不包括的方法
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, Method... excludeMethods) {
		final HashSet<Method> excludeMethodSet = CollUtil.newHashSet(excludeMethods);
		return getPublicMethods(clazz, method -> false == excludeMethodSet.contains(method));
	}

	/**
	 * 获得指定类过滤后的Public方法列表
	 *
	 * @param clazz              查找方法的类
	 * @param excludeMethodNames 不包括的方法名列表
	 * @return 过滤后的方法列表
	 */
	public static List<Method> getPublicMethods(Class<?> clazz, String... excludeMethodNames) {
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
	public static Method getMethodOfObj(Object obj, String methodName, Object... args) throws SecurityException {
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
	public static Method getMethodIgnoreCase(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
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
	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws SecurityException {
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
	public static Method getMethod(Class<?> clazz, boolean ignoreCase, String methodName, Class<?>... paramTypes) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		Method res = null;
		final Method[] methods = getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
					&& ClassUtil.isAllAssignableFrom(method.getParameterTypes(), paramTypes)
					//排除协变桥接方法，pr#1965@Github
					&& (res == null
					|| res.getReturnType().isAssignableFrom(method.getReturnType()))) {
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
	public static Method getMethodByName(Class<?> clazz, String methodName) throws SecurityException {
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
	public static Method getMethodByNameIgnoreCase(Class<?> clazz, String methodName) throws SecurityException {
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
	public static Method getMethodByName(Class<?> clazz, boolean ignoreCase, String methodName) throws SecurityException {
		if (null == clazz || StrUtil.isBlank(methodName)) {
			return null;
		}

		Method res = null;
		final Method[] methods = getMethods(clazz);
		if (ArrayUtil.isNotEmpty(methods)) {
			for (Method method : methods) {
				if (StrUtil.equals(methodName, method.getName(), ignoreCase)
					//排除协变桥接方法，pr#1965@Github
					&& (res == null
					|| res.getReturnType().isAssignableFrom(method.getReturnType()))) {
					res = method;
				}
			}
		}
		return res;
	}

	/**
	 * 获得指定类中的方法名<br>
	 * 去重重载的方法
	 *
	 * @param clazz 类
	 * @return 方法名Set
	 * @throws SecurityException 安全异常
	 */
	public static Set<String> getMethodNames(Class<?> clazz) throws SecurityException {
		final HashSet<String> methodSet = new HashSet<>();
		final Method[] methods = getMethods(clazz);
		for (Method method : methods) {
			methodSet.add(method.getName());
		}
		return methodSet;
	}

	/**
	 * 获得指定类过滤后的方法列表
	 *
	 * @param clazz  查找方法的类
	 * @param filter 过滤器
	 * @return 过滤后的方法列表
	 * @throws SecurityException 安全异常
	 */
	public static Method[] getMethods(Class<?> clazz, Filter<Method> filter) throws SecurityException {
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
	public static Method[] getMethods(Class<?> beanClass) throws SecurityException {
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
	public static Method[] getMethodsDirectly(Class<?> beanClass, boolean withSupers, boolean withMethodFromObject) throws SecurityException {
		Assert.notNull(beanClass);

		if (beanClass.isInterface()) {
			// 对于接口，直接调用Class.getMethods方法获取所有方法，因为接口都是public方法
			return withSupers ? beanClass.getMethods() : beanClass.getDeclaredMethods();
		}

		final UniqueKeySet<String, Method> result = new UniqueKeySet<>(true, ReflectUtil::getUniqueKey);
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
	public static boolean isEqualsMethod(Method method) {
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
	public static boolean isHashCodeMethod(Method method) {
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
	public static boolean isToStringMethod(Method method) {
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
	public static boolean isEmptyParam(Method method) {
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
	public static boolean isGetterOrSetterIgnoreCase(Method method) {
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
	public static boolean isGetterOrSetter(Method method, boolean ignoreCase) {
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
	// --------------------------------------------------------------------------------------------------------- newInstance

	/**
	 * 实例化对象
	 *
	 * @param <T>   对象类型
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
	 * @param <T>    对象类型
	 * @param clazz  类
	 * @param params 构造函数参数
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	public static <T> T newInstance(Class<T> clazz, Object... params) throws UtilException {
		if (ArrayUtil.isEmpty(params)) {
			final Constructor<T> constructor = getConstructor(clazz);
			if (null == constructor) {
				throw new UtilException("No constructor for [{}]", clazz);
			}
			try {
				return constructor.newInstance();
			} catch (Exception e) {
				throw new UtilException(e, "Instance class [{}] error!", clazz);
			}
		}

		final Class<?>[] paramTypes = ClassUtil.getClasses(params);
		final Constructor<T> constructor = getConstructor(clazz, paramTypes);
		if (null == constructor) {
			throw new UtilException("No Constructor matched for parameter types: [{}]", new Object[]{paramTypes});
		}
		try {
			return constructor.newInstance(params);
		} catch (Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
	 * <p>
	 * 对于某些特殊的接口，按照其默认实现实例化，例如：
	 * <pre>
	 *     Map       -》 HashMap
	 *     Collction -》 ArrayList
	 *     List      -》 ArrayList
	 *     Set       -》 HashSet
	 * </pre>
	 *
	 * @param <T>  对象类型
	 * @param type 被构造的类
	 * @return 构造后的对象，构造失败返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceIfPossible(Class<T> type) {
		Assert.notNull(type);

		// 原始类型
		if (type.isPrimitive()) {
			return (T) ClassUtil.getPrimitiveDefaultValue(type);
		}

		// 某些特殊接口的实例化按照默认实现进行
		if (type.isAssignableFrom(AbstractMap.class)) {
			type = (Class<T>) HashMap.class;
		} else if (type.isAssignableFrom(List.class)) {
			type = (Class<T>) ArrayList.class;
		} else if (type.isAssignableFrom(Set.class)) {
			type = (Class<T>) HashSet.class;
		}

		try {
			return newInstance(type);
		} catch (Exception e) {
			// ignore
			// 默认构造不存在的情况下查找其它构造
		}

		// 枚举
		if (type.isEnum()) {
			return type.getEnumConstants()[0];
		}

		// 数组
		if (type.isArray()) {
			return (T) Array.newInstance(type.getComponentType(), 0);
		}

		final Constructor<T>[] constructors = getConstructors(type);
		Class<?>[] parameterTypes;
		for (Constructor<T> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			if (0 == parameterTypes.length) {
				continue;
			}
			setAccessible(constructor);
			try {
				return constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
			} catch (Exception ignore) {
				// 构造出错时继续尝试下一种构造方式
			}
		}
		return null;
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
	 * @param <T>    返回对象类型
	 * @param obj    对象，如果执行静态方法，此值为{@code null}
	 * @param method 方法（对象方法或static方法都可）
	 * @param args   参数对象
	 * @return 结果
	 * @throws UtilException 一些列异常的包装
	 */
	public static <T> T invokeWithCheck(Object obj, Method method, Object... args) throws UtilException {
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
	 * @throws InvocationTargetRuntimeException 目标方法执行异常
	 * @throws UtilException                    {@link IllegalAccessException}异常的包装
	 */
	public static <T> T invoke(Object obj, Method method, Object... args) throws InvocationTargetRuntimeException, UtilException {
		try {
			return invokeRaw(obj, method, args);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new UtilException(e);
		}
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
	 * @throws InvocationTargetException 目标方法执行异常
	 * @throws IllegalAccessException    访问异常
	 * @since 5.8.1
	 */
	@SuppressWarnings("unchecked")
	public static <T> T invokeRaw(Object obj, Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
		setAccessible(method);

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
					final Object targetValue = Convert.convertQuietly(parameterTypes[i], args[i], args[i]);
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

		return (T) method.invoke(ClassUtil.isStatic(method) ? null : obj, actualArgs);
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
	 * @throws UtilException IllegalAccessException等异常包装
	 * @see NullWrapperBean
	 * @since 3.1.2
	 */
	public static <T> T invoke(Object obj, String methodName, Object... args) throws UtilException {
		Assert.notNull(obj, "Object to get method must be not null!");
		Assert.notBlank(methodName, "Method name must be not blank!");

		final Method method = getMethodOfObj(obj, methodName, args);
		if (null == method) {
			throw new UtilException("No such method: [{}] from [{}]", methodName, obj.getClass());
		}
		return invoke(obj, method, args);
	}

	/**
	 * 设置方法为可访问（私有方法可以被外部调用）
	 *
	 * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 * @since 4.6.8
	 */
	public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
		if (null != accessibleObject && false == accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
		return accessibleObject;
	}

	/**
	 * 设置final的field字段可以被修改
	 * 只要不会被编译器内联优化的 final 属性就可以通过反射有效的进行修改 --  修改后代码中可使用到新的值;
	 * <p>以下属性，编译器会内联优化，无法通过反射修改：</p>
	 * <ul>
	 *     <li> 基本类型 byte, char, short, int, long, float, double, boolean</li>
	 *     <li> Literal String 类型(直接双引号字符串)</li>
	 * </ul>
	 * <h3>以下属性，可以通过反射修改：</h3>
	 * <ul>
	 *     <li>基本类型的包装类 Byte、Character、Short、Long、Float、Double、Boolean</li>
	 *     <li>字符串，通过 new String("")实例化</li>
	 *     <li>自定义java类</li>
	 * </ul>
	 * <pre class="code">
	 * {@code
	 *      //示例，移除final修饰符
	 *      class JdbcDialects {private static final List<Number> dialects = new ArrayList<>();}
	 *      Field field = ReflectUtil.getField(JdbcDialects.class, fieldName);
	 * 		ReflectUtil.removeFinalModify(field);
	 * 		ReflectUtil.setFieldValue(JdbcDialects.class, fieldName, dialects);
	 *    }
	 * </pre>
	 *
	 * @param field 被修改的field，不可以为空
	 * @throws UtilException IllegalAccessException等异常包装
	 * @author dazer
	 * @since 5.8.8
	 */
	public static void removeFinalModify(Field field) {
		ModifierUtil.removeFinalModify(field);
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
	private static String getUniqueKey(Method method) {
		final StringBuilder sb = new StringBuilder();
		sb.append(method.getReturnType().getName()).append('#');
		sb.append(method.getName());
		Class<?>[] parameters = method.getParameterTypes();
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
	private static List<Method> getDefaultMethodsFromInterface(Class<?> clazz) {
		List<Method> result = new ArrayList<>();
		for (Class<?> ifc : clazz.getInterfaces()) {
			for (Method m : ifc.getMethods()) {
				if (false == ModifierUtil.isAbstract(m)) {
					result.add(m);
				}
			}
		}
		return result;
	}
}
