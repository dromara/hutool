package cn.hutool.core.bean;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.lang.func.Func0;

/**
 * Bean属性缓存<br>
 * 缓存用于防止多次反射造成的性能问题
 * @author Looly
 *
 */
public enum BeanDescCache {
	INSTANCE;
	
	private final SimpleCache<Class<?>, BeanDesc> bdCache = new SimpleCache<>();
	
	/**
	 * 获得属性名和{@link BeanDesc}Map映射
	 * @param beanClass Bean的类
	 * @param supplier 对象不存在时创建对象的函数
	 * @return 属性名和{@link BeanDesc}映射
	 * @since 5.4.2
	 */
	public BeanDesc getBeanDesc(Class<?> beanClass, Func0<BeanDesc> supplier){
		return bdCache.get(beanClass, supplier);
	}
}
