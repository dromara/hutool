package cn.hutool.core.event;

import java.lang.reflect.Type;

/**
 * 基于事件类型实现的事件发布<br>
 * 将事件实例的具体类型当做key存储，用于绑定监听器<br>
 * <ol>
 *     <li>如果是普通java类型可以使用{@link TypeEventPublisher#publish(Object)}</li>
 *     <li>如果参数化类型则需要使用{@link TypeEventPublisher#publish(Object, Type)}</li>
 * </ol>
 *
 * @author Create by liuwenhao on 2022/7/20 13:21
 */
@FunctionalInterface
public interface TypeEventPublisher extends EventPublisher {

	/**
	 * 发布事件<br>
	 * 接收事件时需要验证参数是否是自身事件所需的类型<br>
	 * 当事件类型为非参数化类型时
	 *
	 * @param o 事件实例
	 */
	@Override
	default void publish(Object o) {
		publish(o, o.getClass());
	}

	/**
	 * 发布事件<br>
	 * 接收事件时需要验证参数是否是自身事件所需的类型<br>
	 * 本类可以适用于不同事件类型的监听
	 *
	 * @param o    事件实例
	 * @param type 事件类型
	 */
	void publish(Object o, Type type);

}
