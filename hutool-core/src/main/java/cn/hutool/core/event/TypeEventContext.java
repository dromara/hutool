package cn.hutool.core.event;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 类型事件上下文<br>
 * 事件和监听器的绑定，解绑，筛选监听器，处理事件等操作。<br>
 * 这是对{@link TypeEventPublisher}的一种实现，不直接使用{@link Event}而是使用事件的类型作为事件的标识<br>
 *
 * @author Create by liuwenhao on 2022/7/20 13:11
 */
public interface TypeEventContext extends TypeEventPublisher {

	/**
	 * 提取上下文的唯一标识
	 *
	 * @return name
	 */
	String name();

	/**
	 * 修改事件的监听模式
	 *
	 * @param type            事件类型
	 * @param listenerPattern 事件监听模式
	 */
	void setListenerPattern(Type type, ListenerPattern listenerPattern);

	/**
	 * 为一个事件绑定监听器
	 *
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回类型
	 */
	<E, R> void bind(Type type, Listener<E, R> listener);

	/**
	 * 为一个事件解绑监听器
	 *
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回类型
	 */
	<E, R> void unbind(Type type, Listener<E, R> listener);

	/**
	 * 为一个事件解绑所有监听器
	 *
	 * @param type 事件类型
	 */
	void unbindAll(Type type);

	/**
	 * 清空当前的上下文
	 */
	void clear();

	/**
	 * 获取与一个事件绑定所有的监听器
	 *
	 * @param type 事件类型
	 * @param <E>  事件类型
	 * @return 监听器集合
	 */
	<E> Collection<Listener<E, Object>> getListeners(Type type);

}
