package cn.hutool.core.event;

import java.util.EventListener;

/**
 * 监听器 <br>
 * E 事件对象 <br>
 * R 返回对象 <br>
 * 如果返回的R和E不相等并且不为null，默认情况下使用的{@link EdgeSpreadPattern}会把R当做一个事件继续发布，直到没有监听这个的监听器为止<br>
 * 如果出现循环，则事件会一直传递下去，直到栈溢出
 *
 * @author Create by liuwenhao on 2022/7/19 20:43
 */
@FunctionalInterface
public interface Listener<E, R> extends EventListener {

	/**
	 * 执行事件
	 *
	 * @param e 事件对象
	 * @return 任意类型的返回值，改返回值可以通过{@link SpreadPattern}的设置生成新的事件进行发布
	 */
	R execEvent(E e);

}
