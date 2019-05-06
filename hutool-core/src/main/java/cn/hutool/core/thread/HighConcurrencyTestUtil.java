package cn.hutool.core.thread;

import cn.hutool.core.exceptions.UtilException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 高并发测试工具类
 * <pre>
 * ps:
 * //模拟1000个线程并发
 * HighConcurrencyTestUtil concurrencyTestUtil = new HighConcurrencyTestUtil(1000);
 * concurrencyTestUtil.run(() -> {
 *      // 需要并发测试的业务代码
 * });
 * </pre>
 *
 * @author kwer
 */
public class HighConcurrencyTestUtil {
    private CountDownLatch start;
    private CountDownLatch end;
    /**
     * 并发数
     */
    private int size = 10;

    public HighConcurrencyTestUtil() {
        this(10);
    }

    public HighConcurrencyTestUtil(int size) {
        this.size = size;
        start = new CountDownLatch(1);
        end = new CountDownLatch(size);
    }

    /**
     * 启动并发测试
     *
     * @param executeFun
     * @throws InterruptedException
     */
    public void run(final ExecuteFun executeFun) throws InterruptedException {
        ExecutorService executorService = ThreadUtil.newExecutor(size);
        for (int i = 0; i < size; i++) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 使线程在此等待，当开始门打开时，一起涌入门中
                        start.await();
                        executeFun.run();
                    } catch (InterruptedException e) {
                        throw new UtilException(e, "高并发测试工具类运行出现异常!");
                    } finally {
                        // 将结束门减1，减到0时，就可以开启结束门了
                        end.countDown();
                    }
                }
            };
            executorService.submit(run);
        }
        // 因开启门只需一个开关，所以立马就开启开始门
        start.countDown();
        // 等结束门开启
        end.await();
        executorService.shutdown();
    }

    /**
     * 并发测试业务代码函数入口
     */
    public interface ExecuteFun {
        void run();
    }

}
