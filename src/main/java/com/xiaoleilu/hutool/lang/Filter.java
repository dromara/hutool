package com.xiaoleilu.hutool.lang;

/**
 * 过滤器接口
 * @author Looly
 *
 * @param <T>
 */
public interface Filter<T> {
	public boolean accept(T t);
}
