package cn.hutool.core.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Observer 注册表
 *
 * @author unknowIfGuestInDream
 */
public class ObserverRegister {
	private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

	/**
	 * 将观察者注册到 注册表中
	 *
	 * @param observer 观察者
	 */
	public void register(Object observer) {
		// 遍历带有注解的方法，将事件和对应的多个处理方法，存储到map中
		Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);
		// 将获取到的单个观察者的可执行方法，放到如全局的map中，使用并发类
		for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : observerActions.entrySet()) {
			Class<?> eventType = entry.getKey();
			Collection<ObserverAction> evenActions = entry.getValue();
			CopyOnWriteArraySet<ObserverAction> registryEvenActions = registry.getOrDefault(eventType,
					new CopyOnWriteArraySet<>());
			registryEvenActions.addAll(evenActions);
			registry.put(eventType, registryEvenActions);
		}
	}

	/**
	 * 从注册表种移除观察者
	 */
	public void unregister(Object observer) {
		registry.values().forEach(o -> o.removeIf(b -> b.getTarget().equals(observer)));
		registry.entrySet().removeIf(e -> e.getValue().isEmpty());
	}

	/**
	 * 清空注册表
	 */
	public void clear() {
		registry.clear();
	}

	/**
	 * 注册表数量
	 */
	public int size() {
		return registry.size();
	}

	/**
	 * 获取匹配的观察者事件
	 */
	public List<ObserverAction> getMatchedObserverActions(Object event) {
		List<ObserverAction> result = new ArrayList<>();
		Class<?> postedEventClass = event.getClass();
		for (Map.Entry<Class<?>, CopyOnWriteArraySet<ObserverAction>> entry : registry.entrySet()) {
			Class<?> eventClass = entry.getKey();
			// 匹配相同类型或父类型
			if (postedEventClass.isAssignableFrom(eventClass)) {
				result.addAll(entry.getValue());
			}
		}
		return result;
	}

	/**
	 * 遍历带有注解的方法，将事件和对应的多个处理方法，存储到map中
	 */
	public Map<Class<?>, Collection<ObserverAction>> findAllObserverActions(Object observer) {
		Map<Class<?>, Collection<ObserverAction>> result = new HashMap<>();
		// 观察者类型
		Class<?> observerClass = observer.getClass();
		for (Method method : getAnnotatedMethods(observerClass)) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			Class<?> eventType = parameterTypes[0];
			result.putIfAbsent(eventType, new ArrayList<>());
			result.get(eventType).add(new ObserverAction(observer, method));
		}
		return result;
	}

	/**
	 * 根据观察者类型，查找方法列表
	 */
	public List<Method> getAnnotatedMethods(Class<?> clazz) {
		List<Method> result = new ArrayList<>();
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Subscribe.class)) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length != 1) {
					throw new EventBusException("The method allows only one parameter.");
				}
				result.add(method);
			}
		}
		return result;
	}
}
