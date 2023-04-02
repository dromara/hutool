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

package org.dromara.hutool.listener;

import org.dromara.hutool.TaskExecutor;
import org.dromara.hutool.StaticLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 监听调度器，统一管理监听
 * @author Looly
 *
 */
public class TaskListenerManager implements Serializable {
	private static final long serialVersionUID = 1L;

	private final List<TaskListener> listeners = new ArrayList<>();

	/**
	 * 增加监听器
	 * @param listener {@link TaskListener}
	 * @return this
	 */
	public TaskListenerManager addListener(final TaskListener listener){
		synchronized (listeners) {
			this.listeners.add(listener);
		}
		return this;
	}

	/**
	 * 移除监听器
	 * @param listener {@link TaskListener}
	 * @return this
	 */
	public TaskListenerManager removeListener(final TaskListener listener){
		synchronized (listeners) {
			this.listeners.remove(listener);
		}
		return this;
	}

	/**
	 * 通知所有监听任务启动器启动
	 * @param executor {@link TaskExecutor}
	 */
	public void notifyTaskStart(final TaskExecutor executor) {
		synchronized (listeners) {
			TaskListener listener;
			for (final TaskListener taskListener : listeners) {
				listener = taskListener;
				if (null != listener) {
					listener.onStart(executor);
				}
			}
		}
	}

	/**
	 * 通知所有监听任务启动器成功结束
	 * @param executor {@link TaskExecutor}
	 */
	public void notifyTaskSucceeded(final TaskExecutor executor) {
		synchronized (listeners) {
			for (final TaskListener listener : listeners) {
				listener.onSucceeded(executor);
			}
		}
	}

	/**
	 * 通知所有监听任务启动器结束并失败<br>
	 * 无监听将打印堆栈到命令行
	 * @param executor {@link TaskExecutor}
	 * @param exception 失败原因
	 */
	public void notifyTaskFailed(final TaskExecutor executor, final Throwable exception) {
		synchronized (listeners) {
			final int size = listeners.size();
			if(size > 0){
				for (final TaskListener listener : listeners) {
					listener.onFailed(executor, exception);
				}
			}else{
				StaticLog.error(exception, exception.getMessage());
			}
		}
	}
}
