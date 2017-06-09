package com.xiaoleilu.hutool.bean;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Bean属性缓存<br>
 * 缓存用于防止多次反射造成的性能问题
 * @author Looly
 *
 */
public class BeanInfoCache {
	private Map<Class<?>, Map<String, PropertyDescriptor>> pdCache = Collections.synchronizedMap(new WeakHashMap<Class<?>, Map<String, PropertyDescriptor>>());
	private Map<Class<?>, Map<String, PropertyDescriptor>> ignoreCasePdCache = Collections.synchronizedMap(new WeakHashMap<Class<?>, Map<String, PropertyDescriptor>>());
	
	/** 单例 */
	public static BeanInfoCache INSTANCE = new BeanInfoCache();
	
	/**
	 * 获得属性名和{@link PropertyDescriptor}Map映射
	 * @param beanClass Bean的类
	 * @param ignoreCase 是否忽略大小写
	 * @return 属性名和{@link PropertyDescriptor}Map映射
	 */
	public Map<String, PropertyDescriptor> getPropertyDescriptorMap(Class<?> beanClass, boolean ignoreCase){
		return (ignoreCase ? ignoreCasePdCache : pdCache).get(beanClass);
	}
	
	/**
	 * 加入缓存
	 * @param beanClass Bean的类
	 * @param fieldNamePropertyDescriptorMap 属性名和{@link PropertyDescriptor}Map映射
	 * @param ignoreCase 是否忽略大小写
	 */
	public void putPropertyDescriptorMap(Class<?> beanClass, Map<String, PropertyDescriptor> fieldNamePropertyDescriptorMap, boolean ignoreCase){
		(ignoreCase ? ignoreCasePdCache : pdCache).put(beanClass, fieldNamePropertyDescriptorMap);
	}
}
