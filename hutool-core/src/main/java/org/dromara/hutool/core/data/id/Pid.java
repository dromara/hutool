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
