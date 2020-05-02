package cn.hutool.core.thread;

import cn.hutool.core.date.TimeInterval;

/**
 * 高并发测试工具类
 *
 * <pre>
 * ps:
 * //模拟1000个线程并发
 * ConcurrencyTester ct = new ConcurrencyTester(1000);
 * ct.test(() -&gt; {
 *      // 需要并发测试的业务代码
 * });
 * </pre>
 *
 * @author kwer
 */
public class ConcurrencyTester {
	private final SyncFinisher sf;
	private final TimeInterval timeInterval;
	private long interval;

	public ConcurrencyTester(int threadSize) {
		this.sf = new SyncFinisher(threadSize);
		this.timeInterval = new TimeInterval();
	}

	/**
	 * 执行测试
	 *
	 * @param runnable 要测试的内容
	 * @return this
	 */
	public ConcurrencyTester test(Runnable runnable) {
		timeInterval.start();
		this.sf//
				.addRepeatWorker(runnable)//
				.setBeginAtSameTime(true)// 同时开始
				.start();

		this.interval = timeInterval.interval();
		return this;
	}

	/**
	 * 获取执行时间
	 *
	 * @return 执行时间，单位毫秒
	 */
	public long getInterval() {
		return this.interval;
	}
}
