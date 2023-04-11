/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.cron.timingwheel;

import org.dromara.hutool.core.thread.ThreadUtil;

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
	public SystemTimer setDelayQueueTimeout(final long delayQueueTimeout){
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
				if(!advanceClock()){
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
	public void addTask(final TimerTask timerTask) {
		//添加失败任务直接执行
		if (!timeWheel.addTask(timerTask)) {
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
			final TimerTaskList timerTaskList = poll();
			if (null != timerTaskList) {
				//推进时间
				timeWheel.advanceClock(timerTaskList.getExpire());
				//执行过期任务（包含降级操作）
				timerTaskList.flush(this::addTask);
			}
		} catch (final InterruptedException ignore) {
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
