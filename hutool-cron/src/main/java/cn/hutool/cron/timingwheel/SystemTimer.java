package cn.hutool.cron.timingwheel;

import cn.hutool.core.thread.ThreadUtil;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

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
	 * 过期任务执行线程
	 */
	private final ExecutorService workerThreadPool;

	/**
	 * 轮询delayQueue获取过期任务线程
	 */
	private ExecutorService bossThreadPool;

	/**
	 * 构造函数
	 */
	public SystemTimer(int timeout) {
		timeWheel = new TimingWheel(1, 20, System.currentTimeMillis(), delayQueue);
		workerThreadPool = ThreadUtil.newExecutor(100);
		bossThreadPool = ThreadUtil.newSingleExecutor();
		bossThreadPool.submit(() -> {
			while (true) {
				this.advanceClock(timeout);
			}
		});
	}

	/**
	 * 添加任务
	 */
	public void addTask(TimerTask timerTask) {
		//添加失败任务直接执行
		if (!timeWheel.addTask(timerTask)) {
			workerThreadPool.submit(timerTask.getTask());
		}
	}

	/**
	 * 获取过期任务
	 */
	private void advanceClock(long timeout) {
		try {
			TimerTaskList timerTaskList = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
			if (timerTaskList != null) {
				//推进时间
				timeWheel.advanceClock(timerTaskList.getExpiration());
				//执行过期任务（包含降级操作）
				timerTaskList.flush(this::addTask);
			}
		} catch (InterruptedException ignore) {
			// ignore
		}
	}
}
