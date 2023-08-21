
package cn.hutool.core.eventbus;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 同步阻塞的 EventBus
 *
 * @author unknowIfGuestInDream
 */
public class EventBus {
	private Executor executor;
	private final ObserverRegister register = new ObserverRegister();

	public EventBus() {
	}

	protected EventBus(Executor executor) {
		this.executor = executor;
	}

	/**
	 * 注册观察者
	 */
	public void register(Object observer) {
		register.register(observer);
	}

	/**
	 * 移除观察者
	 */
	public void unregister(Object observer) {
		register.unregister(observer);
	}

	/**
	 * 清空注册表
	 */
	public void clear() {
		register.clear();
	}

	/**
	 * 注册表中数据数量
	 */
	public int size() {
		return register.size();
	}

	/**
	 * 发布者-发送消息
	 */
	public void post(Object event) {
		List<ObserverAction> observerActions = register.getMatchedObserverActions(event);
		for (ObserverAction observerAction : observerActions) {
			if (executor == null) {
				observerAction.execute(event);
			} else {
				executor.execute(() -> observerAction.execute(event));
			}
		}
	}
}
