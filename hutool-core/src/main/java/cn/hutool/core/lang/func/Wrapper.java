package cn.hutool.core.lang.func;

/**
 * 包装接口
 *
 * @param <T> 原始对象类型
 * @author looly
 * @since 6.0.0
 */
public interface Wrapper<T> {
	/**
	 * 获取原始对象
	 *
	 * @return 原始对象
	 */
	T getRaw();
}
