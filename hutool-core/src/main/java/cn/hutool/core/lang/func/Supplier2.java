package cn.hutool.core.lang.func;

import java.util.function.Supplier;

/**
 * 两个参数的Supplier
 *
 * @param <T>  目标   类型
 * @param <P1> 参数一 类型
 * @param <P2> 参数二 类型
 * @author TomXin
 * @since 5.7.21
 */
@FunctionalInterface
public interface Supplier2<T, P1, P2> {

	/**
	 * 生成实例的方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @return 目标对象
	 */
	T get(P1 p1, P2 p2);

	/**
	 * 将带有参数的Supplier转换为无参{@link Supplier}
	 *
	 * @param p1 参数1
	 * @param p2 参数2
	 * @return {@link Supplier}
	 */
	default Supplier<T> toSupplier(P1 p1, P2 p2) {
		return () -> get(p1, p2);
	}
}
