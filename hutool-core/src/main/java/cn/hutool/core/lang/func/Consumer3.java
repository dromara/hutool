package cn.hutool.core.lang.func;

import java.util.function.Consumer;

/**
 * 3参数Consumer
 *
 * @param <T>  目标类型
 * @param <P1> 参数一类型
 * @param <P2> 参数二类型
 * @param <P3> 参数三类型
 * @author TomXin
 * @since 5.7.21
 */
@FunctionalInterface
public interface Consumer3<T, P1, P2, P3> {
	/**
	 * 接收参数方法
	 *
	 * @param t  对象
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 */
	void accept(T t, P1 p1, P2 p2, P3 p3);

	/**
	 * 将带有参数的Consumer转换为无参{@link Consumer}
	 *
	 * @param p1 参数1
	 * @param p2 参数2
	 * @param p3 参数3
	 * @return {@link Consumer}
	 */
	default Consumer<T> toConsumer(P1 p1, P2 p2, P3 p3) {
		return instant -> accept(instant, p1, p2, p3);
	}
}
