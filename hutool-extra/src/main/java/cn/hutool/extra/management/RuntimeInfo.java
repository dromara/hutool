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

package cn.hutool.extra.management;

import cn.hutool.core.io.file.FileUtil;

import java.io.Serializable;

/**
 * 运行时信息，包括内存总大小、已用大小、可用大小等
 *
 * @author looly
 */
public class RuntimeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Runtime currentRuntime = Runtime.getRuntime();

	/**
	 * 获得运行时对象
	 *
	 * @return {@link Runtime}
	 */
	public final Runtime getRuntime() {
		return currentRuntime;
	}

	/**
	 * 获得JVM最大内存
	 *
	 * @return 最大内存
	 */
	public final long getMaxMemory() {
		return currentRuntime.maxMemory();
	}

	/**
	 * 获得JVM已分配内存
	 *
	 * @return 已分配内存
	 */
	public final long getTotalMemory() {
		return currentRuntime.totalMemory();
	}

	/**
	 * 获得JVM已分配内存中的剩余空间
	 *
	 * @return 已分配内存中的剩余空间
	 */
	public final long getFreeMemory() {
		return currentRuntime.freeMemory();
	}

	/**
	 * 获得JVM最大可用内存
	 *
	 * @return 最大可用内存
	 */
	public final long getUsableMemory() {
		return currentRuntime.maxMemory() - currentRuntime.totalMemory() + currentRuntime.freeMemory();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		ManagementUtil.append(builder, "Max Memory:    ", FileUtil.readableFileSize(getMaxMemory()));
		ManagementUtil.append(builder, "Total Memory:     ", FileUtil.readableFileSize(getTotalMemory()));
		ManagementUtil.append(builder, "Free Memory:     ", FileUtil.readableFileSize(getFreeMemory()));
		ManagementUtil.append(builder, "Usable Memory:     ", FileUtil.readableFileSize(getUsableMemory()));

		return builder.toString();
	}
}
