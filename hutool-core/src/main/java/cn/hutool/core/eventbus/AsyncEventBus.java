package cn.hutool.core.eventbus;

import java.util.concurrent.Executor;

/**
 * 异步非阻塞的EventBus
 *
 * @author unknowIfGuestInDream
 */
public class AsyncEventBus extends EventBus {
	public AsyncEventBus(Executor executor) {
		super(executor);
	}
}
