package cn.hutool.core.event;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 事件监听模式<br>
 * 类型事件机制特尔有的模式，决定了使用哪一个执行器执行事件，对于一个事件仅会有一个监听模式与其对应<br>
 * 事件仅会发布给当前接口返回的监听中
 *
 * @author Create by liuwenhao on 2022/7/20 12:40
 * @see BroadcastListenerPattern
 * @see ExclusiveListenerPattern
 */
@FunctionalInterface
public interface ListenerPattern {

	/**
	 * 决定了使用哪一个执行器执行事件<br>
	 * 将和事件进行绑定的所有监听器进行一次筛选。<br>
	 *
	 * @param type      事件类型
	 * @param listeners 该事件下所有的监听器集合
	 * @param <E>       事件类型
	 * @return 经监听模式筛选后的监听器集合
	 */
	<E> Collection<Listener<E, Object>> getListener(Type type, Collection<Listener<E, Object>> listeners);

}
