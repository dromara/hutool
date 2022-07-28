package cn.hutool.core.event;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 事件发布工具类<br>
 * 通过将大部分事件配置项进行默认，从而提供简单，方便的事务发布模型<br>
 * 主要功能包括：<br>
 * <ol>
 *     <li>创建监听器</li>
 *     <li>绑定监听器</li>
 *     <li>解绑监听器</li>
 *     <li>发布事件</li>
 * </ol>
 *
 * @author Create by liuwenhao on 2022/7/26 17:34
 */
public class EventUtil {

	private EventUtil() {

	}

	static EventPublisherCenter publisherCenter = new EventPublisherCenter();

	/**
	 * 默认的上下文名称
	 */
	static final String DEFAULT_NAME = "DEFAULT_EVENT_CONTEXT";

	static {
		publisherCenter.contextMap.put(DEFAULT_NAME, new TypeDefaultContext(DEFAULT_NAME));
	}

	/**
	 * 返回内部的事件发布中心
	 *
	 * @return EventPublisherCenter
	 */
	public static EventPublisherCenter publisherCenter() {
		return publisherCenter;
	}

	/**
	 * 返回内部的默认环境
	 *
	 * @return EventPublisherCenter
	 */
	public static TypeEventContext eventContext() {
		return publisherCenter.getContext(DEFAULT_NAME);
	}

	/**
	 * 清空默认上下文中的数据<br>
	 *
	 * @return 被删除的上下文
	 */
	public static TypeEventContext clear() {
		return publisherCenter.clearContext(DEFAULT_NAME);
	}

	/**
	 * 创建一个监听构建器，使用它可以快速构造一个自定义的监听器
	 *
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回值
	 * @return ListenerModel
	 */
	public static <E, R> ListenerBuilder<E, R> createListener(Listener<E, R> listener) {
		return publisherCenter.createListener(listener);
	}

	/**
	 * 将一个实例o中所有带有{@link  EventListener}注释的方法转换成监听器并进行绑定
	 *
	 * @param o 获取对象中可以带有{@link EventListener}注解的方法，并解析和绑定
	 * @return 默认上下文
	 */
	public static TypeEventContext bind(Object o) {
		publisherCenter.bind(DEFAULT_NAME, o);
		return eventContext();
	}

	/**
	 * 将一个实例o中的方法转换成监听器并进行绑定<br>
	 * 该方法必须被{@link EventListener}注解标注
	 *
	 * @param method 用作监听器的方法
	 * @param o      获取对象中可以带有{@link EventListener}注解的方法，并解析和绑定
	 * @return 默认上下文
	 * @throws NullPointerException 找不到注解时
	 */
	public static TypeEventContext bind(Method method, Object o) {
		publisherCenter.bind(DEFAULT_NAME, method, o);
		return eventContext();
	}


	/**
	 * 在默认上下文将事件和监听器进行绑定
	 *
	 * @param type            事件类型
	 * @param listenerBuilder 监听器包装信息，提供完整的监听器配置
	 * @param <E>             事件类型
	 * @param <R>             监听器返回值
	 * @return 默认上下文
	 */
	public static <E, R> TypeEventContext bind(Type type, ListenerBuilder<E, R> listenerBuilder) {
		publisherCenter.bind(DEFAULT_NAME, type, listenerBuilder);
		return eventContext();
	}

	/**
	 * 在默认上下文将事件和监听器进行绑定
	 *
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回值
	 * @return 默认上下文
	 */
	public static <E, R> TypeEventContext bind(Type type, Listener<E, R> listener) {
		publisherCenter.bind(DEFAULT_NAME, type, listener, new NoEventProcessor<>());
		return eventContext();
	}

	/**
	 * 在默认上下文将事件和监听器进行绑定
	 *
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回值
	 * @return 默认上下文
	 */
	public static <E, R> TypeEventContext bind(Type type, Listener<E, R> listener, EventProcessor<E, R> processor) {
		publisherCenter.bind(DEFAULT_NAME, type, listener, processor);
		return eventContext();
	}

	/**
	 * 在默认上下文中解绑事件和监听器
	 *
	 * @param type    事件类型
	 * @param builder 监听器包装信息，提供完整的监听器配置
	 * @param <E>     事件类型
	 * @param <R>     监听器返回值
	 * @return 默认上下文
	 */
	public static <E, R> TypeEventContext unbind(Type type, ListenerBuilder<E, R> builder) {
		publisherCenter.unbind(DEFAULT_NAME, type, builder);
		return eventContext();
	}

	/**
	 * 在默认上下文中解绑事件和监听器
	 *
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回值
	 * @return 默认上下文
	 */
	public static <E, R> TypeEventContext unbind(Type type, Listener<E, R> listener) {
		publisherCenter.unbind(DEFAULT_NAME, type, listener);
		return eventContext();
	}

	/**
	 * 解绑默认上下文中某个事件和所有监听器的关联
	 *
	 * @param type 事件类型
	 * @return 默认上下文
	 */
	public static TypeEventContext unbindAll(Type type) {
		TypeEventContext context = publisherCenter.createContext(DEFAULT_NAME);
		context.unbindAll(type);
		return eventContext();
	}

	/**
	 * 在默认上下文中发布事件
	 *
	 * @param event 事件实例
	 */
	public static void publish(Object event) {
		publisherCenter.publish(DEFAULT_NAME, event);
	}

	/**
	 * 在默认上下文中发布事件<br>
	 * 如果event是复杂的参数化类型对象，则可以使用type标注其具体类型
	 *
	 * @param event 事件实例
	 * @param type  事件类型
	 */
	public static void publish(Object event, Type type) {
		publisherCenter.publish(DEFAULT_NAME, event, type);
	}
}
