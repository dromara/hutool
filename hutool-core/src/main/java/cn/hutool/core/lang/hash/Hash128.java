package cn.hutool.core.lang.hash;

/**
 * Hash计算接口
 *
 * @param <T> 被计算hash的对象类型
 * @author looly
 * @since 5.2.5
 */
@FunctionalInterface
public interface Hash128<T> {
	/**
	 * 计算Hash值
	 *
	 * @param t 对象
	 * @return hash
	 */
	Number128 hash128(T t);
}