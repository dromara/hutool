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
