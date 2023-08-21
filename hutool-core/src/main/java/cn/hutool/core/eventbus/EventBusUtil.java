package cn.hutool.core.eventbus;

import cn.hutool.core.thread.ThreadUtil;

/**
 * EventBus工具类
 *
 * @author unknowIfGuestInDream
 */
public class EventBusUtil {

	/**
	 * 返回默认的EventBus
	 */
	public static EventBus getDefault() {
		return SingletonEventBus.EVENT_BUS;
	}

	/**
	 * 返回默认的AsyncEventBus
	 */
	public static EventBus getDefaultAsync() {
		return SingletonAsyncEventBus.EVENT_BUS;
	}

	private static class SingletonEventBus {
		private static final EventBus EVENT_BUS = new EventBus();
	}

	private static class SingletonAsyncEventBus {
		private static final AsyncEventBus EVENT_BUS = new AsyncEventBus(ThreadUtil.newExecutor());
	}
}
