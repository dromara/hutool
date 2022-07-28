package cn.hutool.core.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.thread.ThreadUtil;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 默认的类型事件上下文实现<br>
 * 提供事件类型最基本的处理
 *
 * @author Create by liuwenhao on 2022/7/20 14:45
 */
public class TypeDefaultContext implements TypeEventContext {

	static final ListenerPattern DEFAULT_LISTENER_PATTERN = new BroadcastListenerPattern();
	static final SpreadPattern DEFAULT_SPREAD_PATTERN = new EdgeSpreadPattern();

	/**
	 * 上下文的唯一标识，在多上下文的应用中，唯一标识的作用非常明显
	 */
	String name;

	/**
	 * 事件监听模式
	 */
	Map<Type, ListenerPattern> listenerPatternMap = new ConcurrentHashMap<>();

	/**
	 * 存储所有的监听器，如果采用注解的方式直接在方法上声明的监听器则会通过动态代理为其生成一个Listener对象。
	 * 这是为了在发布事件阶段上下文拿到的监听器一定是Listener或Listener的实现类
	 */
	Map<Type, Set<ListenerDecorate<Object, Object>>> listenerMap = new ConcurrentHashMap<>();

	@Override
	public String name() {
		return name;
	}

	public TypeDefaultContext(String name) {
		this.name = name;
	}

	@Override
	public void setListenerPattern(Type type, ListenerPattern listenerPattern) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(listenerPattern);
		if (!checkEvent(type)) {
			return;
		}
		listenerPatternMap.put(type, listenerPattern);
	}

	/**
	 * 绑定事件和监听器<br>
	 * 使用泛型是因为要保证每一个监听器都有一个固定的事件，但是在存储的时候并不是所有的类型都符合，<br>
	 * 所以使用 Listener&lt;Object, Object&gt; 来代替，这并不会影响到后续的判断<br>
	 * 添加绑定时如果该事件类型还未绑定则新增绑定关系，如果已经绑定则添加绑定关系<br>
	 * 注意：绑定时请注意泛型，如：List&lt;Integer&gt;和List&lt;String&gt;是不同的
	 *
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      返回值类型
	 */
	@Override
	public <E, R> void bind(Type type, Listener<E, R> listener) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(listener);
		ListenerDecorate<Object, Object> objectListener = listenerDecorate(listener);
		listenerMap.compute(type, (t, lSet) -> {
			if (lSet == null) {
				return CollUtil.set(true, objectListener);
			} else {
				lSet.add(objectListener);
				return lSet;
			}
		});

		listenerPatternMap.putIfAbsent(type, DEFAULT_LISTENER_PATTERN);
	}

	/**
	 * 解绑事件和监听器<br>
	 * 使用泛型是因为要保证每一个监听器都有一个固定的事件，但是在存储的时候并不是所有的类型都符合，<br>
	 * 所以使用 Listener&lt;Object, Object&gt; 来代替，这并不会影响到后续的判断<br>
	 * 解绑时如果该事件没有关联过任何监听器则不作任何处理<br>
	 * 注意：绑定时请注意泛型，如：List&lt;Integer&gt;和List&lt;String&gt;是不同的
	 *
	 * @param type     事件类型
	 * @param listener 监听器
	 * @param <E>      事件类型
	 * @param <R>      返回值类型
	 */
	@Override
	public <E, R> void unbind(Type type, Listener<E, R> listener) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(listener);
		if (!checkEvent(type)) {
			return;
		}
		ListenerDecorate<Object, Object> objectListener = listenerDecorate(listener);
		Set<ListenerDecorate<Object, Object>> listeners = listenerMap.get(type);
		if (listeners != null) {
			listeners.removeIf(next -> Objects.equals(next, listener));
			listeners.remove(objectListener);
			// 当type没有与其关联的监听器时移除map中的type
			if (listeners.isEmpty()) {
				listenerMap.remove(type);
				listenerPatternMap.remove(type);
			}
		}

	}

	@Override
	public void unbindAll(Type type) {
		listenerMap.remove(type);
		listenerPatternMap.remove(type);
	}

	@Override
	public void clear() {
		listenerMap.clear();
		listenerPatternMap.clear();
	}

	/**
	 * 获取和type绑定的所有监听器<br>
	 * 该功能是通过{@link ListenerPattern}来实现的。<br>
	 * 无法保证每一个监听器返回的值都是相同类型的，所以使用{@link Object}来取代返回值泛型
	 *
	 * @param type 事件类型
	 * @param <E>  事件类型
	 * @return 和该事件绑定的监听器集合
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <E> Collection<Listener<E, Object>> getListeners(Type type) {
		// 获取对应的监听器
		Set<ListenerDecorate<Object, Object>> listenerSet = Opt.ofNullable(listenerMap.get(type))
			.orElse(null);
		if (listenerSet == null) {
			return Collections.emptyList();
		}
		// 获取对应的监听模式
		ListenerPattern listenerPattern = Opt.ofNullable(listenerPatternMap.get(type))
			.orElse(DEFAULT_LISTENER_PATTERN);
		// 提取内部的监听器实例
		List<Listener<E, Object>> listeners = listenerSet.stream()
			.sorted().map(ListenerDecorate::getListener)
			.map(o -> (Listener<E, Object>) o)
			.collect(Collectors.toList());
		Collection<Listener<E, Object>> listener = listenerPattern.getListener(type, listeners);
		// 后续的操作中需要用到 ListenerDecorate 的部分，监听模式不应忽略这些重要的属性
		return listenerSet.stream().filter(o -> {
				o.clearThrow();
				Listener<Object, Object> oListener = o.getListener();
				for (Listener<E, Object> l : listener) {
					// 受到代理的影响如果直接使用equals判断可能会有影响
					if (oListener == l || oListener.equals(l)) {
						return true;
					}
				}
				return false;
			})
			.map(o -> (Listener<E, Object>) o)
			.sorted().collect(Collectors.toList());
	}

	@Override
	public void publish(Object o, Type type) {
		Collection<Listener<Object, Object>> listeners = getListeners(type);
		listeners.forEach(l -> execute(l, o));
	}

	/**
	 * 单个监听器对事件的处理
	 *
	 * @param listener 监听器，本上下文中为：{@link ListenerDecorate}
	 * @param o        事件实例
	 * @param <E>      事件类型
	 * @param <R>      返回值类型
	 */
	private <E, R> void execute(Listener<E, R> listener, Object o) {

		ListenerDecorate<E, R> listenerDecorate = (ListenerDecorate<E, R>) listener;
		if (listenerDecorate.isAsync()) {
			ThreadUtil.execAsync(() -> doExecute(listenerDecorate, o));
		} else {
			doExecute(listenerDecorate, o);
		}
	}

	@SuppressWarnings("unchecked")
	private <E, R> void doExecute(ListenerDecorate<E, R> listener, Object o) {
		if (o == null) {
			return;
		}
		E event = (E) o;
		try {
			listener.getEventProcessor().before(event, listener);
			SpreadPattern spreadPattern = listener.getSpreadPattern();
			R r = listener.execEvent(event);
			listener.result(r);
			if (r == null) {
				return;
			}
			spreadPattern.spread(this, r, event);
		} catch (Throwable e) {
			listener.throwResult(e);
			listener.getThrowHandler().apply(e);
		} finally {
			listener.getEventProcessor().after(event, listener);
		}

	}

	/**
	 * 检查当前环境中是否存在type类型的事件
	 *
	 * @param type 事件类型
	 * @return 当前环境中是否存在该类型的事件
	 */
	private boolean checkEvent(Type type) {
		return listenerMap.containsKey(type);
	}

	/**
	 * 默认情况下使用的是ListenerDecorate包装器作为监听器的实现<br>
	 * 此处提供了一种转化机制，将{@link Listener}转化为{@link ListenerDecorate}
	 *
	 * @param listener 要绑定的监听器
	 * @param <E>      事件类型
	 * @param <R>      监听器返回值类型
	 * @return ListenerDecorate
	 */
	@SuppressWarnings("unchecked")
	protected <E, R> ListenerDecorate<Object, Object> listenerDecorate(Listener<E, R> listener) {
		ListenerDecorate<Object, Object> objectListener;
		// 在类型不是ListenerDecorate的时候构建ListenerDecorate
		if (!(listener instanceof ListenerDecorate)) {
			objectListener = ListenerDecorate.build()
				.listener((Listener<Object, Object>) listener);
		} else {
			objectListener = (ListenerDecorate<Object, Object>) listener;
		}

		return objectListener.getSpreadPattern() == null ?
			objectListener.spreadPattern(DEFAULT_SPREAD_PATTERN) : objectListener;
	}

}
