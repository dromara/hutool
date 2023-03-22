package cn.hutool.core.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

/**
 * 当任务队列过长时处于阻塞状态，直到添加到队列中
 * 如果阻塞过程中被中断，就会抛出{@link InterruptedException}异常<br>
 * 有时候在线程池内访问第三方接口，只希望固定并发数去访问，并且不希望丢弃任务时使用此策略，队列满的时候会处于阻塞状态(例如刷库的场景)
 *
 * @author luozongle
 * @since 5.8.0
 */
public class BlockPolicy implements RejectedExecutionHandler {

	/**
	 * 线程池关闭时，为避免任务丢失，留下处理方法
	 * 如果需要由调用方来运行，可以{@code new BlockPolicy(Runnable::run)}
	 */
	private final Consumer<Runnable> handlerwhenshutdown;

	/**
	 * 构造
	 *
	 * @param handlerwhenshutdown 线程池关闭后的执行策略
	 */
	public BlockPolicy(final Consumer<Runnable> handlerwhenshutdown) {
		this.handlerwhenshutdown = handlerwhenshutdown;
	}

	/**
	 * 构造
	 */
	public BlockPolicy() {
		this(null);
	}

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		// 线程池未关闭时，阻塞等待
		if (false == e.isShutdown()) {
			try {
				e.getQueue().put(r);
			} catch (InterruptedException ex) {
				throw new RejectedExecutionException("Task " + r + " rejected from " + e);
			}
		} else if (null != handlerwhenshutdown) {
			// 当设置了关闭时候的处理
			handlerwhenshutdown.accept(r);
		}

		// 线程池关闭后，丢弃任务
	}
}
