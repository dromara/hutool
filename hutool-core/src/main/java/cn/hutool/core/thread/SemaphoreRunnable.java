package cn.hutool.core.thread;

import java.util.concurrent.Semaphore;

/**
 * 带有信号量控制的{@link Runnable} 接口抽象实现
 * 
 * <p>
 * 通过设置信号量，可以限制可以访问某些资源（物理或逻辑的）线程数目。<br>
 * 例如：设置信号量为2，表示最多有两个线程可以同时执行方法逻辑，其余线程等待，直到此线程逻辑执行完毕
 * </p>
 * 
 * @author looly
 * @since 4.4.5
 */
public class SemaphoreRunnable implements Runnable {

	/** 实际执行的逻辑 */
	private final Runnable runnable;
	/** 信号量 */
	private final Semaphore semaphore;

	/**
	 * 构造
	 * 
	 * @param runnable 实际执行的线程逻辑
	 * @param semaphore 信号量，多个线程必须共享同一信号量
	 */
	public SemaphoreRunnable(Runnable runnable, Semaphore semaphore) {
		this.runnable = runnable;
		this.semaphore = semaphore;
	}

	/**
	 * 获得信号量
	 *
	 * @return {@link Semaphore}
	 * @since 5.3.6
	 */
	public Semaphore getSemaphore(){
		return this.semaphore;
	}

	@Override
	public void run() {
		if (null != this.semaphore) {
			try{
				semaphore.acquire();
				try {
					this.runnable.run();
				} finally {
					semaphore.release();
				}
			}catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
