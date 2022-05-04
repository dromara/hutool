package cn.hutool.core.reflect;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Filter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
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
	public static <T> Constructor<T> getConstructor(final Class<T> clazz, final Class<?>... parameterTypes) {
		if (null == clazz) {
			return null;
		}

		final Constructor<?>[] constructors = getConstructors(clazz);
		Class<?>[] pts;
		for (final Constructor<?> constructor : constructors) {
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
	public static <T> Constructor<T>[] getConstructors(final Class<T> beanClass) throws SecurityException {
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
	public static Constructor<?>[] getConstructorsDirectly(final Class<?> beanClass) throws SecurityException {
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
	public static boolean hasField(final Class<?> beanClass, final String name) throws SecurityException {
		return null != getField(beanClass, name);
	}

	/**
	 * 获取字段名，如果存在{@link Alias}注解，读取注解的值作为名称
	 *
	 * @param field 字段
	 * @return 字段名
	 * @since 5.1.6
	 */
	public static String getFieldName(final Field field) {
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
	public static Field getField(final Class<?> beanClass, final String name) throws SecurityException {
		final Field[] fields = getFields(beanClass);
		return ArrayUtil.firstMatch((field) -> name.equals(getFieldName(field)), fields);
	}

	/**
	 * 获取本类定义的指定名称的字段，包括私有字段，但是不包括父类字段
	 * @param beanClass Bean的Class
	 * @param name 字段名称
	 * @return 字段对象，如果未找到返回{@code null}
	 */
	public static Field getDeClearField(final Class<?> beanClass, final String name){
		try {
			return beanClass.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	/**
	 * 获取指定类中字段名和字段对应的有序Map，包括其父类中的字段<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass 类
	 * @return 字段名和字段对应的Map，有序
	 * @since 5.0.7
	 */
	public static Map<String, Field> getFieldMap(final Class<?> beanClass) {
		final Field[] fields = getFields(beanClass);
		final HashMap<String, Field> map = MapUtil.newHashMap(fields.length, true);
		for (final Field field : fields) {
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
	public static Field[] getFields(final Class<?> beanClass) throws SecurityException {
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
	public static Field[] getFields(final Class<?> beanClass, final Filter<Field> fieldFilter) throws SecurityException {
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
	public static Field[] getFieldsDirectly(final Class<?> beanClass, final boolean withSuperClassFields) throws SecurityException {
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
	public static Object getFieldValue(final Object obj, final String fieldName) throws UtilException {
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
	public static Object getStaticFieldValue(final Field field) throws UtilException {
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
	public static Object getFieldValue(Object obj, final Field field) throws UtilException {
		if (null == field) {
			return null;
		}
		if (obj instanceof Class) {
			// 静态字段获取时对象为null
			obj = null;
		}

		setAccessible(field);
		final Object result;
		try {
			result = field.get(obj);
		} catch (final IllegalAccessException e) {
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
	public static Object[] getFieldsValue(final Object obj) {
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
	 * 设置字段值
	 *
	 * @param obj       对象,static字段则此处传Class
	 * @param fieldName 字段名
	 * @param value     值，值类型必须与字段类型匹配，不会自动转换对象类型
	 * @throws UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws UtilException {
		Assert.notNull(obj);
		Assert.notBlank(fieldName);

		final Field field = getField((obj instanceof Class) ? (Class<?>) obj : obj.getClass(), fieldName);
		Assert.notNull(field, "Field [{}] is not exist in [{}]", fieldName, obj.getClass().getName());
		setFieldValue(obj, field, value);
	}

	/**
	 * 设置字段值
	 *
	 * @param obj   对象，如果是static字段，此参数为null
	 * @param field 字段
	 * @param value 值，值类型必须与字段类型匹配，不会自动转换对象类型
	 * @throws UtilException UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(final Object obj, final Field field, Object value) throws UtilException {
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
		} catch (final IllegalAccessException e) {
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
	public static boolean isOuterClassField(final Field field) {
		return "this$0".equals(field.getName());
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
	public static <T> T newInstance(final String clazz) throws UtilException {
		try {
			return (T) Class.forName(clazz).newInstance();
		} catch (final Exception e) {
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
	public static <T> T newInstance(final Class<T> clazz, final Object... params) throws UtilException {
		if (ArrayUtil.isEmpty(params)) {
			final Constructor<T> constructor = getConstructor(clazz);
			try {
				return constructor.newInstance();
			} catch (final Exception e) {
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
		} catch (final Exception e) {
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
	 * @param <T>       对象类型
	 * @param type 被构造的类
	 * @return 构造后的对象，构造失败返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceIfPossible(Class<T> type) {
		Assert.notNull(type);

		// 原始类型
		if(type.isPrimitive()){
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
		} catch (final Exception e) {
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
		for (final Constructor<T> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			if (0 == parameterTypes.length) {
				continue;
			}
			setAccessible(constructor);
			try {
				return constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
			} catch (final Exception ignore) {
				// 构造出错时继续尝试下一种构造方式
			}
		}
		return null;
	}

	/**
	 * 设置方法为可访问（私有方法可以被外部调用）
	 *
	 * @param <T>              AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 * @since 4.6.8
	 */
	public static <T extends AccessibleObject> T setAccessible(final T accessibleObject) {
		if (null != accessibleObject && false == accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
		return accessibleObject;
	}
}
