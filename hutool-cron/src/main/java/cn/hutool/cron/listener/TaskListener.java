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

package cn.hutool.cron.listener;

import cn.hutool.cron.TaskExecutor;

/**
 * 定时任务监听接口<br>
 * 通过实现此接口，实现对定时任务的各个环节做监听
 * @author Looly
 *
 */
public interface TaskListener {
	/**
	 * 定时任务启动时触发
	 * @param executor {@link TaskExecutor}
	 */
	void onStart(TaskExecutor executor);

	/**
	 * 任务成功结束时触发
	 *
	 * @param executor {@link TaskExecutor}
	 */
	void onSucceeded(TaskExecutor executor);

	/**
	 * 任务启动失败时触发
	 *
	 * @param executor {@link TaskExecutor}
	 * @param exception 异常
	 */
	void onFailed(TaskExecutor executor, Throwable exception);
}
