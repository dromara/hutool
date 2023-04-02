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

package org.dromara.hutool.timingwheel;

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
