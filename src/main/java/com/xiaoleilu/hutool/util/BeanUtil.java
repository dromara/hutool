package com.xiaoleilu.hutool.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xiaoleilu.hutool.exceptions.UtilException;
import com.xiaoleilu.hutool.lang.Conver;

/**
 * Bean工具类
 * 
 * @author Looly
 *
 */
public class BeanUtil {

	/**
	 * 获得Bean字段描述数组
	 * 
	 * @param clazz Bean类
	 * @return 字段描述数组
	 * @throws IntrospectionException
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
		return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
	}
	
	/**
	 * 获得字段名和字段描述Map
	 * @param clazz Bean类
	 * @return 字段名和字段描述Map
	 * @throws IntrospectionException
	 */
	public static Map<String, PropertyDescriptor> getFieldNamePropertyDescriptorMap(Class<?> clazz) throws IntrospectionException{
		final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		Map<String, PropertyDescriptor> map = new HashMap<>();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			map.put(propertyDescriptor.getName(), propertyDescriptor);
		}
		return map;
	}

	/**
	 * 获得Bean类属性描述
	 * 
	 * @param clazz Bean类
	 * @param fieldName 字段名
	 * @return PropertyDescriptor
	 * @throws IntrospectionException
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, final String fieldName) throws IntrospectionException {
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(clazz);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (ObjectUtil.equals(fieldName, propertyDescriptor.getName())) {
				return propertyDescriptor;
			}
		}
		return null;
	}
	
	/**
	 * Map转换为Bean对象
	 * 
	 * @param map Map
	 * @param beanClass Bean Class
	 * @return Bean
	 */
	public static <T> T mapToBean(Map<?, ?> map, Class<T> beanClass) {
		return fillBeanWithMap(map, ClassUtil.newInstance(beanClass));
	}

	/**
	 * Map转换为Bean对象<br>
	 * 忽略大小写
	 * 
	 * @param map Map
	 * @param beanClass Bean Class
	 * @return Bean
	 */
	public static <T> T mapToBeanIgnoreCase(Map<?, ?> map, Class<T> beanClass) {
		return fillBeanWithMapIgnoreCase(map, ClassUtil.newInstance(beanClass));
	}

	/**
	 * 使用Map填充Bean对象
	 * 
	 * @param map Map
	 * @param bean Bean
	 * @return Bean
	 */
	public static <T> T fillBeanWithMap(final Map<?, ?> map, T bean) {
		return fill(bean, new ValueProvider(){
			@Override
			public Object value(String name) {
				return map.get(name);
			}
		});
	}

	/**
	 * 使用Map填充Bean对象，忽略大小写
	 * 
	 * @param map Map
	 * @param bean Bean
	 * @return Bean
	 */
	public static <T> T fillBeanWithMapIgnoreCase(Map<?, ?> map, T bean) {
		final Map<Object, Object> map2 = new HashMap<Object, Object>();
		for (Entry<?, ?> entry : map.entrySet()) {
			final Object key = entry.getKey();
			if (key instanceof String) {
				final String keyStr = (String) key;
				map2.put(keyStr.toLowerCase(), entry.getValue());
			} else {
				map2.put(key, entry.getValue());
			}
		}

		return fill(bean, new ValueProvider(){
			@Override
			public Object value(String name) {
				return map2.get(name.toLowerCase());
			}
		});
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param request ServletRequest
	 * @param beanClass Bean Class
	 * @return Bean
	 */
	public static <T> T requestParamToBean(javax.servlet.ServletRequest request, Class<T> beanClass) {
		return fillBeanWithRequestParam(request, ClassUtil.newInstance(beanClass));
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param request ServletRequest
	 * @param bean Bean
	 * @return Bean
	 */
	public static <T> T fillBeanWithRequestParam(final javax.servlet.ServletRequest request, T bean) {
		final String beanName = StrUtil.lowerFirst(bean.getClass().getSimpleName());
		return fill(bean, new ValueProvider(){
			@Override
			public Object value(String name) {
				String value = request.getParameter(name);
				if (StrUtil.isEmpty(value)) {
					// 使用类名前缀尝试查找值
					value = request.getParameter(beanName + StrUtil.DOT + name);
					if (StrUtil.isEmpty(value)) {
						// 此处取得的值为空时跳过，包括null和""
						value = null;
					}
				}
				return value;
			}
		});
	}

	/**
	 * ServletRequest 参数转Bean
	 * 
	 * @param <T>
	 * @param beanClass Bean Class
	 * @param valueProvider 值提供者
	 * @return Bean
	 */
	public static <T> T toBean(Class<T> beanClass, ValueProvider valueProvider) {
		return fill(ClassUtil.newInstance(beanClass), valueProvider);
	}

	/**
	 * 填充Bean
	 * 
	 * @param <T>
	 * @param bean Bean
	 * @param valueProvider 值提供者
	 * @return Bean
	 */
	public static <T> T fill(T bean, ValueProvider valueProvider) {
		if (null == valueProvider) {
			return bean;
		}

		Class<?> beanClass = bean.getClass();
		try {
			PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(beanClass);
			String propertyName;
			Object value;
			for (PropertyDescriptor property : propertyDescriptors) {
				propertyName = property.getName();
				value = valueProvider.value(propertyName);
				if (null == value) {
					// 此处取得的值为空时跳过，包括null和""
					continue;
				}

				try {
					property.getWriteMethod().invoke(bean, Conver.parse(property.getPropertyType(), value));
				} catch (Exception e) {
					throw new UtilException(StrUtil.format("Inject [{}] error!", property.getName()), e);
				}
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
		return bean;
	}

	/**
	 * 对象转Map
	 * 
	 * @param bean bean对象
	 * @return Map
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {

		if (bean == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			final PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(bean.getClass());
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (false == key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(bean);
					if (null != value) {
						map.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			throw new UtilException(e);
		}
		return map;
	}

	/**
	 * 复制Bean对象属性
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 */
	public static void copyProperties(Object source, Object target) {
		copyProperties(source, target, null, (String[]) null);
	}
	
	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @param ignoreProperties 不拷贝的的属性列表
	 */
	public static void copyProperties(Object source, Object target, String... ignoreProperties) {
		copyProperties(source, target, null, ignoreProperties);
	}
	
	/**
	 * 复制Bean对象属性<br>
	 * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
	 * @param source 源Bean对象
	 * @param target 目标Bean对象
	 * @param editable 限制的类或接口，必须为target对象的实现接口或父类
	 * @param ignoreProperties 不拷贝的的属性列表
	 */
	private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties) {
		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			//检查限制类是否为target的父类或接口
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException(StrUtil.format("Target class [{}] not assignable to Editable class [{}]", target.getClass().getName(), editable.getName()));
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = null;
		Map<String, PropertyDescriptor> sourcePdMap;
		try {
			sourcePdMap = getFieldNamePropertyDescriptorMap(source.getClass());
			targetPds = getPropertyDescriptors(actualEditable);
		} catch (IntrospectionException e) {
			throw new UtilException(e);
		}
		
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || false == ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = sourcePdMap.get(targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					// 源对象字段的getter方法返回值必须可转换为目标对象setter方法的第一个参数
					if (readMethod != null && ClassUtil.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
						try {
							Object value = ClassUtil.setAccessible(readMethod).invoke(source);
							ClassUtil.setAccessible(writeMethod).invoke(target, value);
						} catch (Throwable ex) {
							throw new UtilException(ex, "Copy property [{}] to [{}] error: {}", sourcePd.getName(), targetPd.getName(), ex.getMessage());
						}
					}
				}
			}
		}
	}

	/**
	 * 值提供者，用于提供Bean注入时参数对应值得抽象接口
	 * 
	 * @author Looly
	 *
	 */
	public static interface ValueProvider {
		/**
		 * 获取值
		 * 
		 * @param name Bean对象中参数名
		 * @return 对应参数名的值
		 */
		public Object value(String name);
	}
}
