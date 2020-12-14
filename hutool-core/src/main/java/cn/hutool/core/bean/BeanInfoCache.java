package cn.hutool.core.bean;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.lang.func.Func0;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Bean属性缓存<br>
 * 缓存用于防止多次反射造成的性能问题
 *
 * @author Looly
 */
public enum BeanInfoCache {
	INSTANCE;

	private final SimpleCache<Class<?>, Map<String, PropertyDescriptor>> pdCache = new SimpleCache<>();
	private final SimpleCache<Class<?>, Map<String, PropertyDescriptor>> ignoreCasePdCache = new SimpleCache<>();

	/**
	 * 获得属性名和{@link PropertyDescriptor}Map映射
	 *
	 * @param beanClass  Bean的类
	 * @param ignoreCase 是否忽略大小写
	 * @return 属性名和{@link PropertyDescriptor}Map映射
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> beanClass, boolean ignoreCase) {
		return getCache(ignoreCase).get(beanClass);
	}

	/**
	 * 获得属性名和{@link PropertyDescriptor}Map映射
	 *
	 * @param beanClass  Bean的类
	 * @param ignoreCase 是否忽略大小写
	 * @param supplier   缓存对象产生函数
	 * @return 属性名和{@link PropertyDescriptor}Map映射
	 * @since 5.4.1
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptorMap(
			Class<?> beanClass,
			boolean ignoreCase,
			Func0<Map<String, PropertyDescriptor>> supplier) {
		return getCache(ignoreCase).get(beanClass, supplier);
	}

	/**
	 * 加入缓存
	 *
	 * @param beanClass                      Bean的类
	 * @param fieldNamePropertyDescriptorMap 属性名和{@link PropertyDescriptor}Map映射
	 * @param ignoreCase                     是否忽略大小写
	 */
	public void putPropertyDescriptorMap(Class<?> beanClass, Map<String, PropertyDescriptor> fieldNamePropertyDescriptorMap, boolean ignoreCase) {
		getCache(ignoreCase).put(beanClass, fieldNamePropertyDescriptorMap);
	}

	/**
	 * 根据是否忽略字段名的大小写，返回不用Cache对象
	 *
	 * @param ignoreCase 是否忽略大小写
	 * @return SimpleCache
	 * @since 5.4.1
	 */
	private SimpleCache<Class<?>, Map<String, PropertyDescriptor>> getCache(boolean ignoreCase) {
		return ignoreCase ? ignoreCasePdCache : pdCache;
	}
}
