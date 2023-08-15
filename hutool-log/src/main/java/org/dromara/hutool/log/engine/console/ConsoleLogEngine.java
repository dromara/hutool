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

package org.dromara.hutool.log.engine.console;

import org.dromara.hutool.log.AbsLogEngine;
import org.dromara.hutool.log.Log;

/**
 * 利用System.out.println()打印日志
 * @author Looly
 *
 */
public class ConsoleLogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public ConsoleLogEngine() {
		super("Hutool Console Logging");
	}

	@Override
	public Log createLog(final String name) {
		return new ConsoleLog(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new ConsoleLog(clazz);
	}

}
