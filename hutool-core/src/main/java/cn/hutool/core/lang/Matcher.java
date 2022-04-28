package cn.hutool.core.lang;

/**
 * 匹配接口
 *
 * @param <T> 匹配的对象类型
 * @author Looly
 */
@FunctionalInterface
public interface Matcher<T> {
	/**
	 * 给定对象是否匹配
	 *
	 * @param t 对象
	 * @return 是否匹配
	 */
	boolean match(T t);
}
