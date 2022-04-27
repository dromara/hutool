package cn.hutool.core.lang.func;

import java.util.function.Supplier;

/**
 * 4参数Supplier
 *
 * @param <T>  目标   类型
 * @param <P1> 参数一 类型
 * @param <P2> 参数二 类型
 * @param <P3> 参数三 类型
 * @param <P4> 参数四 类型
 * @author TomXin
 * @since 5.7.21
 */
@FunctionalInterface
public interface Supplier4<T, P1, P2, P3, P4> {

	/**
	 * 生成实例的方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 * @param p4 参数四
	 * @return 目标对象
	 */
	T get(P1 p1, P2 p2, P3 p3, P4 p4);

	/**
	 * 将带有参数的Supplier转换为无参{@link Supplier}
	 *
	 * @param p1 参数1
	 * @param p2 参数2
	 * @param p3 参数3
	 * @param p4 参数4
	 * @return {@link Supplier}
	 */
	default Supplier<T> toSupplier(P1 p1, P2 p2, P3 p3, P4 p4) {
		return () -> get(p1, p2, p3, p4);
	}
}
