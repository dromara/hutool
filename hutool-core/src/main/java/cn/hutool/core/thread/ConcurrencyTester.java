package cn.hutool.core.thread;

/**
 * 高并发测试工具类
 * 
 * <pre>
 * ps:
 * //模拟1000个线程并发
 * ConcurrencyTester ct = new ConcurrencyTester(1000);
 * ct.run(() -> {
 *      // 需要并发测试的业务代码
 * });
 * </pre>
 *
 * @author kwer
 */
public class ConcurrencyTester {
	private SyncFinisher sf;

	public ConcurrencyTester(int threadSize) {
		this.sf = new SyncFinisher(threadSize);
	}

	/**
	 * 执行测试
	 * 
	 * @param runnable 要测试的内容
	 */
	public void test(Runnable runnable) {
		this.sf.addRepeatWorker(runnable);
		this.sf.start();
	}
}
