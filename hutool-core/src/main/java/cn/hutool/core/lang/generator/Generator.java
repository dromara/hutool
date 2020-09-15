package cn.hutool.core.lang.generator;

/**
 * 生成器泛型接口<br>
 * 通过实现此接口可以自定义生成对象的策略
 *
 * @param <T> 生成对象类型
 * @since 5.4.3
 */
public interface Generator<T> {

	/**
	 * 生成新的对象
	 *
	 * @return 新的对象
	 */
	T next();
}
