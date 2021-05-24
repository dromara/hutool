package cn.hutool.cron.timingwheel;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 系统计时器
 *
 * @author eliasyaoyc, looly
 */
public class SystemTimer {
	/**
	 * 底层时间轮
	 */
	private final TimingWheel timeWheel;

	/**
	 * 一个Timer只有一个delayQueue
	 */
	private final DelayQueue<TimerTaskList> delayQueue = new DelayQueue<>();

	/**
	 * 执行队列取元素超时时长，单位毫秒，默认100
	 */
	private long delayQueueTimeout = 100;

	/**
	 * 轮询delayQueue获取过期任务线程
	 */
	private ExecutorService bossThreadPool;

	/**
	 * 构造
	 */
	public SystemTimer() {
		timeWheel = new TimingWheel(1, 20, delayQueue::offer);
	}

	/**
	 * 设置执行队列取元素超时时长，单位毫秒
	 * @param delayQueueTimeout 执行队列取元素超时时长，单位毫秒
	 * @return this
	 */
	public SystemTimer setDelayQueueTimeout(long delayQueueTimeout){
		this.delayQueueTimeout = delayQueueTimeout;
		return this;
	}

	/**
	 * 启动，异步
	 *
	 * @return this
	 */
	public SystemTimer start() {
		bossThreadPool = ThreadUtil.newSingleExecutor();
		bossThreadPool.submit(() -> {
			while (true) {
				if(false == advanceClock()){
					break;
				}
			}
		});
		return this;
	}

	/**
	 * 强制结束
	 */
	public void stop(){
		this.bossThreadPool.shutdown();
	}

	/**
	 * 添加任务
	 *
	 * @param timerTask 任务
	 */
	public void addTask(TimerTask timerTask) {
		//添加失败任务直接执行
		if (false == timeWheel.addTask(timerTask)) {
			ThreadUtil.execAsync(timerTask.getTask());
		}
	}

	/**
	 * 指针前进并获取过期任务
	 *
	 * @return 是否结束
	 */
	private boolean advanceClock() {
		try {
			TimerTaskList timerTaskList = poll();
			if (null != timerTaskList) {
				//推进时间
				timeWheel.advanceClock(timerTaskList.getExpire());
				//执行过期任务（包含降级操作）
				timerTaskList.flush(this::addTask);
			}
		} catch (InterruptedException ignore) {
			return false;
		}
		return true;
	}

	/**
	 * 执行队列取任务列表
	 * @return 任务列表
	 * @throws InterruptedException 中断异常
	 */
	private TimerTaskList poll() throws InterruptedException {
		return this.delayQueueTimeout > 0 ?
				delayQueue.poll(delayQueueTimeout, TimeUnit.MILLISECONDS) :
				delayQueue.poll();
	}
}
