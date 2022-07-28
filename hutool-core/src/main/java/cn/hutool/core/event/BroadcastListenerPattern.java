package cn.hutool.core.event;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 广播模式<br>
 * 将数据传递给所有绑定的监听器
 *
 * @author Create by liuwenhao on 2022/7/21 13:46
 */
public class BroadcastListenerPattern implements ListenerPattern {

	/**
	 * 将数据传递给所有绑定的监听器
	 *
	 * @param type      事件类型
	 * @param listeners 该事件下所有的监听器集合
	 * @param <E>       事件类型
	 * @return 所有和该事件绑定的监听器
	 */
	@Override
	public <E> Collection<Listener<E, Object>> getListener(Type type, Collection<Listener<E, Object>> listeners) {
		return listeners;
	}
}
