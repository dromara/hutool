package cn.hutool.extra.cglib;

import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.lang.func.Func0;
import cn.hutool.core.util.StrUtil;
import net.sf.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;

/**
 * BeanCopier属性缓存<br>
 * 缓存用于防止多次反射造成的性能问题
 *
 * @author Looly
 * @since 5.4.1
 */
public enum BeanCopierCache {
	INSTANCE;

	private final SimpleCache<String, BeanCopier> cache = new SimpleCache<>();

	/**
	 * 获得属性名和{@link PropertyDescriptor}Map映射
	 *
	 * @param srcClass    源Bean的类
	 * @param targetClass 目标Bean的类
	 * @param supplier    缓存对象产生函数
	 * @return 属性名和{@link PropertyDescriptor}Map映射
	 * @since 5.4.1
	 */
	public BeanCopier get(Class<?> srcClass, Class<?> targetClass, Func0<BeanCopier> supplier) {
		return this.cache.get(StrUtil.format("{}_{}", srcClass.getName(), srcClass.getName()), supplier);
	}
}
