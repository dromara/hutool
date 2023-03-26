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

package cn.hutool.core.reflect;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.map.WeakConcurrentMap;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ArrayUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * 反射中{@link Field}字段工具类，包括字段获取和字段赋值。
 *
 * @author looly
 */
public class FieldUtil {

	/**
	 * 字段缓存
	 */
	private static final WeakConcurrentMap<Class<?>, Field[]> FIELDS_CACHE = new WeakConcurrentMap<>();

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
	 *
	 * @param beanClass Bean的Class
	 * @param name      字段名称
	 * @return 字段对象，如果未找到返回{@code null}
	 */
	public static Field getDeClearField(final Class<?> beanClass, final String name) {
		try {
			return beanClass.getDeclaredField(name);
		} catch (final NoSuchFieldException e) {
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
		return FIELDS_CACHE.computeIfAbsent(beanClass, (key) -> getFieldsDirectly(beanClass, true));
	}


	/**
	 * 获得一个类中所有满足条件的字段列表，包括其父类中的字段<br>
	 * 如果子类与父类中存在同名字段，则这两个字段同时存在，子类字段在前，父类字段在后。
	 *
	 * @param beanClass      类
	 * @param fieldPredicate field过滤器，过滤掉不需要的field，{@link Predicate#test(Object)}为{@code true}保留，null表示全部保留
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 * @since 5.7.14
	 */
	public static Field[] getFields(final Class<?> beanClass, final Predicate<Field> fieldPredicate) throws SecurityException {
		return ArrayUtil.filter(getFields(beanClass), fieldPredicate);
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

		ReflectUtil.setAccessible(field);
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
	 * 设置静态（static）字段值
	 *
	 * @param field 字段
	 * @param value 值，值类型必须与字段类型匹配，不会自动转换对象类型
	 * @throws UtilException UtilException 包装IllegalAccessException异常
	 */
	public static void setStaticFieldValue(final Field field, final Object value) throws UtilException {
		setFieldValue(null, field, value);
	}

	/**
	 * 设置字段值，如果值类型必须与字段类型匹配，会自动转换对象类型
	 *
	 * @param obj   对象，如果是static字段，此参数为null
	 * @param field 字段
	 * @param value 值，类型不匹配会自动转换对象类型
	 * @throws UtilException UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValue(final Object obj, final Field field, Object value) throws UtilException {
		Assert.notNull(field, "Field in [{}] not exist !", obj);

		// 值类型检查和转换
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

		setFieldValueExact(obj, field, value);
	}

	/**
	 * 设置字段值，传入的字段值必须和字段类型一致，否则抛出异常
	 *
	 * @param obj   对象，如果是static字段，此参数为null
	 * @param field 字段
	 * @param value 值，值类型必须与字段类型匹配
	 * @throws UtilException UtilException 包装IllegalAccessException异常
	 */
	public static void setFieldValueExact(final Object obj, final Field field, final Object value) throws UtilException {
		ReflectUtil.setAccessible(field);
		try {
			field.set(obj instanceof Class ? null : obj, value);
		} catch (final IllegalAccessException e) {
			throw new UtilException(e, "IllegalAccess for [{}.{}]", null == obj ? field.getDeclaringClass() : obj, field.getName());
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
}
