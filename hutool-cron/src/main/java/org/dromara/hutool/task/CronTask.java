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

package org.dromara.hutool.task;

import org.dromara.hutool.pattern.CronPattern;

/**
 * 定时作业，此类除了定义了作业，也定义了作业的执行周期以及ID。
 *
 * @author looly
 * @since 5.4.7
 */
public class CronTask implements Task{

	private final String id;
	private CronPattern pattern;
	private final Task task;

	/**
	 * 构造
	 * @param id ID
	 * @param pattern 表达式
	 * @param task 作业
	 */
	public CronTask(final String id, final CronPattern pattern, final Task task) {
		this.id = id;
		this.pattern = pattern;
		this.task = task;
	}

	@Override
	public void execute() {
		task.execute();
	}

	/**
	 * 获取作业ID
	 *
	 * @return 作业ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取表达式
	 *
	 * @return 表达式
	 */
	public CronPattern getPattern() {
		return pattern;
	}

	/**
	 * 设置新的定时表达式
	 * @param pattern 表达式
	 * @return this
	 */
	public CronTask setPattern(final CronPattern pattern){
		this.pattern = pattern;
		return this;
	}

	/**
	 * 获取原始作业
	 *
	 * @return 作业
	 */
	public Task getRaw(){
		return this.task;
	}
}
