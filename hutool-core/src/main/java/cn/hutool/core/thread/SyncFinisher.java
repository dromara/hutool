package cn.hutool.core.thread;

import cn.hutool.core.exceptions.NotInitedException;
import cn.hutool.core.exceptions.UtilException;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 线程同步结束器<br>
 * 在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
 * 
 * <pre>
 * ps:
 * //模拟1000个线程并发
 * SyncFinisher sf = new SyncFinisher(1000);
 * concurrencyTestUtil.run(() -&gt; {
 *      // 需要并发测试的业务代码
 * });
 * </pre>
 * 
 * 
 * @author Looly
 * @since 4.1.15
 */
public class SyncFinisher {

	private final Set<Worker> workers;
	private final int threadSize;
	private final ExecutorService executorService;

	private boolean isBeginAtSameTime;
	/** 启动同步器，用于保证所有worker线程同时开始 */
	private final CountDownLatch beginLatch;
	/** 结束同步器，用于等待所有worker线程同时结束 */
	private CountDownLatch endLatch;

	/**
	 * 构造
	 * 
	 * @param threadSize 线程数
	 */
	public SyncFinisher(int threadSize) {
		this.beginLatch = new CountDownLatch(1);
		this.threadSize = threadSize;
		this.executorService = ThreadUtil.newExecutor(threadSize);
		this.workers = new LinkedHashSet<>();
	}

	/**
	 * 设置是否所有worker线程同时开始
	 * 
	 * @param isBeginAtSameTime 是否所有worker线程同时开始
	 * @return this
	 */
	public SyncFinisher setBeginAtSameTime(boolean isBeginAtSameTime) {
		this.isBeginAtSameTime = isBeginAtSameTime;
		return this;
	}

	/**
	 * 增加定义的线程数同等数量的worker
	 * 
	 * @param runnable 工作线程
	 * @return this
	 */
	public SyncFinisher addRepeatWorker(final Runnable runnable) {
		for (int i = 0; i < this.threadSize; i++) {
			addWorker(new Worker() {
				@Override
				public void work() {
					runnable.run();
				}
			});
		}
		return this;
	}

	/**
	 * 增加工作线程
	 * 
	 * @param runnable 工作线程
	 * @return this
	 */
	public SyncFinisher addWorker(final Runnable runnable) {
		return addWorker(new Worker() {
			@Override
			public void work() {
				runnable.run();
			}
		});
	}

	/**
	 * 增加工作线程
	 * 
	 * @param worker 工作线程
	 * @return this
	 */
	synchronized public SyncFinisher addWorker(Worker worker) {
		workers.add(worker);
		return this;
	}

	/**
	 * 开始工作
	 */
	public void start() {
		start(true);
	}

	/**
	 * 开始工作
	 * 
	 * @param sync 是否阻塞等待
	 * @since 4.5.8
	 */
	public void start(boolean sync) {
		endLatch = new CountDownLatch(workers.size());
		for (Worker worker : workers) {
			executorService.submit(worker);
		}
		// 保证所有worker同时开始
		this.beginLatch.countDown();

		if (sync) {
			try {
				this.endLatch.await();
			} catch (InterruptedException e) {
				throw new UtilException(e);
			}
		}
	}

	/**
	 * 等待所有Worker工作结束，否则阻塞
	 * 
	 * @throws InterruptedException 用户中断
	 * @deprecated 使用start方法指定是否阻塞等待
	 */
	@Deprecated
	public void await() throws InterruptedException {
		if (endLatch == null) {
			throw new NotInitedException("Please call start() method first!");
		}

		endLatch.await();
	}

	/**
	 * 清空工作线程对象
	 */
	public void clearWorker() {
		workers.clear();
	}

	/**
	 * 剩余任务数
	 * 
	 * @return 剩余任务数
	 */
	public long count() {
		return endLatch.getCount();
	}

	/**
	 * 工作者，为一个线程
	 * 
	 * @author xiaoleilu
	 *
	 */
	public abstract class Worker implements Runnable {

		@Override
		public void run() {
			if (isBeginAtSameTime) {
				try {
					beginLatch.await();
				} catch (InterruptedException e) {
					throw new UtilException(e);
				}
			}
			try {
				work();
			} finally {
				endLatch.countDown();
			}
		}

		public abstract void work();
	}
}
