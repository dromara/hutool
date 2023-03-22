package cn.hutool.core.lang.func;

/**
 * 3参数Consumer
 *
 * @param <P1> 参数一类型
 * @param <P2> 参数二类型
 * @param <P3> 参数三类型
 * @author TomXin
 * @since 5.7.22
 */
@FunctionalInterface
public interface Consumer3<P1, P2, P3> {

	/**
	 * 接收参数方法
	 *
	 * @param p1 参数一
	 * @param p2 参数二
	 * @param p3 参数三
	 */
	void accept(P1 p1, P2 p2, P3 p3);
}
