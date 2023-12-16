/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.data.id;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.management.ManagementFactory;

/**
 * 进程ID单例封装<br>
 * 第一次访问时调用{@link ManagementFactory#getRuntimeMXBean()}获取PID信息，之后直接使用缓存值
 *
 * @author looly
 * @since 5.8.0
 */
public enum Pid {
	/**
	 * 单例
	 */
	INSTANCE;

	private final int pid;

	Pid() {
		this.pid = getPid();
	}

	/**
	 * 获取PID值
	 *
	 * @return pid
	 */
	public int get() {
		return this.pid;
	}

	/**
	 * 获取当前进程ID，首先获取进程名称，读取@前的ID值，如果不存在，则读取进程名的hash值
	 *
	 * @return 进程ID
	 * @throws HutoolException 进程名称为空
	 */
	private static int getPid() throws HutoolException {
		final String processName = ManagementFactory.getRuntimeMXBean().getName();
		if (StrUtil.isBlank(processName)) {
			throw new HutoolException("Process name is blank!");
		}
		final int atIndex = processName.indexOf('@');
		if (atIndex > 0) {
			return Integer.parseInt(processName.substring(0, atIndex));
		} else {
			return processName.hashCode();
		}
	}
}
