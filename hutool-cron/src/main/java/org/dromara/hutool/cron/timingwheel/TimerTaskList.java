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
		while (!timerTask.equals(root)) {
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
