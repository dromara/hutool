package com.xiaoleilu.hutool.clone;

/**
 * 克隆支持接口
 * @author Looly
 *
 * @param <T>
 */
public interface Cloneable<T> extends java.lang.Cloneable{
	
	/**
	 * 克隆当前对象，浅复制
	 * @return 克隆后的对象
	 */
	T clone();
}
