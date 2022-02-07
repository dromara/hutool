package cn.hutool.core.lang.func;

import java.util.function.Consumer;

/**
 * 1参数Consumer
 *
 * @param <T>  目标类型
 * @param <P1> 参数一类型
 * @author TomXin
 * @since 5.7.21
 */
@FunctionalInterface
public interface Consumer1<T, P1> {
	/**
	 * 接收参数方法
	 *
	 * @param t  对象
	 * @param p1 参数1
	 */
	void accept(T t, P1 p1);

	/**
	 * 将带有参数的Consumer转换为无参{@link Consumer}
	 *
	 * @param p1 参数1
	 * @return {@link Consumer}
	 */
	default Consumer<T> toConsumer(P1 p1) {
		return instant -> accept(instant, p1);
	}
}
