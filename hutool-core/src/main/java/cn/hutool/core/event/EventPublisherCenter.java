package cn.hutool.core.event;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 事件发布中心
 *
 * @author Create by liuwenhao on 2022/7/1 16:57
 */
@SuppressWarnings("unused")
public class EventPublisherCenter {

	/**
	 * 所有的上下文
	 */
	Map<String, TypeEventContext> contextMap = new HashMap<>();

	static final String LISTENER_METHOD_NAME = "execEvent";

	/**
	 * 构造
	 *
	 * @param contextMap 上下文映射表
	 */
	public EventPublisherCenter(Map<String, TypeEventContext> contextMap) {
		this.contextMap = contextMap;
	}

	public EventPublisherCenter() {
	}

	// ========================= 有关上下文的操作 =========================

	/**
	 * 在映射表中寻找一个上下文，如果不存在映射则返回null
	 *
	 * @param name 名称，上下文唯一标识
	 * @return name对应的上下文
	 */
	public TypeEventContext getContext(String name) {
		Objects.requireNonNull(name);
		return contextMap.get(name);
	}

	/**
	 * 创建一个默认类型的上下文，如果上下文已存在则返回已存在的上下文实例
	 *
	 * @param name 名称，上下文唯一标识
	 * @return name对应的上下文
	 */
	public TypeEventContext createContext(String name) {
		Objects.requireNonNull(name);
		return contextMap.computeIfAbsent(name, TypeDefaultContext::new);
	}

	/**
	 * 将一个上下文添加到映射表中并返回。如果映射表中已经存在则根据参数确认是否覆盖
	 *
	 * @param context 需要添加映射的上下文
	 * @param cover   是否覆盖，true：覆盖
	 *                false：不覆盖
	 * @return 最终加入映射表的上下文
	 */
	public TypeEventContext addContext(TypeEventContext context, boolean cover) {
		Objects.requireNonNull(context);
		if (cover) {
			contextMap.put(context.name(), context);
			return context;
		} else {
			return contextMap.putIfAbsent(context.name(), context);
		}
	}

	/**
	 * 通过标识清空一个上下文并返回被清空的上下文，如果上下文原本就不在映射表中则返回null<br>
	 *
	 * @param name 上下文标识
	 * @return 被删除的上下文
	 */
	public TypeEventContext clearContext(String name) {
		TypeEventContext context = contextMap.get(name);
		if (Objects.nonNull(context)) {
			context.clear();
		}
		return context;
	}

	/**
	 * 通过标识删除一个上下文并返回被删除的上下文，如果上下文原本就不在映射表中则返回null<br>
	 *
	 * @param name 上下文标识
	 * @return 被删除的上下文
	 */
	public TypeEventContext removeContext(String name) {
		return contextMap.remove(name);
	}

	// ========================= 事件和监听器操作 =========================

	/**
	 * 设置指定上下文中某个事件的监听模式，如果上下文不存在映射表中则无效
	 *
	 * @param name            上下文标识
	 * @param type            事件类型
	 * @param listenerPattern 监听模式
	 */
	public void setListenerPattern(String name, Type type, ListenerPattern listenerPattern) {
		if (contextMap.containsKey(name)) {
			TypeEventContext context = contextMap.get(name);
			context.setListenerPattern(type, listenerPattern);
		}
	}

	/**
	 * 创建一个监听模块，使用它可以快速构造一个自定义的监听器
	 *
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回值
	 * @return ListenerModel
	 */
	public <E, R> ListenerBuilder<E, R> createListener(Listener<E, R> listener) {
		return ListenerBuilder.build(listener);
	}

	/**
	 * 在一个上下文将事件和监听器进行绑定，如果上下文不存在则创建
	 *
	 * @param name          上下文标识
	 * @param type          事件类型
	 * @param listenerModel 监听器包装信息，提供完整的监听器配置
	 * @param <E>           事件类型
	 * @param <R>           监听器返回值
	 */
	public <E, R> void bind(String name, Type type, ListenerBuilder<E, R> listenerModel) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(listenerModel);

		TypeEventContext context = createContext(name);
		context.bind(type, listenerModel.listenerDecorate());
	}

	/**
	 * 在一个上下文将事件和监听器进行绑定，如果上下文不存在则创建<br>
	 * 同时可以为监听器绑定操作
	 *
	 * @param name           上下文标识
	 * @param type           事件类型
	 * @param listener       监听器
	 * @param eventProcessor 监听器的前置操作和后置操作，如果为null则使用默认的处理器
	 * @param <E>            事件类型
	 * @param <R>            监听器返回值
	 */
	public <E, R> void bind(String name, Type type, Listener<E, R> listener, EventProcessor<E, R> eventProcessor) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(listener);

		TypeEventContext context = createContext(name);
		ListenerDecorate<E, R> listenerDecorate = ListenerDecorate
			.<E, R>build()
			.eventProcessor(eventProcessor)
			.listener(listener);
		context.bind(type, listenerDecorate);
	}

	/**
	 * 将一个实例o中的方法转换成监听器并进行绑定<br>
	 * 该方法必须被{@link EventListener}注解标注
	 *
	 * @param name   上下文标识
	 * @param method 用作监听器的方法
	 * @param o      获取对象中可以带有{@link EventListener}注解的方法，并解析和绑定
	 * @param <E>    监听器
	 * @param <R>    事件类型
	 * @throws NullPointerException 找不到注解时
	 */
	public <E, R> void bind(String name, Method method, Object o) {
		Objects.requireNonNull(method);
		Objects.requireNonNull(name);
		Objects.requireNonNull(o);
		// 只能去代理 PUBLIC 标识的方法
		if (method.getModifiers() != Modifier.PUBLIC){
			return;
		}
		Listener<E, R> listenerProxy = newListenerProxy(o, method);
		EventListener eventListener = method.getAnnotation(EventListener.class);
		// 事件类型
		Class<?> value = eventListener.value();
		Class<?>[] arguments = eventListener.arguments();
		Type type = value;
		if (ArrayUtil.isEmpty(arguments)) {
			type = ParameterizedTypeImpl.make(value, arguments, null);
		}
		ListenerBuilder<E, R> listenerModel = createListener(listenerProxy)
			.order(eventListener.order())
			.async(eventListener.isAsync())
			.spreadPattern(ReflectUtil.newInstance(eventListener.spread().getName()));
		// 异常方法
		String throwHandlerName = eventListener.throwHandler();
		Method throwMethod = ReflectUtil.getMethodByName(o.getClass(), throwHandlerName);
		if (throwMethod != null) {
			listenerModel.throwableFn(throwable -> ReflectUtil.invoke(o, throwMethod, throwable));
		}
		// 绑定
		bind(name, type, listenerModel);
	}

	/**
	 * 将一个实例o中所有带有{@link  EventListener}注释的方法转换成监听器并进行绑定
	 *
	 * @param name 上下文标识
	 * @param o    获取对象中可以带有{@link EventListener}注解的方法，并解析和绑定
	 */
	public void bind(String name, Object o) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(o);
		// 获取方法
		Method[] methods = ReflectUtil.getMethods(
			o.getClass(),
			m -> m.getAnnotation(EventListener.class) != null
		);
		if (methods == null || methods.length == 0) {
			return;
		}
		for (Method method : methods) {
			bind(name, method, o);
		}
	}

	/**
	 * 在一个上下文中解绑事件和监听器
	 *
	 * @param name          上下文名称
	 * @param type          事件类型
	 * @param listenerModel 监听器包装信息，提供完整的监听器配置
	 * @param <E>           事件类型
	 * @param <R>           监听器返回值
	 */
	public <E, R> void unbind(String name, Type type, ListenerBuilder<E, R> listenerModel) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(listenerModel);
		TypeEventContext context = createContext(name);
		context.unbind(type, listenerModel.listenerDecorate());
	}

	/**
	 * 在一个上下文中解绑事件和监听器
	 *
	 * @param name     上下文名称
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回值
	 */
	public <E, R> void unbind(String name, Type type, Listener<E, R> listener) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(listener);

		TypeEventContext context = createContext(name);
		ListenerDecorate<E, R> listenerDecorate = ListenerDecorate
			.<E, R>build()
			.listener(listener);
		context.unbind(type, listenerDecorate);
	}

	/**
	 * 解绑一个上下文中某个事件和所有监听器的关联
	 *
	 * @param name 上下文名称
	 * @param type 事件类型
	 */
	public void unbindAll(String name, Type type) {
		Objects.requireNonNull(name);
		TypeEventContext context = createContext(name);
		context.unbindAll(type);
	}

	// ========================= 发布操作 =========================

	/**
	 * 在一个上下文中发布事件，如果上下文不存在则无效
	 *
	 * @param name  上下文名称
	 * @param event 事件实例
	 */
	public void publish(String name, Object event) {
		TypeEventContext context = getContext(name);
		if (context != null) {
			context.publish(event);
		}
	}

	/**
	 * 在一个上下文中发布事件，如果上下文不存在则无效<br>
	 * 如果event是复杂的参数化类型对象，则可以使用type标注其具体类型
	 *
	 * @param name  上下文名称
	 * @param event 事件实例
	 * @param type  事件类型
	 */
	public void publish(String name, Object event, Type type) {
		TypeEventContext context = getContext(name);
		if (context != null) {
			context.publish(event, type);
		}
	}

	/**
	 * 生成一个Listener代理类
	 *
	 * @param o      代理的对象
	 * @param method 需要被代理的方法
	 * @return 监听器代理类
	 */
	@SuppressWarnings("unchecked")
	private <E, R> Listener<E, R> newListenerProxy(Object o, Method method) {
		Class<?>[] classes = {Listener.class};
		ClassLoader classLoader = this.getClass().getClassLoader();
		return (Listener<E, R>) Proxy.newProxyInstance(
			classLoader,
			classes,
			(p, m, a) -> {
				if (LISTENER_METHOD_NAME.equals(m.getName())) {
					return method.invoke(o, a);
				}
				if (refuseGroup.contains(m.getName())) {
					return m.invoke(o, a);
				}
				return null;
			}
		);
	}


	/**
	 * 动态代理时忽略的方法
	 */
	static List<String> refuseGroup = new ArrayList<>();

	static {
		refuseGroup.add("hashCode");
		refuseGroup.add("equals");
		refuseGroup.add("clone");
		refuseGroup.add("toString");
		refuseGroup.add("finalize");
	}

}
