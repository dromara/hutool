package com.xiaoleilu.hutool.lang;

/**
 * 过滤器接口<br>
 * 此过滤器两个作用：<br>
 * 1、使用返回值是否为<code>null</code>判定此对象被过滤与否<br>
 * 2、在过滤过程中，实现对对象的修改
 * @author Looly
 *
 * @param <T>
 */
public interface Filter<T> {
	/**
	 * 修改过滤后的结果
	 * @param t 被过滤的对象
	 * @return 修改后的对象，如果被过滤返回<code>null</code>
	 */
	public T modify(T t);
}
