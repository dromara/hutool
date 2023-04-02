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

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 任务队列，任务双向链表
 *
 * @author siran.yao，looly
 */
public class TimerTaskList implements Delayed {

	/**
	 * 过期时间
	 */
	private final AtomicLong expire;

	/**
	 * 根节点
	 */
	private final TimerTask root;

	/**
	 * 构造
	 */
	public TimerTaskList(){
		expire = new AtomicLong(-1L);

		root = new TimerTask( null,-1L);
		root.prev = root;
		root.next = root;
	}

	/**
	 * 设置过期时间
	 *
	 * @param expire 过期时间，单位毫秒
	 * @return 是否设置成功
	 */
	public boolean setExpiration(final long expire) {
		return this.expire.getAndSet(expire) != expire;
	}

	/**
	 * 获取过期时间
	 * @return 过期时间
	 */
	public long getExpire() {
		return expire.get();
	}

	/**
	 * 新增任务，将任务加入到双向链表的头部
	 *
	 * @param timerTask 延迟任务
	 */
	public void addTask(final TimerTask timerTask) {
		synchronized (this) {
			if (timerTask.timerTaskList == null) {
				timerTask.timerTaskList = this;
				final TimerTask tail = root.prev;
				timerTask.next = root;
				timerTask.prev = tail;
				tail.next = timerTask;
				root.prev = timerTask;
			}
		}
	}

	/**
	 * 移除任务
	 *
	 * @param timerTask 任务
	 */
	public void removeTask(final TimerTask timerTask) {
		synchronized (this) {
			if (this.equals(timerTask.timerTaskList)) {
				timerTask.next.prev = timerTask.prev;
				timerTask.prev.next = timerTask.next;
				timerTask.timerTaskList = null;
				timerTask.next = null;
				timerTask.prev = null;
			}
		}
	}

	/**
	 * 重新分配，即将列表中的任务全部处理
	 *
	 * @param flush 任务处理函数
	 */
	public synchronized void flush(final Consumer<TimerTask> flush) {
		TimerTask timerTask = root.next;
		while (false == timerTask.equals(root)) {
			this.removeTask(timerTask);
			flush.accept(timerTask);
			timerTask = root.next;
		}
		expire.set(-1L);
	}

	@Override
	public long getDelay(final TimeUnit unit) {
		return Math.max(0, unit.convert(expire.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
	}

	@Override
	public int compareTo(final Delayed o) {
		if (o instanceof TimerTaskList) {
			return Long.compare(expire.get(), ((TimerTaskList) o).expire.get());
		}
		return 0;
	}
}
