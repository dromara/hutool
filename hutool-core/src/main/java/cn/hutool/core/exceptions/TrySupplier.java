package cn.hutool.core.exceptions;

public interface TrySupplier<T> {
	/**
	 * 把异常抛出去
	 */
	T get() throws Throwable;
}
