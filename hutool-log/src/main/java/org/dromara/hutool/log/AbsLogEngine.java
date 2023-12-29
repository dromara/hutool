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

package org.dromara.hutool.log;

import org.dromara.hutool.log.engine.LogEngine;

/**
 * 抽象日期引擎<br>
 * 提供保存日志框架名称和checkLogExist方法
 *
 * @author looly
 * @since 6.0.0
 */
public abstract class AbsLogEngine implements LogEngine {

	/**
	 * 日志框架名，用于打印当前所用日志框架
	 */
	private final String name;

	/**
	 * 构造
	 *
	 * @param name 日志框架名
	 */
	public AbsLogEngine(final String name) {
		this.name = name;
	}

	/**
	 * 获取日志框架名，用于打印当前所用日志框架
	 *
	 * @return 日志框架名
	 * @since 4.1.21
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 检查日志实现是否存在<br>
	 * 此方法仅用于检查所提供的日志相关类是否存在，当传入的日志类类不存在时抛出ClassNotFoundException<br>
	 * 此方法的作用是在detectLogFactory方法自动检测所用日志时，如果实现类不存在，调用此方法会自动抛出异常，从而切换到下一种日志的检测。
	 *
	 * @param logClassName 日志实现相关类
	 */
	@SuppressWarnings("unused")
	protected void checkLogExist(final Class<?> logClassName) {
		// 不做任何操作
	}
}
