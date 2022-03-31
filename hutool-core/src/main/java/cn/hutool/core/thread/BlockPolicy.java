package cn.hutool.core.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 当任务队列过长时处于阻塞状态，直到添加到队列中
 * 如果阻塞过程中被中断，就会抛出{@link InterruptedException}异常<br>
 * 有时候在线程池内访问第三方接口，只希望固定并发数去访问，并且不希望丢弃任务时使用此策略，队列满的时候会处于阻塞状态(例如刷库的场景)
 *
 * @author luozongle
 * @since 5.8.0
 */
public class BlockPolicy implements RejectedExecutionHandler {

	public BlockPolicy() {
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		try {
			e.getQueue().put(r);
		} catch (InterruptedException ex) {
			throw new RejectedExecutionException("Task " + r + " rejected from " + e);
		}
	}
}
