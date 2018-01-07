package cn.hutool.core.bean;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanDesc.PropDesc;
import cn.hutool.core.bean.copier.BeanCopier;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * Bean工具类
 * 
 * <p>
 * 把一个拥有对属性进行set和get方法的类，我们就可以称之为JavaBean。
 * </p>
 * 
 * @author Looly
 * @since 3.1.2
 */
public class BeanUtil {

	/**
	 * 判断是否为Bean对象<br>
	 * 判定方法是是否存在只有一个参数的setXXX方法
	 * 
	 * @param clazz 待测试类
	 * @return 是否为Bean对象
	 */
	public static boolean isBean(Class<?> clazz) {
		if (ClassUtil.isNormalClass(clazz)) {
			final Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
					// 检测包含标准的setXXX方法即视为标准的JavaBean
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 创建动态Bean
	 * 
	 * @param bean 普通Bean或Map
	 * @return {@link DynaBean}
	 * @since 3.0.7
	 */
	public static DynaBean createDynaBean(Object bean) {
		return new DynaBean(bean);
	}

	/**
	 * 查找类型转换器 {@link PropertyEditor}
	 * 
	 * @param type 需要转换的目标类型
	 * @return {@link PropertyEditor}
	 */
	public static PropertyEditor findEditor(Class<?> type) {
		return PropertyEditorManager.findEditor(type);
	}

	public static boolean hasNull(Object bean, boolean ignoreError) {
		final Field[] fields = ClassUtil.getDeclaredFields(bean.getClass());

		Object fieldValue = null;
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				fieldValue = field.get(bean);
			} catch (Exception e) {

			}
			if (null == fieldValue) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取{@link BeanDesc} Bean描述信息
	 * 
	 * @param clazz Bean类
	 * @return {@link BeanDesc}
	 * @since 3.1.2
	 */
	public static BeanDesc getBeanDesc(Class<?> clazz) {
		BeanDesc beanDesc = BeanDescCache.INSTANCE.getBeanDesc(clazz);
		if (null == beanDesc) {
			beanDesc = new BeanDesc(clazz);
			BeanDescCache.INSTANCE.putBeanDesc(clazz, beanDesc);
		}
		return beanDesc;
	}

	// --------------------------------------------------------------------------------------------------------- PropertyDescriptor
	/**
	 * 获得Bean字段描述数组
	 * 
	 * @param clazz Bean类
	 * @return 字段描述数组
	 * @throws IntrospectionException 获取属性异常
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
		return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
	}

	/**
	 * 获得字段名和字段描述Map，获得的结果会缓存在 {@link BeanInfoCache}中
	 * 
	 * @param clazz Bean类
	 * @param ignoreCase 是否忽略大小写
	 * @return 字段名和字段描述Map
	 * @throws IntrospectionException 获取属性异常
	 */
	public static Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> clazz, boolean ignoreCase) throws IntrospectionException {
		Map<String, PropertyDescriptor> map = BeanInfoCache.INSTANCE.getPropertyDescriptorMap(clazz, ignoreCase);
		if (null == map) {
			map = internalGetPropertyDescriptorMap(clazz, ignoreCase);
			BeanInfoCache.INSTANCE.putPropertyDescriptorMap(clazz, map, ignoreCase);
		}
		return map;
	}

	/**
	 * 获得字段名和字段描述Map。内部使用，直接获取Bean类的PropertyDescriptor
	 * 
	 * @param clazz Bean类
	 * @param ignoreCase 是否忽略大小写
	 * @return 字段名和字段描述Map
	 * @throws IntrospectionException 获取属性异常
	 */
	private static Map<String, PropertyDescriptor> internalGetPropertyDescriptorMap(Class<?> clazz, boolean ignoreCase) throws IntrospectionException {
		final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		final Map<String, PropertyDescriptor> map = ignoreCase ? new CaseInsensitiveMap<String, PropertyDescriptor>(propertyDescriptors.length, 1)
				: new HashMap<String, PropertyDescriptor>((int) (propertyDescriptors.length), 1);

		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			map.put(propertyDescriptor.getName(), propertyDescriptor);
		}
		return map;
	}

	/**
	 * 获得Bean类属性描述，大小写敏感
	 * 
	 * @param clazz Bean类
	 * @param fieldName 字段名
	 * @return PropertyDescriptor
	 * @throws IntrospectionException 获取属性异常
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, final String fieldName) throws IntrospectionException {
		return getPropertyDescriptor(clazz, fieldName, false);
	}

	/**
	 * 获得Bean类属性描述
	 * 
	 * @param clazz Bean类
	 * @param fieldName 字段名
	 * @param ignoreCase 是否忽略大小写
	 * @return PropertyDescriptor
	 * @throws IntrospectionException 获取属性异常
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, final String fieldName, boolean ignoreCase) throws IntrospectionException {
		final Map<String, PropertyDescriptor> map = getPropertyDescriptorMap(clazz, ignoreCase);
		return (null == map) ? null : map.get(fieldName);
	}

	/**
	 * 获得字段值，通过反射直接获得字段值，并不调用getXXX方法<br>
	 * 对象同样支持Map类型，fieldName即为key
	 * 
	 * @param bean Bean对象
	 * @param fieldName 字段名
	 * @return 字段值
	 */
	public static Object getFieldValue(Object bean, String fieldName) {
		if (null == bean || StrUtil.isBlank(fieldName)) {
			return null;
		}

		if (bean instanceof Map) {
			return ((Map<?, ?>) bean).get(fieldName);
		} else if (bean instanceof List) {
			return ((List<?>) bean).get(Integer.parseInt(fieldName));
		} else if (bean instanceof Collection) {
			return ((Collection<?>) bean).toArray()[Integer.parseInt(fieldName)];
		} else if (ArrayUtil.isArray(bean)) {
			return Array.get(bean, Integer.parseInt(fieldName));
		} else {// 普通Bean对象
			Field field;
			try {
				field = ClassUtil.getDeclaredField(bean.getClass(), fieldName);
				if (null != field) {
					field.setAccessible(true);
					return field.get(bean);
				}
			} catch (Exception e) {
				throw new UtilException(e);
			}
		}
		return null;
	}

	/**
	 * 解析Bean中的属性值
	 * 
	 * @param bean Bean对象，支持Map、List、Collection、Array
	 * @param expression 表达式，例如：person.friend[5].name
	 * @return Bean属性值
	 * @see BeanResolver#resolveBean(Object, String)
	 * @since 3.0.7
	 */
	public static Object getProperty(Object bean, String expression) {
		return BeanResolver.resolveBean(bean, expression);
	}

	// --------------------------------------------------------------------------------------------- mapToBean
	/**
	 * Map转换为Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param map {@link Map}
	 * @param beanClass Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
		return fillBeanWithMap(map, ReflectUtil.newInstance(beanClass), isIgnoreError);
	}

	/**
	 * Map转换为Bean对象<br>
	 * 忽略大小写
	 * 
	 * @param <T> Bean类型
	 * @param map Map
	 * @param beanClass Bean Class
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T mapToBeanIgnoreCase(Map<?, ?> map, Class<T> beanClass, boolean isIgnoreError) {
		return fillBeanWithMapIgnoreCase(map, ReflectUtil.newInstance(beanClass), isIgnoreError);
	}

	/**
	 * Map转换为Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param map {@link Map}
	 * @param beanClass Bean Class
	 * @param copyOptions 转Bean选项
	 * @return Bean
	 */
	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass, CopyOptions copyOptions) {
		return fillBeanWithMap(map, ReflectUtil.newInstance(beanClass), copyOptions);
	}
	// --------------------------------------------------------------------------------------------- fillBeanWithMap
	/**
	 * 使用Map填充Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param map Map
	 * @param bean Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isIgnoreError) {
		return fillBeanWithMap(map, bean, false, isIgnoreError);
	}

	/**
	 * 使用Map填充Bean对象，可配置将下划线转换为驼峰
	 * 
	 * @param <T> Bean类型
	 * @param map Map
	 * @param bean Bean
	 * @param isToCamelCase 是否将下划线模式转换为驼峰模式
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isToCamelCase, boolean isIgnoreError) {
		return fillBeanWithMap(map, bean, isToCamelCase, CopyOptions.create().setIgnoreError(isIgnoreError));
	}

	/**
	 * 使用Map填充Bean对象，忽略大小写
	 * 
	 * @param <T> Bean类型
	 * @param map Map
	 * @param bean Bean
	 * @param isIgnoreError 是否忽略注入错误
	 * @return Bean
	 */
	public static <T> T fillBeanWithMapIgnoreCase(Map<?, ?> map, T bean, boolean isIgnoreError) {
		return fillBeanWithMap(map, bean, CopyOptions.create().setIgnoreCase(true).setIgnoreError(isIgnoreError));
	}

	/**
	 * 使用Map填充Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param map Map
	 * @param bean Bean
	 * @param copyOptions 属性复制选项 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, CopyOptions copyOptions) {
		return fillBeanWithMap(map, bean, false, copyOptions);
	}
	
	/**
	 * 使用Map填充Bean对象
	 * 
	 * @param <T> Bean类型
	 * @param map Map
	 * @param bean Bean
	 * @param isToCamelCase 是否将Map中的下划线风格key转换为驼峰风格
	 * @param copyOptions 属性复制选项 {@link CopyOptions}
	 * @return Bean
	 * @since 3.3.1
	 */
	public static <T> T fillBeanWithMap(Map<?, ?> map, T bean, boolean isToCamelCase, CopyOptions copyOptions) {
		if(MapUtil.isEmpty(map)) {
			return bean;
		}
		if(isToCamelCase) {
			map = MapUtil.toCamelCaseMap(map);
		}
		return BeanCopier.create(map, bean, copyOptions).copy();
	}
	// --------------------------------------------------------------------------------------------- fillBean
	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param <T> Bean类型
	 * @param beanClass Bean Class
	 * @param valueProvider 值提供者
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T toBean(Class<T> beanClass, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
		return fillBean(ReflectUtil.newInstance(beanClass), valueProvider, copyOptions);
	}

	/**
	 * 填充Bean的核心方法
	 * 
	 * @param <T> Bean类型
	 * @param bean Bean
	 * @param valueProvider 值提供者
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 * @return Bean
	 */
	public static <T> T fillBean(T bean, ValueProvider<String> valueProvider, CopyOptions copyOptions) {
		if (null == valueProvider) {
			return bean;
		}

		return BeanCopier.create(valueProvider, bean, copyOptions).copy();
	}

	// --------------------------------------------------------------------------------------------- beanToMap
	/**
	 * 对象转Map，不进行驼峰转下划线，不忽略值为空的字段
	 * 
	 * @param bean bean对象
	 * @return Map
	 */
	public static Map<String, Object> beanToMap(Object bean) {
		return beanToMap(bean, false, false);
	}
	
	/**
	 * 对象转Map
	 * 
	 * @param bean bean对象
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @return Map
	 */
	public static Map<String, Object> beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
		return beanToMap(bean, new HashMap<String, Object>(), isToUnderlineCase, ignoreNullValue);
	}

	/**
	 * 对象转Map
	 * 
	 * @param bean bean对象
	 * @param targetMap 目标的Map
	 * @param isToUnderlineCase 是否转换为下划线模式
	 * @param ignoreNullValue 是否忽略值为空的字段
	 * @return Map
	 * @since 3.2.3
	 */
	public static Map<String, Object> beanToMap(Object bean, Map<String, Object> targetMap, boolean isToUnderlineCase, boolean ignoreNullValue) {
		if (bean == null) {
			return null;
		}

		final Collection<PropDesc> props = BeanUtil.getBeanDesc(bean.getClass()).getProps();
		
		String key;
		Method getter;
		Object value;
		for (PropDesc prop : props) {
			key = prop.getFieldName();
			// 过滤class属性
			// 得到property对应的getter方法
			getter = prop.getGetter();
			if (null != getter) {
				// 只读取有getter方法的属性
				try {
					value = getter.invoke(bean);
				} catch (Exception ignore) {
					continue;
				}
				if (false == ignoreNullValue || (null != value && false == value.equals(bean))) {
					targetMap.put(isToUnderlineCase ? StrUtil.toUnderlineCase(key) : key, value);
				}
			}
		}
		return targetMap;
	}

	// --------------------------------------------------------------------------------------------- copyProperties
	/**
	 * 复制Bean对象属性
	 * 
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 */
	public static void copyProperties(Object source, Object target) {
		copyProperties(source, target, CopyOptions.create());
	}

	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 * 
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @param ignoreProperties 不拷贝的的属性列表
	 */
	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		copyProperties(source, target, CopyOptions.create().setIgnoreProperties(ignoreProperties));
	}

	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 * 
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 */
	public static void copyProperties(final Object source, Object target, CopyOptions copyOptions) {
		copyProperties(source, target, false, copyOptions);
	}

	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将CopyOptions.editable设置为父类
	 * 
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @param ignoreCase 是否忽略大小写
	 * @param copyOptions 拷贝选项，见 {@link CopyOptions}
	 */
	public static void copyProperties(final Object source, Object target, boolean ignoreCase, CopyOptions copyOptions) {
		if (null == copyOptions) {
			copyOptions = new CopyOptions();
		}
		BeanCopier.create(source, target, copyOptions).copy();
	}
}
