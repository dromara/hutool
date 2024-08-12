/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.log.engine.console;

import org.dromara.hutool.log.AbsLogEngine;
import org.dromara.hutool.log.Log;

/**
 * 利用System.out.println()打印彩色日志
 *
 * @author hongda.li
 * @since 5.8.0
 */
public class ConsoleColorLogEngine extends AbsLogEngine {

	/**
	 * 构造
	 */
	public ConsoleColorLogEngine() {
		super("Hutool Console Color Logging");
	}

	@Override
	public Log createLog(final String name) {
		return new ConsoleColorLog(name);
	}

	@Override
	public Log createLog(final Class<?> clazz) {
		return new ConsoleColorLog(clazz);
	}
}
