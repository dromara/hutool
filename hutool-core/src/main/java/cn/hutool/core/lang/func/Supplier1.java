package cn.hutool.core.lang.func;

import java.util.function.Supplier;

/**
 * 1参数Supplier
 *
 * @param <T>  目标   类型
 * @param <P1> 参数一 类型
 * @author TomXin
 * @since 5.7.21
 */
@FunctionalInterface
public interface Supplier1<T, P1> {
	/**
	 * 生成实例的方法
	 *
	 * @param p1 参数一
	 * @return 目标对象
	 */
	T get(P1 p1);

	/**
	 * 将带有参数的Supplier转换为无参{@link Supplier}
	 *
	 * @param p1 参数1
	 * @return {@link Supplier}
	 */
	default Supplier<T> toSupplier(P1 p1) {
		return () -> get(p1);
	}
}
