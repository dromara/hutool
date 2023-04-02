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

package org.dromara.hutool;

/**
 * 作业启动器<br>
 * 负责检查 {@link TaskTable} 是否有匹配到此时运行的Task<br>
 * 检查完毕后启动器结束
 *
 * @author Looly
 */
public class TaskLauncher implements Runnable {

	private final Scheduler scheduler;
	private final long millis;

	/**
	 * 构造
	 *
	 * @param scheduler {@link Scheduler}
	 * @param millis    毫秒数
	 */
	public TaskLauncher(final Scheduler scheduler, final long millis) {
		this.scheduler = scheduler;
		this.millis = millis;
	}

	@Override
	public void run() {
		//匹配秒部分由用户定义决定，始终不匹配年
		scheduler.taskTable.executeTaskIfMatch(this.scheduler, this.millis);

		//结束通知
		scheduler.taskLauncherManager.notifyLauncherCompleted(this);
	}
}
