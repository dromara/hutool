/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.cron.timingwheel;

/**
 * 延迟任务
 *
 * @author eliasyaoyc, looly
 */
public class TimerTask {

	/**
	 * 延迟时间
	 */
	private final long delayMs;

	/**
	 * 任务
	 */
	private final Runnable task;

	/**
	 * 时间槽
	 */
	protected TimerTaskList timerTaskList;

	/**
	 * 下一个节点
	 */
	protected TimerTask next;

	/**
	 * 上一个节点
	 */
	protected TimerTask prev;

	/**
	 * 任务描述
	 */
	public String desc;

	/**
	 * 构造
	 *
	 * @param task 任务
	 * @param delayMs 延迟毫秒数（以当前时间为准）
	 */
	public TimerTask(final Runnable task, final long delayMs) {
		this.delayMs = System.currentTimeMillis() + delayMs;
		this.task = task;
	}

	/**
	 * 获取任务
	 *
	 * @return 任务
	 */
	public Runnable getTask() {
		return task;
	}

	/**
	 * 获取延迟时间点，即创建时间+延迟时长（单位毫秒）
	 * @return 延迟时间点
	 */
	public long getDelayMs() {
		return delayMs;
	}

	@Override
	public String toString() {
		return desc;
	}
}
