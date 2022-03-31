package cn.hutool.core.thread.rejected;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略工具类
 *
 * @author luozongle
 */
public class RejectedExecutionHandlerUtility {

	/**
	 * 当任务队列过长时处于阻塞状态，直到添加到队列中
	 * 如果阻塞过程中被中断，就会抛出{@link InterruptedException}异常
	 */
    public static class BlockPolicy implements RejectedExecutionHandler {

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
}
