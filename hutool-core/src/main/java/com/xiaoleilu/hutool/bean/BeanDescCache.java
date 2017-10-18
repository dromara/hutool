package com.xiaoleilu.hutool.bean;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Bean属性缓存<br>
 * 缓存用于防止多次反射造成的性能问题
 * @author Looly
 *
 */
public enum BeanDescCache {
	INSTANCE;
	
	private Map<Class<?>, BeanDesc> bdCache = Collections.synchronizedMap(new WeakHashMap<Class<?>, BeanDesc>());
	private Map<Class<?>, BeanDesc> ignoreCaseBdCache = Collections.synchronizedMap(new WeakHashMap<Class<?>, BeanDesc>());
	
	/**
	 * 获得属性名和{@link BeanDesc}Map映射
	 * @param beanClass Bean的类
	 * @param ignoreCase 是否忽略大小写
	 * @return 属性名和{@link BeanDesc}映射
	 */
	public BeanDesc getBeanDesc(Class<?> beanClass, boolean ignoreCase){
		return (ignoreCase ? ignoreCaseBdCache : bdCache).get(beanClass);
	}
	
	/**
	 * 加入缓存
	 * @param beanClass Bean的类
	 * @param BeanDesc 属性名和{@link BeanDesc}映射
	 * @param ignoreCase 是否忽略大小写
	 */
	public void putBeanDesc(Class<?> beanClass, BeanDesc BeanDesc, boolean ignoreCase){
		(ignoreCase ? ignoreCaseBdCache : bdCache).put(beanClass, BeanDesc);
	}
}
